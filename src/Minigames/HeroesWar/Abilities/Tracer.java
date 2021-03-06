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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
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
import Minigames.HeroesWar.Team;
import Utility.MyUtility;
import Utility.Vector3D;
import Utility.VirtualProjectile;

public class Tracer extends Ability{

	List<SidebarString> strList = new ArrayList<SidebarString>(7);
	
	////추격자 전용
	private boolean timeBacking = false;
	private int backHealth[] = new int[25];
	private Location backLocation[] = new Location[25];
	private int primarySkillCnt = 4;
	private int moveDir_X = 0;
	private int moveDir_Z = 0;
	
	//추격자 스탯
	public int stat_selfheal = 0;
	public int stat_stickCnt = 0;
	
	public Tracer(HRWBase hrw, Player p, String abilityName, Team team) {
		
		//기본 설정
		super(hrw, p, abilityName, team);
		cooldown = new MyCooldown(this);
		
		//데미지설정
		dmg = 8;
		
		
		weapon = new ItemStack(Material.IRON_AXE, 1);
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
		cooldown_primarySkill = 3;
		cooldown_secondarySkill = 17;
		cooldown_ultimateSkill = 50;
		
		//체력설정
		health = 56;

		String teamName = hrw.getTeam(p);
		SidebarString line1 = new SidebarString("");
		sidebar = new Sidebar((teamName.equalsIgnoreCase("BLUE") ? (ChatColor.BLUE+""+ChatColor.BOLD) : (ChatColor.RED+""+ChatColor.BOLD) )+abilityName, hrw.server, 72000, line1);
		
		for(int i = 0; i < 25; i++) {
			backHealth[i] = (int)p.getHealth();
			backLocation[i] = p.getLocation();
		}
		
		tracerTimer();
		
		setEquipment(p);
		
		makeStatItem();
	}
	
	public void helpMsg(Player p) {
		p.sendMessage("§6============= §f[ §b추격자 §f] - §e직업군 §7: §cDealer §6=============\n"
				+ "§f- §a주스킬 §7: §d축지\n§7전방 5칸앞으로 이동합니다. §c벽을 뚫지는 못합니다. 3초마다 잔여 횟수가 1증가합니다.(최대 4회)\n"
				+ "§f- §a보조스킬 §7: §d역행\n§75초전의 시간으로 이동합니다. §c체력, 위치가 5초전으로 복구됩니다.\n"
				+ "§f- §2궁극기§f(§c구매시 사용가능§f) §7: §d점착폭탄\n§72칸 범위내에 바라보는 플레이어 또는 자신의 위치에 폭탄을 부착합니다. 2.0초후 폭발합니다.\n"
				+ "§f- §2패시브§f(§c구매시 사용가능§f) §7: §d빠른축지\n§7에너지를 더 빠르게 모읍니다. 축지의 횟수가 회복되는 간격이 3초에서 2.4초로 감소합니다.");	
	}
	
	@Override
	public void respawned() {
		cooldown.setCooldownSpec("secondary", 7);
	}
	
	@Override
	public void returned() {
		cooldown.setCooldownSpec("secondary", 7);
	}
	
	@Override
	public void invenHelper(Player p) {
		p.openInventory(hrw.inven_desc_tracer);
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
		statLoreList.add("§7출환으로 회복한 체력 : §a"+stat_selfheal+"  ");
		statLoreList.add("§7부착식 폭탄을 부착한 수 : §a"+stat_stickCnt+"  ");
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
		tracerTimer();
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
	
	public void resetSecondary() {
		for(Location l : backLocation) {
			l = team.loc_spawn;
			Bukkit.getLogger().info(""+l);
		}
	}
	
	public Vector getMovingDirection() {
		Vector v = null;
		
		if(moveDir_X == 0 && moveDir_Z == 0) {
			v = null;
		}else {
			v = new Vector(moveDir_X, 0 ,moveDir_Z);
		}
		//Bukkit.getServer().broadcastMessage(""+moveDir_X+" | "+moveDir_Z);
		
		return v;
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
			} else if(primarySkillCnt > 0) {
				tmpStr = "§aPrim §6"+primarySkillCnt+"회";
			} else {
				tmpStr = "§4Prim §6"+primarySkillCnt+"회";
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
			//Bukkit.getLogger().info("hiding");
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
			} else if(primarySkillCnt > 0) {
				tmpStr = "§aPrim §6"+primarySkillCnt+"회";
			} else {
				tmpStr = "§4Prim §6"+primarySkillCnt+"회";
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
			//Bukkit.getLogger().info("hiding");
		}
	}

	public void tracerTimer(){
		EGScheduler sch = new EGScheduler(hrw);
		sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
			public void run(){
				if(!hrw.ingame) sch.cancelTask(true);
				if(!hrw.ingamePlayer.contains(playerName)) sch.cancelTask(true);
				Player p = Bukkit.getPlayer(playerName);
				if(exitsPlayer(p)) {
					if(primarySkillCnt < 5 && ++sch.schTime > (passive && !isNoSkill() ? (cooldown_primarySkill*5) - 3 : cooldown_primarySkill*5)){
						sch.schTime = 0;
						primarySkillCnt += 1;
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 2.0f);
					}
					if(!timeBacking){
							for(int i = 0; i < backHealth.length-1; i++){
								backHealth[i] = backHealth[i+1];
							}
							for(int i = 0; i < backLocation.length-1; i++){
								backLocation[i] = backLocation[i+1];
							}
							/*for(int i = 0; i < Backtime_count.length-1; i++){
								Backtime_count[i] = Backtime_count[i+1];
							}*/
							backHealth[24] = (int) p.getHealth();
							backLocation[24] = p.getLocation();
							//Backtime_count[24] = tracer.getExp();
					}			
				}
			}
		}, 0l, 5l);
	}
	
	
	/*public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 70);
			return;
		}
		if(primarySkillCnt <= 0){
			ActionBarAPI.sendActionBar(p, "§2§l축지의 사용가능 횟수가 부족합니다.", 80);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		} else {
			primarySkillCnt -= 1;
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.0f, 2.0f);
			//p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 1, 1, 1, 1, 0);
			
	        final Player observer = p;
	       
	        Location observerPos = observer.getEyeLocation();
	        //Vector3D observerDir = new Vector3D(observerPos.getDirection());
	        Vector3D observerDir;
	        Vector v = getMovingDirection();
	        if(v == null) v = observerPos.getDirection();
	        v.normalize();
	        //
//	        p.sendMessage(v+"");
	        boolean checkFinite = false;
	        try {
	        	v.checkFinite();	
	        } catch(Exception IllegalArgumentException) {
	        	checkFinite = true;
	        }
	        
	        if(checkFinite) {
	        	observerDir = new Vector3D(observerPos.getDirection());
	        } else {
	        	observerDir = new Vector3D(v);
	        }	        
	        Vector3D observerStart = new Vector3D(observerPos);
	        
	        Location loc = p.getLocation();
	        
	        for(int i = 1; i < 6; i++){
	            Vector3D observerEnd = observerStart.add(observerDir.multiply(i));
	            loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
	        	if(loc.getBlock().getType() != Material.AIR){
	        		if(i == 1){
	        			loc = p.getLocation();
	        			
	        		} else {
	        			observerEnd = observerStart.add(observerDir.multiply(i-1));
	                    loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
	        		}
	    			break;
	        	}
	        }         
	        loc.setPitch(p.getLocation().getPitch());
	        loc.setYaw(p.getLocation().getYaw());
	        loc.setY(p.getLocation().getY()+0.8f);
	        p.teleport(loc);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.0f, 2.0f);
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 5, 1, 1, 1, 0.1f);
		}
	}*/
	
	public void primarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			return;
		}
		if(isNoMove()) {
			ActionBarAPI.sendActionBar(p, ChatColor.RED+""+ChatColor.BOLD+"이동불가 상태입니다.", 70);
			return;
		}
		if(primarySkillCnt <= 0){
			ActionBarAPI.sendActionBar(p, "§2§l축지의 사용가능 횟수가 부족합니다.", 80);
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		} else {
			primarySkillCnt -= 1;
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.0f, 2.0f);
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 20, 1, 1, 1, 0);
			
	        final Player observer = p;
	        
	        Location observerPos = observer.getEyeLocation();
	        Vector3D observerDir = new Vector3D(observerPos.getDirection());
	        Vector3D observerStart = new Vector3D(observerPos);
	        
	        Location loc = p.getLocation();
	        
	        for(int i = 1; i < 6; i++){
	            Vector3D observerEnd = observerStart.add(observerDir.multiply(i));
	            loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
	        	if(loc.getBlock().getType() != Material.AIR){
	        		if(i == 1){
	        			loc = p.getLocation();
	        			
	        		} else {
	        			observerEnd = observerStart.add(observerDir.multiply(i-1));
	                    loc = new Location(p.getWorld(), observerEnd.x, observerEnd.y, observerEnd.z);
	        		}
	    			break;
	        	}
	        }         
	        loc.setPitch(p.getLocation().getPitch());
	        loc.setYaw(p.getLocation().getYaw());
	        loc.setY(p.getLocation().getY()+0.5f);
	        p.teleport(loc, TeleportCause.PLUGIN);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.0f, 2.0f);
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation(), 20, 1, 1, 1, 0);
		}
	}
	
	public void secondarySkill(){
		Player p = Bukkit.getPlayer(playerName);
		if(!exitsPlayer(p)) return;
		if(!p.getInventory().getItemInMainHand().equals(weapon)) return;
		if(isNoSkill()) {
			ActionBarAPI.sendActionBar(p, "§2§l스킬 사용 불가 상태입니다.", 40);
			return;
		}
		if(cooldown.checkCooldown("secondary") && !p.isDead()){
			timeBacking = true;
			invincible = true;
			cooldown.setCooldown("secondary");
			customWeakness(p, p, 1, true);
			EGScheduler sch = new EGScheduler(hrw);
			int beforeHp = (int) p.getHealth();
			sch.schTime = 24;
			sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
				public void run(){
					if(sch.schTime >= 0){
						p.getWorld().spawnParticle(Particle.DRIP_WATER, p.getLocation(), 3, 0.3f, 0.3f, 0.3f, 0);
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2.0f, 3.0f);
						try{
							if(p.getHealth() < backHealth[sch.schTime]) {
								p.setHealth(backHealth[sch.schTime]);
							}		
							p.setFallDistance(0);
							p.teleport(backLocation[sch.schTime], TeleportCause.PLUGIN);
						} catch(Exception e){
							Bukkit.getScheduler().cancelTask(sch.schId);
							backLocation[0].getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 2.0f, 3.0f);
							ccTimeTo_noMove = System.currentTimeMillis();
							ccTimeTo_noAttack = System.currentTimeMillis();
							ccTimeTo_noSkill = System.currentTimeMillis();
							ccTimeTo_noHeal = System.currentTimeMillis();
							ccTimeTo_noDamaged = System.currentTimeMillis();
							MyUtility.potionClear(p);
							timeBacking = false;
							invincible = false;
						}
						sch.schTime -= 1;
					} else {
						sch.cancelTask(true);
						backLocation[0].getWorld().playSound(p.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 2.0f, 3.0f);
						timeBacking = false;
						invincible = false;
						int healAmt = (int) (p.getHealth() - beforeHp);
						stat_selfheal += healAmt > 0 ? healAmt : 0;
					}					
				}
			}, 0l, 1l);
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
				return;
			}
			
			cooldown.setCooldown("Ultimate");
			blockPerUlti = true;
			List<String> enemyList = hrw.getEnemyList(p.getName());
			List<Player> enemypList = MyUtility.stringListToPlayer(enemyList);
			
			VirtualProjectile vp = new VirtualProjectile(hrw.server, p, enemypList);
			
			vp.c_removeOnBlock = true;
			vp.c_removeOnHit = true;
			
			vp.c_speed = 2;
			vp.c_distance = 2;
			
			vp.setOnDuring(new Runnable() {
				public void run() {
					p.getWorld().spawnParticle(Particle.REDSTONE, vp.projectileLoc, 3, 0.1f, 0.1f, 0.1f, 0.01f);
				}
			});
			
			vp.launchProjectile();
			
			
			vp.setOnNonHit(new Runnable() {
				public void run() {
					Location l = MyUtility.getGroundLocation(vp.projectileLoc).add(0, -1.2, 0);
					
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 5;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
						public void run(){
							if(sch.schTime > 0){
								sch.schTime -= 1;
								l.getWorld().playSound(l, Sound.BLOCK_ANVIL_LAND, 3.0f, 3.0f);
								l.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, l, 3, 0.2f, 0.2f, 0.2f, 0.5f);
							} else {
								sch.cancelTask(true);
								l.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, l, 2, 1.5f, 1.5f, 1.5f, 0.03f);
								l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 1.0f);
								for(String enemyName : hrw.getEnemyList(hrw.getTeam(playerName))) {
									Player t = Bukkit.getPlayer(enemyName);
									if(exitsPlayer(t)) {
										if(t.getLocation().distance(l) <= 4){
											//ignore = true;
											customDamage(t, p, (16-(int)t.getLocation().distance(l))*2, false);
										}
									}
								}
							}					
						}
					}, 0l, 4l);		
				}
			});
			
			vp.setOnHit(new Runnable() {
				public void run() {
					Player hitP = vp.hitPlayer.get(0);
					stat_stickCnt += 1;
					TitleAPI.sendFullTitle(p, 0, 30, 0, "부착함!", "");
					TitleAPI.sendFullTitle(hitP, 0, 30, 0, "부착됨!", "");
					EGScheduler sch = new EGScheduler(hrw);
					sch.schTime = 5;
					sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable(){
						public void run(){
							if(sch.schTime > 0){
								sch.schTime -= 1;
								hitP.getWorld().playSound(hitP.getLocation(), Sound.BLOCK_ANVIL_LAND, 3.0f, 3.0f);
								hitP.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, hitP.getLocation(), 1, 0.2f, 0.2f, 0.2f, 0.5f);
							} else {
								sch.cancelTask(true);
								hitP.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, hitP.getLocation(), 1, 1.5f, 1.5f, 1.5f, 0.03f);
								hitP.getWorld().playSound(hitP.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 1.0f);
								for(String enemyName : hrw.getEnemyList(hrw.getTeam(playerName))) {
									Player t = Bukkit.getPlayer(enemyName);
									if(exitsPlayer(t)) {
										if(t.getLocation().distance(hitP.getLocation()) <= 4){
											//ignore = true;
											customDamage(t, p, (16-(int)(t.getLocation().distance(hitP.getLocation())))*2, false);
										}
									}
								}
								blockPerUlti = false;
							}					
						}
					}, 0l, 4l);
				}
			});

		}else {
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.2f, 1.8f);
		}
	}
	
	@Override
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
	
	/*@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(isNoMove()) {
			e.setCancelled(true);
		}
		if(team.predator_portal1 != null && team.predator_portal2 != null ){
			Location l = p.getLocation();
			if(l.getBlockX() == team.predator_portal1.getBlockX() 
					&& l.getBlockY() == team.predator_portal1.getBlockY() 
					&& l.getBlockZ() == team.predator_portal1.getBlockZ()) {
				p.teleport(team.predator_portal2);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SHULKER_TELEPORT, 1.2f, 2.0f);
			}
		}	
		if(sch_Return.schId != -1) {
			sch_Return.cancelTask(true);
		}
		if(Math.abs(Math.abs(e.getTo().getX()) - Math.abs(e.getFrom().getX())) > 0.03) {
			if(e.getTo().getX() > e.getFrom().getX()) {
				moveDir_X = 1;
			}else if(e.getTo().getX() < e.getFrom().getX()) {
				moveDir_X = -1;
			}
		}else {
			moveDir_X = 0;
		}
		
		if(Math.abs(Math.abs(e.getTo().getZ()) - Math.abs(e.getFrom().getZ())) > 0.03) {
			if(e.getTo().getZ() > e.getFrom().getZ()) {
				moveDir_Z = 1;
			}else if(e.getTo().getZ() < e.getFrom().getZ()) {
				moveDir_Z = -1;
			}
		} else {
			moveDir_Z = 0;
		}
	}*/
}
