package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.connorlinfoot.titleapi.TitleAPI;

import Minigames.Minigame;
import Minigames.RandomWeaponWar.Weapons.CustomCooldown.CooldownType;

public class TeleportStick extends SpecialWeapon{
	
	public TeleportStick(Minigame minigame) {
		super(minigame, Material.STICK, 1, (short)0, "�����̵� ����", 11, 12); //�������ڵ�, ����, �������ڵ�, �̸�, �ּҵ�, �ִ뵩
		
		List<String> loreList = new ArrayList<String>();
				
		//���� ����
		loreList.add("��e���İ��� ��Ҹ� �̵��� �� �ִ� ����");
		loreList.add("��e��Ŭ�� �� 30ĭ�� �ٶ󺸴� ������");
		loreList.add("��e�Ѽ����� �̵��� �� �ִ�.");
		loreList.add("");
		loreList.add("��e��Ÿ�� : ��b8��");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		if(!checkCooldown(p, CooldownType.Primary)) return;
		
		setCooldown(p, CooldownType.Primary, 8);
			
		Block b = p.getTargetBlock(null, 30);
		if(b == null) {
			TitleAPI.sendFullTitle(p, 10,40, 20, "", "��c��l�ٶ󺸴� ���� �ʹ� �ٴϴ�.");
			p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
		}else {
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
			Location l = b.getLocation();
			l.setPitch(p.getLocation().getPitch());
			l.setYaw(p.getLocation().getYaw());
			p.teleport(l.add(0,1,0), TeleportCause.PLUGIN);
			p.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f);
		}
		
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		
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
