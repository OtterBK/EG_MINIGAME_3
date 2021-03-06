package Utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class VirtualProjectile{ //가상 투사체를 날려 히트햇는지 확인할려는 클래스임
	
	public float c_speed = 1; //1틱당 몇만큼 움직일지 정함
	public Location c_startLoc; //시작 지점
	public float c_frontOffset = 0; //시작지점에서 얼마나 앞에서 던질지
	public float c_upOffset = 0; //시작지점에서 얼마나 위에서 던질지
	public float c_rightOffset = 0; //시작지점에서 얼마나 오른쪽에서 던질지
	public float c_leftOffset = 0; //시작지점에서 얼마나 오른쪽에서 던질지
	public float c_pHorizenOffset = 0.1f; //가로 크기 
	public float c_pVerticalOffset = 0.1f; //투사체 세로 크기
	public boolean c_surface = false;
	public Location projectileLoc; //투사체들의 현재 위치들
	public List<Player>  targetList; //투사체에 맞을수 있는 적
	public List<Player>  hitPlayer; //투사체에 맞은적
	public boolean c_onlyPlayer = true; //대상은 플레이어만?
	public boolean c_removeOnHit = true; //맞추면 없앰?
	public boolean c_removeOnBlock = true; //땅맞추면 없앰?
	public float c_distance = 20; //최대 얼마나 날라갈지
	public int c_time = 30; //몇틱동안 날라갈지
	public Player owner;
	public Vector vector;
	
	//내부카운트
	private int loopCnt = 0;
	private float moveDis = 0;
	private float calHorizen = 0;
	private float calVertical = 0;
	public Runnable onDuring = null; //날라가는동안 할 동작
	public Runnable onHit = null; //맞았을 때 할 동장
	public Runnable onNonHit = null; //안맞고 끝났을 때 할 동작
	public Runnable onEnd = null; //투사체 사라질 때 할 동작
	private float moveDisPerTick;
	private JavaPlugin pl;
	private List<Player> alreadyHit = new ArrayList<Player>(5);
	
	public VirtualProjectile(JavaPlugin plugin, Player p, List<Player> targetList, Location loc, Vector vector) {
		
		pl = plugin;
		this.vector = vector.normalize();
		owner = p;				
		c_startLoc = loc;
		this.targetList = targetList;
		
	}
	
	public VirtualProjectile(JavaPlugin plugin, Player p, List<Player> targetList) {
		
		pl = plugin;
		owner = p;				
		c_startLoc = p.getEyeLocation();
		this.vector = p.getLocation().getDirection();
		this.targetList = targetList;
		
	}
	
	public void setOnHit(Runnable run) {
		
		this.onHit = run;
		
	}
	
	public void setOnDuring(Runnable run) {
		
		this.onDuring = run;
		
	}
	
	public void setOnNonHit(Runnable run) {
		
		this.onNonHit = run;
		
	}

	public void setOnEnd(Runnable run) {
		
		this.onEnd = run;
		
	}
	
	public void createProjectile() {
		if(c_frontOffset != 0) {
			Vector v = vector.clone();
			v.multiply(c_frontOffset);
			c_startLoc.add(v);
		}
		if(c_upOffset != 0) {
			Vector v = vector.clone();
			v.setY(v.getY()+c_upOffset);
			c_startLoc.add(v);
		}
		if(c_rightOffset != 0) { //우측은 X,Z교체후 Z * -1
			Vector v = vector.clone();
			double tmp = v.getX();
			v.setX(v.getZ());
			v.setZ(tmp*-1);
			v.multiply(c_rightOffset);
			c_startLoc.add(v);
		}
		if(c_leftOffset != 0) { //우측은 X,Z교체후 Z * -1
			Vector v = vector.clone();
			double tmp = v.getX();
			v.setX(v.getZ()*-1);
			v.setZ(tmp);
			v.multiply(c_leftOffset);
			c_startLoc.add(v);
		}
		projectileLoc = (new Location(c_startLoc.getWorld(), c_startLoc.getX(), c_startLoc.getY(), c_startLoc.getZ())); //메인 투사체 시작 위치
		if(c_surface) {
			projectileLoc.setY(projectileLoc.getY()+1);
			projectileLoc = MyUtility.getGroundLocation(projectileLoc);
			projectileLoc.setY(projectileLoc.getY()-0.5f);
		}
	}
	
	public void launchProjectile() {
		
		createProjectile();
		
		loopCnt = 0;
		moveDis = 0;
		calVertical = 0.9f+(c_pVerticalOffset/2);
		calHorizen = 0.32f+(c_pHorizenOffset/2);
		moveDisPerTick = calHorizen*2;
		hitPlayer = new ArrayList<Player>();
		//owner.sendMessage("x: "+vector.getX()+" y: "+vector.getY()+" z: "+vector.getZ());
		//for(Player t : targetList) {
		//	owner.sendMessage(t.getName());
		//}
		if(c_surface) {
			vector.setY(0);
		}
		vector.multiply(moveDisPerTick);
		moveProjectile();
		
	}
	
	public void moveProjectile() {
		
		if(moveDis > c_distance) { //최대 거리 도달
			if(onNonHit != null)
				onNonHit.run();
			return;
		} else {
			moveDis += c_speed; 
		}
		
		if(++loopCnt > c_time) { //최대 유지 틱 도달
			if(onNonHit != null)
				onNonHit.run();
			return;
		}
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(pl, new Runnable() {
			
			public void run() {
				boolean result = false;
				float dis = c_speed;
				
				do {
					if(onDuring != null)
						onDuring.run();
					
					Material projectileBlockType =  projectileLoc.getBlock().getType();
					boolean blockHit = false;
					
					if((projectileBlockType != Material.AIR
							&& projectileBlockType != Material.YELLOW_FLOWER
							&& projectileBlockType != Material.CHORUS_FLOWER
							&& projectileBlockType != Material.RED_ROSE
							&& projectileBlockType != Material.CHORUS_PLANT
							&& projectileBlockType != Material.DOUBLE_PLANT
							&& projectileBlockType != Material.LADDER
							&& projectileBlockType != Material.RED_MUSHROOM
							&& projectileBlockType != Material.SAPLING
							&& projectileBlockType != Material.GRASS_PATH
							&& projectileBlockType != Material.LONG_GRASS
							&& projectileBlockType != Material.TORCH
							&& projectileBlockType != Material.VINE
							&& projectileBlockType != Material.WHEAT
							&& projectileBlockType != Material.SEEDS
							&& projectileBlockType != Material.CHORUS_PLANT)) { //블록에 닿았을 때 삭제하는거면
						
						if(c_surface) {
							Location l = projectileLoc.clone();
							l.setY(l.getY()+1);
							Material mt = l.getBlock().getType();
							if(mt != Material.AIR
							&& mt != Material.YELLOW_FLOWER
							&& mt != Material.CHORUS_FLOWER
							&& mt != Material.RED_ROSE
							&& mt != Material.CHORUS_PLANT
							&& mt != Material.DOUBLE_PLANT
							&& mt != Material.LADDER
							&& mt != Material.RED_MUSHROOM
							&& mt != Material.SAPLING
							&& mt != Material.GRASS_PATH
							&& mt != Material.LONG_GRASS
							&& mt != Material.TORCH
							&& mt != Material.VINE
							&& mt != Material.WHEAT
							&& mt != Material.SEEDS
							&& mt != Material.CHORUS_PLANT
							&& mt != Material.STATIONARY_WATER){
								blockHit = true;
							} else {
								projectileLoc.setY(projectileLoc.getY()+1);
							}
						} else {
							blockHit = true;
						}
													
					} else if(c_surface){
						Location l = projectileLoc.clone();
						l.setY(l.getY()-1);
						Material mt = l.getBlock().getType();
						if(mt != Material.AIR
								&& mt != Material.YELLOW_FLOWER
								&& mt != Material.CHORUS_FLOWER
								&& mt != Material.RED_ROSE
								&& mt != Material.CHORUS_PLANT
								&& mt != Material.DOUBLE_PLANT
								&& mt != Material.LADDER
								&& mt != Material.RED_MUSHROOM
								&& mt != Material.SAPLING
								&& mt != Material.GRASS_PATH
								&& mt != Material.LONG_GRASS
								&& mt != Material.TORCH
								&& mt != Material.VINE
								&& mt != Material.WHEAT
								&& mt != Material.SEEDS
								&& mt != Material.CHORUS_PLANT
								&& mt != Material.STATIONARY_WATER){
							
								} else {
									l.setY(l.getY()-1);
									mt = l.getBlock().getType();
									if(mt != Material.AIR
											&& mt != Material.YELLOW_FLOWER
											&& mt != Material.CHORUS_FLOWER
											&& mt != Material.RED_ROSE
											&& mt != Material.CHORUS_PLANT
											&& mt != Material.DOUBLE_PLANT
											&& mt != Material.LADDER
											&& mt != Material.RED_MUSHROOM
											&& mt != Material.SAPLING
											&& mt != Material.GRASS_PATH
											&& mt != Material.LONG_GRASS
											&& mt != Material.TORCH
											&& mt != Material.VINE
											&& mt != Material.WHEAT
											&& mt != Material.SEEDS
											&& mt != Material.CHORUS_PLANT
											&& mt != Material.STATIONARY_WATER) {
										projectileLoc.setY(projectileLoc.getY()-1);
									} else {
										blockHit = true;
									}
								}
					}
					
					if(blockHit && c_removeOnBlock) {
						if (onNonHit != null)
							onNonHit.run();
						result = true;
						return;
					}						
					
					hitPlayer.clear();
					
					for(Player t : targetList) {
						if(t==null || !t.isOnline() || t.getName() == owner.getName()) continue; //쏜사람이거나 타겟이 오프라인이면 다음대상으로
						Location tLoc = t.getLocation();
						tLoc.setY(tLoc.getY()+1);
						
						if(Math.abs(projectileLoc.getY() - tLoc.getY()) <= calVertical
								&& Math.abs(projectileLoc.getX() - tLoc.getX()) <= calHorizen
								&& Math.abs(projectileLoc.getZ() - tLoc.getZ()) <= calHorizen) { //투사체 히트 계산
							if(!alreadyHit.contains(t)) //이미 맞은 사람 목록에 없다면
								hitPlayer.add(t); //맞은 플레이어 목록에 추가
						} else {
							//owner.sendMessage("x: "+Math.abs(projectileLoc.getX() - tLoc.getX())
							//		+" y: "+Math.abs(projectileLoc.getY() - tLoc.getY())+" z: "
							//+Math.abs(projectileLoc.getZ() - tLoc.getZ())+ " dis: "+dis); //디버깅용
						}
					}
					
					if(hitPlayer.size() > 0) { //만약 맞은 사람이 여러명이면
						
						if(c_removeOnHit) { //관통된다면 해당 적에게 다 적용
							for(int i = 0; i < hitPlayer.size(); i++) {
								alreadyHit.add(hitPlayer.get(i));
							}
							if(onHit != null)
								onHit.run();
						} else { //관통안되면 투사체와 가장 가까운 플레이어에게 적용
							int tIndex = 0;
							for(int i = 1; i < hitPlayer.size()-1; i++) {
								if(hitPlayer.get(i).getLocation().distance(projectileLoc) < hitPlayer.get(tIndex).getLocation().distance(projectileLoc)) {
									tIndex = -i;
								}
							}
							Player t = hitPlayer.get(tIndex);
							hitPlayer.clear();
							hitPlayer.add(t);
							
							if(onHit != null) {
								alreadyHit.add(t);
								onHit.run();
							}
						}
						
								
						if(c_removeOnHit) result = true; //관통안되면 끝
					}
					
					if(dis >= moveDisPerTick) {
						projectileLoc.add(vector);
					} else {
						if(dis > 0) {
							Vector v = vector.clone();
							v.normalize();
							v.multiply(dis);
							projectileLoc.add(v);
						}
					}
					dis -= moveDisPerTick;
						
				}while(dis > 0 && !result);

				if(!result) {
					moveProjectile();
				} 
			}
			
			
		}, 1l);
		
	}
	
	

}

