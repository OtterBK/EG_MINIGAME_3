package Minigames.HeroesWar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.HeroesWar.Abilities.Ability;

public class CatchPoint {

	public HRWBase hrw;
	public List<List<Location>> woolBlocks;
	public Location pointLoc;
	public String catchTeam;
	public String catchingTeam = "";
	public String catchingPlayer = "";
	public int pointNum;
	public int leftCatchTime;
	public String pointName;
	public int catchingTime = 10;
	
	public CatchPoint(HRWBase hrw, int pointNum, String pointName) {
		
		this.hrw = hrw;
		woolBlocks = new ArrayList<List<Location>>();
		catchTeam = "중립";
		this.pointNum = pointNum;
		leftCatchTime = 10;
		pointLoc = null;
		this.pointName = pointName;
		
	}
	
	public boolean loadData(String path, String fileName) {
		File file = new File(path, fileName);
		if (!file.exists() || file.isDirectory())
			return false;
		String posW;
		int posX, posY, posZ;
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			posW = fileConfig.getString("catchPoint" +pointNum+ "_w");
			posX = fileConfig.getInt("catchPoint" +pointNum+ "_x");
			posY = fileConfig.getInt("catchPoint" +pointNum+ "_y");
			posZ = fileConfig.getInt("catchPoint" +pointNum+ "_z");
			pointLoc = new Location(Bukkit.getWorld(posW), posX, posY, posZ);
			
			int woolCnt = fileConfig.getInt("catchPoint"+pointNum+ "_wool_amt");
			if(woolCnt == 0) return false;
			String worldName = fileConfig.getString("catchPoint"+pointNum+ "_wool_world");
			World world = Bukkit.getWorld(worldName);
			int nowY = -1;
			List<Location> tmpList = new ArrayList<Location>();
			for(int i = 0; i < woolCnt; i++) {
				posX = fileConfig.getInt("catchPoint"+pointNum+ "_wool_"+i+"_x");
				posY = fileConfig.getInt("catchPoint"+pointNum+ "_wool_"+i+"_y");
				posZ = fileConfig.getInt("catchPoint"+pointNum+ "_wool_"+i+"_z");
				if(nowY == -1) nowY = posY;
				else if(nowY != posY) {
					woolBlocks.add(tmpList);
					nowY = posY;
					tmpList = new ArrayList<Location>();
				}
				tmpList.add(new Location(world, posX, posY, posZ));
			}
			woolBlocks.add(tmpList);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	public boolean savePointLoc(String path, String fileName, Location loc) {
		try {
			File file = new File(path, fileName);
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("catchPoint" +pointNum+ "_w", loc.getWorld().getName());
			fileConfig.set("catchPoint" +pointNum+ "_x", Integer.valueOf(loc.getBlockX()));
			fileConfig.set("catchPoint" +pointNum+ "_y", Integer.valueOf(loc.getBlockY()));
			fileConfig.set("catchPoint" +pointNum+ "_z", Integer.valueOf(loc.getBlockZ()));
			fileConfig.save(file);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public int saveWoolLoc(String path, String fileName, Location loc1, Location loc2) {
		int woolCnt = 0;
		
		String world = loc1.getWorld().getName();
		
		int loc1_x = loc1.getBlockX();
		int loc1_y = loc1.getBlockY();
		int loc1_z = loc1.getBlockZ();
		
		int loc2_x = loc2.getBlockX();
		int loc2_y = loc2.getBlockY();
		int loc2_z = loc2.getBlockZ();
		
		///작은수에서 큰수로 반복할거임
		if(loc1_x > loc2_x) {
			int tmp = loc1_x;
			loc1_x = loc2_x;
			loc2_x = tmp;
		}
		
		if(loc1_y > loc2_y) {
			int tmp = loc1_y;
			loc1_y = loc2_y;
			loc2_y = tmp;
		}
		
		if(loc1_z > loc2_z) {
			int tmp = loc1_z;
			loc1_z = loc2_z;
			loc2_z = tmp;
		}
		
		/*hrw.server.egPM.printLog(""+loc1_x);
		hrw.server.egPM.printLog(""+loc1_y);
		hrw.server.egPM.printLog(""+loc1_z);
		hrw.server.egPM.printLog(""+loc2_x);
		hrw.server.egPM.printLog(""+loc2_y);
		hrw.server.egPM.printLog(""+loc2_z);*/
		
		try {
			File file = new File(path, fileName);
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("catchPoint" +pointNum+ "_wool_world", world);
			for(int i1 = loc1_y; i1 <= loc2_y; i1++) {
				for(int i2 = loc1_z; i2 <= loc2_z; i2++) {
					for(int i3 = loc1_x; i3 <= loc2_x; i3++) {
						Location l = new Location(loc1.getWorld(), i3, i1, i2);
						if(l.getBlock().getType() == Material.WOOL) {
							fileConfig.set("catchPoint"+pointNum+ "_wool_"+woolCnt+"_x", Integer.valueOf(i3));
							fileConfig.set("catchPoint"+pointNum+ "_wool_"+woolCnt+"_y", Integer.valueOf(i1));
							fileConfig.set("catchPoint"+pointNum+ "_wool_"+woolCnt+"_z", Integer.valueOf(i2));
							woolCnt += 1;
						}
					}
				}
			}
			fileConfig.set("catchPoint"+pointNum+"_wool_amt", woolCnt);
			fileConfig.save(file);
		} catch (Exception e) {
			return 0;
		}

		return woolCnt;
	}
	
	/*public void paintWoolBlocks(byte type) {
		long waitTick = 0;
		for(List<Location> tmpList : woolBlocks) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					for(Location l : tmpList) {
						Block tmpB = l.getBlock();
						if(tmpB.getType() != Material.WOOL) continue;
						tmpB.setData(type, true);
					}
				}
			}, waitTick);
			waitTick += 2;
		}
	}*/
	
	public void paintWoolBlocks(byte type) {
		long waitTick = 0;
		for(List<Location> tmpList : woolBlocks) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(hrw.server, new Runnable() {
				public void run() {
					for(Location l : tmpList) {
						Block tmpB = l.getBlock();
						if(tmpB.getType() != Material.WOOL) continue;
						tmpB.setData(type, true);
					}
				}
			}, waitTick);
			waitTick += 2;
		}
	}
	
	public void occupied(Player p, String team, final boolean isNeute) {
		if(isNeute) {
			if(catchTeam.equalsIgnoreCase("중립")) {
				TitleAPI.sendFullTitle(p, 5, 70, 5,"",ChatColor.GOLD+"이미 중립화한 신전입니다.");
				return;
			} 
		}
		
		if(catchTeam.equalsIgnoreCase(team)) {
			TitleAPI.sendFullTitle(p, 5, 70, 5,"",ChatColor.GOLD+"이미 점령한 신전입니다.");
		} else {
			if(catchingPlayer.equalsIgnoreCase("")) {
				HRWPlayer hrwP = hrw.playerMap.get(p.getName());
				if(hrwP == null) return;
				EGScheduler sch = new EGScheduler(hrw);
				sch.schTime = catchingTime;
				catchingTeam = team;
				catchingPlayer = p.getName();
				sch.schId = Bukkit.getScheduler().scheduleSyncRepeatingTask(hrw.server, new Runnable() {
					public void run() {
						Location pL = p.getLocation();
						Location l = new Location(pL.getWorld(), pL.getBlockX(), pL.getBlockY()-1, pL.getBlockZ());
						if(l.equals(pointLoc) && !p.isDead()) {
							if (sch.schTime > 0) {
								//p.sendMessage(hrw.ms+"점령 완료까지 "+sch.schTime+" 초 남았습니다.");
								boolean block_catching = false;
								Ability ab = hrw.playerMap.get(p.getName()).ability;
								block_catching = ab.block_noCatching;
								if(block_catching) {
									p.sendMessage("§c은신 상태에서는 점령이 불가능합니다.");
									catchingPlayer = "";
									catchingTeam = "";
									sch.cancelTask(true);
								}
								TitleAPI.sendFullTitle(p, 0, 22, 0,"", ChatColor.RED+"점령 완료까지 "+sch.schTime+" 초 남았습니다.");
								leftCatchTime = sch.schTime;
								sch.schTime -= 1;
							} else {	
								catchingPlayer = "";
								catchingTeam = "";
								if(isNeute) {
									if(catchTeam.equalsIgnoreCase("중립")) {
										if(hrwP.team.leftCarrot > 0) {
											TitleAPI.sendFullTitle(p, 5, 70, 5,"",ChatColor.GOLD+"이미 중립화한 신전입니다.");
										}else {
											TitleAPI.sendFullTitle(p, 5, 70, 5,"",ChatColor.GOLD+"먼저 신전을 활성화 해야합니다!");
										}
									} else {
										catchTeam = "중립";
										TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.GOLD+"신전 중립화 성공", 
												ChatColor.GOLD+(team.equalsIgnoreCase("BLUE") ? "블루팀 " : "레드팀 ")
												+p.getName()+" 님이 신전을 중립화하였습니다.");
										hrw.sendTitle("", ChatColor.GOLD+(team.equalsIgnoreCase("BLUE") ? "블루팀 " : "레드팀 ")
												+p.getName()+" 님이 "+pointName+"신전§f을 중립화하였습니다.", 70);
										paintWoolBlocks((byte)(team.equalsIgnoreCase("BLUE") ? 11 : 14));
									}
								} else {
									if(catchTeam.equalsIgnoreCase(team)) {
										TitleAPI.sendFullTitle(p, 5, 70, 5,"",ChatColor.GOLD+"이미 점령한 신전입니다.");
									} else {
										catchTeam = team;
										TitleAPI.sendFullTitle(p, 10, 70, 30,ChatColor.GOLD+"신전 점령 성공", ChatColor.GOLD+(team.equalsIgnoreCase("BLUE") ? "블루팀 " : "레드팀 ")
												+p.getName()+" 님이 신전을 점령하였습니다.");
										hrw.sendTitle("", ChatColor.GOLD+(team.equalsIgnoreCase("BLUE") ? "블루팀 " : "레드팀 ")
												+p.getName()+" 님이 "+pointName+"신전§f을 점령하였습니다.", 70);
										paintWoolBlocks((byte)(team.equalsIgnoreCase("BLUE") ? 11 : 14));
									}
								}
								sch.cancelTask(true);
							}
						} else {
							catchingPlayer = "";
							catchingTeam = "";
							TitleAPI.sendFullTitle(p, 0, 22, 0,"", ChatColor.RED+"점령이 취소 되었습니다.");
							sch.cancelTask(true);
						}
					}
				}, 0L, 20L);
			} else {
				TitleAPI.sendFullTitle(p, 0, 22, 0,"", ChatColor.RED+"이미 "+ChatColor.RESET+catchingPlayer+ChatColor.RED+" 님이 점령중입니다.");
			}
		}
	}
	
}
