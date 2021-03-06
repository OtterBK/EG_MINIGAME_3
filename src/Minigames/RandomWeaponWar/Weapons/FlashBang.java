package Minigames.RandomWeaponWar.Weapons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Minigames.Minigame;

public class FlashBang extends SpecialWeapon{
	
	public FlashBang(Minigame minigame) {
		super(minigame, Material.BAKED_POTATO, 1, (short)0, "섬광탄", 1, 1); //아이템코드, 갯수, 데이터코드, 이름, 최소뎀, 최대뎀
		
		List<String> loreList = new ArrayList<String>();
				
		//설명 설정
		loreList.add("§e우클릭시 섬광탄을 던질 수 있다.");
		loreList.add("§e최대 6칸내 적에게 폭심지와의 거리에 따라");
		loreList.add("§e8초까지 실명 디버프를 부여한다.");
		loreList.add("§e던진후 1.2초후 터진다.");
		loreList.add("§e이 아이템은 자신도 대상이다.");
	
		setLore(loreList);
	}

	@Override
	public void onInit() {
		cooldown.clear();
	}

	@Override
	public void onRightClick(Player p) {
		minigame.removeItem(p, this.getType(), 1);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1.5F, 0.1F);
		
		ItemStack throwStack = new ItemStack(this);
        throwStack.setAmount(1);
        Location pLoc = p.getEyeLocation();
   
        Item thrownItem = p.getWorld().dropItem(pLoc, throwStack);
        thrownItem.setVelocity(pLoc.getDirection().multiply(1.3f));
        thrownItem.setPickupDelay(200);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(minigame.server, new Runnable() {
        	public void run() {
        		Location sl = thrownItem.getLocation();
        		sl.getWorld().playSound(p.getLocation(), Sound.ENTITY_FIREWORK_LARGE_BLAST, 1.5f, 0.5f);
				sl.getWorld().spawnParticle(Particle.CLOUD, sl, 40, 1F, 1f, 1f, 0.20f);
				thrownItem.remove();
				for(String tName : minigame.ingamePlayer) {
					Player t = Bukkit.getPlayer(tName);
					if(existPlayer(t)) {
						double dis = t.getLocation().distance(sl);
						int time = (int) (6 - dis)+2;
						if(dis <= 6) {
							t.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS , 20*time, 0));
						}
					}			
				}
        	}
        }, 24l);
	}

	@Override
	public void onLeftClick(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEntityDamaged(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FIRE ||
			e.getCause() == DamageCause.FIRE_TICK) {
			e.setCancelled(true);
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
