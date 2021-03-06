package Minigames.RandomWeaponWar.Weapons;

public class CustomCooldown {

	public static enum CooldownType{
		
		Primary, Secondary, Teritary;
		
	}
	
	private String playerName;
	
	private long next_primary;
	private long next_secondary;
	private long next_teritary;
	
	public CustomCooldown(String playerName) {
		this.playerName = playerName;
	}
	
	public CustomCooldown() {
		
	}
	
	public void setCooldown(CooldownType type, double cooldown) {
		
		long addTime = (long)(cooldown*1000);
		
		if(type.equals(CooldownType.Primary)) {
			next_primary = System.currentTimeMillis() + addTime;
		}else if(type.equals(CooldownType.Secondary)) {
			next_secondary = System.currentTimeMillis() + addTime;
		}else if(type.equals(CooldownType.Teritary)) {
			next_teritary = System.currentTimeMillis() + addTime;
		}
		
	}
	
	public long getLeftCooldown(CooldownType type) {
		
		long leftCooldown = 0;
		
		if(type.equals(CooldownType.Primary)) {
			leftCooldown = next_primary - System.currentTimeMillis();
		}else if(type.equals(CooldownType.Secondary)) {
			leftCooldown = next_secondary - System.currentTimeMillis();;
		}else if(type.equals(CooldownType.Teritary)) {
			leftCooldown = next_teritary - System.currentTimeMillis();;
		}
		
		if(leftCooldown < 0) leftCooldown = 0;
		
		return leftCooldown;
		
	}
	
	public boolean checkCooldown(CooldownType type) {
		long leftCooldown = getLeftCooldown(type);
		
		if(leftCooldown <= 0) {
			return true;
		}else {
			return false;
		}
	}
	
}
