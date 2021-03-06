package Utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class VirtualProjectile{ //���� ����ü�� ���� ��Ʈ�޴��� Ȯ���ҷ��� Ŭ������
	
	public float c_speed = 1; //1ƽ�� �ŭ �������� ����
	public Location c_startLoc; //���� ����
	public float c_frontOffset = 0; //������������ �󸶳� �տ��� ������
	public float c_upOffset = 0; //������������ �󸶳� ������ ������
	public float c_rightOffset = 0; //������������ �󸶳� �����ʿ��� ������
	public float c_leftOffset = 0; //������������ �󸶳� �����ʿ��� ������
	public float c_pHorizenOffset = 0.1f; //���� ũ�� 
	public float c_pVerticalOffset = 0.1f; //����ü ���� ũ��
	public boolean c_surface = false;
	public Location projectileLoc; //����ü���� ���� ��ġ��
	public List<Player>  targetList; //����ü�� ������ �ִ� ��
	public List<Player>  hitPlayer; //����ü�� ������
	public boolean c_onlyPlayer = true; //����� �÷��̾?
	public boolean c_removeOnHit = true; //���߸� ����?
	public boolean c_removeOnBlock = true; //�����߸� ����?
	public float c_distance = 20; //�ִ� �󸶳� ������
	public int c_time = 30; //��ƽ���� ������
	public Player owner;
	public Vector vector;
	
	//����ī��Ʈ
	private int loopCnt = 0;
	private float moveDis = 0;
	private float calHorizen = 0;
	private float calVertical = 0;
	public Runnable onDuring = null; //���󰡴µ��� �� ����
	public Runnable onHit = null; //�¾��� �� �� ����
	public Runnable onNonHit = null; //�ȸ°� ������ �� �� ����
	public Runnable onEnd = null; //����ü ����� �� �� ����
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
		if(c_rightOffset != 0) { //������ X,Z��ü�� Z * -1
			Vector v = vector.clone();
			double tmp = v.getX();
			v.setX(v.getZ());
			v.setZ(tmp*-1);
			v.multiply(c_rightOffset);
			c_startLoc.add(v);
		}
		if(c_leftOffset != 0) { //������ X,Z��ü�� Z * -1
			Vector v = vector.clone();
			double tmp = v.getX();
			v.setX(v.getZ()*-1);
			v.setZ(tmp);
			v.multiply(c_leftOffset);
			c_startLoc.add(v);
		}
		projectileLoc = (new Location(c_startLoc.getWorld(), c_startLoc.getX(), c_startLoc.getY(), c_startLoc.getZ())); //���� ����ü ���� ��ġ
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
		
		if(moveDis > c_distance) { //�ִ� �Ÿ� ����
			if(onNonHit != null)
				onNonHit.run();
			return;
		} else {
			moveDis += c_speed; 
		}
		
		if(++loopCnt > c_time) { //�ִ� ���� ƽ ����
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
							&& projectileBlockType != Material.CHORUS_PLANT)) { //��Ͽ� ����� �� �����ϴ°Ÿ�
						
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
						if(t==null || !t.isOnline() || t.getName() == owner.getName()) continue; //�����̰ų� Ÿ���� ���������̸� �����������
						Location tLoc = t.getLocation();
						tLoc.setY(tLoc.getY()+1);
						
						if(Math.abs(projectileLoc.getY() - tLoc.getY()) <= calVertical
								&& Math.abs(projectileLoc.getX() - tLoc.getX()) <= calHorizen
								&& Math.abs(projectileLoc.getZ() - tLoc.getZ()) <= calHorizen) { //����ü ��Ʈ ���
							if(!alreadyHit.contains(t)) //�̹� ���� ��� ��Ͽ� ���ٸ�
								hitPlayer.add(t); //���� �÷��̾� ��Ͽ� �߰�
						} else {
							//owner.sendMessage("x: "+Math.abs(projectileLoc.getX() - tLoc.getX())
							//		+" y: "+Math.abs(projectileLoc.getY() - tLoc.getY())+" z: "
							//+Math.abs(projectileLoc.getZ() - tLoc.getZ())+ " dis: "+dis); //������
						}
					}
					
					if(hitPlayer.size() > 0) { //���� ���� ����� �������̸�
						
						if(c_removeOnHit) { //����ȴٸ� �ش� ������ �� ����
							for(int i = 0; i < hitPlayer.size(); i++) {
								alreadyHit.add(hitPlayer.get(i));
							}
							if(onHit != null)
								onHit.run();
						} else { //����ȵǸ� ����ü�� ���� ����� �÷��̾�� ����
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
						
								
						if(c_removeOnHit) result = true; //����ȵǸ� ��
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

