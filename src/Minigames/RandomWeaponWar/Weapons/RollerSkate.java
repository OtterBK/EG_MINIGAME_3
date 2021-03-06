package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import de.slikey.effectlib.effect.ShieldEffect;

public class RollerSkate extends SpecialWeapon{
	
	private PotionEffect longSpeed = new PotionEffect(PotionEffectType.SPEED, 200, 3);
	private PotionEffect shortSpeed = new PotionEffect(PotionEffectType.SPEED, 100, 0);
	
	public RollerSkate(Minigame minigame) {
		super(minigame, Material.PRISMARINE_CRYSTALS, 1, (short)0, "�ѷ� ������Ʈ", 10, 14); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e�̽� �޸��� �ִ� �ѷ� ������Ʈ��.");
		loreList.add("��e��Ŭ���� 10�ʰ� �̼����� 4������ �޴´�.");
		loreList.add("��e���ظ� ���� �� 5�ʰ� �̼����� 1������ �޴´�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b20��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 20);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 1.0f, 2.0f);	
		p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 15, 0.25F, 0.25f, 0.25f, 0.4f);
		TitleAPI.sendFullTitle(p, 10, 60, 10, "", "��c��l�޷� �޷�~.");
		p.addPotionEffect(longSpeed);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(p.hasPotionEffect(PotionEffectType.SPEED)) return;
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1.0f, 2.0f);
		if(!p.hasPotionEffect(PotionEffectType.SPEED)) p.addPotionEffect(shortSpeed);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHitPlayer(EntityDamageByEntityEvent e, Player damager, Player victim) {
		
	}

	@Override
	public void onHitted(EntityDamageByEntityEvent e, Player damager, Player victim) {
		
	}
	
}
