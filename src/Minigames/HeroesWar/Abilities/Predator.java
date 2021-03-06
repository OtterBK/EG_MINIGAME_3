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
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.effect.CircleEffect;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.WarpEffect;

public class Predator extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);

	////포식자 스탯
	public int stat_grab = 0;
	public int stat_portalCnt = 0;
	
	public Predator(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 5;
		

		weapon = new ItemStack(Material.SHEARS, 1);
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
		cooldown_primarySkill = 12;
		cooldown_secondarySkill = 9;
		cooldown_teritarySkill = 0;
		cooldown_ultimateSkill = 100;
		
		//체력설정
		health = 113;

		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		setEquipment(p);
		
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
		p.openInventory(hrw.inven_desc_predator);
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
		statLoreList.add("§7적을 포획한 횟수 : §a"+stat_grab+"  ");
		statLoreList.add("§7포탈을 설치한 횟수 : §a"+stat_portalCnt+"  ");
		statLoreList.add("§7적을 기절시킨 시간 : §a"+hrwData.faintTime+"초"+"  ");
		statLoreList.add("§7적의 이동속도를 저해한 시간 : §a"+hrwData.speedSlowTime+"초"+"  ");
		statLoreList.add("");
		meta.setLore(statLoreList);
		statItem.setItemMeta(meta);
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
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("primary") && !p.isDead()){
			cooldown.setCooldown("primary");
			noCC = true;
			EGScheduler sch = new EGScheduler(hrw);
			sch.schTime = 8;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
				public void run() {
					if(sch.schTime-- > 0) {
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 0.1f);
						CircleEffect ef1 = new CircleEffect(effectManager);
						ef1.particle = Particle.REDSTONE;
						ef1.color = Color.fromRGB(0, 204, 0);
						ef1.angularVelocityX = 0;
						ef1.angularVelocityY = 0;
						ef1.angularVelocityZ = 0;
						ef1.particleSize = 1;
						ef1.wholeCircle = true;
						ef1.particleOffsetX = 0.1f;
						ef1.particleOffsetZ = 0.1f;
						ef1.particleOffsetY = 0.1f;
						ef1.particleCount = 2;
						ef1.radius = 1f;
						ef1.period = 1;
						ef1.iterations = 1;
						ef1.setLocation(p.getLocation().add(0,1,0));
						ef1.start();	
					} else {
						sch.cancelTask(true);
						noCC = false;
					}
				}
			}, 0l, 10l);
			
			
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
			cooldown.setCooldown("secondary");
		
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_DONKEY_CHEST, 1.2f, 0.1f);
			LineEffect ef = new LineEffect(effectManager);
			ef.particle = Particle.REDSTONE;
			ef.color = Color.fromRGB(0, 0, 0);
			ef.iterations = 1;
			ef.setLocation(p.getEyeLocation().add(p.getEyeLocation().getDirection()));
			ef.setTargetLocation(p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(3)).add(p.getEyeLocation().getDirection()));
			ef.start();
			List<Player> hit = hrw.getCorsurPlayer(p, getEnemyList(p.getName()), 5, false);
			if(hit != null) {	
				customSpeedUp(p, p, 0.25f, 5, false);
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 255));
				CircleEffect ef1 = new CircleEffect(effectManager);
				ef1.particle = Particle.REDSTONE;
				ef1.color = Color.fromRGB(00, 00, 00);
				ef1.angularVelocityX = 0;
				ef1.angularVelocityY = 0;
				ef1.angularVelocityZ = 0;
				ef1.particleSize = 1;
				ef1.wholeCircle = true;
				ef1.particleOffsetX = 0.1f;
				ef1.particleOffsetZ = 0.1f;
				ef1.particleOffsetY = 0.1f;
				ef1.particleCount = 2;
				ef1.radius = 1f;
				ef1.period = 50;
				ef1.iterations = 2;
				ef1.setEntity(p);
				ef1.start();	
				
				CircleEffect ef2 = new CircleEffect(effectManager);
				ef2.particle = Particle.REDSTONE;
				ef2.color = Color.fromRGB(00, 00, 00);
				ef2.angularVelocityX = 0;
				ef2.angularVelocityY = 0;
				ef2.angularVelocityZ = 0;
				ef2.particleSize = 1;
				ef2.wholeCircle = true;
				ef2.particleOffsetX = 0.1f;
				ef2.particleOffsetZ = 0.1f;
				ef2.particleOffsetY = 0.1f;
				ef2.particleCount = 2;
				ef2.radius = 1f;
				ef2.period = 50;
				ef2.iterations = 2;
				ef2.setEntity(p);
				ef2.start();
				
				Player t = hit.get(0);
				Team team = hrw.playerMap.get(t.getName()).team;
				Location pl = t.getLocation();
				Location l1 = team.loc_base1;
				Location l2 = team.loc_base2;	
				//p.sendMessage(""+pl);
				//p.sendMessage(""+l1);
				//p.sendMessage(""+l2);
				if(l1.getX() < pl.getX() &&
						l2.getX() > pl.getX() &&
						l1.getY() < pl.getY() &&
						l2.getY() > pl.getY() &&
						l1.getZ() < pl.getZ() &&
						l2.getZ() > pl.getZ()) {
					ActionBarAPI.sendActionBar(t, "§c§l기지안에서는 데미지를 입지 않습니다.", 60);
					ActionBarAPI.sendActionBar(t, "§c§l기지안의 적을 끌어올 수 없습니다.", 60);
					return;
				}
				customFaint(t, p, 3, false);
				stat_grab += 1;
				
				EGScheduler sch = new EGScheduler(hrw);
				sch.schTime = 20;
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
					public void run() {
						if(p.isDead()) sch.cancelTask(true);
						if(sch.schTime-- > 0) {
							p.getWorld().playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.2f, 2.0f);
							Location l = p.getLocation().add(t.getLocation().getDirection());
							l.setYaw(l.getYaw()*-1);
							l.setY(p.getLocation().getY());
							t.teleport(l, TeleportCause.PLUGIN);
						} else {
							sch.cancelTask(true);
							customSpeedUp(t, p, 0.3f, 4, false);
						}
					}
				}, 0l, 2l);
			}
			
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	/*public void teritarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
			return;
		}
		if(cooldown.checkCooldown("teritary") && !p.isDead()){
			cooldown.setCooldown("teritary");
			
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SHULKER_BOX_CLOSE, 2.0f, 2.0f);
			
			CircleEffect ef1 = new CircleEffect(effectManager);
			ef1.particle = Particle.REDSTONE;
			ef1.color = Color.fromRGB(00, 56, 66);
			ef1.angularVelocityX = 0;
			ef1.angularVelocityY = 0;
			ef1.angularVelocityZ = 0;
			ef1.particleSize = 1;
			ef1.wholeCircle = true;
			ef1.particleOffsetX = 0.1f;
			ef1.particleOffsetZ = 0.1f;
			ef1.particleOffsetY = 0.1f;
			ef1.particleCount = 2;
			ef1.radius = 2f;
			ef1.period = 3;
			ef1.iterations = 3;
			ef1.setLocation(p.getLocation().add(0,1,0));
			ef1.start();	
			
			CircleEffect ef2 = new CircleEffect(effectManager);
			ef2.particle = Particle.REDSTONE;
			ef2.color = Color.fromRGB(00, 56, 66);
			ef2.angularVelocityX = 0;
			ef2.angularVelocityY = 0;
			ef2.angularVelocityZ = 0;
			ef2.particleSize = 1;
			ef2.wholeCircle = true;
			ef2.particleOffsetX = 0.1f;
			ef2.particleOffsetZ = 0.1f;
			ef2.particleOffsetY = 0.1f;
			ef2.particleCount = 2;
			ef2.radius = 1f;
			ef2.period = 3;
			ef2.iterations = 3;
			ef2.setLocation(p.getLocation().add(0,1,0));
			ef2.start();
			
			for(Player t : getEnemyList(p.getName())) {
				if(t.getLocation().distance(p.getLocation()) < 2) {
					customDamage(t, p, 7, false);
					customWeakness(t, p, 2, false);
				}
			}

		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
			
	}*/
	
	public void ultimateSkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p) || !p.isOnline()) return;
		if(!ultimate) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"먼저 궁극기를 구매하셔야합니다.", 70);
			return;
		}
		
		if(cooldown.checkCooldown("ultimate")){
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
			
			Block b = p.getTargetBlock(null, 120);
			if(b.getType() == Material.AIR) {
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "해당 위치에는 설치가 불가능합니다.");
			} else {
				Location l = b.getRelative(0,1,0).getLocation();
				Location tmpL = null;
				if(b.getRelative(0, 2, 0).getType() != Material.AIR) {
					tmpL = MyUtility.getUpLocation(b.getLocation());
				}
						
				if(tmpL != null && (tmpL.getBlockY() - l.getBlockY() > 2 || tmpL.getBlock().getType() == Material.BARRIER)) {
					TitleAPI.sendFullTitle(p, 10,40, 20, "", "해당 위치에는 설치가 불가능합니다.");
				} else {
					cooldown.setCooldown("Ultimate");
					stat_portalCnt += 1;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.2f, 0.1f);
					team.predator_portal2 = l;
					Location pl = p.getLocation().getBlock().getLocation();
					team.predator_portal1 = pl.clone();
					Material plType = pl.getBlock().getType();
					team.predator_portal1_backupType = plType;
					pl.getBlock().setType(Material.ENDER_PORTAL);
					
					CircleEffect ef3 = new CircleEffect(effectManager);
					ef3.particle = Particle.REDSTONE;
					ef3.color = team.teamColor;
					ef3.angularVelocityX = 0;
					ef3.angularVelocityY = 0;
					ef3.angularVelocityZ = 0;
					ef3.particleSize = 1;
					ef3.wholeCircle = true;
					ef3.particleOffsetX = 0.1f;
					ef3.particleOffsetZ = 0.1f;
					ef3.particleOffsetY = 0.1f;
					ef3.radius = 2f;
					ef3.period = 40;
					ef3.iterations = 40;
					ef3.setLocation(pl.clone().add(0,0.5,0));
					ef3.start();	
					
					CircleEffect ef4 = new CircleEffect(effectManager);
					ef4.particle = Particle.REDSTONE;
					ef4.color = team.teamColor;
					ef4.angularVelocityX = 0;
					ef4.angularVelocityY = 0;
					ef4.angularVelocityZ = 0;
					ef4.particleSize = 1;
					ef4.wholeCircle = true;
					ef4.particleOffsetX = 0.1f;
					ef4.particleOffsetZ = 0.1f;
					ef4.particleOffsetY = 0.1f;
					ef4.radius = 1f;
					ef4.period = 40;
					ef4.iterations = 40;
					ef4.setLocation(pl.clone().add(0,0.5,0));
					ef4.start();
					
					WarpEffect ef5 = new WarpEffect(effectManager);
					ef5.particle = Particle.REDSTONE;
					ef5.color = team.teamColor;
					ef5.radius = 1f;
					ef5.period = 8;
					ef5.iterations = 200;
					ef5.setLocation(l);
					ef5.start();
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
						public void run() {
							if(team.predator_portal1 != null && team.predator_portal1_backupType != null) {
								team.predator_portal1.getBlock().setType(team.predator_portal1_backupType);
								team.predator_portal1 = null;
								team.predator_portal2 = null;
							}
							if(hrw.ingame && hrw.ingamePlayer.contains(p.getName())) {
								p.getWorld().playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1.2f, 2.0f);
								TitleAPI.sendFullTitle(p, 10,40, 20, "", "차원 닫힘");
							}												
						}
					}, 1900l);
				}
			}
		} else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onPlayerKill(Player t) {
		if(passive) {
			if(!isNoSkill()) {
				Player p = Bukkit.getPlayer(playerName);
				customHeal(p, p, (int)health/2, true);
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "적을 처치하여 체력회복");
			}		
		}
	}
	
}
