package Utility;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import net.minecraft.server.v1_12_R1.WorldBorder;

public class WorldBorderAPI {

	public static void sendBorder(Player p, boolean shrinking, boolean growing, double centerX, double centerY, int size,int damage) {
		WorldBorder wb = new WorldBorder();
		CraftPlayer player = (CraftPlayer) p;
		wb.world = ((CraftWorld) player.getWorld()).getHandle();
		wb.setCenter(centerX, centerY);
		// System.out.println(f.getCenter().getX() + ":" + f.getCenter().getZ());
		 wb.setDamageAmount(damage);
		// wb.setDamageBuffer(-1);
		// wb.a(15);

		wb.setSize(size);
		
		if (shrinking)
			wb.transitionSizeBetween(size, 0, 1000);
		if (growing)
			wb.transitionSizeBetween(0, size, 1000);
		// System.out.println(wb.getSize());
		wb.setWarningDistance(0);
		wb.setWarningTime(300);

		// p.getWorld().getWorldBorder().reset();
		PacketPlayOutWorldBorder worldBorderPacketInit = new PacketPlayOutWorldBorder(wb,
				EnumWorldBorderAction.INITIALIZE);
		player.getHandle().playerConnection.sendPacket(worldBorderPacketInit);
		// Nations.logger.info("WorldBorder packet send!");
	}

	public static void sendPulsingBorder(Plugin server, Player p, boolean shrinking, boolean growing, double centerX, double centerY, int size,int damage) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {

			@Override
			public void run() {
				removeBorder(p);
				Bukkit.getScheduler().scheduleSyncDelayedTask(server, new Runnable() {

					@Override
					public void run() {
						sendBorder(p, shrinking, growing, centerX, centerY, size, damage);
						
					}
				}, 10);
			}
		}, 10);
	}

	public static void removeBorder(Player p) {
		WorldBorder wb = new WorldBorder();
		CraftPlayer player = (CraftPlayer) p;
		wb.world = ((CraftWorld) player.getWorld()).getHandle();

		wb.setCenter(0, 0);
		wb.setSize(60000000);

		PacketPlayOutWorldBorder worldBorderPacketInit = new PacketPlayOutWorldBorder(wb,
				EnumWorldBorderAction.INITIALIZE);
		player.getHandle().playerConnection.sendPacket(worldBorderPacketInit);
		// Nations.logger.info("WorldBorder removed!");
	}

}
