package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class C4Bomber extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public C4Bomber(Minigame minigame) {
		super(minigame, Material.LEVER, 1, (short)0, "C4", 11, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ���۽� ��ġ�� ��ź�̴�.");
		loreList.add("��e��Ŭ���� 3ĭ�� �ٶ󺸴� ����");
		loreList.add("��e��ź�� ��ġ�Ѵ�.");
		loreList.add("��e����Ʈ ��Ŭ���� ��ź�� �Ͷ߸���.");
		loreList.add("��e��ź�� ���������� �Ÿ��� ���� �ִ� 48�� ���ظ� �ش�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b5��");
		loreList.add("��e����Ʈ+ ��Ŭ�� ��Ÿ�� : ��b12��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(p.isSneaking()) {
			Location sl = saved_Location.get(p.getName());
			if(sl != null) {
				if (!checkCooldown(p, CooldownType.Secondary))
					return;

				setCooldown(p, CooldownType.Secondary, 12);
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 0.5f);
				sl.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
				sl.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, sl, 2, 0.05F, 0.05f, 0.05f, 0.1f);
				
				for(Player t : getEnemyList(p)) {
					double dis = t.getLocation().distance(sl);
					if(dis < 6) {
						t.damage((6 - dis) * 9);
					}
				}
				
				
				saved_Location.remove(p.getName());
			}else {
				p.sendMessage("��6���� ��ź�� ��ġ���ּ���.");
				p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_OFF, 1.0f, 1.5f);
			}		
		}else {
			if (!checkCooldown(p, CooldownType.Primary))
				return;

			setCooldown(p, CooldownType.Primary, 5);
			Block b = p.getTargetBlock(null, 3);
			if(b == null) {
				TitleAPI.sendFullTitle(p, 10,40, 20, "", "��c��l�ٶ󺸴� ���� �ʹ� �ٴϴ�.");
			}else {
				Location pl = b.getLocation();
				saved_Location.put(p.getName(), pl);
				p.sendMessage("��7--------- ��ź ��ġ�� ------------");
				p.sendMessage("��a" + pl.getBlockX() + ", " + pl.getBlockY() + ", " + pl.getBlockZ());
				p.sendMessage("��7--------------------------------");
				pl.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1.0f, 2.0f);
			}
			
		}
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		// TODO Auto-generated method stub
		
	}
	
}
