package Utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class NamaTagChange {
	
	public static void changeNametag(Plugin server, Player p, String chgName) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(server, new Runnable() {
			public void run() {
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER));
			    GameProfile gp = ((CraftPlayer)p).getProfile();
			    try {
			        Field nameField = GameProfile.class.getDeclaredField("name");
			        nameField.setAccessible(true);

			        Field modifiersField = Field.class.getDeclaredField("modifiers");
			        modifiersField.setAccessible(true);
			        modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

			        nameField.set(gp, chgName);
			    } catch (IllegalAccessException | NoSuchFieldException ex) {
			        throw new IllegalStateException(ex);
			    }
			    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER));
			    for(Player pl : Bukkit.getOnlinePlayers()){
			        if(pl != p){
			            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(new int[]{p.getEntityId()}));
			            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)p).getHandle()));
			        }
			    }
			}
		}, 0l, 1l);
		
	}

    
	
}
