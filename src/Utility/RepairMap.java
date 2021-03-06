package Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import EGServer.EGServer;

public class RepairMap{
	
	public Location pos1;
	public Location pos2;
	public HashMap<String, BlockData> blockData = new HashMap<String, BlockData>(1000);
	public String mapName;
	public int timerid = -1;
	public int didTime;
	public int loopCnt = 0;
	public JavaPlugin server;
	public int allCnt = 0;
	public boolean repaired = false;
	
	int x = 0, y = 0, z = 0;
	int removeCnt = 0;
	int repairCnt = 0;
	
	public RepairMap(EGServer server, String mapName) {
		this.server = server;
		this.mapName = mapName;
	}
	
	public void Repair(int time) { //맵 완전 복구
		if(repaired) return;
		if(timerid != -1) return;
		//Player p = Bukkit.getPlayer("Tester1");
		//if(blockData.size() == 0) return;
		if(allCnt == 0) return;
		if(pos1 == null ||pos2 == null) return;
	
		loopCnt = 0;
		removeCnt = 0;
		repairCnt = 0;
		
		//int loopPerTick = (allCnt/(time*20) == 0 ? allCnt :  allCnt/(time*20));
		int loopPerTick = (allCnt/(time*20) == 0 ? 1 :  allCnt/(time*20));
		
		int	loc1_x = pos1.getBlockX();
		int loc1_y = pos1.getBlockY();
		int loc1_z = pos1.getBlockZ();
		
		int	loc2_x = pos2.getBlockX();
		int loc2_y = pos2.getBlockY();
		int loc2_z = pos2.getBlockZ();
		
		y = loc1_y;
		z = loc1_z;
		x = loc1_x; 
		//removeCnt = 0;
		//repairCnt = 0;
		//Bukkit.getLogger().info("looppertick: "+loopPerTick+ " allcnt: "+allCnt);
		timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {			
				if(y < loc2_y) {
					Bukkit.getScheduler().cancelTask(timerid);
					timerid = -1;
					repaired = true;
					//Bukkit.getLogger().info("removeCnt: "+removeCnt + " repairCnt: "+repairCnt);	
					//Bukkit.getLogger().info("finish");	
				}
				for(; y >= loc2_y; y--) {
					for(; z >= loc2_z; z--) {
						for(; x >= loc2_x; x--) {
							loopCnt++;
							String key = x+","+y+","+z;
							BlockData bd = blockData.get(key);
							//Bukkit.getLogger().info(key+" : "+type);
							Location l = new Location(pos1.getWorld(), x,y,z);
							if(bd == null) {
								if(l.getBlock().getType() != Material.AIR) {
									l.getBlock().setType(Material.AIR);
								}					
								removeCnt++;
							} else {
								Block b = l.getBlock();
								Material type = bd.mt;
								byte data = bd.data;
								//Bukkit.getLogger().info("target: "+b.getType()+", repair: "+type);
								if(b.getType() != type) {
									b.setType(type);
								}
								if(b.getData() != data) {
									b.setData(data);
								}
								repairCnt++;
							}
							if(loopCnt >= loopPerTick) {
								loopCnt = 0;
								x--;
								return;
							}
						}
						x = loc1_x;
					}
					z = loc1_z;
				}
			}
		}, 0l, 1l);
	}
	
	//복구하되 맵에 설치된 것을 공기 블럭으로 바꾸지는 않음
	public void RepairWithoutAir(int time) {
		if(repaired) return;
		if(timerid != -1) return;
		//Player p = Bukkit.getPlayer("Tester1");
		if(blockData.size() == 0) return;
		if(allCnt == 0) return;
		if(pos1 == null ||pos2 == null) return;
	
		loopCnt = 0;
		List<String> keyList = new ArrayList<String>(blockData.keySet());
		List<BlockData> valueList = new ArrayList<BlockData>(blockData.values());
		
		//int loopPerTick = (allCnt/(time*20) == 0 ? allCnt :  allCnt/(time*20));
		int loopPerTick = (keyList.size()/(time*20) == 0 ? 1 :  (keyList.size()/(time*20))+1);
		//Bukkit.broadcastMessage("lpt: "+loopPerTick);
		timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {			
				for(int i = 0; i < loopPerTick; i++) {
					if(loopCnt == keyList.size()-1) {
						//Bukkit.broadcastMessage("fin");
						Bukkit.getScheduler().cancelTask(timerid);
						timerid = -1;
						repaired = true;
						break;
					}
					String key = keyList.get(loopCnt);
					BlockData bd = valueList.get(loopCnt);
					String[] loc = key.split(",");
					int x = Integer.valueOf(loc[0]);
					int y = Integer.valueOf(loc[1]);
					int z = Integer.valueOf(loc[2]);
					Location l = new Location(pos1.getWorld(), x, y ,z);
					Block b = l.getBlock();
					if(b.getType() != bd.mt) {
						b.setType(bd.mt);
					}		
					if(b.getData() != bd.data) {
						b.setData(bd.data);
					}
					loopCnt++;
				}
			}
		}, 0l, 1l);
	}
	
	//복구하되 맵에 설치된 것을 공기 블럭으로 바꾸지도 않으며 복구 되는 블럭들을 특정 블럭으로 복구
	public void RepairToSpecBlock(int time, Material type) {
		if(repaired) return;
		if(timerid != -1) return;
		//Player p = Bukkit.getPlayer("Tester1");
		if(blockData.size() == 0) return;
		if(allCnt == 0) return;
		if(pos1 == null ||pos2 == null) return;
	
		loopCnt = 0;
		List<String> keyList = new ArrayList<String>(blockData.keySet());
		
		//int loopPerTick = (allCnt/(time*20) == 0 ? allCnt :  allCnt/(time*20));
		int loopPerTick = (keyList.size()/(time*20) == 0 ? 1 :  (keyList.size()/(time*20))+1);
		//Bukkit.broadcastMessage("lpt: "+loopPerTick);
		timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {			
				for(int i = 0; i < loopPerTick; i++) {
					if(loopCnt == keyList.size()-1) {
						//Bukkit.broadcastMessage("fin");
						Bukkit.getScheduler().cancelTask(timerid);
						repaired = true;
						timerid = -1;
						break;
					}
					String key = keyList.get(loopCnt);
					String[] loc = key.split(",");
					int x = Integer.valueOf(loc[0]);
					int y = Integer.valueOf(loc[1]);
					int z = Integer.valueOf(loc[2]);
					Location l = new Location(pos1.getWorld(), x, y ,z);
					Block b = l.getBlock();
					if(b.getType() != type) {
						b.setType(type);
					}				
					loopCnt++;
				}
			}
		}, 0l, 1l);
	}
	
	//복구하되 맵에 설치된 것을 공기 블럭으로 바꾸지도 않으며 복구 되는 블럭들을 특정 블럭으로 복구하고 데이터도 설정가능
	public void RepairToSpecBlock(int time, Material type, byte data) {
		if(repaired) return;
		if(timerid != -1) return;
		//Player p = Bukkit.getPlayer("Tester1");
		if(blockData.size() == 0) return;
		if(allCnt == 0) return;
		if(pos1 == null ||pos2 == null) return;
	
		loopCnt = 0;
		List<String> keyList = new ArrayList<String>(blockData.keySet());
		
		//int loopPerTick = (allCnt/(time*20) == 0 ? allCnt :  allCnt/(time*20));
		int loopPerTick = (keyList.size()/(time*20) == 0 ? 1 :  (keyList.size()/(time*20))+1);
		Bukkit.getLogger().info((keyList.size()+" / loopertick: "+loopPerTick));
		//Bukkit.broadcastMessage("lpt: "+loopPerTick);
		timerid = Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {			
				for(int i = 0; i < loopPerTick; i++) {
					if(loopCnt == keyList.size()-1) {
						//Bukkit.broadcastMessage("fin");
						Bukkit.getScheduler().cancelTask(timerid);
						repaired = true;
						timerid = -1;
						break;
					}
					String key = keyList.get(loopCnt);
					String[] loc = key.split(",");
					int x = Integer.valueOf(loc[0]);
					int y = Integer.valueOf(loc[1]);
					int z = Integer.valueOf(loc[2]);
					Location l = new Location(pos1.getWorld(), x, y ,z);
					Block b = l.getBlock();
					if(b.getType() != type || b.getData() != data) {
						b.setType(type);
						b.setData(data);
					} 
					loopCnt++;
				}
			}
		}, 0l, 1l);
	}
	
	
	public boolean setPos1(Location p){
		pos1 = p;
		
		if(pos1 == null ||pos2 == null) return false;
		Location l1  = new Location(p.getWorld(), 0,0,0);
		Location l2 = new Location(p.getWorld(), 0,0,0);
		
		if(pos1.getX() > pos2.getX()) {
			l1.setX(pos1.getX());
			l2.setX(pos2.getX());
		} else {
			l1.setX(pos2.getX());
			l2.setX(pos1.getX());
		}
		
		if(pos1.getY() > pos2.getY()) {
			l1.setY(pos1.getY());
			l2.setY(pos2.getY());
		} else {
			l1.setY(pos2.getY());
			l2.setY(pos1.getY());
		}
		
		if(pos1.getZ() > pos2.getZ()) {
			l1.setZ(pos1.getZ());
			l2.setZ(pos2.getZ());
		} else {
			l1.setZ(pos2.getZ());
			l2.setZ(pos1.getZ());
		}
		
		pos1 = l1;
		pos2 = l2;
		Bukkit.getLogger().info("pos1: "+pos1+" pos2: "+pos2);
		
		return true;
	}
	
	public boolean setPos2(Location p){
		pos2 = p;
		
		if(pos1 == null ||pos2 == null) return false;
		Location l1  = new Location(p.getWorld(), 0,0,0);
		Location l2 = new Location(p.getWorld(), 0,0,0);
		
		if(pos1.getX() > pos2.getX()) {
			l1.setX(pos1.getX());
			l2.setX(pos2.getX());
		} else {
			l1.setX(pos2.getX());
			l2.setX(pos1.getX());
		}
		
		if(pos1.getY() > pos2.getY()) {
			l1.setY(pos1.getY());
			l2.setY(pos2.getY());
		} else {
			l1.setY(pos2.getY());
			l2.setY(pos1.getY());
		}
		
		if(pos1.getZ() > pos2.getZ()) {
			l1.setZ(pos1.getZ());
			l2.setZ(pos2.getZ());
		} else {
			l1.setZ(pos2.getZ());
			l2.setZ(pos1.getZ());
		}
		
		pos1 = l1;
		pos2 = l2;
		
		return true;
	}
	
	public int saveMapBlocks(String path) {
		if(pos1 == null || pos2 == null) {
			return 0;
		}
		
		int blockCnt = 0;
		int blockAllCnt = 0;
		
		int	loc1_x = pos1.getBlockX();
		int loc1_y = pos1.getBlockY();
		int loc1_z = pos1.getBlockZ();
		
		int	loc2_x = pos2.getBlockX();
		int loc2_y = pos2.getBlockY();
		int loc2_z = pos2.getBlockZ();
		
		try {
			File file = new File(path, this.mapName+".yml");
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("block_pos1_x", pos1.getBlockX());
			fileConfig.set("block_pos1_y", pos1.getBlockY());
			fileConfig.set("block_pos1_z", pos1.getBlockZ());

			fileConfig.set("block_pos2_x", pos2.getBlockX());
			fileConfig.set("block_pos2_y", pos2.getBlockY());
			fileConfig.set("block_pos2_z", pos2.getBlockZ());
			Bukkit.getLogger().info("did2");
			
			fileConfig.set("block_world", pos1.getWorld().getName());
			for(int i1 = loc1_y; i1 >= loc2_y; i1--) {
				for(int i2 = loc1_z; i2 >= loc2_z; i2--) {
					for(int i3 = loc1_x; i3 >= loc2_x; i3--) {
						Location l = new Location(pos1.getWorld(), i3, i1, i2);
						if(l.getBlock().getType() != Material.AIR) {
							Material type = l.getBlock().getType();
							byte data = l.getBlock().getData();
							if(type == Material.STATIONARY_WATER || type == Material.WATER) {
								type = Material.WATER_BUCKET;
							}
							ItemStack blockitem = new ItemStack(type, 1);
							fileConfig.set("block_"+blockCnt+"_x", Integer.valueOf(i3));
							fileConfig.set("block_"+blockCnt+"_y", Integer.valueOf(i1));
							fileConfig.set("block_"+blockCnt+"_z", Integer.valueOf(i2));
							fileConfig.set("block_"+blockCnt+"_b", blockitem);
							fileConfig.set("block_"+blockCnt+"_b_data", data);
							blockCnt += 1;
						} 
						blockAllCnt += 1;
					}
				}
			}
			Bukkit.getLogger().info(mapName+"_Savedone,"+blockAllCnt);
			fileConfig.set("block_amt", blockCnt);
			fileConfig.set("block_allamt", blockAllCnt);
			fileConfig.save(file);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		return blockCnt;
	}
	
	public int saveMapBlocks(String path, Material block) { //특정 블럭만 저장
		if(pos1 == null || pos2 == null) {
			return 0;
		}
		
		int blockCnt = 0;
		int blockAllCnt = 0;
		
		int	loc1_x = pos1.getBlockX();
		int loc1_y = pos1.getBlockY();
		int loc1_z = pos1.getBlockZ();
		
		int	loc2_x = pos2.getBlockX();
		int loc2_y = pos2.getBlockY();
		int loc2_z = pos2.getBlockZ();
		
		try {
			File file = new File(path, this.mapName+".yml");
			if(file.exists()) file.delete();
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("block_pos1_x", pos1.getBlockX());
			fileConfig.set("block_pos1_y", pos1.getBlockY());
			fileConfig.set("block_pos1_z", pos1.getBlockZ());

			fileConfig.set("block_pos2_x", pos2.getBlockX());
			fileConfig.set("block_pos2_y", pos2.getBlockY());
			fileConfig.set("block_pos2_z", pos2.getBlockZ());
			Bukkit.getLogger().info("did2");
			
			fileConfig.set("block_world", pos1.getWorld().getName());
			for(int i1 = loc1_y; i1 >= loc2_y; i1--) {
				for(int i2 = loc1_z; i2 >= loc2_z; i2--) {
					for(int i3 = loc1_x; i3 >= loc2_x; i3--) {
						Location l = new Location(pos1.getWorld(), i3, i1, i2);
						if(l.getBlock().getType() == block) {
							Material type = l.getBlock().getType();
							byte data = l.getBlock().getData();
							if(type == Material.STATIONARY_WATER || type == Material.WATER) {
								type = Material.WATER_BUCKET;
							}
							ItemStack blockitem = new ItemStack(type, 1);
							fileConfig.set("block_"+blockCnt+"_x", Integer.valueOf(i3));
							fileConfig.set("block_"+blockCnt+"_y", Integer.valueOf(i1));
							fileConfig.set("block_"+blockCnt+"_z", Integer.valueOf(i2));
							fileConfig.set("block_"+blockCnt+"_b", blockitem);
							fileConfig.set("block_"+blockCnt+"_b_data", data);
							blockCnt += 1;
						} 
						blockAllCnt += 1;
					}
				}
			}
			Bukkit.getLogger().info(mapName+"_Savedone,"+blockAllCnt);
			fileConfig.set("block_amt", blockCnt);
			fileConfig.set("block_allamt", blockAllCnt);
			fileConfig.save(file);
		} catch (Exception e) {
			return 0;
		}

		return blockCnt;
	}
	
	//특정 블럭을 제외하고 블럭 저장
	public int saveMapBlocksWithout(String path, Material block) {
		if(pos1 == null || pos2 == null) {
			return 0;
		}
		
		int blockCnt = 0;
		int blockAllCnt = 0;
		
		int	loc1_x = pos1.getBlockX();
		int loc1_y = pos1.getBlockY();
		int loc1_z = pos1.getBlockZ();
		
		int	loc2_x = pos2.getBlockX();
		int loc2_y = pos2.getBlockY();
		int loc2_z = pos2.getBlockZ();
		
		try {
			File file = new File(path, this.mapName+".yml");
			if(file.exists()) file.delete();
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			fileConfig.set("block_pos1_x", pos1.getBlockX());
			fileConfig.set("block_pos1_y", pos1.getBlockY());
			fileConfig.set("block_pos1_z", pos1.getBlockZ());

			fileConfig.set("block_pos2_x", pos2.getBlockX());
			fileConfig.set("block_pos2_y", pos2.getBlockY());
			fileConfig.set("block_pos2_z", pos2.getBlockZ());
			Bukkit.getLogger().info("did2");
			
			fileConfig.set("block_world", pos1.getWorld().getName());
			for(int i1 = loc1_y; i1 >= loc2_y; i1--) {
				for(int i2 = loc1_z; i2 >= loc2_z; i2--) {
					for(int i3 = loc1_x; i3 >= loc2_x; i3--) {
						Location l = new Location(pos1.getWorld(), i3, i1, i2);
						if(l.getBlock().getType() != block && l.getBlock().getType() != Material.AIR) {
							Material type = l.getBlock().getType();
							byte data = l.getBlock().getData();
							if(type == Material.STATIONARY_WATER || type == Material.WATER) {
								type = Material.WATER_BUCKET;
							}
							ItemStack blockitem = new ItemStack(type, 1);
							fileConfig.set("block_"+blockCnt+"_x", Integer.valueOf(i3));
							fileConfig.set("block_"+blockCnt+"_y", Integer.valueOf(i1));
							fileConfig.set("block_"+blockCnt+"_z", Integer.valueOf(i2));
							fileConfig.set("block_"+blockCnt+"_b", blockitem);
							fileConfig.set("block_"+blockCnt+"_b_data", data);
							blockCnt += 1;
						} 
						blockAllCnt += 1;
					}
				}
			}
			Bukkit.getLogger().info(mapName+"_Savedone,"+blockAllCnt);
			fileConfig.set("block_amt", blockCnt);
			fileConfig.set("block_allamt", blockAllCnt);
			fileConfig.save(file);
		} catch (Exception e) {
			return 0;
		}

		return blockCnt;
	}
	
	public boolean loadData(String path) {
		Bukkit.getLogger().info(mapName+".yml 리페어 맵 로딩중");
		File file = new File(path, mapName+".yml");
		if (!file.exists() || file.isDirectory())
			return false;
		//String posW;
		int posX, posY, posZ;
		ItemStack blockitem;
		try {
			FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
			String worldName = fileConfig.getString("block_world");
			World world = Bukkit.getWorld(worldName);
			int tmpX = fileConfig.getInt("block_pos1_x");
			int tmpY = fileConfig.getInt("block_pos1_y");
			int tmpZ = fileConfig.getInt("block_pos1_z");
			
			pos1 = new Location(world, tmpX,tmpY,tmpZ);
			
			tmpX = fileConfig.getInt("block_pos2_x");
			tmpY = fileConfig.getInt("block_pos2_y");
			tmpZ = fileConfig.getInt("block_pos2_z");
			pos2 = new Location(world, tmpX,tmpY,tmpZ);
			
			allCnt = fileConfig.getInt("block_allamt");
			if(allCnt == 0) return false;
			
			int blockCnt = fileConfig.getInt("block_amt");		
			
			//List<Location> tmpList = new ArrayList<Location>();
			for(int i = 0; i < blockCnt; i++) {
				posX = fileConfig.getInt("block_"+i+"_x");
				posY = fileConfig.getInt("block_"+i+"_y");
				posZ = fileConfig.getInt("block_"+i+"_z");
				blockitem = fileConfig.getItemStack("block_"+i+"_b");
				int data = fileConfig.getInt("block_"+i+"_b_data");
				Material type = blockitem.getType();
				if(type == null) continue;
				BlockData bd = new BlockData(type, (byte)data);
				
				if(type == Material.WATER_BUCKET) {
					type = Material.STATIONARY_WATER;
				}			
				String key = posX + "," + posY + "," + posZ;
				blockData.put(key, bd);
				
			}
		} catch (Exception ex) {		
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private class BlockData{
	
		public Material mt;
		public byte data = 0;
		
		public BlockData(Material mt, byte data) {
			this.mt = mt;
			this.data = data;
		}
		
	}

}
