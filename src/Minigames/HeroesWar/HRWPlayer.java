package Minigames.HeroesWar;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Minigames.HeroesWar.Abilities.Ability;

public class HRWPlayer {

	public Ability ability;
	public Team team;
	public int kill; //óġ
	public int death; //����
	public int dealAmt; //����
	public int poisonTime; //�� �ɸ����� �ð�
	public int weaknessTime; //���(ħ��) �ɸ����� �ð�
	public int bindTime; //�̵��Ұ� �ɸ����� �ð�
	public int speedUpTime; //�̵��ӵ� ���� �ɸ����� �ð�
	public int speedSlowTime; //�̼� ���� �ɸ����� �ð�
	public int stopTime; //���� �ɸ����� �ð�
	public int noHealTime; //���� �ɸ����� �ð�
	public int freezeTime; //�� �ð�
	public int airboneCnt; //��� Ƚ��
	public int fireTime; //ȭ�� �ɸ����� �ð�
	public int faintTime; //���� �ɸ����� �ð�
	public int barrierAmt; //������
	public int healAmt; //������
	public int absorbAmt; //�����
	public int addDamageAmt; //���ݷ� ������
	public int reverseAmt; //��Ȱ�� �÷��̾�
	public int noDeathAmt; //��� ������
	public int breakTotem; //���� �ı���
	public int occupy; //���ɼ�
	public long lastKillTime;
	public int maxStackKill;
	public int nowStackKill;
	public String lastDamager = "";
	public boolean alreadyDead;
	public ItemStack item_stone = null;
	public ItemStack item_ring = null;
	public ItemStack item_neck = null;
	public ItemStack item_tailsman = null;
	public int carrotCnt = 0;

	
	
	public HRWPlayer(Player p, Ability ability, Team team) {
		
		this.ability = ability;
		this.team = team;
		this.ability.hrwData = this;
	}
	
}
