package EGServer.NoteBlockPlayer;

import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import EGServer.EGServer;
import EGServer.ServerManager.EGPlugin;

public class NoteBlockPlayer extends EGPlugin{

	private String dirPath = server.getDataFolder().getPath() + "/Songs";
	private String ms = "§f[ §aEG DJ §f] ";
	
	public NoteBlockPlayer(EGServer server) {
		super(server);
		dirSetting("NoteBlockPlayer");
	}
	
	public static enum SongPlayType{
		
		Radio, Position, NoteBlock, Entity;
		
	}
	
	public Song getSong(String fileName) {
		Song song = null;
		File file = new File(dirPath+"/"+fileName+".nbs");
		if(file.exists()) {
			song = NBSDecoder.parse(file);
		}
		
		return song;
	}
	
	public void songCommand(Player p, String cmdLine) {
		String[] cmd = cmdLine.split(" ");
		if(cmd.length == 1) {
			p.sendMessage(ms+"/노래 끄기 - 자신이 듣는 노래를 꺼줍니다.");
			p.sendMessage(ms+"/노래 설치 <제목> [거리] - 현재 장소에 노래를 설치합니다."); //포지션
			p.sendMessage(ms+"/노래 틀기 <닉넴> <제목> - 노래를 틀어줍니다."); //라디오
			p.sendMessage(ms+"/노래 끄기 <닉넴> <제목>- 노래를 꺼줍니다.");
			p.sendMessage(ms+"/노래 달기 <닉넴> <제목> - 노래를 달아줍니다."); //엔티티
			return;
		}else {
			if(cmd[1].equalsIgnoreCase("설치")) {
				if(cmd.length <= 2) {
					p.sendMessage(ms+"노래 이름을 입력해주세요.");
				}else {
					Song song = getSong(cmd[2]);
					if(song == null) {
						p.sendMessage(ms+cmd[2]+"와 일치하는 노래파일이 없습니다.");
					}else {
						PositionSongPlayer psp = new PositionSongPlayer(song);
						// Set location where the song will be playing
						psp.setTargetLocation(p.getLocation());
						// Set distance from target location in which will players hear the SongPlayer
						psp.setDistance(cmd.length <= 3 ? 16 : Integer.valueOf(cmd[3])); // Default: 16
						// Add player to SongPlayer so he will hear the song.
						//psp.add
						// Start RadioSongPlayer playback
						psp.setPlaying(true);
					}
				}
			}
		}
	}
	
	private class NoteBlockPlayerEventHandler implements Listener{
		
		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayercommand(PlayerCommandPreprocessEvent e)	{
			Player p = e.getPlayer();
			if(e.getMessage() == null) return;
			String cmdMain = e.getMessage().split(" ")[0];
			if(cmdMain == null) return;
			if(cmdMain.equalsIgnoreCase("/노래")) {
				songCommand(p, e.getMessage());
				e.setCancelled(true);
			}
		}
		
	}
}
	
