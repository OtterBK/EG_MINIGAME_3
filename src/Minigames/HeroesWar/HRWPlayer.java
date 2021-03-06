package Minigames.HeroesWar;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Minigames.HeroesWar.Abilities.Ability;

public class HRWPlayer {

	public Ability ability;
	public Team team;
	public int kill; //처치
	public int death; //데스
	public int dealAmt; //딜량
	public int poisonTime; //독 걸리게한 시간
	public int weaknessTime; //쇠약(침묵) 걸리게한 시간
	public int bindTime; //이동불가 걸리게한 시간
	public int speedUpTime; //이동속도 증가 걸리게한 시간
	public int speedSlowTime; //이속 감소 걸리게한 시간
	public int stopTime; //정지 걸리게한 시간
	public int noHealTime; //힐벤 걸리게한 시간
	public int freezeTime; //얼린 시간
	public int airboneCnt; //에어본 횟수
	public int fireTime; //화상 걸리게한 시간
	public int faintTime; //기절 걸리게한 시간
	public int barrierAmt; //막은량
	public int healAmt; //막은량
	public int absorbAmt; //흡수량
	public int addDamageAmt; //공격력 증폭량
	public int reverseAmt; //부활한 플레이어
	public int noDeathAmt; //사망 방지량
	public int breakTotem; //토템 파괴수
	public int occupy; //점령수
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
