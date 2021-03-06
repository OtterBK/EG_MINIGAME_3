package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class GlowingDust extends SpecialWeapon{

	private PotionEffect glowPt = new PotionEffect(PotionEffectType.GLOWING, 300, 0);
	
	public GlowingDust(Minigame minigame) {
		super(minigame, Material.GLOWSTONE_DUST, 1, (short)0, "���� ����", 14, 16); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���� ���� ��� ����");
		loreList.add("��e��Ŭ���� �ҷ��� ���縦 ���߿� �Ѹ���.");
		loreList.add("��e���߿� �ѷ��� ����� ��� �÷��̾�(�ڽ�����)��");
		loreList.add("��e��ġ�� 15�ʰ� ǥ���Ѵ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b75��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 75);
		
		List<Player> enemyList = getEnemyList(p);
		
		TitleAPI.sendFullTitle(p, 0, 70, 0, "", "��e��l���縦 �ѷȽ��ϴ�.");
		for(Player t : enemyList) {
			TitleAPI.sendFullTitle(t, 0, 70, 0, "", "��e��l���� ���縦 ������ ����ϴ�.");
			t.addPotionEffect(glowPt);
		}
		
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
