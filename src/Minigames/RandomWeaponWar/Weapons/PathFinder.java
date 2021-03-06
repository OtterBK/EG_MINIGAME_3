package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class PathFinder extends SpecialWeapon{

	public PathFinder(Minigame minigame) {
		super(minigame, Material.EMERALD, 1, (short)0, "��ġ Ž����", 11, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e��ġ�� �����ϴ� ���̴�");
		loreList.add("��e��Ŭ���� ��� �÷��̾���� �Ÿ���");
		loreList.add("��e��ġ ��ǥ�� �� �� �ִ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b10��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 10);
		
		List<String> findList = minigame.ingamePlayer;
		
		Location pl = p.getLocation();
		p.sendMessage("��7--------- ��ġ ��Ȳ ------------");
		for(String tName : findList) {
			if(tName.equalsIgnoreCase(p.getName())) continue;
			Player t = Bukkit.getPlayer(tName);
			if(existPlayer(t)) {
				Location tl = t.getLocation();
				p.sendMessage("��6"+t.getName()+" : ��a"+tl.getBlockX()+", "+tl.getBlockY()+", "+tl.getBlockZ()+" ��6| �Ÿ� : ��a"+(int)tl.distance(pl)+"m");
			}
		}
		p.sendMessage("��7--------------------------------");
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0f, 2.0f);
		
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
