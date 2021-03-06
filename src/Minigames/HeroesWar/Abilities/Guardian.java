package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.HRWPlayer;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;

public class Guardian extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//수호자 전용
	Player dashTarget;
	boolean isDashing;
	float dashYaw = 0;
	float dashPitch = 0;
	int timerId = -1;
	int timerTime = 0;
	int timerTime2 = 0;
	int shield= 100;
	
	//수호자 스탯
	public int stat_dashHit = 0;
	public int stat_knockback = 0;
	
	public Guardian(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 4;

		weapon = new ItemStack(Material.SHIELD, 1);
		ItemMeta meta = weapon.getItemMeta();
		meta.setDisplayName("§7[ §6"+abilityName+" §7]");
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.setUnbreakable(true);
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("§6공격력 §7: §d"+dmg);
		loreList.add("");
		meta.setLore(loreList);
		weapon.setItemMeta(meta);
		
		///쿨타임 설정
		cooldown_primarySkill = 0;
		cooldown_secondarySkill = 13;
		cooldown_teritarySkill = 11;
		cooldown_ultimateSkill = 95;
		
		//체력설정
		health = 100;

		
		//탱커 등록
		team.reduceAbilityList.add(this);
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		guardianTimer();
		
		makeStatItem();
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§6============= §f[ §b자객 §f] - §e직업군 §7: §cDealer §6=============\n"
				+ "§f- §a주스킬 §7: §d축지\n§7전방 5칸앞으로 이동합니다. §c벽을 뚫지는 못합니다. 3초마다 잔여 횟수가 1증가합니다.(최대 4회)\n"
				+ "§f- §a보조스킬 §7: §d역행\n§75초전의 시간으로 이동합니다. §c체력, 위치가 5초전으로 복구됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d점착폭탄\n§72칸 범위내에 바라보는 플레이어 또는 자신의 위치에 폭탄을 부착합니다. 2.0초후 폭발합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d빠른축지\n§7에너지를 더 빠르게 모읍니다. 축지의 횟수가 회복되는 간격이 3초에서 2.4초로 감소합니다.");	
	}
	
	@Override
	public void invenHelper(Player p) {
		p.openInventory(hrw.inven_desc_guardian);
	}
	
	@Override
	public void updateStatItem() {
		if(statItem == null) return; 
		ItemMeta meta = statItem.getItemMeta();
		statLoreList.clear();
		statLoreList.add("");
		statLoreList.add("§7영웅 : §a"+abilityName+"  ");
		statLoreList.add("");
		statLoreList.add("§7K/D : §a"+hrwData.kill + "§f /§a "+hrwData.death+"  ");
		statLoreList.add("§7적에게 준 피해량 : §a"+hrwData.dealAmt+"  ");
		statLoreList.add("§7최대 연속 처치 : §a"+hrwData.maxStackKill+"  ");
		statLoreList.add("");
		statLoreList.add("§7막은 피해량 : §a"+hrwData.barrierAmt+"  ");
		statLoreList.add("§7적을 밀쳐낸 횟수 : §a"+stat_knockback+"  ");
		statLoreList.add("§7돌진을 맞춘 횟수 : §a"+stat_dashHit+"  ");
		statLoreList.add("§7적을 기절시킨 시간 : §a"+hrwData.faintTime+"초"+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
	}
	
	@Override
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
		guardianTimer();
		applyItems();
	}
	
	public void setEquipment(Player p) {
		p.getInventory().setItem(0, weapon);
		p.getInventory().setHeldItemSlot(8);
		MyUtility.setMaxHealth(p, health);
		MyUtility.healUp(p);
		lastInvenMap = p.getInventory().getContents();
		lastArmorMap = p.getInventory().getArmorContents();
	}
	
	
	@Override
	public void setUI() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(hrw.ingame) {
			String team = hrw.getTeam(playerName);
			
			strList.clear();
			
			SidebarString line;
			String tmpStr;
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString((isNoHeal() ? ChatColor.RED : ChatColor.GREEN)+"HP "+ChatColor.YELLOW+(int)p.getHealth()+"/"+(int)p.getMaxHealth());
			strList.add(line);
			
			line = new SidebarString((isNoMove() ? ChatColor.RED : ChatColor.GREEN)+"SPD "+ChatColor.YELLOW+MyUtility.getTwoRound(p.getWalkSpeed()*4));
			strList.add(line);
			
			line = new SidebarString((isNoAttack() ? ChatColor.RED : ChatColor.GREEN)+"K/D "+ChatColor.YELLOW+(int)hrwData.kill+"/"+(int)hrwData.death);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_primarySkill > ) {
				tmpStr = "§4Main §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aMain §6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "§4Prim §6Blocked";
			} else if(p.isBlocking()) {
				tmpStr = "§4Prim §6Using";
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "§4Tert §6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "§aTert §6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Ulmt §6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "§aUlmt §6Ready";
					}else {
						tmpStr = "§4Ulmt §6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "§4Ulmt §6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "§aUlmt §6Ready";
					}
				}			
			} else {
				tmpStr = "§4Ulmt §6구매필요";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "§cPasv §6Blocked";
			} else if(passive) {
				tmpStr = "§aPasv §6사용중";
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			if(team.equalsIgnoreCase("BLUE")) {
				if(hrw.blueTeam.leftCarrot > 0) {
					tmpStr = "§c남은 공물 : §e"+hrw.blueTeam.leftCarrot+"개";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
					else point1Team = "§6§l중립";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "§b§l블루";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "§4§l레드";
					else point2Team = "§6§l중립";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "§b§l블루";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "§4§l레드";
					else point3Team = "§6§l중립";
					
					tmpStr = "§e§l엔더 신전 "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§f§l중앙 신전 "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§c§l네더 신전 "+point3Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
				}
			} else {
				if(hrw.redTeam.leftCarrot > 0) {
					tmpStr = "§c남은 공물 : §e"+hrw.redTeam.leftCarrot+"개";
					line = new SidebarString(tmpStr);
					strList.add(line);
				} else {
					String point1Team = null;
					String point2Team = null;
					String point3Team = null;
					if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
					else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
					else point1Team = "§6§l중립";
							
					if(hrw.point2.catchTeam.equalsIgnoreCase("BLUE")) point2Team = "§b§l블루";
					else if(hrw.point2.catchTeam.equalsIgnoreCase("RED")) point2Team = "§4§l레드";
					else point2Team = "§6§l중립";
					
					if(hrw.point3.catchTeam.equalsIgnoreCase("BLUE")) point3Team = "§b§l블루";
					else if(hrw.point3.catchTeam.equalsIgnoreCase("RED")) point3Team = "§4§l레드";
					else point3Team = "§6§l중립";
					
					tmpStr = "§e§l엔더 신전 "+point1Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§f§l중앙 신전 "+point2Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
					tmpStr = "§c§l네더 신전 "+point3Team;
					line = new SidebarString(tmpStr);
					strList.add(line);
				}
			}
			
			line = new SidebarString("---------------");
			strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
			ccUI(p);
		} else {
			sidebar.hideFrom(p);
			Bukkit.getLogger().info("hiding");
		}
	}
	
	@Override
	public void setUI_Ulaf() {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(hrw.ingame) {
			String team = hrw.getTeam(playerName);
			
			strList.clear();
			
			SidebarString line;
			String tmpStr;
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			line = new SidebarString((isNoHeal() ? ChatColor.RED : ChatColor.GREEN)+"HP "+ChatColor.YELLOW+(int)p.getHealth()+"/"+(int)p.getMaxHealth());
			strList.add(line);
			
			line = new SidebarString((isNoMove() ? ChatColor.RED : ChatColor.GREEN)+"SPD "+ChatColor.YELLOW+MyUtility.getTwoRound(p.getWalkSpeed()*4));
			strList.add(line);
			
			line = new SidebarString((isNoAttack() ? ChatColor.RED : ChatColor.GREEN)+"K/D "+ChatColor.YELLOW+(int)hrwData.kill+"/"+(int)hrwData.death);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
			
			/*if(nowCooldown_primarySkill > ) {
				tmpStr = "§4Main §6"+nowCooldown_primarySkill;
			} else {
				tmpStr = "§aMain §6Ready";
			}*/
			
			if(isNoSkill()) {
				tmpStr = "§4Prim §6Blocked";
			} else if(p.isBlocking()) {
				tmpStr = "§4Prim §6Using";
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(cooldown.getLeftCooldown("teritary") > 0) {
				tmpStr = "§4Tert §6"+cooldown.getLeftCooldown("teritary");
			} else {
				tmpStr = "§aTert §6Ready";
			}
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Ulmt §6Blocked";
			} else if(ultimate) {
				if(percentMode) {
					int percent = cooldown.getUltimatePercent();
					if(percent >= 100) {
						tmpStr = "§aUlmt §6Ready";
					}else {
						tmpStr = "§4Ulmt §6"+percent+"%";
					}
				}else {
					if(cooldown.getLeftCooldown("ultimate") > 0) {
						tmpStr = "§4Ulmt §6"+cooldown.getLeftCooldown("ultimate");
					} else {
						tmpStr = "§aUlmt §6Ready";
					}
				}			
			} else {
				tmpStr = "§4Ulmt §6구매필요";
			}
			
			line = new SidebarString(tmpStr);
			strList.add(line);

			
			if(isNoSkill()) {
				tmpStr = "§cPasv §6Blocked";
			} else if(passive) {
				tmpStr = "§aPasv §6사용중";
			} else {
				tmpStr = "§4Pasv §6구매필요";
			}

			line = new SidebarString(tmpStr);
			strList.add(line);

			line = new SidebarString("---------------");
			strList.add(line);
			
			String point1Team = null;
			if(hrw.point1.catchTeam.equalsIgnoreCase("BLUE")) point1Team = "§b§l블루";
			else if(hrw.point1.catchTeam.equalsIgnoreCase("RED")) point1Team = "§4§l레드";
			else point1Team = "§6§l중립";
			
			tmpStr = "§e§l점령지 "+point1Team;
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			line = new SidebarString("---------------");
			strList.add(line);
					
			sidebar.setEntries(strList);
			sidebar.update();
			sidebar.showTo(p);
			ccUI(p);
		} else {
			sidebar.hideFrom(p);
			Bukkit.getLogger().info("hiding");
		}
	}
	
	public int calcDamage(Player victim , Player damager, int damage) {
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return -1;
		if(p.isBlocking()) {
			if(shield > 0) {
				if(victim.getLocation().distance(p.getLocation()) <= 10) {
					shield -= damage;
					if(shield <= 0) {
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.5f, 0.5f);
						shield = 0;
					} else {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 1.5f, 0.1f);
						victim.getWorld().playSound(victim.getLocation(), Sound.BLOCK_METAL_BREAK, 1.5f, 0.1f);
					}
					p.setLevel(shield);
					p.setExp((float)shield/(!passive? 100 : 150));
					hrwData.barrierAmt += damage;
					return 0;
				}
			}
		}
		return -1;
	}
	
	public void guardianTimer() {
		timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
			public void run() {
				Player p = Bukkit.getPlayer(playerName);
				if(!hrw.ingamePlayer.contains(playerName)) Bukkit.getScheduler().cancelTask(timerId);
				if(!exitsPlayer(p)) return;
				if(!hrw.ingame) Bukkit.getScheduler().cancelTask(timerId);
				if(p.isBlocking()) {
					if(++timerTime2 > 1) {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 2.0f, 3.5f);
						timerTime2 = 0;
					}
					//ShieldEffect particle = new ShieldEffect(effectManager);
					//particle.setLocation(p.getLocation());
					//particle.particle = Particle.REDSTONE;
					//particle.color = team.teamColor;
					//particle.period = 1;
					//particle.iterations = 1;
					//particle.radius = 3d;
					//particle.particles = 20;
					//particle.start();
					timerTime = 0;
				} else {
					if(!isNoSkill()) {
						if(shield < (!passive? 100 : 150)) {
							if(++timerTime >= 4) {
								timerTime = 0;
								shield += 10;
								p.playSound(p.getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1.0F, 3.0F);
							}
							if(shield > (!passive ? 100 : 150)){
								shield = !passive ? 100 : 150;
							}
						}
						p.setLevel(shield);
						p.setExp((float)shield/(!passive? 100 : 150));
					}		
				}
			}
		}, 0l, 10l);
	}
	
	/*public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§c스킬 사용 불가 상태입니다.", 40);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			
			ItemStack item = p.getInventory().getItemInMainHand();
			if(item.equals(weapon)) {
				if(p.isBlocking()) isShielding = true;
			}
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}*/
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			cooldown.setCooldown("secondary");
			
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0f, 0.1f);
			p.getWorld().spawnParticle(Particle.SLIME, p.getLocation(), 1, 1.5F, 1.5f, 1.5f, 0.1f);
			for(Player t : getEnemyList(p.getName())) {
				if(exitsPlayer(t)) {
					double dis = t.getLocation().distance(p.getLocation());
					if(dis <= 7){
						Vector v = t.getLocation().toVector().subtract(p.getLocation().toVector());
						v.normalize();
						try {
							v.checkFinite();
						}catch (Exception e) {
							continue;
						}
						v.multiply(7.5-dis);
						t.setVelocity(v);
						t.getWorld().playSound(t.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 1.0f, 2.0f);
						t.getWorld().spawnParticle(Particle.SWEEP_ATTACK, t.getLocation(), 1, 0.01f, 0.01f, 0.01f, 1f);
						stat_knockback += 1;
					}
				}
			}
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			cooldown.setCooldown("teritary");
			
			isDashing = true;
			Location pl = p.getLocation();
			pl.setPitch(0);
			p.teleport(pl, TeleportCause.PLUGIN);
			
			Location l = p.getEyeLocation();
			l.setPitch(0);
			Vector v = l.getDirection();
			v.multiply(0.7);
			
			dashPitch = p.getEyeLocation().getPitch();
			dashYaw = p.getEyeLocation().getYaw();
			dashTarget = null;
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 30;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					boolean stopFlag = false; 
					if(sch.schTime-- < 0) {
						stopFlag = true;
					}
					Location tl = p.getLocation().add(v);
					Material material = tl.getBlock().getType();
					if(material != Material.AIR
							&& material != Material.YELLOW_FLOWER
							&& material != Material.CHORUS_FLOWER
							&& material != Material.RED_ROSE
							&& material != Material.CHORUS_PLANT
							&& material != Material.DOUBLE_PLANT
							&& material != Material.LADDER
							&& material != Material.RED_MUSHROOM
							&& material != Material.SAPLING
							&& material != Material.GRASS_PATH
							&& material != Material.LONG_GRASS
							&& material != Material.TORCH
							&& material != Material.VINE
							&& material != Material.WHEAT
							&& material != Material.SEEDS
							&& material != Material.CHORUS_PLANT
							///////반블럭들
							&& material != Material.STEP
							&& material != Material.DOUBLE_STEP
							&& material != Material.WOOD_DOUBLE_STEP
							&& material != Material.WOOD_STEP){
						stopFlag = true;
					}
					if(dashTarget == null) {
						List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 1, false);
						if(hit != null) {
							dashTarget = hit.get(0);
							customWeakness(dashTarget, p, 3, false);
							stat_dashHit += 1;
						}
					} else {
						if(!stopFlag) {
							if(!p.isDead()) {
								Location dl = p.getLocation();
								dl.add(l.getDirection().multiply(3));
								dl.setYaw(dashYaw * -1);
								dl.setPitch(dashPitch);
								dashTarget.teleport(dl, TeleportCause.PLUGIN);
							}else {
								stopFlag = true;
							}
						}										
					}
					if(!stopFlag) {
						p.setVelocity(v);
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_METAL_BREAK, 1.0f, 0.4f);
						p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 1, 0.5F, 0.5f, 0.5f, 0.25f);
					} else {
						sch.cancelTask(true);
						if(!p.isDead()) {
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.25f);
							if(dashTarget != null) {
								customDamage(dashTarget, p, 18, false);
								customFaint(dashTarget, p, 2, false);
								dashTarget.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, dashTarget.getLocation(), 1, 0.5F, 0.5f, 0.5f, 0.25f);
							}
						}					
						isDashing = false;			
					}
				}
			}, 0l, 1l);

		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
			
	}
	
	public void ultimateSkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p) || !p.isOnline()) return;
		if(!ultimate) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"먼저 궁극기를 구매하셔야합니다.", 70);
			return;
		}
		
		if(cooldown.checkCooldown("Ultimate")){
			if(isNoSkill()) {
				ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			if(p.getLocation().getBlockY() - MyUtility.getGroundLocation(p.getLocation()).getBlockY() > 2) {
				ActionBarAPI.sendActionBar(p, "§2§l공중에서는 사용이 불가능합니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("Ultimate");
			blockPerUlti = true;
			Location tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()-85);
			Vector direction1 = tmp.getDirection();
			direction1.setY(0);
			direction1.multiply(4);
			tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()+85);
			Vector direction2 = tmp.getDirection();
			direction2.setY(0);
			direction2.multiply(4);
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 0.01f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			
			vp.c_speed = 1f;
			vp.c_distance = 10;
			vp.c_time = 20;
			vp.c_surface = true;
			vp.c_pHorizenOffset = 6.0f;
			vp.c_pVerticalOffset = 0.5f;
			vp.c_removeOnHit = false;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					Location l1 = vp.projectileLoc.clone();
					l1.add(direction1);
					
					Location l2 = vp.projectileLoc.clone();
					l2.add(direction2);
					
					LineEffect ef = new LineEffect(effectManager);
					ef.particle = Particle.CRIT;
					ef.iterations = 1;
					ef.setLocation(l1);
					ef.setTargetLocation(l2);
					ef.start();
					
					//vp.projectileLoc.getWorld().spawnParticle(Particle.FLAME, vp.projectileLoc, 1, 0F, 0f, 0f, 0f);
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					for(Player hit : vp.hitPlayer) {
						hit.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, hit.getLocation(), 1, 0.1F, 0.1f, 0.1f, 0.1f);
						hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 2.0f);
						customDamage(hit, p, 8, false);
						customFaint(hit, p, 3, false);
					}			
				}
			});
				
			vp.launchProjectile();
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					blockPerUlti = false;
				}
			}, 60l);
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(isDashing) {
			if(dashYaw != e.getTo().getYaw() || dashPitch != e.getTo().getPitch()) {
				e.getTo().setPitch(dashPitch);
				e.getTo().setYaw(dashYaw);
			}	
		} else {
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
	}
	
	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = Bukkit.getPlayer(playerName);
		Player p = (Player) e.getEntity();
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			if(hrw.playerMap.get(d.getName()).ability.isNoAttack() || isDashing) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(d, "§2§l공격 불가 상태입니다.", 40);
				return;
			} 
			e.setDamage(0);
			if(hrw.getHeldMainItemName(d).contains(abilityName)) {
				HRWPlayer victimHrwp = hrw.playerMap.get(p.getName());
				if(!p.getName().equalsIgnoreCase(d.getName())) {			
					victimHrwp.lastDamager = d.getName();
					hrwData.dealAmt += dmg;
				}
				
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
						if(tailsman_heal) customHeal(d, d, 1, true);
					}
				}
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	@Override
	public void onItemDrop(PlayerDropItemEvent e) {
		if(e.getPlayer().isSneaking()) ultimateSkill();
	}
	
	@Override
	public void onMouseClick(PlayerInteractEvent e) {
		
	}
	
	@Override
	public void onSneak(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if(p.isBlocking() && !p.isSneaking()) {
			secondarySkill();
		}
	}
}
