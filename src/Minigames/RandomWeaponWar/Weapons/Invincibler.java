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

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.ServerManager.EGScheduler;
import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;
import de.slikey.effectlib.effect.ShieldEffect;

public class Invincibler extends SpecialWeapon{
	
	boolean invincible = false;
	
	public Invincibler(Minigame minigame) {
		super(minigame, Material.GOLD_NUGGET, 1, (short)0, "�ݰ� ����", 11, 12); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e����� �����ϴ� ����");
		loreList.add("��e��Ŭ���� 4�ʰ� ��� ��������");
		loreList.add("��e���� �ʴ´�.");
		loreList.add("��eȿ�� ����߿� ���⸦ ����־�� ����ȴ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b35��");
		loreList.add("��c�޼տ� �� �ɷ� ������");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 35);
			
		invincible = true;
		
		ShieldEffect particle = new ShieldEffect(effectManager);
		particle.setLocation(p.getLocation().add(0,0.5f,0));
		particle.particle = Particle.REDSTONE;
		particle.color = Color.fromRGB(255, 240, 0);
		particle.period = 20;
		particle.iterations = 4;
		particle.particleCount = 20;
		particle.radius = 1.5d;
		particle.start();
						
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0F, 0.1F);
		
		EGScheduler sch = new EGScheduler(minigame);
		sch.schTime = 4;
		sch.schId =	Bukkit.getScheduler().scheduleSyncRepeatingTask(minigame.server, new Runnable() {
			public void run() {
				if(!minigame.ingamePlayer.contains(p.getName())) sch.cancelTask(true);
				if(sch.schTime > 1) {
					sch.schTime--;
					TitleAPI.sendFullTitle(p, 0, 30, 0, "��c��l"+sch.schTime, "��e��l���� ȿ���� ����������");
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 0.5f);
				}else {
					sch.cancelTask(true);
					TitleAPI.sendFullTitle(p, 0, 60, 0, "", "��e��l���� ȿ���� ����������");
					invincible = false;
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0F, 2.0F);
				}
				
			}
		}, 0l, 20l);
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(invincible) {
			e.setCancelled(true);
			e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1.0F, 2.0F);
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
		
	}
	
}
