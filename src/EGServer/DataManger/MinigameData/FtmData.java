package EGServer.DataManger.MinigameData;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import EGServer.DataManger.PlayerData;
import Minigames.FindTheMurder.FindTheMurder.FtmPlayer;

public class FtmData extends MinigameData{
	
	public int death; //�����
	public int innocent_kill; //������ �÷��̾ ���μ�
	public int murder_kill; //�츶�� �ù��� ������ ��
	public int civil_kill; //�ù������� ���θ��� ų�� ��
	public int doctor_revive; //�ǻ�� �츰��
	public int police_success; //������ ���θ��� ã����
	public int spy_contact; //�����̷� ������ ��
	public int soldier_revive; //�������� ��Ȱ�� ��
	public int crazy_kill; //��ġ���̷� ����� ���� ��
	public int reporter_report; //���ڷ� ���� ��
	public int reporter_reportSuccess; //���ڷ� ���θ��� ���� ��
	public int priest_effort; //�������� ǥ�� ������� ������ ��
	public int priest_noVoted; //�����ڷ� ���� ������
	public int magician_take; //������� �ɷ��� ������
	public int magician_takeMurderTeam; //������� ���θ��� �ɷ��� ������
	public int creator_addictionItem; //�߸��� �߰� �������� ���� ��
	public int farmer_kill; //��η� ų�Ѽ�
	public int contractor_success; //����ڷ� �Ƿڸ� �޼��� ��
	public int shaman_success; //���Ż�� �ɷ��� ���� ��
	public int shaman_successMurderTeam; //���Ż�� ���θ��� �ɷ��� ���� ��
	public int negotiator_success; //���󰡷� ������ ��
	public int negotiator_fail; //���󰡷� ���� ������ ��
	public int keySmith_Use; //������� ���踦 �����  ��
	public int beVotedPlayer; //���ڵ� ��
	
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
		death += ftmPlayer.death; //�����
		innocent_kill += ftmPlayer.innocent_kill; //������ �÷��̾ ���μ�
		murder_kill += ftmPlayer.murder_kill; //�츶�� �ù��� ������ ��
		civil_kill += ftmPlayer.civil_kill; //�ù������� ���θ��� ų�� ��
		doctor_revive += ftmPlayer.doctor_revive; //�ǻ�� �츰��
		police_success += ftmPlayer.police_success; //������ ���θ��� ã����
		spy_contact += ftmPlayer.spy_contact; //�����̷� ������ ��
		soldier_revive += ftmPlayer.soldier_revive; //�������� ��Ȱ�� ��
		crazy_kill += ftmPlayer.crazy_kill; //��ġ���̷� ����� ���� ��
		reporter_report += ftmPlayer.reporter_report; //���ڷ� ���� ��
		reporter_reportSuccess += ftmPlayer.reporter_reportSuccess; //���ڷ� ���θ��� ���� ��
		priest_effort += ftmPlayer.priest_effort; //�������� ǥ�� ������� ������ ��
		priest_noVoted += ftmPlayer.priest_noVoted; //�����ڷ� ���� ������
		magician_take += ftmPlayer.magician_take; //������� �ɷ��� ������
		magician_takeMurderTeam += ftmPlayer.magician_takeMurderTeam; //������� ���θ��� �ɷ��� ������
		creator_addictionItem += ftmPlayer.creator_addictionItem; //�߸��� �߰� �������� ���� ��
		farmer_kill += ftmPlayer.farmer_kill; //��η� ų�Ѽ�
		contractor_success += ftmPlayer.contractor_success; //����ڷ� �Ƿڸ� �޼��� ��
		shaman_success += ftmPlayer.shaman_success; //���Ż�� �ɷ��� ���� ��
		shaman_successMurderTeam += ftmPlayer.shaman_successMurderTeam; //���Ż�� ���θ��� �ɷ��� ���� ��
		negotiator_success += ftmPlayer.negotiator_success;  //���󰡷� ������ ��
		negotiator_fail += ftmPlayer.negotiator_fail; //���󰡷� ���� ������ ��
		keySmith_Use += ftmPlayer.keySmith_Use; //������� ���踦 �����  ��
		beVotedPlayer += ftmPlayer.beVotedPlayer;
	}
	
}

