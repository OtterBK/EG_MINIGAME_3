package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.HRWPlayer;
import Minigames.HeroesWar.HeroesWar;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.WarpEffect;

public abstract class Ability {

	public HRWBase hrw; //호출 객체
	public String playerName; //플레이어이름
	public String abilityName; //직업명
	public boolean passive = false; //패시브 구매유무
	public boolean ultimate= false; //궁극기 구매 유무
	public int addtionDamage  = 0; //추가 또는 감소 뎀지
	public Location lastDeathLoc; //마지막에 죽은 위치
	public double cooldown_primarySkill = 0; //주스킬 쿨타임 
	public double cooldown_secondarySkill = 0; //보조스클 쿨타임
	public double cooldown_teritarySkill = 0; //세번째스킬 쿨타임
	public double cooldown_ultimateSkill = 0; //궁극기 쿨타임
	public double cooldown_passiveSkill = 100; //패시브 쿨타임, 특정 영웅만 해당
	public double cooldown_healSpring = 60; //치유의샘 쿨타임
	public double cooldown_tailsman_Miracle = 100; //기적의부적 쿨타임
	public long nextTime_primarySkill = 0; //주스킬 남은 쿨타임
	public long nextTime_secondarySkill = 0; //보조스킬 남은 쿨타임
	public long nextTime_teritarySkill = 0; //3스킬 남은 쿨타임
	public long nextTime_ultimateSkill = 0; //궁극기 남은 쿨타임
	public long nextTime_passiveSkill = 0; //패시브 남은 쿨타임, 특정 영웅만 해당
	public long nextTime_healSpring = 0; //치유의샘 남은 쿨타임
	public long nextTime_tailsman_Miracle = 0; //기적의부적 남은 쿨타임
	public boolean doubleCheck = false; //더블 쉬프팅 확인용
	public int health = 20; //hp
	public int dmg = 5; //데미지
	public MyCooldown cooldown;
	public Sidebar sidebar;
	public HRWPlayer hrwData;
	public Team team;
	public EffectManager effectManager;
	public boolean invincible = false;
	public boolean noCC = false;
	public ItemStack weapon;
	public EGScheduler sch_Return;
	
	//퍼센트 모드
	public boolean percentMode = false;
	public double per_Ultimate_Max = 1000;
	public double per_Ultimate_Now = 0;
	public boolean blockPerUlti = false;
	
	//상태이상
	public long ccTimeTo_noMove;
	public long ccTimeTo_noSkill;
	public long ccTimeTo_noAttack;
	public long ccTimeTo_noHeal;
	public long ccTimeTo_noDamaged;
	public int ccTimerId;
	public boolean reviving = false;
	
	public boolean block_noCatching = false;
	
	//리스폰관련
	public EGScheduler respawnSch;
	public ItemStack[] backUpInvenMap = null;
	public ItemStack[] backUpArmorMap = null;
	
	public ItemStack[] lastInvenMap = null;
	public ItemStack[] lastArmorMap = null;
	
	//부적
	public boolean tailsman_miracle = false;
	public boolean tailsman_eminent = false;
	public boolean tailsman_heal = false;
	public boolean tailsman_wisdom = false;
	
	//속도 버그 방지용
	
	//스탯 관련
	public ItemStack statItem;
	public List<String> statLoreList = new ArrayList<String>();
	
	public Ability(HRWBase hrw, Player p, String abilityName, Team team) {
		this.hrw = hrw;
		respawnSch = new EGScheduler(hrw);
		playerName = p.getName();
		this.abilityName = abilityName;
		this.team = team;
		
		setItems(p);
		
		NametagEdit.getApi().setPrefix(p, "[§6§l"+abilityName+"§f]"+(hrw.getTeam(p).equalsIgnoreCase("BLUE") ? "§b" : "§c"));
		p.updateInventory();
		sch_Return = new EGScheduler(hrw);
		effectManager = hrw.server.egEM;
	}
	
	public void finalize() {
		//hrw.server.egPM.printLog(abilityName+"어빌리티 객체 삭제됨"); 
	}

	
	public void setPercentMode(boolean doMode) {
		percentMode = doMode;
		per_Ultimate_Max = cooldown_ultimateSkill * 9;
		per_Ultimate_Now = 0;
	}
	
	public void makeStatItem() {
		if(team != null) { //team이 널 아니면
			statItem = weapon.clone();
			ItemMeta meta = statItem.getItemMeta();
			meta.setDisplayName("§f[ §a"+playerName+" §f]");
			statItem.setItemMeta(meta);
		}
	}
	
	public ItemStack getstatItem() {
		return statItem;
	}
	
	public void updateStatItem() {
		
	}
	
	public void applyItems() { 
		if(!hrw.ingamePlayer.contains(playerName)) return;
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		String stone = "";
		String ring = "";
		String neck = "";
		String tailsman = "";
		
		float addHpPercent = 0;
		float addSpdPercent = 0;
		int addDmg = 0;
		
		try {
			if(p.getInventory().getItem(4).hasItemMeta()) {
				stone = p.getInventory().getItem(4).getItemMeta().getDisplayName();
			}
			if(p.getInventory().getItem(5).hasItemMeta()) {
				ring = p.getInventory().getItem(5).getItemMeta().getDisplayName();
			}
			if(p.getInventory().getItem(6).hasItemMeta()) {
				neck = p.getInventory().getItem(6).getItemMeta().getDisplayName();
			}
			if(p.getInventory().getItem(7).hasItemMeta()) {
				tailsman = p.getInventory().getItem(7).getItemMeta().getDisplayName();
			}
			
			if(stone.equalsIgnoreCase(hrw.stone_forest.getItemMeta().getDisplayName())) {
				addHpPercent += 8;
				addSpdPercent += 4;
			} else if(stone.equalsIgnoreCase(hrw.stone_miracle.getItemMeta().getDisplayName())) {
				addHpPercent += 15;
			} else if(stone.equalsIgnoreCase(hrw.stone_water.getItemMeta().getDisplayName())) {
				addHpPercent += 25;
				addDmg -= 1;
			} else if(stone.equalsIgnoreCase(hrw.stone_fire.getItemMeta().getDisplayName())) {
				addHpPercent += 30;
				addSpdPercent -= 10;
			}  
			
			if(ring.equalsIgnoreCase(hrw.ring_speed.getItemMeta().getDisplayName())) {
				addHpPercent += 6;
				addSpdPercent += 4;
			} else if(ring.equalsIgnoreCase(hrw.ring_gravity.getItemMeta().getDisplayName())) {
				addSpdPercent += 8;
			} else if(ring.equalsIgnoreCase(hrw.ring_gale.getItemMeta().getDisplayName())) {
				addDmg -= 1;
				addSpdPercent += 10;
			} else if(ring.equalsIgnoreCase(hrw.ring_storm.getItemMeta().getDisplayName())) {
				addHpPercent -= 20;
				addSpdPercent += 15;
			}
			
			if(neck.equalsIgnoreCase(hrw.neck_iron.getItemMeta().getDisplayName())) {
				addDmg += 1;
				addHpPercent += 8;
			} else if(neck.equalsIgnoreCase(hrw.neck_gold.getItemMeta().getDisplayName())) {
				addDmg += 1;
				addSpdPercent += 8;
			} else if(neck.equalsIgnoreCase(hrw.neck_diamond.getItemMeta().getDisplayName())) {
				addDmg += 2;
			} else if(neck.equalsIgnoreCase(hrw.neck_emerald.getItemMeta().getDisplayName())) {
				addDmg += 3;
				addHpPercent -= 25;
			}
			
			tailsman_miracle = false;
			tailsman_eminent = false;
			tailsman_heal = false;
			tailsman_wisdom = false;
			
			if(tailsman.equalsIgnoreCase(hrw.tailsman_miracle.getItemMeta().getDisplayName())) {
				tailsman_miracle = true; //50퍼 확률로 즉사방지, 방어구 즉시 5제공 쿨120초
			} else if(tailsman.equalsIgnoreCase(hrw.tailsman_eminent.getItemMeta().getDisplayName())) {
				tailsman_eminent = true; //모든 스킬데미지 1.25배
			} else if(tailsman.equalsIgnoreCase(hrw.tailsman_heal.getItemMeta().getDisplayName())) {
				tailsman_heal = true; //적에게 데미지 줄때마다 피1 회복
			} else if(tailsman.equalsIgnoreCase(hrw.tailsman_wisdom.getItemMeta().getDisplayName())) {
				tailsman_wisdom	 = true; //궁극기 재사용시간 10퍼 감소
			}		
			
		}catch(Exception e) {
			
		}
		MyUtility.setMaxHealth(p, health+(health*(addHpPercent/100)));
		MyUtility.healUp(p);
		p.setWalkSpeed(0.25f+(addSpdPercent/400));
		//p.sendMessage(""+p.getWalkSpeed());
		addtionDamage = addDmg;
		p.setGravity(true);
		
	}
	
	public void gameQuit(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		hrwData.carrotCnt = hrw.countItem(p.getInventory(), Material.GOLDEN_CARROT);
		sidebar.hideFrom(p);
	}
	
	public void helpMsg(){
		
	}
	
	public abstract void invenHelper(Player p);
	
	public void primarySkill() {
		
	}
	
	public void secondarySkill() {
		
	}
	
	public void teritarySkill() {
		
	}
	
	public void ultimateSkill() {
		
	}
	
	public void setSidebar() {
		
	}
	
	public boolean exitsPlayer(Player p) {
		if(p == null || !p.isOnline()) return false;
		return true;
	}	
	
	public int getTimerId() {
		return this.cooldown.timerId;
	}
	
	public abstract void setUI();
	
	public abstract void setUI_Ulaf();
	
	public void healSpring() {
		if(cooldown.checkCooldown("healSpring")) {
			Player p = Bukkit.getPlayer(playerName);
			if((int)p.getMaxHealth() == (int)p.getHealth()) {
				return;
			}
			cooldown.setCooldown("healSpring");
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 40;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(!hrw.ingame) sch.cancelTask(true);
					if(sch.schTime-- > 0) {
						double amt = p.getHealth()+1;
						if(amt > p.getMaxHealth()) {
							amt = p.getMaxHealth();
							sch.cancelTask(true);
						}
						if(!p.isDead())
						p.setHealth(amt);
					} else {
						sch.cancelTask(true);
					}	
				}
			}, 0l, 5l);
		}
	}
	
	public void returned() {
		
	}
	
	public void returning() {
		
		if(sch_Return.schId != -1) {
			return;
		}
		if(isCCed()) return;
		Player p = Bukkit.getPlayer(playerName);
		sch_Return.schTime = 6;
		sch_Return.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(sch_Return.schTime > 0) {
					sch_Return.schTime--;
					if(isCCed()) sch_Return.cancelTask(true);
					TitleAPI.sendSubtitle(p, 0, 30, 0, "§c§l"+(sch_Return.schTime+1));
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDEREYE_LAUNCH, 1.0F, 0.1F);
					//p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 254));
					//p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 30, 248));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30, 255));
					WarpEffect ef5 = new WarpEffect(effectManager);
					ef5.particle = Particle.REDSTONE;
					ef5.color = Color.fromRGB(255,196,00);
					ef5.radius = 1f;
					ef5.iterations = 10;
					ef5.setLocation(p.getLocation());
					ef5.start();
				} else {
					sch_Return.cancelTask(true);
					p.teleport(team.loc_spawn, TeleportCause.PLUGIN);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SLIME_JUMP, 1.0F, 0.1F);
					p.getWorld().playSound(team.loc_spawn, Sound.ENTITY_SLIME_JUMP, 1.0F, 0.1F);
					returned();
				}
			}
		}, 0l, 20l);
		
	}
	
	public void healPotion1() {
		Player p = Bukkit.getPlayer(playerName);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
		EGScheduler sch = new EGScheduler(hrw);
		sch.schTime = 10;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!hrw.ingame) sch.cancelTask(true);
				if (sch.schTime-- > 0) {
					double amt = p.getHealth() + 1;
					if (amt > p.getMaxHealth()) {
						amt = p.getMaxHealth();
						sch.cancelTask(true);
					}
					if (!p.isDead())
						p.setHealth(amt);
				} else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 10l);
	}
	
	public void healPotion2() {
		Player p = Bukkit.getPlayer(playerName);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.1f);
		EGScheduler sch = new EGScheduler(hrw);
		sch.schTime = 25;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!hrw.ingame) sch.cancelTask(true);
				if (sch.schTime-- > 0) {
					double amt = p.getHealth() + 1;
					if (amt > p.getMaxHealth()) {
						amt = p.getMaxHealth();
						sch.cancelTask(true);
					}
					if (!p.isDead())
						p.setHealth(amt);
				} else {
					sch.cancelTask(true);
				}
			}
		}, 0l, 10l);
	}
	
	public void customDie(String damager, boolean ignore) {
		Player p = Bukkit.getPlayer(playerName);
		p.setHealth(0);
		if(!ignore) hrw.playerMap.get(damager).death += 1; //상대 킬수 증가
		if(!ignore) hrwData.death += 1; //자신 데스수 증가
	}
	
	public void customHeal(Player victim, Player healer, int healAmt, boolean ignore) {
		double amt = victim.getHealth();
		amt += healAmt;
		if(amt > victim.getMaxHealth()) amt = victim.getMaxHealth();
		if(!victim.isDead()) {
			int resHeal = (int) (amt - victim.getHealth());
			victim.setHealth(amt);
			if(!blockPerUlti) per_Ultimate_Now += resHeal;
			hrwData.healAmt += resHeal;
		}	
	}

	public void customSlowHeal(Player victim, Player healer, int healAmt, boolean ignore) {
		EGScheduler sch = new EGScheduler(hrw);
		sch.schTime = healAmt;
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(!hrw.ingame) sch.cancelTask(true);
				if(sch.schTime-- > 0) {
					customHeal(victim, healer, 1, ignore);	
				} else {
					sch.cancelTask(true);
				}	
			}
		}, 0l, 10l);
	}
	
	public void customDamage(Player victim, Player damager, int damage, boolean ignore) {
		HRWPlayer victim_hrwP = hrw.playerMap.get(victim.getName());
		Team team = victim_hrwP.team;
		if(team == null) return;
		Location pl = victim.getLocation();
		Location l1 = team.loc_base1;
		Location l2 = team.loc_base2;
		if (l1.getX() < pl.getX() && l2.getX() > pl.getX() && l1.getY() < pl.getY() && l2.getY() > pl.getY()
				&& l1.getZ() < pl.getZ() && l2.getZ() > pl.getZ()) {
			ActionBarAPI.sendActionBar(victim, "§c§l기지안에서는 데미지를 입지 않습니다.", 60);
			ActionBarAPI.sendActionBar(damager, "§c§l상대방 기지안에 있는 적은 공격이 불가능합니다.", 60);
			return;
		}
		int resDmg = tailsman_eminent ? (int) (damage * 1.20) : damage;
		resDmg += addtionDamage;
		int excep = victim_hrwP.ability.onHitted(victim, damager, resDmg);
		if(excep != -1) {
			resDmg = excep;
		} 
		if(!victim.getName().equalsIgnoreCase(damager.getName())) hrw.playerMap.get(victim.getName()).lastDamager = damager.getName();
//		Bukkit.broadcastMessage(hrw.playerMap.get(victim.getName()).lastDamager+", "+hrw.playerMap.get(victim.getName()).ability.abilityName);
		if(victim.getHealth() - resDmg <= 0 && !victim.isDead()) {
			Ability ab = hrw.playerMap.get(victim.getName()).ability;
			if(!victim.isDead() && ab.tailsman_miracle && MyUtility.getRandom(0, 1) == 1) {
				ab.cooldown.setCooldown("tailsman_Miracle");
				customHeal(victim, victim, 8, true);
			} else {
				if(ab.backUpInvenMap == null) {
					if(hrw.hasItem(victim, Material.BOOK, 1)) {
						ab.backUpInvenMap = victim.getInventory().getContents();
						ab.backUpArmorMap = victim.getInventory().getArmorContents();
					}
					//if(tailsman_heal) customHeal(damager, damager, 1, true);
				}	
				victim.setHealth(0);
				if(tailsman_heal) customHeal(damager, damager, 20, true);
				if(!blockPerUlti) per_Ultimate_Now += resDmg;
			}	
		}else {
			try{
				victim.setHealth(victim.getHealth() - resDmg);
				if(!blockPerUlti) per_Ultimate_Now += resDmg;
				if(resDmg >= 1) {
					//if(tailsman_heal) customHeal(damager, damager, 1, true);
				}
			} catch(Exception e) {
				
			}
		}
		victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 0.5F);
		victim.damage(0.01);
		damager.playSound(damager.getLocation(), Sound.ENTITY_PLAYER_ATTACK_WEAK, 1.0F, 0.5F);
		if(!ignore) hrw.playerMap.get(damager.getName()).dealAmt += resDmg; //딜량 증가
	}

	public void customPoison(Player victim, Player damager, int time, boolean ignore) {
		victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, time*20, 0));
		if(!ignore) hrw.playerMap.get(damager.getName()).poisonTime += time; //중독시간 증가
	}
	
	public void customWeakness(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "weakness", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).weaknessTime += time; 
	}
	
	public void customBind(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "bind", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).bindTime += time; 
	}
	
	public void customDamageUp(Player victim, Player buffer, float amt ,int time, boolean ignore) {
		hrw.playerMap.get(victim.getName()).ability.addtionDamage += amt;
		
		if(!ignore) hrw.playerMap.get(buffer.getName()).addDamageAmt += amt; 
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
			public void run() {
				if(hrw.playerMap.containsKey(victim.getName()))
				hrw.playerMap.get(victim.getName()).ability.addtionDamage -= amt;
			}
		}, time*20);
	}
	
	public void customSpeedUp(Player victim, Player buffer, float amt ,int time, boolean ignore) {
		final float chg = amt / 4;
		victim.setWalkSpeed(MyUtility.getNormalizeSpeed(victim.getWalkSpeed()+chg));
		
		if(!ignore) hrw.playerMap.get(buffer.getName()).speedUpTime += time; 
		
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
			public void run() {
				if(hrw.playerMap.containsKey(victim.getName()))
				victim.setWalkSpeed(MyUtility.getNormalizeSpeed(victim.getWalkSpeed()-chg));
			}
		}, time*20);
		
		
	}
	
	public void customSpeedSlow(Player victim, Player buffer, float amt ,int time, boolean ignore) {
		final float chg = amt / 5;
		victim.setWalkSpeed(MyUtility.getNormalizeSpeed(victim.getWalkSpeed()-chg));
		
		if(!ignore) hrw.playerMap.get(buffer.getName()).speedSlowTime += time; 
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
			public void run() {
				if(hrw.playerMap.containsKey(victim.getName()))
				victim.setWalkSpeed(MyUtility.getNormalizeSpeed(victim.getWalkSpeed()+chg));
			}
		}, time*20);
	}
	
	public void customStop(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "stop", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).stopTime += time; 
	}

	public void customHealban(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "healban", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).noHealTime += time; 
	}
	
	public void customAirbon(Player victim, Player damager, float strength, boolean ignore) {
		victim.setVelocity(new Vector(0, strength, 0));
		if(!ignore) hrw.playerMap.get(damager.getName()).airboneCnt += 1; 
	}
	
	public void customFire(Player victim, Player damager, int time, boolean ignore) {
		victim.setFireTicks(time*20);
		if(!ignore) hrw.playerMap.get(damager.getName()).fireTime += time; 
	}
	
	public void customFaint(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "faint", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).faintTime += time; 
	}
	 
	public void customFreeze(Player victim, Player damager, int time, boolean ignore) {
		setCC(victim, "freeze", time);
		if(!ignore) hrw.playerMap.get(damager.getName()).freezeTime += time; 
	}
	
	public List<Player> getEnemyList(String pName) {
		return MyUtility.stringListToPlayer(hrw.getEnemyList(pName));
	}
	
	public List<Player> getTeamList(String pName) {
		return MyUtility.stringListToPlayer(team.teamList);
	}
	
	public int tankerCheck(Player victim, Player damager ,int damage) {
		int excepDmg = -1;
		
		for(Ability ability : team.reduceAbilityList) {
			if(ability instanceof Guardian) {
				Guardian tanker = (Guardian) ability;
				excepDmg = tanker.calcDamage(victim, damager ,damage);
			}
			if(ability instanceof Monk) {
				Monk tanker = (Monk) ability;
				excepDmg = tanker.calcDamage(victim, damager ,damage);
			}
			if(ability instanceof Alchemist) {
				Alchemist tanker = (Alchemist) ability;
				excepDmg = tanker.calcDamage(victim, damager ,damage);
			}
			if(ability instanceof Warrior) {
				Warrior tanker = (Warrior) ability;
				excepDmg = tanker.calcDamage(victim, damager ,damage);
			}
			if(ability instanceof Monarch) {
				Monarch tanker = (Monarch) ability;
				excepDmg = tanker.calcDamage(victim, damager ,damage);
			}
			if(excepDmg != -1) {
				break;
			}
		}
		return excepDmg;
	}
	
	
	///////////이벤트
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		
	}
	
	public void onEntityDamaged(EntityDamageEvent e) {
		if(invincible) e.setCancelled(true);
		else {
			Player p = (Player)e.getEntity();
			if(p.getHealth() - e.getDamage() <= 0) {
				if(backUpInvenMap == null) {
					if(hrw.hasItem(p, Material.BOOK, 1)) {
						backUpInvenMap = p.getInventory().getContents();
						backUpArmorMap = p.getInventory().getArmorContents();
					}
				}
			}
		}
	}
	
	public void onClickInventory(InventoryClickEvent e) {
		
	}
	
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = Bukkit.getPlayer(playerName);
		Player p = (Player) e.getEntity();
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			if(hrw.playerMap.get(d.getName()).ability.isNoAttack()) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(d, "§2§l공격 불가 상태입니다.", 40);
				return;
			} 
			e.setDamage(0);
			if(hrw.getHeldMainItemName(d).contains(abilityName)) {
				HRWPlayer victimHrwp = hrw.playerMap.get(p.getName());
				
				int resDmg = dmg+addtionDamage;
				int excep = victimHrwp.ability.onHitted(p, d, resDmg);
				if (excep != -1) {
					resDmg = excep;
				} 
				if(p.getHealth() - resDmg <= 0) {
					Ability ab = victimHrwp.ability;
					if(!p.isDead() && ab.tailsman_miracle && MyUtility.getRandom(0, 1) == 1) {
						ab.cooldown.setCooldown("tailsman_Miracle");
						customHeal(p, p, 8, true);
					} else {
						if(ab.backUpInvenMap == null) {
							if(hrw.hasItem(p, Material.BOOK, 1)) {
								ab.backUpInvenMap = p.getInventory().getContents();
								ab.backUpArmorMap = p.getInventory().getArmorContents();
							}
							if(tailsman_heal) customHeal(d, d, 20, true);
						}	
						p.setHealth(0);
						if(!blockPerUlti) per_Ultimate_Now += resDmg;
					}
				} else {
					p.setHealth(p.getHealth() - resDmg);
					if(!blockPerUlti) per_Ultimate_Now += resDmg;
					if(resDmg >= 1) {
						//if(tailsman_heal) customHeal(d, d, 1, true);
					}
				}
				if(!p.getName().equalsIgnoreCase(d.getName())) {			
					victimHrwp.lastDamager = d.getName();
					hrwData.dealAmt += resDmg;
				}
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	public int onHitted(Player victim, Player damager, int damage) {
		if(invincible) return 0;
		if(sch_Return.schId != -1) {
			sch_Return.cancelTask(true);
		}
		int calcDamage = tankerCheck(victim, damager ,damage);
		return calcDamage; //-1은 적용안함, 그 외는 체력을 이걸로 설정해달라
	}
	
	public void onBlockBreak(BlockBreakEvent e) {
		
	}
	
	public void onBlockPlaced(BlockPlaceEvent e) {
		
	}
	
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		//Player p = e.getPlayer();
		//p.getInventory().getItemInMainHand().setType(Material.AIR);
		if(item.getType() == Material.POTION) {
			String s = item.getItemMeta().getDisplayName();
			if(s.equalsIgnoreCase("§7- §c회복약(약) §7-")) {
				healPotion1();
			} else if(s.equalsIgnoreCase("§7- §c회복약(강) §7-")) {
				healPotion2();
			}
		}
		item.setType(Material.AIR);
		e.setItem(item);
	}
	
	public void onPlayerDeath(PlayerDeathEvent e) {

	}
	
	public void setItems(Player p) {
		p.getInventory().setItem(0, weapon);
		p.getInventory().setItem(3, hrw.item_returnBase); //귀환
		p.getInventory().setItem(4, hrw.item_none_stone); //포석
		p.getInventory().setItem(5, hrw.item_none_ring); //반지
		p.getInventory().setItem(6, hrw.item_none_neck); //목걸이
		p.getInventory().setItem(7, hrw.item_none_tailsman); //부적
		p.getInventory().setItem(8, hrw.item_hrwHelper); //게임 메뉴
		team.setArmor(p);
	}
	
	public void reConnect() {
		Player p = Bukkit.getPlayer(playerName);
		setItems(p);
		
		NametagEdit.getApi().setPrefix(p, "[§6§l"+abilityName+"§f]"+(hrw.getTeam(p).equalsIgnoreCase("BLUE") ? "§b" : "§c"));
		p.updateInventory();
		hrw.spawn(p);
		if(hrwData.item_stone != null) p.getInventory().setItem(4, hrwData.item_stone);
		if(hrwData.item_ring != null) p.getInventory().setItem(5, hrwData.item_ring);
		if(hrwData.item_neck != null) p.getInventory().setItem(6, hrwData.item_neck);
		if(hrwData.item_tailsman != null) p.getInventory().setItem(7, hrwData.item_tailsman);
		if(hrwData.carrotCnt != 0) {
			p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, hrwData.carrotCnt));
			hrwData.carrotCnt = 0;
		}
		applyItems();
	}
	
	public boolean checkBook(ItemStack[] contents) {
		for(ItemStack item : contents) {
			if(item == null) continue;
			if(item.getType() == Material.BOOK) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWeapon(ItemStack[] contents) {
		for(ItemStack item : contents) {
			if(item == null) continue;
			if(item.getType() == weapon.getType()) {
				return true;
			}
		}
		return false;
	}
	
	public void respawned() {
		
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		p.setGameMode(GameMode.SPECTATOR);
		if(!team.respawning.contains(p.getName())) team.respawning.add(p.getName());
		respawnSch.cancelTask(true);
		respawnSch.schTime = 10;
		respawnSch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				if(respawnSch.schTime > 0) {
					TitleAPI.sendFullTitle(p, 0, 30, 0, ChatColor.RED+""+respawnSch.schTime+"초 후 부활","");
					respawnSch.schTime  -= 1;
				} else {
					respawnSch.cancelTask(true);
					hrw.spawn(p);
					TitleAPI.sendFullTitle(p, 10, 70, 30, "", ChatColor.RED+"부활하였습니다.");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 1.0f, 1.0f);
					if(backUpInvenMap != null) {
						p.getInventory().setContents(backUpInvenMap);
						p.getInventory().setArmorContents(backUpArmorMap);
						lastInvenMap = backUpInvenMap.clone();
						lastArmorMap = backUpArmorMap.clone();
					} else {
						//p.sendMessage(hrw.ms+"아이템 복구 실패, 이전 인벤토리를 불러옵니다.");
						setItems(p);

						if(hrwData.item_stone != null) p.getInventory().setItem(4, hrwData.item_stone);
						if(hrwData.item_ring != null) p.getInventory().setItem(5, hrwData.item_ring);
						if(hrwData.item_neck != null) p.getInventory().setItem(6, hrwData.item_neck);
						if(hrwData.item_tailsman != null) p.getInventory().setItem(7, hrwData.item_tailsman);
						if(hrwData.carrotCnt != 0) {
							p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, hrwData.carrotCnt));
						}
						
						p.updateInventory();
					}
					backUpInvenMap = null;
					backUpArmorMap = null;
					team.respawning.remove(p.getName());
					respawned();
					Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
						public void run() {
							if(!checkBook(p.getInventory().getContents()) || !checkWeapon(p.getInventory().getContents())) {
								p.sendMessage("§c아이템 복구 실패 발생... 인벤토리를 재설정합니다.");
								setItems(p);
								if(hrwData.item_stone != null) p.getInventory().setItem(4, hrwData.item_stone);
								if(hrwData.item_ring != null) p.getInventory().setItem(5, hrwData.item_ring);
								if(hrwData.item_neck != null) p.getInventory().setItem(6, hrwData.item_neck);
								if(hrwData.item_tailsman != null) p.getInventory().setItem(7, hrwData.item_tailsman);
								if(hrwData.carrotCnt != 0) {
									p.getInventory().addItem(new ItemStack(Material.GOLDEN_CARROT, hrwData.carrotCnt));
									hrwData.carrotCnt = 0;
								}
							}
							hrwData.carrotCnt = 0;
							applyItems();
							//p.damage(0.01);
						}
					}, 20l);
				}
			}
		}, 0l, 20l);
	}
	
	public void onPlayerQuit(PlayerQuitEvent e) {
		
	}
	
	public void onMouseClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		Player p = e.getPlayer();
		if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if(p.isSneaking()) {
				secondarySkill();
			} else {
				primarySkill();
			}
		}
	}
	
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		
	}
	
	public void onInventoryClose(InventoryCloseEvent e) {
		
	}
	
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(isNoMove()) {
			hrw.egCancelMovement(e);
		}
		if(team.predator_portal1 != null && team.predator_portal2 != null ){
			Location l = p.getLocation();
			if(l.getBlockX() == team.predator_portal1.getBlockX() 
					&& l.getBlockY() == team.predator_portal1.getBlockY() 
					&& l.getBlockZ() == team.predator_portal1.getBlockZ()) {
				p.teleport(team.predator_portal2, TeleportCause.PLUGIN);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.2f, 2.0f);
			}
		}	
		if(sch_Return.schId != -1) {
			sch_Return.cancelTask(true);
		}
	}
	
	public void onRegainHealth(EntityRegainHealthEvent e) {
		if (e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN)
			e.setCancelled(true);
	}
	
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(weapon != null) {
			if(p.isSneaking() && e.getItemDrop().getItemStack().equals(weapon)) {
				ultimateSkill();
			}
		}	
	}
	

	public void onChangeHand(PlayerSwapHandItemsEvent e) {
		Player p = e.getPlayer();
		//if(p.isSneaking()) {
			teritarySkill();
		//}
	}
	
	public void onChat(PlayerChatEvent e) {
		
	}
	
	public void onPlayerKill(Player t) {
	
	}
	
	public void onSneak(PlayerToggleSneakEvent e) {

	}
	
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		
	}
	
	
	public void onPlayerShotBow(EntityShootBowEvent e) {
		
	}
	
	////////////////////// CC관련
	public void setCC(Player victim, String cc, int time) {
		
		if(hrw.playerMap.get(victim.getName()).ability.noCC) {
			victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 1.0f, 2.5f);
			return;
		} 
		
		long newCCTime = System.currentTimeMillis() + time * 1000;
		int ptTime = time * 20;
		Ability va = hrw.playerMap.get(victim.getName()).ability;
		
		if(sch_Return.schId != -1) {
			sch_Return.cancelTask(true);
		}
		
		if(cc.equalsIgnoreCase("bind")) {
			if(va.ccTimeTo_noMove < newCCTime) {
				va.ccTimeTo_noMove = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ptTime, 254));
				//victim.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ptTime, 248));
			}		
		} else if(cc.equalsIgnoreCase("freeze")) {
			if(va.ccTimeTo_noMove < newCCTime) {
				va.ccTimeTo_noMove = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ptTime, 254));
				//victim.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ptTime, 254));
			}
			if(va.ccTimeTo_noAttack < newCCTime) {
				va.ccTimeTo_noAttack = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, ptTime, 0));
			}
		} else if(cc.equalsIgnoreCase("weakness")) {
			if(va.ccTimeTo_noSkill < newCCTime) {
				va.ccTimeTo_noSkill = newCCTime;
			}
		} else if(cc.equalsIgnoreCase("faint")) {
			if(va.ccTimeTo_noSkill < newCCTime) {
				va.ccTimeTo_noSkill = newCCTime;
			}
			if(va.ccTimeTo_noMove < newCCTime) {
				va.ccTimeTo_noMove = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ptTime, 254));
				//victim.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ptTime, 254));
			}
			if(va.ccTimeTo_noAttack < newCCTime) {
				va.ccTimeTo_noAttack = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, ptTime, 0));
			}
		} else if(cc.equalsIgnoreCase("stop")) {
			if(va.ccTimeTo_noSkill < newCCTime) {
				va.ccTimeTo_noSkill = newCCTime;
			}
			if(va.ccTimeTo_noMove < newCCTime) {
				va.ccTimeTo_noMove = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ptTime, 254));
				//victim.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ptTime, 254));
			}
			if(va.ccTimeTo_noAttack < newCCTime) {
				va.ccTimeTo_noAttack = newCCTime;
				victim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, ptTime, 0));
			}
			if(va.ccTimeTo_noDamaged < newCCTime) {
				va.ccTimeTo_noDamaged = newCCTime;
			}
		} else if(cc.equalsIgnoreCase("healban")) {
			if(va.ccTimeTo_noHeal < newCCTime) {
				va.ccTimeTo_noHeal = newCCTime;
			}
		} 
		
	}
	
	public boolean isNoMove() {
		if(ccTimeTo_noMove > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public boolean isNoAttack() {
		if(ccTimeTo_noAttack > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public boolean isNoSkill() {
		if(ccTimeTo_noSkill > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public boolean isNoDamaged() {
		if(ccTimeTo_noDamaged > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public boolean isNoHeal() {
		if(ccTimeTo_noHeal > System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
	
	public boolean isCCed() {
		if(isNoMove() || isNoAttack() || isNoSkill() || isNoDamaged() || isNoHeal()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void ccUI(Player p) {
		boolean noSkill = isNoSkill();
		boolean noDamaged = isNoDamaged();
		boolean noHeal = isNoHeal();
		boolean noAttack = isNoAttack();
		boolean noMove = isNoMove();
		String ccMsg = null;
		if(reviving) {
			ccMsg = "§e§l부활중";
		} else if(sch_Return.schId != -1) {
			ccMsg = "§e§l귀환중";
		} else if(noMove && noAttack && noSkill && noDamaged) {
			ccMsg = "§2§l정지됨";
		} else if(noMove && noAttack && noSkill) {
			ccMsg = "§2§l기절함";
		} else if(noMove && noAttack) {
			ccMsg = "§2§l냉동";
		} else if(noSkill) {
			ccMsg = "§2§l쇠약";
		} else if(noMove) {
			ccMsg = "§2§l구속됨";
		} else if(noHeal) {
			ccMsg = "§2§l치유불가";
		} 
		
		if(ccMsg != null) {
			if(!ccMsg.equalsIgnoreCase("§6§l귀환중"));
				TitleAPI.sendTitle(p, 0, 6, 0, ""+ChatColor.ITALIC+ccMsg);
			NametagEdit.getApi().setSuffix(p, "§f["+ccMsg+"§f]");
		}else {
			if(!NametagEdit.getApi().getNametag(p).getSuffix().equalsIgnoreCase("")) {
				NametagEdit.getApi().setSuffix(p,"");
			}
		}
	}
	
	////// 임시 이펙트
	  public void writePentagon(Location loc, Location loc2, Location loc3, Location loc4, Location loc5) {
	    EffectManager eManager = effectManager;

	    LineEffect effect = new LineEffect(eManager);
	    LineEffect effect1 = new LineEffect(eManager);
	    LineEffect effect2 = new LineEffect(eManager);
	    LineEffect effect3 = new LineEffect(eManager);
	    LineEffect effect4 = new LineEffect(eManager);
	    LineEffect effect5 = new LineEffect(eManager);
	    LineEffect effect6 = new LineEffect(eManager);
	    LineEffect effect7 = new LineEffect(eManager);
	    LineEffect effect8 = new LineEffect(eManager);
	    LineEffect effect9 = new LineEffect(eManager);

	    effect.setLocation(loc);
	    effect.setTargetLocation(loc2);
	    effect1.setLocation(loc);
	    effect1.setTargetLocation(loc3);
	    effect2.setLocation(loc);
	    effect2.setTargetLocation(loc4);
	    effect3.setLocation(loc);
	    effect3.setTargetLocation(loc5);
	    effect4.setLocation(loc2);
	    effect4.setTargetLocation(loc3);
	    effect5.setLocation(loc2);
	    effect5.setTargetLocation(loc4);
	    effect6.setLocation(loc2);
	    effect6.setTargetLocation(loc5);
	    effect7.setLocation(loc3);
	    effect7.setTargetLocation(loc4);
	    effect8.setLocation(loc3);
	    effect8.setTargetLocation(loc5);
	    effect9.setLocation(loc4);
	    effect9.setTargetLocation(loc5);

	    effect.particle = Particle.REDSTONE;
	    effect1.particle = Particle.REDSTONE;
	    effect2.particle = Particle.REDSTONE;
	    effect3.particle = Particle.REDSTONE;
	    effect4.particle = Particle.REDSTONE;
	    effect5.particle = Particle.REDSTONE;
	    effect6.particle = Particle.REDSTONE;
	    effect7.particle = Particle.REDSTONE;
	    effect8.particle = Particle.REDSTONE;
	    effect9.particle = Particle.REDSTONE;

	    effect.start();
	    effect1.start();
	    effect2.start();
	    effect3.start();
	    effect4.start();
	    effect5.start();
	    effect6.start();
	    effect7.start();
	    effect8.start();
	    effect9.start();
	  }
	
	////////////// 쿨타운
	
	public class MyCooldown {
		
		Ability ability;
		private int timerId;
		
		public MyCooldown(Ability ability) {
			this.ability = ability;
		}
		
		public boolean checkCooldown(String skillName) {
			
			boolean ret = false;
			int leftTime = 0;
			
			if(skillName.equalsIgnoreCase("primary")) {
				if(ability.nextTime_primarySkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_primarySkill- System.currentTimeMillis()) / 1000)+1;
				}
			} else if (skillName.equalsIgnoreCase("secondary")) {
				if(ability.nextTime_secondarySkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_secondarySkill - System.currentTimeMillis()) / 1000)+1;
				}
			} else if (skillName.equalsIgnoreCase("teritary")) {
				if(ability.nextTime_teritarySkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_teritarySkill  - System.currentTimeMillis()) / 1000)+1;
				}
			}else if (skillName.equalsIgnoreCase("ultimate")) {
				if(percentMode) {
					Player p = Bukkit.getPlayer(ability.playerName);
					if(ability.exitsPlayer(p)) {
						if(per_Ultimate_Now >= per_Ultimate_Max) {
							ret = true;
						}else {
							ActionBarAPI.sendActionBar(p,
									ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "§e§l아직 궁극기가 준비되지 않았습니다.", 20);
							p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
						}					
					}
				}else {
					if(ability.nextTime_ultimateSkill <= System.currentTimeMillis()) {
						ret = true;
					} else {
						leftTime = ((int)(ability.nextTime_ultimateSkill - System.currentTimeMillis()) / 1000)+1;
					}
				}			
			} else if (skillName.equalsIgnoreCase("passive")) {
				if(ability.nextTime_passiveSkill <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_passiveSkill - System.currentTimeMillis()) / 1000)+1;
				}
			}else if (skillName.equalsIgnoreCase("healSpring")) {
				if(ability.nextTime_healSpring <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_healSpring - System.currentTimeMillis()) / 1000)+1;
				}
			} else if (skillName.equalsIgnoreCase("tailsman_Miracle")) {
				if(ability.nextTime_tailsman_Miracle <= System.currentTimeMillis()) {
					ret = true;
				} else {
					leftTime = ((int)(ability.nextTime_tailsman_Miracle - System.currentTimeMillis()) / 1000)+1;
				}
			} 
			
			if(!ret) {
				Player p = Bukkit.getPlayer(ability.playerName);
				if(ability.exitsPlayer(p)) {
					if(skillName.equalsIgnoreCase("healSpring")) {
						ActionBarAPI.sendActionBar(p, ChatColor.YELLOW+""+ChatColor.BOLD+"치유의 샘은 §a§l"+leftTime+"초 §e§l후 사용하실 수 있습니다.", 20);
					} else if(skillName.equalsIgnoreCase("tailsman_Miracle")) {
						ActionBarAPI.sendActionBar(p, ChatColor.YELLOW+""+ChatColor.BOLD+"기적의 부적은§a§l"+leftTime+"초 §e§l후 사용하실 수 있습니다.", 20);
					} else {
						ActionBarAPI.sendActionBar(p, ChatColor.DARK_GREEN+""+ChatColor.BOLD+leftTime+"§e§l초 후 사용하실 수 있습니다.", 20);
						p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
					}	
				}
			}
			return ret;
		}
		
		public int getLeftCooldown(String skillName) {
			float leftTime = 0;
			
			if(skillName.equalsIgnoreCase("primary")) {
				leftTime = (ability.nextTime_primarySkill - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("secondary")) {
				leftTime = (ability.nextTime_secondarySkill - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("teritary")) {
				leftTime = (ability.nextTime_teritarySkill- System.currentTimeMillis());
			}	else if (skillName.equalsIgnoreCase("ultimate")) {
				leftTime = (ability.nextTime_ultimateSkill - System.currentTimeMillis());
			}  else if (skillName.equalsIgnoreCase("passive")) {
				leftTime = (ability.nextTime_passiveSkill - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("healSpring")) {
				leftTime = (ability.nextTime_healSpring - System.currentTimeMillis());
			} else if (skillName.equalsIgnoreCase("tailsman_Miracle")) {
				leftTime = (ability.nextTime_tailsman_Miracle - System.currentTimeMillis());
			} 
			
			if(leftTime < 0) return 0;
			
			return (int)(leftTime / 1000)+1;
		}
		
		public int getUltimatePercent() {
			return (int) (per_Ultimate_Now / per_Ultimate_Max * 100);
		}
		
		public void setCooldown(String skillName) {			
			if(skillName.equalsIgnoreCase("primary")) {
				nextTime_primarySkill = (long) (System.currentTimeMillis()+ability.cooldown_primarySkill*1000);
			} else if (skillName.equalsIgnoreCase("secondary")) {
				nextTime_secondarySkill = (long) (System.currentTimeMillis()+ability.cooldown_secondarySkill*1000);
			} else if (skillName.equalsIgnoreCase("teritary")) {
				nextTime_teritarySkill = (long) (System.currentTimeMillis()+ability.cooldown_teritarySkill*1000);
			}  else if (skillName.equalsIgnoreCase("passive")) {
				nextTime_passiveSkill = (long) (System.currentTimeMillis()+ability.cooldown_passiveSkill*1000);
			}else if (skillName.equalsIgnoreCase("ultimate")) {
				if(percentMode) {
					if(tailsman_wisdom) {
						per_Ultimate_Now = (per_Ultimate_Max/100)*10;
					} else {
						per_Ultimate_Now = 0;
					}
				}else {
					if(tailsman_wisdom) {
						nextTime_ultimateSkill = (long) (System.currentTimeMillis()+(ability.cooldown_ultimateSkill - (ability.cooldown_ultimateSkill/15))*1000);
					} else {
						nextTime_ultimateSkill = (long) (System.currentTimeMillis()+ability.cooldown_ultimateSkill*1000);	
					}
				}		
			} else if (skillName.equalsIgnoreCase("healSpring")) {
				nextTime_healSpring = (long) (System.currentTimeMillis()+ability.cooldown_healSpring*1000);
			} else if (skillName.equalsIgnoreCase("tailsman_Miracle")) {
				nextTime_tailsman_Miracle = (long) (System.currentTimeMillis()+ability.cooldown_tailsman_Miracle*1000);
			}
			
		}
		
		public void setCooldownSpec(String skillName, int leftTime) {
			if(skillName.equalsIgnoreCase("primary")) {
				nextTime_primarySkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("secondary")) {
				nextTime_secondarySkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("teritary")) {
				nextTime_teritarySkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("ultimate")) {
				if(percentMode) per_Ultimate_Now = (per_Ultimate_Max/100)*leftTime;
				else nextTime_ultimateSkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("passive")) {
				nextTime_passiveSkill = System.currentTimeMillis() + (leftTime * 1000);
			} else if (skillName.equalsIgnoreCase("healSpring")) {
				nextTime_healSpring = System.currentTimeMillis() + (leftTime * 1000);
			} 
			
		}
	}	
}
