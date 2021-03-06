package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.nametagedit.plugin.NametagEdit;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.HeroesWar;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.VirtualProjectile;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.ShieldEffect;

public class Alchemist extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	//연금술사 전용
	private int reflectDmg = 0; 
	private boolean reflecting = false;
	
	//연금술사 기록 저장용
	private int stat_allReflectDmg = 0;
	private int stat_knockBack = 0;
	private int stat_grab = 0;
	private int stat_buffTime = 0;
	
	public Alchemist(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 5;

		weapon = new ItemStack(Material.FLINT_AND_STEEL, 1);
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
		cooldown_primarySkill = 8;
		cooldown_secondarySkill = 11;
		cooldown_teritarySkill = 15;
		cooldown_ultimateSkill = 45;
		
		//체력설정
		health = 63;

		
		//탱커 등록
		team.reduceAbilityList.add(this);
		
		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
		makeStatItem();

		alchemistTimer();
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
		p.openInventory(hrw.inven_desc_alchemist);
	}
	
	@Override
	public void updateStatItem() {
		if(statItem == null) return;
		ItemMeta meta = statItem.getItemMeta();
		statLoreList.clear();
		statLoreList.clear();
		statLoreList.add("");
		statLoreList.add("§7영웅 : §a"+abilityName+"  ");
		statLoreList.add("");
		statLoreList.add("§7K/D : §a"+hrwData.kill + "§f /§a "+hrwData.death+"  ");
		statLoreList.add("§7적에게 준 피해량 : §a"+hrwData.dealAmt+"  ");
		statLoreList.add("§7최대 연속 처치 : §a"+hrwData.maxStackKill+"  ");
		statLoreList.add("");
		statLoreList.add("§7적의 이동속도를 저해한 시간 : §a"+hrwData.speedSlowTime+"초"+"  ");
		statLoreList.add("§7적을 밀쳐낸 수 : §a"+stat_knockBack+"  ");
		statLoreList.add("§7적을 끌어당긴 수 : §a"+stat_grab+"  ");
		statLoreList.add("§7반사한 피해 : §a"+stat_allReflectDmg+"  ");
		statLoreList.add("§7아군 치유량 : §a"+hrwData.healAmt+"  ");
		statLoreList.add("§7아군을 강화한 시간 : §a"+stat_buffTime+"초"+"  ");
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
		alchemistTimer();
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
	
	public void alchemistTimer(){
		EGScheduler sch = new EGScheduler(hrw);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
			public void run(){
				if(!hrw.ingame) sch.cancelTask(true);
				if(!hrw.ingamePlayer.contains(playerName)) sch.cancelTask(true);
				if(passive) {
					Player p = Bukkit.getPlayer(playerName);
					if(exitsPlayer(p)) {
						if(p.isDead()) return;
						if(p.getHealth() < p.getMaxHealth()) {
							double newHp = p.getHealth() + 1;
							if(newHp > p.getMaxHealth()) newHp = p.getMaxHealth();
							p.setHealth(newHp);
						}
					}
				}			
			}
		}, 0l, 20l);
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
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
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
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
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
		if(!reflecting) return -1;
		if(victim.getName().equalsIgnoreCase(p.getName()) && !p.isDead()) { //죽지않았고 피해자가 자신이면
			customDamage(damager, p, (int)damage/2, false);
			reflectDmg += damage;
			return 0;
		}
		return -1;
	}

	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§c스킬 사용 불가 상태입니다.", 40);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			
			Location tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()-85);
			Vector direction1 = tmp.getDirection();
			direction1.setY(0);
			direction1.multiply(1.9f);
			tmp = p.getEyeLocation();
			tmp.setYaw(tmp.getYaw()+85);
			Vector direction2 = tmp.getDirection();
			direction2.setY(0);
			direction2.multiply(1.9f);
			
			Location pEye = p.getEyeLocation().clone();
			pEye.setPitch(0);
			Vector knockBack = pEye.getDirection();
			
			List<Player> enemyList = getEnemyList(p.getName());
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_HORSE_JUMP, 1.0f, 0.1f);
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemyList);
			
			vp.c_speed = 0.5f;
			vp.c_distance = 7;
			vp.c_time = 20;
			vp.c_pHorizenOffset = 4.0f;
			vp.c_removeOnHit = false;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					Location l1 = vp.projectileLoc.clone();
					l1.add(direction1);
					
					Location l2 = vp.projectileLoc.clone();
					l2.add(direction2);
					
					LineEffect ef = new LineEffect(effectManager);
					ef.particle = Particle.REDSTONE;
					ef.color = Color.fromRGB(153, 255, 204);
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
						hit.getWorld().spawnParticle(Particle.FALLING_DUST, hit.getLocation(), 10, 0.1F, 0.1f, 0.1f, 0.1f);
						hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_HOSTILE_SWIM, 1.0f, 0.1f);
						customDamage(hit, p, 4, false);
						customSpeedSlow(hit, p, 0.40f, 2, false);
						hit.setVelocity(knockBack.multiply(3.2f));
						stat_knockBack += 1;
					}			
				}
			});
				
			vp.launchProjectile();
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
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
			
			List<Player> hit = hrw.getCorsurPlayer(p, getTeamList(p.getName()), passive ? 17 : 12, false);
			if(hit != null) {
				cooldown.setCooldown("secondary");
				
				Player target = hit.get(0);
				
				p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 0.5F);
				target.playSound(target.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0F, 0.5F);
				TitleAPI.sendFullTitle(target, 0, 60, 0, "§b§l사기고양", "§e§l연금술사가 당신을 강화 시켰습니다.");
				customSpeedUp(target, p, 0.3f, 7, false);
				customDamageUp(target, p, 2, 7, false);
				customSlowHeal(target, p, 40, false);
				
				stat_buffTime += 7;
				
				ActionBarAPI.sendActionBar(p, "§e§l"+target.getName()+" 님을 강화했습니다.", 40);
			}else {
				ActionBarAPI.sendActionBar(p, "§2§l대상이 존재하지 않습니다.", 40);
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
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), passive ? 15 : 10, false);
			if(hit != null) {
				cooldown.setCooldown("teritary");
				
				Player target = hit.get(0);
				
				stat_grab += 1;
				
				EGScheduler sch = new EGScheduler(hrw);
				sch.schTime = 5;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
					public void run() {
						if(sch.schTime > 0) {
							sch.schTime--;
							Vector v = p.getLocation().toVector().subtract(target.getLocation().toVector());
							v.normalize();
							v.multiply(1f);
							target.setVelocity(v);
							target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1.0f, 2.0f);
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1.0f, 2.0f);
						}else {
							sch.cancelTask(true);
						}						
					}
				}, 0l, 10l);
			}else {
				ActionBarAPI.sendActionBar(p, "§2§l대상이 존재하지 않습니다.", 40);
			}

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
			cooldown.setCooldown("ultimate");
			
			reflectDmg = 0;
			reflecting = true;
			blockPerUlti = true;
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 4;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime > 0) {
						sch.schTime--;
						
						ShieldEffect particle = new ShieldEffect(effectManager);
						particle.setLocation(p.getLocation().add(0,0.5f,0));
						particle.particle = Particle.REDSTONE;
						particle.color = Color.fromRGB(200, 255, 255);
						particle.period = 20;
						particle.iterations = 1;
						particle.particleCount = 20;
						particle.radius = 2d;
						particle.start();
										
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 0.1F);
					}else {
						reflecting = false;
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0F, 2.0F);
						sch.cancelTask(true);
						TitleAPI.sendFullTitle(p, 10, 60, 10, "§6성사의 신식 종료", "§e§l"+reflectDmg+" 만큼의 피해를 반사했습니다.");
						stat_allReflectDmg += reflectDmg;
						blockPerUlti = false;
					}
				}
			}, 0l ,20l);
		
		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
}
