package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class DeadRevive extends SpecialWeapon{

	private HashMap<String, Location> saved_Location = new HashMap<String, Location>();
	
	public DeadRevive(Minigame minigame) {
		super(minigame, Material.DIAMOND, 1, (short)0, "�������� �һ�", 9, 9); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�����ڸ� �һ���Ű�� ������");
		loreList.add("��e��Ŭ���� ���� ��Ҹ� �����صд�.");
		loreList.add("��e��Ұ� ����� ���¿���");
		loreList.add("��e�� ���⸦ ��c�տ� ��e��� ü���� 5���Ϸ� ��������");
		loreList.add("��e������ ��ġ���� ��Ȱ�ϸ� ��� ü���� ȸ���Ѵ�.");
		loreList.add("��e��Ȱ�� �� �� �������� �Ҹ��Ѵ�.");
		loreList.add("");
		loreList.add("��e��Ŭ�� ��Ÿ�� : ��b53��");
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
		saved_Location.clear();
	}

	@Override
	public void onRightClick(Player p) {
			if (!checkCooldown(p, CooldownType.Primary))
				return;

			setCooldown(p, CooldownType.Primary, 53);
			Location pl = p.getLocation();
			saved_Location.put(p.getName(), pl);
			p.sendMessage("��7--------- ��ġ ��ϵ� ------------");
			p.sendMessage("��a" + pl.getBlockX() + ", " + pl.getBlockY() + ", " + pl.getBlockZ());
			p.sendMessage("��7--------------------------------");

			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CLOTH_BREAK, 1.0f, 0.1f);
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		//Bukkit.getLogger().info("������ �ν� "+e.getDamage());
		if(p.getHealth() - e.getDamage() <= 5) {//���� �� ������
			Location l = saved_Location.get(p.getName());
			if(l != null) {//��Ҹ� �����؊x�ٸ�
				e.setCancelled(true); //��� ����
				p.setHealth(p.getMaxHealth());
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1.0f,2.0f);
				p.teleport(l, TeleportCause.PLUGIN);
				p.getInventory().remove(this.getType());
				if(p.getInventory().getItemInOffHand().getType() == this.getType()) {
					p.getInventory().setItemInOffHand(null);
				}
				
				p.getWorld().playSound(l, Sound.BLOCK_PORTAL_TRAVEL, 1.0f,2.0f);
				TitleAPI.sendFullTitle(p, 10, 60, 10, "", "��c��l���� ��ȣ�� ����� �һ����׽��ϴ�.");
			}
		}
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
