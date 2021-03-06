package Minigames.HeroesWar.Abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.coloredcarrot.api.sidebar.Sidebar;
import com.coloredcarrot.api.sidebar.SidebarString;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.HeroesWar.HRWBase;
import Minigames.HeroesWar.HRWPlayer;
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import de.slikey.effectlib.EffectManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class Mercenary extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	public EffectManager effectManager;
	
	//용병 전용
	public int using = 1;
	
	//용병 스탯
	public int stat_knockback = 0;
	public int stat_avoid = 0;
	
	public Mercenary(HRWBase hrw, Player p, String abilityName, Team team) {
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지 설정
		dmg = 8;

		weapon = new ItemStack(Material.GOLD_SWORD, 1);
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
		cooldown_primarySkill = 1;
		cooldown_secondarySkill = 1;
		cooldown_teritarySkill = 1;
		cooldown_ultimateSkill = 30;
		
		//체력설정
		health = 64;

		
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
		p.openInventory(hrw.inven_desc_mercenary);
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
		statLoreList.add("§7적을 중독시킨 시간 : §a"+hrwData.poisonTime+"초"+"  ");
		statLoreList.add("§7적을 크게 밀쳐낸 횟수 : §a"+stat_knockback+"  ");
		statLoreList.add("§7공격을 회피한 횟수 : §a"+stat_avoid+"  ");
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
	public void respawned() {
		cooldown.setCooldownSpec("teritary", 4);
	}
	
	@Override
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(using == 3) using = 1;
		Player p = (Player) e.getEntity();
		lastDeathLoc = p.getLocation().add(0,0.5,0);		
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
			} else if(using == 1) {
				tmpStr = "§aPrim §bUsing";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(using == 2) {
				tmpStr = "§aSecd §bUsing";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(using == 3) {
				tmpStr = "§aTert §bUsing";
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
				tmpStr = "§4Pasv §6Blocked";
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
			} else if(using == 1) {
				tmpStr = "§aPrim §bUsing";
			} else if(cooldown.getLeftCooldown("primary") > 0) {
				tmpStr = "§4Prim §6"+cooldown.getLeftCooldown("primary");
			} else {
				tmpStr = "§aPrim §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Secd §6Blocked";
			} else if(using == 2) {
				tmpStr = "§aSecd §bUsing";
			} else if(cooldown.getLeftCooldown("secondary") > 0) {
				tmpStr = "§4Secd §6"+cooldown.getLeftCooldown("secondary");
			} else {
				tmpStr = "§aSecd §6Ready";
			}
			
			
			line = new SidebarString(tmpStr);
			strList.add(line);
			
			if(isNoSkill()) {
				tmpStr = "§4Tert §6Blocked";
			} else if(using == 3) {
				tmpStr = "§aTert §bUsing";
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
				tmpStr = "§4Pasv §6Blocked";
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
			if(using == 1) {
				ActionBarAPI.sendActionBar(p, "§2§l이미 선택된 스킬입니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("primary");
			cooldown.setCooldown("secondary");
			cooldown.setCooldown("teritary");
			if(using == 3) p.setWalkSpeed(p.getWalkSpeed() - (0.25f/4f));
			ActionBarAPI.sendActionBar(p, "§2§l독검 선택됨", 40);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.2f, 0.25f);
			using = 1;
			
			
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
			if(using == 2) {
				ActionBarAPI.sendActionBar(p, "§2§l이미 선택된 스킬입니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("primary");
			cooldown.setCooldown("secondary");
			cooldown.setCooldown("teritary");
			if(using == 3) p.setWalkSpeed(p.getWalkSpeed() - (0.25f/4f));
			ActionBarAPI.sendActionBar(p, "§2§l백병전 선택됨", 40);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.2f, 0.25f);
			using = 2;
			
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
			if(using == 3) {
				ActionBarAPI.sendActionBar(p, "§2§l이미 선택된 스킬입니다.", 40);
				p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
				return;
			}
			cooldown.setCooldown("primary");
			cooldown.setCooldown("secondary");
			cooldown.setCooldown("teritary");
			if(using == 3) p.setWalkSpeed(p.getWalkSpeed() - (0.35f/4f));
			ActionBarAPI.sendActionBar(p, "§2§l전력질주 선택됨", 40);
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.2f, 0.25f);
			p.setWalkSpeed(p.getWalkSpeed() + (0.25f/4f));
			using = 3;
			
		} else {
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
			Block b = p.getTargetBlock(null, 30);
			if(b.getType() == Material.AIR) {
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "해당 위치에는 사용이 불가능합니다.");
			} else {
				cooldown.setCooldown("ultimate");
				Location tmpL = b.getLocation();
				Location pl = p.getLocation();
				if(b.getRelative(0, 2, 0).getType() != Material.AIR) {
					tmpL = MyUtility.getUpLocation(b.getLocation());
				}
				p.getWorld().spawnParticle(Particle.CLOUD, pl, 2, 0.2F, 0.2f, 0.2f, 0.2f);
				p.getWorld().spawnParticle(Particle.CLOUD, tmpL, 2, 0.2F, 0.2f, 0.2f, 0.2f);
				
				PlayerInventory pInven = p.getInventory();
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				NPC npc = registry.createNPC(EntityType.PLAYER, playerName);
				npc.getTrait(Equipment.class).set(EquipmentSlot.HAND, weapon);
				npc.getTrait(Equipment.class).set(EquipmentSlot.HELMET, pInven.getHelmet());
				npc.getTrait(Equipment.class).set(EquipmentSlot.CHESTPLATE, pInven.getChestplate());	
				npc.getTrait(Equipment.class).set(EquipmentSlot.LEGGINGS, pInven.getLeggings());	
				npc.getTrait(Equipment.class).set(EquipmentSlot.BOOTS, pInven.getBoots());
				npc.spawn(p.getLocation());
				
				p.getWorld().playSound(pl, Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 0.2f);
				p.getWorld().playSound(tmpL, Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 0.2f);
				p.teleport(tmpL.add(0,2,0), TeleportCause.PLUGIN);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
					public void run() {
						npc.destroy();
						p.getWorld().playSound(pl, Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 1.0f);
						p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.2f, 1.0f);
						p.getWorld().spawnParticle(Particle.CLOUD, pl, 2, 0.2F, 0.2f, 0.2f, 0.2f);
					}
				}, 80l);
				//이펙트 및 사운드
			}

		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e) {
		Player d = Bukkit.getPlayer(playerName);
		Player p = (Player) e.getEntity();
		if(e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE) {
			if(hrw.playerMap.get(d.getName()).ability.isNoAttack()) {
				e.setCancelled(true);
				ActionBarAPI.sendActionBar(d, "§2§l공격 불가 상태입니다.", 40);
				return;
			} 
			if(hrw.getHeldMainItemName(d).contains(abilityName)) {			
				e.setDamage(0);	
				int resDmg = dmg+addtionDamage;
				if (using == 1) {
					//ActionBarAPI.sendActionBar(d, "§2§l중독 성공", 40);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.2f, 1.7f);
					customPoison(p, d, 10, false);
				} else if (using == 2) {
					//ActionBarAPI.sendActionBar(d, "§2§l밀쳐냄!", 40);
					Vector v = d.getEyeLocation().getDirection();
					v.setY(0);
					v.normalize();
					v.multiply(2.0f);
					p.setVelocity(v);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1.2f, 0.75f);
					stat_knockback += 1;
				}			
				
				HRWPlayer victimHrwp = hrw.playerMap.get(p.getName());
				if(!p.getName().equalsIgnoreCase(d.getName())) {			
					victimHrwp.lastDamager = d.getName();
					hrwData.dealAmt += dmg;
				}
				
				int excep = victimHrwp.ability.onHitted(p, d, resDmg);
				if(excep != -1) {
					resDmg = excep;
				} 
				if(p.getHealth() - resDmg <= 0) {
					Ability ab = hrw.playerMap.get(p.getName()).ability;
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
							//if(tailsman_heal) customHeal(d, d, 1, true);
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
			}
		}
	}
	
	@Override
	public int onHitted(Player victim, Player damager, int damage) {
		if(invincible) return 0;
		if (passive) {
			if (MyUtility.getRandom(1, 10) <= 1) {
				Location tl = damager.getLocation().subtract(damager.getEyeLocation().getDirection());
				tl.setY(damager.getLocation().getY());
				victim.teleport(tl, TeleportCause.PLUGIN);
				victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.2f, 0.5f);
				ActionBarAPI.sendActionBar(victim, "§2§l회피 성공", 40);
				stat_avoid += 1;
				return (int) (damage / 2);
			}
		}
		int calcDamage = tankerCheck(victim, damager ,damage);
		return calcDamage; //-1은 적용안함, 그 외는 체력을 이걸로 설정해달라
	}
	
}
