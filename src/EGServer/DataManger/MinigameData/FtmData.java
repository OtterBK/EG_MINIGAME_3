package EGServer.DataManger.MinigameData;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.PlayerData;
import Minigames.FindTheMurder.FindTheMurder.FtmPlayer;

public class FtmData extends MinigameData{
	
	public int death; //사망수
	public int innocent_kill; //무고한 플레이어를 죽인수
	public int murder_kill; //살마로 시민팀 살인한 수
	public int civil_kill; //시민팀으로 살인마팀 킬한 수
	public int doctor_revive; //의사로 살린수
	public int police_success; //경찰로 살인마팀 찾은수
	public int spy_contact; //스파이로 접선한 수
	public int soldier_revive; //군인으로 부활한 수
	public int crazy_kill; //미치광이로 사람을 죽인 수
	public int reporter_report; //기자로 밝힌 수
	public int reporter_reportSuccess; //기자로 살인마팀 밝힌 수
	public int priest_effort; //성직자의 표로 영향력을 발휘한 수
	public int priest_noVoted; //성직자로 포박 저지수
	public int magician_take; //마술사로 능력을 뺏은수
	public int magician_takeMurderTeam; //마술사로 살인마팀 능력을 뺏은수
	public int creator_addictionItem; //발명가로 추가 아이템을 얻은 수
	public int farmer_kill; //농부로 킬한수
	public int contractor_success; //계약자로 의뢰를 달성한 수
	public int shaman_success; //영매사로 능력을 얻은 수
	public int shaman_successMurderTeam; //영매사로 살인마팀 능력을 얻은 수
	public int negotiator_success; //협상가로 포섭한 수
	public int negotiator_fail; //협상가로 포섭 실패한 수
	public int keySmith_Use; //열쇠공이 열쇠를 사용한  수
	public int beVotedPlayer; //포박된 수
	
	public FtmData(PlayerData playerData, String gameName) {
		super(playerData, gameName);
	}

	@Override
	public void customDataSave(ConfigurationSection gameSection) {
		
		gameSection.set("death", death);
		gameSection.set("innocent_kill", innocent_kill);
		gameSection.set("murder_kill", murder_kill);
		gameSection.set("civil_kill", civil_kill);
		gameSection.set("doctor_revive", doctor_revive);
		gameSection.set("police_success", police_success);
		gameSection.set("spy_contact", spy_contact);
		gameSection.set("soldier_revive", soldier_revive);
		gameSection.set("crazy_kill", crazy_kill);
		gameSection.set("reporter_report", reporter_report);
		gameSection.set("reporter_reportSuccess", reporter_reportSuccess);
		gameSection.set("priest_effort", priest_effort);
		gameSection.set("priest_noVoted", priest_noVoted);
		gameSection.set("magician_take", magician_take);
		gameSection.set("magician_takeMurderTeam", magician_takeMurderTeam);
		gameSection.set("creator_addictionItem", creator_addictionItem);
		gameSection.set("contractor_success", contractor_success);
		gameSection.set("shaman_success", shaman_success);
		gameSection.set("shaman_successMurderTeam", shaman_successMurderTeam);
		gameSection.set("negotiator_success", negotiator_success);
		gameSection.set("negotiator_fail", negotiator_fail);
		gameSection.set("keySmith_Use", keySmith_Use);		
		gameSection.set("beVotedPlayer", beVotedPlayer);
	}
	
	@Override
	public void customDataLoad(ConfigurationSection gameSection) {
		
		death = gameSection.getInt("death");
		innocent_kill = gameSection.getInt("innocent_kill");
		murder_kill = gameSection.getInt("murder_kill");
		civil_kill = gameSection.getInt("civil_kill");
		doctor_revive = gameSection.getInt("doctor_revive");
		police_success = gameSection.getInt("police_success");
		spy_contact = gameSection.getInt("spy_contact");
		soldier_revive = gameSection.getInt("soldier_revive");
		crazy_kill = gameSection.getInt("crazy_kill");
		reporter_report = gameSection.getInt("reporter_report");
		reporter_reportSuccess = gameSection.getInt("reporter_reportSuccess");
		priest_effort = gameSection.getInt("priest_effort");
		priest_noVoted = gameSection.getInt("priest_noVoted");
		magician_take = gameSection.getInt("magician_take");
		magician_takeMurderTeam = gameSection.getInt("magician_takeMurderTeam");
		creator_addictionItem = gameSection.getInt("creator_addictionItem");
		contractor_success = gameSection.getInt("contractor_success");
		shaman_success = gameSection.getInt("shaman_success");
		shaman_successMurderTeam = gameSection.getInt("shaman_successMurderTeam");
		negotiator_success = gameSection.getInt("negotiator_success");
		negotiator_fail = gameSection.getInt("negotiator_fail");
		keySmith_Use	= gameSection.getInt("keySmith_Use");		
		beVotedPlayer	 = gameSection.getInt("beVotedPlayer");	
	}
	
	public void applyNewData(FtmPlayer ftmPlayer) {
		death += ftmPlayer.death; //사망수
		innocent_kill += ftmPlayer.innocent_kill; //무고한 플레이어를 죽인수
		murder_kill += ftmPlayer.murder_kill; //살마로 시민팀 살인한 수
		civil_kill += ftmPlayer.civil_kill; //시민팀으로 살인마팀 킬한 수
		doctor_revive += ftmPlayer.doctor_revive; //의사로 살린수
		police_success += ftmPlayer.police_success; //경찰로 살인마팀 찾은수
		spy_contact += ftmPlayer.spy_contact; //스파이로 접선한 수
		soldier_revive += ftmPlayer.soldier_revive; //군인으로 부활한 수
		crazy_kill += ftmPlayer.crazy_kill; //미치광이로 사람을 죽인 수
		reporter_report += ftmPlayer.reporter_report; //기자로 밝힌 수
		reporter_reportSuccess += ftmPlayer.reporter_reportSuccess; //기자로 살인마팀 밝힌 수
		priest_effort += ftmPlayer.priest_effort; //성직자의 표로 영향력을 발휘한 수
		priest_noVoted += ftmPlayer.priest_noVoted; //성직자로 포박 저지수
		magician_take += ftmPlayer.magician_take; //마술사로 능력을 뺏은수
		magician_takeMurderTeam += ftmPlayer.magician_takeMurderTeam; //마술사로 살인마팀 능력을 뺏은수
		creator_addictionItem += ftmPlayer.creator_addictionItem; //발명가로 추가 아이템을 얻은 수
		farmer_kill += ftmPlayer.farmer_kill; //농부로 킬한수
		contractor_success += ftmPlayer.contractor_success; //계약자로 의뢰를 달성한 수
		shaman_success += ftmPlayer.shaman_success; //영매사로 능력을 얻은 수
		shaman_successMurderTeam += ftmPlayer.shaman_successMurderTeam; //영매사로 살인마팀 능력을 얻은 수
		negotiator_success += ftmPlayer.negotiator_success;  //협상가로 포섭한 수
		negotiator_fail += ftmPlayer.negotiator_fail; //협상가로 포섭 실패한 수
		keySmith_Use += ftmPlayer.keySmith_Use; //열쇠공이 열쇠를 사용한  수
		beVotedPlayer += ftmPlayer.beVotedPlayer;
	}
	
}

