package Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.connorlinfoot.titleapi.TitleAPI;

import EGServer.EGServer;
import EGServer.ServerManager.EGPlugin;

public class ParkourManager extends EGPlugin {

	public List<String> plist = new ArrayList<String>();
	public List<String> jumpinglist = new ArrayList<String>();
	public HashMap<String, Integer> climbinglist = new HashMap<String, Integer>();

	public ParkourManager(EGServer server) {
		super(server);
		timer();
		server.getServer().getPluginManager().registerEvents(new EventHandlerParkourManager(), server);
	}

	public void quit(String pName) {
		plist.remove(pName);
		jumpinglist.remove(pName);
		climbinglist.remove(pName);
		Player p = Bukkit.getPlayer(pName);
		if(existPlayer(p)) {
			p.setFlying(false);
			p.setAllowFlight(false);
		}
	}
	
	
	public boolean isUsing(String pName) {
		return plist.contains(pName);
	}

	public void join(String pName) {
		if (plist.contains(pName)) {
			return;
		}
		plist.add(pName);
		Player p = Bukkit.getPlayer(pName);
		if(existPlayer(p)) p.setFlySpeed(0.12f);
	}

	public void timer() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				for (String pName : plist) {
					Player p = Bukkit.getPlayer(pName);
					if (p == null)
						continue;
					if (p.getExp() != 1) {
						if (!climbinglist.containsKey(p.getName())) {
							if (!p.isSprinting()) {
								p.setExp(p.getExp() + 0.075f > 1 ? 1 : p.getExp() + 0.075f);
							} else {
								p.setExp(p.getExp() + 0.05f > 1 ? 1 : p.getExp() + 0.05f);
							}
						} else {
							p.setExp(p.getExp() + 0.05f > 1 ? 1 : p.getExp() + 0.05f);
						}
					}
				}
			}
		}, 20L, 30l);
	}

	public boolean useStamina(Player p, int amt) {
		float minus = (float) amt / 100;
		if (p.getExp() - minus < 0) {
			TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§c§l스테미나 부족");
			// p.sendMessage("§c스테미나가 부족합니다.");
			return true;
		}
		p.setExp(p.getExp() - minus);
		return false;
	}

	public class EventHandlerParkourManager implements Listener {
		//// 이벤트
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent e) {
			Player p = e.getPlayer();
			if (!plist.contains(p.getName()))
				return;
			if (p.getGameMode() == GameMode.CREATIVE)
				return;
			/*
			 * if(p.getLocation().getY() < 0){ p.damage(100); return; } y좌표가 0이하면 사망
			 */
			if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY()
					&& e.getFrom().getZ() == e.getTo().getZ())
				return;
			Material reb = p.getLocation().getBlock().getRelative(0, -1, 0).getType();
			if (reb == Material.PISTON_STICKY_BASE && p.getExp() < 1) {
				p.setExp(1);
				TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§e스테미나 회복됨");
			}
			if (reb == Material.SPONGE) {
				p.setVelocity(p.getLocation().getDirection().multiply(0.8D).setY(0.8D));
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 0.8f);
			}
			if (climbinglist.containsKey(p.getName())) {
				egCancelMovement(e);
			}
			if (reb != Material.AIR) {
				if (jumpinglist.contains(p.getName()))
					jumpinglist.remove(p.getName());
			}
			Block tb = null;
			try {
				tb = p.getTargetBlock(null, 1);
			} catch (Exception exception) {
				return;
			}
			if (tb == null) {
				return;
			}
			double dix_x = Math.abs(p.getLocation().getX() - tb.getLocation().getBlockX());
			double dix_z = Math.abs(p.getLocation().getZ() - tb.getLocation().getBlockZ());
			if(!climbinglist.containsKey(p.getName())) {
				if ((tb.getTypeId() == 85 || tb.getTypeId() == 113 || tb.getTypeId() == 139 || tb.getTypeId() == 190)
						&& (dix_x <= 0.8 || dix_z <= 0.8)) {
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_SNOW_BREAK, 3.0f, 0.7f);
					p.setAllowFlight(true);
					p.setFlying(true);
				} else {
					p.setAllowFlight(false);
					p.setFlying(false);
				}
			}		
			if (e.getTo().getBlockY() > e.getFrom().getBlockY()) {
				if (p.isSneaking()) {
					if ((!jumpinglist.contains(p.getName()) && reb != Material.AIR)
							|| p.getLocation().getBlock().getType() == Material.STEP) {
						if (useStamina(p, 10))
							return;
						jumpinglist.add(p.getName());
						p.setVelocity(new Vector(0, 0.77f, 0));
						p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MAGMACUBE_JUMP, 3.0f, 0.8f);
					}
				}
			}
		}

		@EventHandler
		public void onSneak(PlayerToggleSneakEvent e) {
			if (!plist.contains(e.getPlayer().getName()))
				return;
			Player p = e.getPlayer();
			if (p.isSneaking())
				return;
			if (p.getGameMode() == GameMode.CREATIVE)
				return;
			Block tb = null;
			try {
				tb = p.getTargetBlock(null, 1);
			} catch (Exception exception) {
				return;
			}
			if (tb == null) {
				return;
			}
			if (climbinglist.containsKey(p.getName())) {
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 3.0f, 1.6f);
				switch (climbinglist.get(p.getName())) {
				case 1:
					p.setVelocity(new Vector(0, 0.35f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.65D));
					break;
				case 2:
					p.setVelocity(new Vector(0, 0.50f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.76D));
					break;
				case 3:
					p.setVelocity(new Vector(0, 0.65f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.87D));
					break;
				case 4:
					p.setVelocity(new Vector(0, 0.77f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(0.96D));
					break;
				case 5:
					p.setVelocity(new Vector(0, 0.77f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.25D).setY(1.05D));
					break;

				default:
					p.setVelocity(new Vector(0, 0.77f, 0));
					p.setVelocity(p.getLocation().getDirection().multiply(0.2D).setY(1.05D));
					break;
				}
				TitleAPI.sendFullTitle(p, 0, 10, 0, "", "");
				climbinglist.remove(p.getName());
				return;
			}
			/*
			 * double dix_x = Math.abs(p.getLocation().getX() -
			 * tb.getLocation().getBlockX()); double dix_z = Math.abs(p.getLocation().getZ()
			 * - tb.getLocation().getBlockZ());
			 */
			if (p.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR
					&& p.getLocation().getBlock().getType() != Material.STEP && tb.getType() != Material.AIR
					&& !climbinglist.containsKey(p.getName())) {
				if (useStamina(p, 5))
					return;
				p.setVelocity(p.getLocation().getDirection().multiply(-0.65D).setY(0.65D));
				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_STONE_PLACE, 3.0f, 0.3f);
				return;
			}
		}

		@EventHandler
		public void onRightClick(PlayerInteractEvent e) {
			if (!plist.contains(e.getPlayer().getName()))
				return;
			Player p = e.getPlayer();
			if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
				if (climbinglist.containsKey(e.getPlayer().getName())) {
					climbinglist.remove(e.getPlayer().getName());
					TitleAPI.sendFullTitle(p, 0, 60, 0, "", "§c§l손을 놓았습니다.");
					e.getPlayer().setAllowFlight(false);
					e.getPlayer().setFlying(false);
					return;
				} else if (e.getClickedBlock() != null && p.isSprinting()) {
					Block bl = e.getClickedBlock();
					if (bl.getLocation().getY() == (p.getLocation().getY() + 1)
							&& (bl.getRelative(0, 1, 0).getType() == Material.AIR
									|| bl.getRelative(0, 1, 0).getType() == Material.RAILS)) {
						if (useStamina(p, 3))
							return;
						p.setVelocity(p.getLocation().getDirection().multiply(0.55D).setY(0.62D));
						p.playSound(p.getLocation(), Sound.BLOCK_LAVA_POP, 2.0f, 0.5f);
					}
				}
			}
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
				if (p.getGameMode() == GameMode.CREATIVE)
					return;
				if (e.getClickedBlock() != null
						&& p.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.AIR
						&& e.getClickedBlock().getLocation().getBlockY() > p.getLocation().getY()
						&& (e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.AIR
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.THIN_GLASS
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.IRON_FENCE
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.FENCE
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.NETHER_FENCE
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.COBBLE_WALL
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.RAILS
								|| e.getClickedBlock().getRelative(0, 1, 0).getType() == Material.STAINED_GLASS_PANE)) {
					if(!climbinglist.containsKey(p.getName())) {
						Block bl = e.getClickedBlock();
						double dis_y = Math.abs(bl.getLocation().getY() - p.getLocation().getY());
						if (dis_y <= 4 && (Math.abs(bl.getLocation().getBlockX() - p.getLocation().getX()) <= 3
								&& Math.abs(bl.getLocation().getBlockZ() - p.getLocation().getZ()) <= 3)) {
							if (useStamina(p, 25))
								return;
							p.setAllowFlight(true);
							p.setFlying(true);
							p.teleport(p.getLocation(), TeleportCause.PLUGIN);
							climbinglist.put(p.getName(), (int) Math.round(dis_y) + 1);
							TitleAPI.sendFullTitle(p, 0, 200, 0, "§6§l매달림",
									"§f§l<§e§l좌클릭§b§l:§7§l손놓기 §a§l| §e§l쉬프트§f§l§b§l:§7§l오르기>");
							// p.sendMessage(MS+"매달리셨습니다. - 좌클릭 : 해제 , 쉬프트 : 오르기");
						}
					}				
				}
			}
		}

		@EventHandler
		public void onPlayerDamage(EntityDamageEvent e) {
			if (!(e.getEntity() instanceof Player))
				return;
			final Player p = (Player) e.getEntity();
			if (!plist.contains(p.getName()))
				return;
			if ((e.getCause() == EntityDamageEvent.DamageCause.FALL)) {
				e.setCancelled(true);
				if (!p.isSneaking() && e.getDamage() >= 8) {
					if (p.getLocation().getBlock().getRelative(0, 1, 0).getType() == Material.SPONGE)
						return;
					p.damage(0.1);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2.0f, 2.0f);
					TitleAPI.sendFullTitle(p, 0, 60, 0, "§c§l다리 골절", "§6§l잠시동안  점프가 불가능합니다.");
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 60, 199));
				} else {
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 2.0f, 3.0f);
				}
				return;
			}
		}
	}
}
