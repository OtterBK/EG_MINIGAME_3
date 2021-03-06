package Minigames.HeroesWar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import EGServer.EGServer;
import Minigames.Minigame;
import Utility.SkullCreator;

public class HRWBase extends Minigame{
	//��
	public Team blueTeam;
	public Team redTeam;
	
	//�÷��̾��
	public HashMap<String, HRWPlayer> playerMap = new HashMap<String, HRWPlayer>(30);
	
	//������
	
	//public ItemStack stone_land;
	public ItemStack stone_forest;
	public ItemStack stone_miracle;
	public ItemStack stone_water;
	public ItemStack stone_fire;
	
	//public ItemStack ring_light;
	public ItemStack ring_speed;
	public ItemStack ring_gravity;
	public ItemStack ring_gale;
	public ItemStack ring_storm;
	
	//public ItemStack neck_black;
	public ItemStack neck_iron;
	public ItemStack neck_gold;
	public ItemStack neck_diamond;
	public ItemStack neck_emerald;
	
	public ItemStack tailsman_miracle;
	public ItemStack tailsman_eminent;
	public ItemStack tailsman_heal;
	//public ItemStack tailsman_meditation;
	public ItemStack tailsman_wisdom;
	
	/////// ���� ������
	public ItemStack item_hrwHelper;
	public ItemStack item_selectJob;
	public ItemStack item_returnBase;
	public ItemStack item_none_stone;
	public ItemStack item_none_ring;
	public ItemStack item_none_neck;
	public ItemStack item_none_tailsman;
	
	//������
	public CatchPoint point1;
	public CatchPoint point2;
	public CatchPoint point3;
	
	//���� �κ�	
	public Inventory inven_desc_tracer;
	public Inventory inven_desc_assasin;
	public Inventory inven_desc_hunter;
	public Inventory inven_desc_mercenary;
	public Inventory inven_desc_archer;
	public Inventory inven_desc_wizard;
	public Inventory inven_desc_hider;
	public Inventory inven_desc_virtuous;
	public Inventory inven_desc_marksman;
	public Inventory inven_desc_guardian;
	public Inventory inven_desc_monk;
	public Inventory inven_desc_knight;
	public Inventory inven_desc_fighter;
	public Inventory inven_desc_monarch;
	public Inventory inven_desc_predator;
	public Inventory inven_desc_priest;
	public Inventory inven_desc_alchemist;
	public Inventory inven_desc_blackMagician;
	public Inventory inven_desc_warrior;
	
	public String ms = "��7[��6��ü��7] ";
	public String allms = "��7[ ��6��ü ��7] ";
	
	
	////// ���� �κ��丮
	public Inventory inven_gameHelper;
	public Inventory inven_hrwHelper;
	
	//�ɷ� ���
	public List<Integer> abCodeList = new ArrayList<Integer>();
	
	public HRWBase(EGServer server) {
		super(server);

		abCodeList.add(3);
		abCodeList.add(4);
		abCodeList.add(5);
		abCodeList.add(6);
		abCodeList.add(7);
		abCodeList.add(12);
		abCodeList.add(13);
		abCodeList.add(14);
		abCodeList.add(15);
		//��Ŀ
		abCodeList.add(21);
		abCodeList.add(22);
		abCodeList.add(23);
		abCodeList.add(24);
		abCodeList.add(25);
		abCodeList.add(30);
		abCodeList.add(31);
		//����
		abCodeList.add(39);
		abCodeList.add(40);
		abCodeList.add(41);
			
		//��
		blueTeam = new Team(this, "BLUE", Color.BLUE,"��7[ ��b����� ��7] ", Color.fromRGB(0,0,255), abCodeList);
		redTeam = new Team(this, "RED", Color.RED,"��7[ ��c������ ��7] ", Color.fromRGB(255,0,0), abCodeList);
		
		/////////////////// ����� ��������
		blueTeam.jobList = Bukkit.createInventory(null, 54, "��f[ ��b��l�������� ��f]");

		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i < 54; i += 9) {
			blueTeam.jobList.setItem(i, item);
		}
		for (int i = 8; i < 54; i += 9) {
			blueTeam.jobList.setItem(i, item);
		}
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���ݱ� ��7-");
		List<String> loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ���ݽ�ų�� ���� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(1, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� �����ϰų� ������ ���� �Ʊ��� ��ȣ�մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(19, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/41a6a411b802b22e5324795ce34e3cae4a83796a18d92332666cbf21488c0");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ġ�� �Ǵ� ��ȭ��ų�� �̿��Ͽ� �Ʊ��� �����ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(37, item);
		
		item = new ItemStack(Material.IRON_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�⵿���� �پ �������� ���� ��������");
		loreList.add("��7���������� �����ϱ� �����ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(3, item);
		
		item = new ItemStack(Material.DIAMOND_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�ڰ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ؼ��� �⵿���� �ټ��� ���� ���� ���ݿ�");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(4, item);
		
		item = new ItemStack(Material.GOLD_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a��ɲ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ſ� ���� ���� ��ų�� ������");
		loreList.add("��7�ϳ��� ���� �����ϰ� ������ �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(5, item);
		
		item = new ItemStack(Material.GOLD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�뺴 ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �������� �⺻�ɷ��� ���������� �����Ͽ� �����ϴ�");
		loreList.add("��7�������� ��ų����� ���������� ���뼺�� �پ�ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(6, item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�ü� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ��� ���� ������ ���Ÿ������� Ư¡�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(7, item);
		
		item = new ItemStack(Material.BLAZE_ROD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ټ����� ���Ÿ� ���� ������ ����ϴ�");
		loreList.add("��7ȭ�¿� Ưȭ�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7����� ���� ���¿��� ���� �޽��ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(13, item);
		
		item = new ItemStack(Material.GOLD_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ ���ƴٴϸ� ���� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(14, item);
		
		item = new ItemStack(Material.IRON_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a���ݼ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���Ÿ����� ������ ġ������ ������ ���մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(15, item);
		
		item = new ItemStack(Material.SHIELD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b��ȣ�� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�溮�� ����Ͽ� �ֺ� �Ʊ��� ��ȣ�ϸ�");
		loreList.add("��7������ CC��ų�� ������ �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(21, item);
		
		item = new ItemStack(Material.DIAMOND, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ����� �溮�� �ο��Ͽ� ���� ������ ����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(22, item);
		
		item = new ItemStack(Material.BONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b��� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ���Ǹ� ���� �Ʊ��� ��ȣ�ϰ�");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(23, item);
		
		item = new ItemStack(Material.IRON_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���Ӿ��� �������� Ư�� ����� ������ �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(24, item);
		
		item = new ItemStack(Material.BLAZE_POWDER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ��� �޴� �������� ��� �ްų�");
		loreList.add("��7���� �̵��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(25, item);
		
		item = new ItemStack(Material.SHEARS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7Ư�� ����� �Ʊ� �������� ����� �� ������");
		loreList.add("��7���ϴ� ������ ��Ż�� �� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(30, item);
		
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �����Ͽ� ���� �̵���");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(31, item);
		
		item = new ItemStack(Material.SLIME_BALL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ��� ü���� ȸ����Ű�ų� ���� �Ʊ���");
		loreList.add("��7��Ȱ��ų �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(39, item);
		
		item = new ItemStack(Material.FLINT_AND_STEEL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� �ൿ�� �����ϰ�");
		loreList.add("��7�Ʊ��� ��ȭ�ϸ� ������ �ݻ��մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(40, item);
		
		item = new ItemStack(Material.COAL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e�渶���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ��ų ����� �����ϰ�");
		loreList.add("��7�Ʊ��� ��ȭ�մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		blueTeam.jobList.setItem(41, item);
		
		////////////////

		/////////////////// ������ ��������
		redTeam.jobList = Bukkit.createInventory(null, 54, "��f[ ��c��l�������� ��f]");

		item = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		for (int i = 0; i < 54; i += 9) {
			redTeam.jobList.setItem(i, item);
		}
		for (int i = 8; i < 54; i += 9) {
			redTeam.jobList.setItem(i, item);
		}
	
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���ݱ� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ������ ���ݽ�ų�� ���� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(1, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c���� ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ���� �����ϰų� ������ ���� �Ʊ��� ��ȣ�մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(19, item);
		
		item = SkullCreator.itemFromUrl("http://textures.minecraft.net/texture/41a6a411b802b22e5324795ce34e3cae4a83796a18d92332666cbf21488c0");
		meta = item.getItemMeta();
		meta.setDisplayName("��7- ��c������ ��7-");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ġ�� �Ǵ� ��ȭ��ų�� �̿��Ͽ� �Ʊ��� �����ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(37, item);
		
		item = new ItemStack(Material.IRON_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�⵿���� �پ �������� ���� ��������");
		loreList.add("��7���������� �����ϱ� �����ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(3, item);
		
		item = new ItemStack(Material.DIAMOND_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�ڰ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ؼ��� �⵿���� �ټ��� ���� ���� ���ݿ�");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(4, item);
		
		item = new ItemStack(Material.GOLD_AXE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a��ɲ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ſ� ���� ���� ��ų�� ������");
		loreList.add("��7�ϳ��� ���� �����ϰ� ������ �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(5, item);
		
		item = new ItemStack(Material.GOLD_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�뺴 ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �������� �⺻�ɷ��� ���������� �����Ͽ� �����ϴ�");
		loreList.add("��7�������� ��ų����� ���������� ���ݷ��� �پ�ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(6, item);
		
		item = new ItemStack(Material.BOW, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a�ü� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ��� ���� ������ ���Ÿ������� Ư¡�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(7, item);
		
		item = new ItemStack(Material.BLAZE_ROD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�ټ����� ���Ÿ� ���� ������ ����ϴ�");
		loreList.add("��7ȭ�¿� Ưȭ�� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(12, item);
		
		item = new ItemStack(Material.DIAMOND_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7����� ���� ���¿��� ���� �޽��ϴ� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(13, item);
		
		item = new ItemStack(Material.GOLD_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ ���ƴٴϸ� ���� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(14, item);
		
		item = new ItemStack(Material.IRON_HOE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��a���ݼ� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���Ÿ����� ������ ġ������ ������ ���մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(15, item);
		
		item = new ItemStack(Material.SHIELD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b��ȣ�� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�溮�� ����Ͽ� �ֺ� �Ʊ��� ��ȣ�ϸ�");
		loreList.add("��7������ CC��ų�� ������ �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(21, item);
		
		item = new ItemStack(Material.DIAMOND, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ����� �溮�� �ο��Ͽ� ���� ������ ����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(22, item);
		
		item = new ItemStack(Material.BONE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b��� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ���Ǹ� ���� �Ʊ��� ��ȣ�ϰ�");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(23, item);
		
		item = new ItemStack(Material.IRON_SPADE, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���Ӿ��� �������� Ư�� ����� ������ �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(24, item);
		
		item = new ItemStack(Material.BLAZE_POWDER, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ��� �޴� �������� ��� �ްų�");
		loreList.add("��7���� �̵��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(25, item);
		
		item = new ItemStack(Material.SHEARS, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b������ ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7Ư�� ����� �Ʊ� �������� ����� �� ������");
		loreList.add("��7���ϴ� ������ ��Ż�� �� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(30, item);
		
		item = new ItemStack(Material.IRON_SWORD, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��b���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7������ �����Ͽ� ���� �̵���");
		loreList.add("��7�����մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(31, item);
		
		item = new ItemStack(Material.SLIME_BALL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�Ʊ��� ü���� ȸ����Ű�ų� ���� �Ʊ���");
		loreList.add("��7��Ȱ��ų �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(39, item);
		
		item = new ItemStack(Material.FLINT_AND_STEEL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� �ൿ�� �����ϰ�");
		loreList.add("��7�Ʊ��� ��ȭ�ϸ� ������ �ݻ��մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(40, item);
		
		item = new ItemStack(Material.COAL, 1);
		meta = item.getItemMeta();
		meta.setDisplayName("��f[ ��e�渶���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7���� ��ų ����� �����ϰ�");
		loreList.add("��7�Ʊ��� ��ȭ�մϴ�.");
		loreList.add("");
		loreList.add("��7���̵�: ��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		redTeam.jobList.setItem(41, item);
		
		inven_desc_tracer = Bukkit.createInventory(null, 9, "��0��l���� - ������");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�̵����� �������� ª�� �Ÿ��� ���İ���");
		loreList.add("��7�̵��մϴ�.");
		loreList.add("��7�ִ� 5ȸ���� �����ص� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a3��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ȯ��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��76������ ��ġ�� ü������ �ǵ��ư��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a17��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(1 ,item);
		
		/*item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ȯ��f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��76������ ��ġ�� ü������ �ǵ��ư��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a6��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_itemShop.setItem(2 ,item);*/
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������ ��ź��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��71ĭ�� �ٶ󺸴� ������");
		loreList.add("��7��ź�� ���� �Ǵ� ��ġ�մϴ�.");
		loreList.add("��7��ź�� �Ÿ��� ���� �ִ� 36�� ���ظ� �ݴϴ�.");
		loreList.add("��7���� ������ �ڽŵ� ���� �� ���ظ� �޽��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a50��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��������f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7������ ������ �ð��� 3�ʿ���");
		loreList.add("��72.4�ʷ� �پ��ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_tracer.setItem(8 ,item);
		
		inven_desc_assasin = Bukkit.createInventory(null, 9, "��0��l���� - �ڰ�");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ���ɡ�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7������ �ڷ� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a3��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eǥâ ��ô��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7���濡 3���� ǥâ�� �����ϴ�.");
		loreList.add("��7ǥâ�� ������ ���� 8�� ���ظ� �ְ� 8�ʰ� �����·� ����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a6��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ǳ��f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7������ �������� ���ư���");
		loreList.add("��7��λ��� ������ 12�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a9��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�Ű���ü��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7��� 0.5�� �� 6�ʰ� ������ ���⸦ ��ȯ�մϴ�.");
		loreList.add("��7�̵��ӵ��� ũ�� �����ϰ� ���ݷ���");
		loreList.add("��715�� �Ǹ� ���ڱ߱��� ���� ���ð��� �ʱ�ȭ�˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a88��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�̴� �ٱ��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7����Ű�� �������� 2�� ������");
		loreList.add("��72�� ������ ���������� ���� óġ�� ��");
		loreList.add("��7���ڱ߱��� ���� ���ð��� �ʱ�ȭ�˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a2��");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_assasin.setItem(8 ,item);
		
		
		inven_desc_hunter = Bukkit.createInventory(null, 9, "��0��l���� - ��ɲ�");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��簨��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��73�ʰ� �̵��ӵ��� 75% �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a10��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� �����f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7���� ������� ��븦 ���� ����");
		loreList.add("��713�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a5��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� �����f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��72�� ������ϴ�.");
		loreList.add("��71ȸ�� 8�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ɡ�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�������� ������ �����ϴ�.");
		loreList.add("��7���� ���ٿ� ���� �� �ش� ���� �ڷ� �̵��ϰ�");
		loreList.add("��725�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a39��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� �����f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� ���� ���� 2ȸ�� �ƴ�");
		loreList.add("��73ȸ ������ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hunter.setItem(8 ,item);
		
		
		inven_desc_mercenary = Bukkit.createInventory(null, 9, "��0��l���� - �뺴");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�� �ˡ�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7Ÿ�� ����� 10�ʰ� �ߵ� ���·� ����ϴ�.");
		loreList.add("");
		loreList.add("��4�⺻ ��� �ߵ� �ɷ��� �ش� �ɷ����� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�麴����f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7Ÿ�� ����� �ָ� �������ϴ�.");
		loreList.add("");
		loreList.add("��4�⺻ ��� �ߵ� �ɷ��� �ش� �ɷ����� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ���֡�f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7�̵� �ӵ��� 25% �����մϴ�.");
		loreList.add("");
		loreList.add("��4�⺻ ��� �ߵ� �ɷ��� �ش� �ɷ����� �����մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eȸ�͡�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��730ĭ���� �ٶ󺸴� ������ ���İ��� �̵��մϴ�.");
		loreList.add("��7�̵��� ���� ��ġ�� 4�ʰ� �ܻ��� ����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a35��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eȸ�ǡ�f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��710% Ȯ���� ���� ������ ȸ���ϰ�");
		loreList.add("��7�ش� ���� �ڷ� �̵��մϴ�.");
		loreList.add("��7ȸ�ǽÿ� �޴� ���ذ� ������ �˴ϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_mercenary.setItem(8 ,item);
		
		
		inven_desc_archer = Bukkit.createInventory(null, 9, "��0��l���� - �ü�");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ȭ���f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7��ų ����� ��� ȭ�� 1���� ���� Ÿ���� ��");
		loreList.add("��7�ش� ���� 1~3�ʰ� �����մϴ�.");
		loreList.add("��7(Ȱ ������ ��丸ŭ �ð��� �þ)");
		loreList.add("��7�ִ� ���ӽð��� 6���Դϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a9��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ӻ��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7��ų ����� ��� 4���� ȭ���� �ſ� ������");
		loreList.add("��7���󰩴ϴ�.");
		loreList.add("��7�ش� ȭ���� �ִ� ���ش� 8�� �����˴ϴ�.");
		loreList.add("��7�ִ� ���ӽð��� 8���Դϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a19��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ������ ������ ���� ȭ���� ����߸��ϴ�.");
		loreList.add("��7ȭ���� �� �Ǵ� ���� ����� �����ϸ� ���� 27��");
		loreList.add("��7���ظ�  �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a77��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���á�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7��ȭ���� ���� ���� ��");
		loreList.add("��7������ �ƴ� ������ŵ�ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_archer.setItem(8 ,item);
		
		
		
		inven_desc_wizard = Bukkit.createInventory(null, 9, "��0��l���� - ������");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eȭ��ź��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�������� ȭ��ź�� �����ϴ�.");
		loreList.add("��7ȭ��ź�� �� �Ǵ� ���� ���� �� �����ϸ�");
		loreList.add("��7�������� 3ĭ���� ������ 5�� ���ظ� �ָ� ������ �÷��̾�");
		loreList.add("��7���Դ� 9�� �߰����ظ� �ְ�");
		loreList.add("��78���� ȭ�����ظ� �����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a3��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ҵ��̡�f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ���� ȭ�����̸� �����ϴ�.");
		loreList.add("��7ȭ�����̴� ��ź�� �ұ����̸� �����");
		loreList.add("��7�ұ����� ���� ������ �ʴ� 4�� ���ؿ�");
		loreList.add("��77���� ȭ�����ظ� �����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a15��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���ȭ��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ����  �ſ� ū ������ ����ŵ�ϴ�.");
		loreList.add("��7���� ���� ���� ���� �߽������� �Ÿ��� ����");
		loreList.add("��7�ִ� 50�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a91��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7��� ȭ�����ذ� 4�� �� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_wizard.setItem(8 ,item);
		
		
		inven_desc_hider = Bukkit.createInventory(null, 9, "��0��l���� - ������");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���С�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7������ ����� ����ϴ�.");
		loreList.add("��7���� ���¿��� ���� ������ ��");
		loreList.add("��76�� �߰� ���ظ� �ݴϴ�.");
		loreList.add("��7���� �Ǵ� ���ظ� ���� �� ������ Ǯ���ϴ�.");
		loreList.add("��7���� ���¿����� ���� ����, �����ı��� �Ұ����մϴ�.");
		loreList.add("��7������ �ִ� 25�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�׸��� �̵���f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7���� ���� ��ġ�� �����صΰ�");
		loreList.add("��7�� ���� �����ص� ��ġ�� �̵��մϴ�.");
		loreList.add("��7�����ص� ��ġ�� �̵��� ������ �����˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a9��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��谭����f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�ſ� ������ 5�� �������");
		loreList.add("��7�ٶ󺸴� ����� �����մϴ�.");
		loreList.add("��7�� ������ 4��ŭ ���ظ� �ݴϴ�.");
		loreList.add("��7[5ȸ �������� ��޵˴ϴ�.]");
		loreList.add("��7[���� ������ �����˴ϴ�.]");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a38��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�Ͼ��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� ���¿��� ���� Ÿ�� ��");
		loreList.add("��7���� 1�ʰ� ��ų��� �Ұ��� ���·� ����ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_hider.setItem(8 ,item);
		
		
		inven_desc_virtuous = Bukkit.createInventory(null, 9, "��0��l���� - ����");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�޻�¡�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF��f]");
		loreList.add("");
		loreList.add("��7���� ������ ����մϴ�.");
		loreList.add("��7������ ���� ���¿����� ��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a10��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ǳ��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� �������� ��ǳ�� ����");
		loreList.add("��7��ź �������� �Ÿ��� ����");
		loreList.add("��7�ִ� 9�� ���ظ� �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a2��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� �����f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7�������� �Ҹ��Ͽ� ���߿� ��¦");
		loreList.add("��7�����մϴ�.");
		loreList.add("��7�������� �ִ� 7�� ���� �����մϴ�.");
		loreList.add("��7�������� ������ ��� �����ÿ��� �����˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a16��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ǳ��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7���濡 �ſ� ���� ���� ��ǳ�� �����ϴ�.");
		loreList.add("��7����߿��� �̵� �� ��ų����� �Ұ����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a102��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(3 ,item);
		
		/*item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� ���ظ� ���� �ʽ��ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(8 ,item);*/
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���깫����f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7��ǳ�� ���� ������ 2ĭ �þ��");
		loreList.add("��7�ִ� ������ �������� 3�� �þ�ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_virtuous.setItem(8 ,item);
		
		
		inven_desc_marksman = Bukkit.createInventory(null, 9, "��0��l���� - ���");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� Ż���f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��73ĭ�� �ٷκ��� ���� ��ġ��");
		loreList.add("��7�ڽ��� �ڷ� ����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a10��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���ذ��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ + ��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�þ߸� Ȯ���մϴ�.");
		loreList.add("��7�ٽ��ѹ� �� �� �ǵ����ϴ�.");
		loreList.add("��7�þ� Ȯ���߿��� �̵��� �Ұ����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a0��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ݡ�f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ������ �Ѿ��� �߻��մϴ�.");
		loreList.add("��7�Ѿ��� ���� ������ 18�� ���ظ� �ݴϴ�.");
		loreList.add("��7���� �Ѿ��� �Ҹ��ϸ�");
		loreList.add("��7�Ѿ��� �ִ� 7�� ���� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a1��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ + F ��f]");
		loreList.add("");
		loreList.add("��7�Ѿ��� �����մϴ�.");
		loreList.add("��7�ʴ� 1���� �����ϸ� ����Ʈ�� �� �� ����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a0��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e����ź��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7��� 7���� ����ź�� �����մϴ�.");
		loreList.add("��7����ź�� �Ѿ��� ��ź ������ ������ ������");
		loreList.add("��7��ź�������� �Ÿ��� ���� �ִ� 13�� �߰� ���ظ� �����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a60��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(4 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e����� �����֡�f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� �ӵ��� 0.5�� �������ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_marksman.setItem(8 ,item);
		
		
		inven_desc_guardian = Bukkit.createInventory(null, 9, "��0��l���� - ��ȣ��");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�溮 ������f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�溮�� �����մϴ�.");
		loreList.add("��7������ 10ĭ ���� �Ʊ��� �޴� ���ظ� �溮�� ���");
		loreList.add("��7������ �溮�� �������� �����ٷ� �� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a0��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���С�f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f+ ��e����Ʈ ��f]");
		loreList.add("");
		loreList.add("��7�溮���� �ִ� ���� ���");
		loreList.add("��7�溮 ������ ���ĳ��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a15��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�͵�����f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7�������� �����մϴ�.");
		loreList.add("��7���������� �ִ� ���� �Բ� ������");
		loreList.add("��7���� �΋H���ų� ������ ������ ������ 20�� ���ظ� �ָ�");
		loreList.add("��72�ʰ� ������ŵ�ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a11��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��������f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7���� 10ĭ ���� ������ ����Ű��");
		loreList.add("��7������ ���� ���� 8�� ���ظ� �ް�");
		loreList.add("��73�ʰ� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a95��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�溮 ��ȭ��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7�溮�� �ִ� �������� 50 �����մϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_guardian.setItem(8 ,item);
		
		
		inven_desc_monk = Bukkit.createInventory(null, 9, "��0��l���� - ������");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� �溮��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ڽſ��� �溮�� ����մϴ�.");
		loreList.add("��7�溮�� �ִ� 25��  ���ظ� �����ָ�");
		loreList.add("��73�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a13��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��745ĭ�� �ٶ󺸴� �Ʊ����� �溮�� ����մϴ�.");
		loreList.add("��7�溮�� �ִ� 25��  ���ظ� �����ָ�");
		loreList.add("��73�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a11��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ݷ���f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�ֺ� 30ĭ �� ��� �Ʊ����� �溮�� �ο��մϴ�.");
		loreList.add("��7�溮�� �ִ� 15��  ���ظ� �����ָ�");
		loreList.add("��74�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a81��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eȿ��  ������f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7�溮�� ���� �� �ִ� �ִ� ���ط���");
		loreList.add("��715 �����մϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monk.setItem(8 ,item);
		
		
		inven_desc_knight = Bukkit.createInventory(null, 9, "��0��l���� - ���");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e����������f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7������ ���� ������ �ݴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a2��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���ߡ�f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��712ĭ���� ���� �ڽ������� �������");
		loreList.add("��7�̵��ӵ��� 15% ���ҽ�ŵ�ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ϱ� ������f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��72ĭ�� ������ 5�� ���ظ� �ְ�");
		loreList.add("��72�ʰ� ��ų ���Ұ� ���·� ����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a14��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���ġ�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7��ų ����� 0.6�� ��");
		loreList.add("��7�ٶ󺸴� ���� 50ĭ ������ ���� ������ �����մϴ�.");
		loreList.add("��7���� ������ ���� 10��  ���ظ� �ް�");
		loreList.add("��72�ʰ� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a89��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��絵 ���š�f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7�������� ���� �� �� �ָ� �ٸ�");
		loreList.add("��7���߰� �ϱⰡ���� ������ 2ĭ �þ�ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_knight.setItem(8 ,item);
		
		inven_desc_fighter = Bukkit.createInventory(null, 9, "��0��l���� - ������");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ���� ������ ���İ��� �̵��մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a6��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��Ÿ��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��74ĭ�� �ٶ󺸴� ������ 6�� ���ظ�");
		loreList.add("��7�ְ� 1�ʰ� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a4��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ���ϡ�f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��75�ʰ� �̵��Ұ� ���°��Ǹ�");
		loreList.add("��740�� ü���� ������ ȸ���մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a19��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ϰ� �ʻ��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��72ĭ�� ������ ������ ����");
		loreList.add("��7�ش� ���� ������Ű�� 26�� ���ظ� �ݴϴ�.");
		loreList.add("��7�����߿��� �������°� �˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a43��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���߷� ����f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� ���� ���� 4�ʸ���");
		loreList.add("��7ü���� 40 ȸ���ϰ� �˴ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_fighter.setItem(8 ,item);
		
		inven_desc_monarch = Bukkit.createInventory(null, 9, "��0��l���� - ����");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��罽��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7��罽�� ���� ���� ���� 1�ʰ� ������Ű��");
		loreList.add("��7�ڽ��� ������ ����ɴϴ�.");
		loreList.add("��7�ִ� �����Ÿ��� 25ĭ�Դϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ������f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7����� �ٶ��� ���� ��ź ������ ��������");
		loreList.add("��7���� ���������ϴ�.");
		loreList.add("��7�������� ���� ���� �ϴ÷� ������ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a11��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� �����f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��74�ʰ� 8ĭ ���� �Ʊ��� �޴� ���ظ�");
		loreList.add("��7�������� �ϰ� �ڽ��� ��� �޽��ϴ�.");
		loreList.add("��7�ش� ��ų ����߿��� ü���� 1���Ϸ� ��������");
		loreList.add("��7�ʽ��ϴ�. (�ߵ�, ȭ�� ���� ����)");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a16��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ���ա�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� ������ ������ �����մϴ�.");
		loreList.add("��7���յ� ������ 8ĭ �� ������ ��� �������");
		loreList.add("��74�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a91��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ܷõ� ����f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7�������밡 ���ҽ�Ű�� ���ط��� ���ݿ���");
		loreList.add("��71/3 ���� �����մϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_monarch.setItem(8 ,item);
		
		inven_desc_predator = Bukkit.createInventory(null, 9, "��0��l���� - ������");		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eö����f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��75�ʰ� ��� CC��  �鿪�˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ȹ��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��75ĭ�� �ٶ󺸴� ���� 2�ʰ� ��ȹ�մϴ�.");
		loreList.add("��7���� ��ȹ���� �� �ڽ��� �̵��ӵ��� 25% �����ϸ�");
		loreList.add("��7��ȹ�� ���� ���� �� �ֽ��ϴ�.");
		loreList.add("��7��ȹ�� ���� 3�ʰ�  �����ϰ� 6�ʰ�");
		loreList.add("��7�̵��ӵ��� 30% �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a9��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������ ƴ��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��750ĭ�� �ٶ󺸴� ������ ��Ż�� �����մϴ�.");
		loreList.add("��7�Ʊ��� �ش� ��Ż�� ����Ͽ� ������ ��������");
		loreList.add("��7�ڷ���Ʈ �� �� �ֽ��ϴ�.");
		loreList.add("��7��Ż�� 95�ʰ� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a100��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��ȥ ���ġ�f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� óġ�� �� ��üü���� ���ݸ�ŭ ȸ���մϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_predator.setItem(8 ,item);
		
		inven_desc_priest = Bukkit.createInventory(null, 9, "��0��l���� - ����");		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eġ��������f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7ġ�������� �����ϴ�.");
		loreList.add("��7ġ�������� ���� �Ʊ��� ���");
		loreList.add("��715 ü���� ȸ���մϴ�.");
		loreList.add("��7��Ʈ ������ �����ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a4��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eġ�� �ĵ���f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��77ĭ�� �Ʊ��� ü���� ���");
		loreList.add("��718��ŭ ȸ����ŵ�ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a6��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��Ȱ�� �⵵��f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��7������ ������� �Ʊ���");
		loreList.add("��7���������� ���� ��ҿ��� ��Ȱ��ŵ�ϴ�.");
		loreList.add("��7������ ������� �÷��̾��� ����");
		loreList.add("��7���� ���̵�ٿ� ǥ�õ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a98��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��eġ�� ������f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7��� ġ������ 5 �����մϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_priest.setItem(8 ,item);
		
		inven_desc_alchemist = Bukkit.createInventory(null, 9, "��0��l���� - ���ݼ���");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e����ó�С�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�������� �����ϴ�. �������� ���� ����");
		loreList.add("��7�ڷ� �з����� 4�� ���ظ� �ް� 2�ʰ� �̵��ӵ���");
		loreList.add("��740% �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� ����f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��712ĭ���� �Ʊ��� �ٶ󺸰� ����");
		loreList.add("��7�ش� �Ʊ��� 7�ʰ� �̵��ӵ��� 30% �����ϰ�");
		loreList.add("��7���ݷ��� 2 �����մϴ�.");
		loreList.add("��7���� ü���� 40��ŭ ������ ȸ����ŵ�ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a11��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ��ġ��f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��710ĭ���� ���� �ٶ󺸰� ����");
		loreList.add("��72.5�ʿ� ���� 5ȸ�� ���� �ణ����ɴϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a15��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e������ �Žġ�f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��74�ʰ� �޴� ��� ���ظ� ���������Ͽ�");
		loreList.add("��7�����ڿ��� �ݻ��մϴ�.");
		loreList.add("��7���� ȭ���� �����Դϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a45��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��� ��ġ��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7������ ������ġ�� ���� ������");
		loreList.add("��7������ 5ĭ �þ�� �ʴ� 1�� ü����");
		loreList.add("��7ȸ���Ѵ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_alchemist.setItem(8 ,item);
		
		inven_desc_blackMagician = Bukkit.createInventory(null, 9, "��0��l���� - �渶����");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ��ǳ��f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7���� �ٶ��� �����ϴ�. �ٶ��� ���� ����");
		loreList.add("��72�ʰ� �����¿� �����ϴ�.");
		loreList.add("��7���� �ٶ��� ��ź ������ �ٶ��� �۶߷�");
		loreList.add("��7��ź ���� ��ó�� ���� 1�ʰ� �����°� �˴ϴ�.");
		loreList.add("��7�ٶ��� �ִ� 30ĭ���� ���󰩴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(0 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���汸��f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7���� ����� �����ϴ�.");
		loreList.add("��7���汸�� ��ź�� ������ 4ĭ�� ����");
		loreList.add("��7����� �� �ȿ� �ִ� ����");
		loreList.add("��7�����°� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a13��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���֡�f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7������ �� �����ϴ�.");
		loreList.add("��7���ֿ� ���� ���� 1�ʰ� ���ӵǸ�");
		loreList.add("��74�ʰ� �̵��ӵ��� 60% �����մϴ�.");
		loreList.add("��7���� �����κ��� 20�� ü���� ����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a10��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ص���f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��77ĭ�� �Ʊ� �� ������ ������� �ǽ��� �����մϴ�.");
		loreList.add("��7�ǽ� �������� �ִ� �Ʊ���");
		loreList.add("��7�ǽ� �������� �ִ� ������ �� * 10 ��ŭ ü���� ȸ���ϰ�");
		loreList.add("��75�ʰ� �ǽ� �������� �ִ� ������ ����ŭ ���ݷ��� �����մϴ�.");
		loreList.add("��7�ǽ� �������� ������ ������ �ǽ��� �����մϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a75��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�溮��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7�ڽ��� ��ó 2.5ĭ�� ����");
		loreList.add("��7�����°� ���ӵ˴ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_blackMagician.setItem(8 ,item);		
		
		
		inven_desc_warrior = Bukkit.createInventory(null, 9, "��0��l���� - ����");
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���� ���ԡ�f(��6Prim��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7�ٶ󺸴� �������� �����մϴ�.");
		loreList.add("��7������ �� 4ĭ���� �ִ� ������");
		loreList.add("��76��ŭ ���ظ� ������");
		loreList.add("��7���� ���ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a12��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(0 ,item);

		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e���ݡ�f(��6Secd��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��e��Ŭ�� ��f]");
		loreList.add("");
		loreList.add("��7���� ª�� �Ÿ��� ������ �����ϴ�.");
		loreList.add("��7������ �Ÿ��� ������ ��� ������ ��ġ��");
		loreList.add("��7���ݿ� ���� ���� 7��ŭ ���ظ� �ް�");
		loreList.add("��72�ʰ� ���ӻ��°� �˴ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a8��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(1 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�� ������f(��6Tert��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��eF ��f]");
		loreList.add("");
		loreList.add("��7���� �����մϴ�.");
		loreList.add("��7�⺻ ���� 10�̸� 4ĭ�� �� 1���");
		loreList.add("��710��ŭ �߰� ���� ����ϴ�.");
		loreList.add("��7���� �����ִ� ���¿�����");
		loreList.add("��7���� �޴� ���ظ� ���� ��� �޽��ϴ�.");
		loreList.add("��7���� 10�ʰ� �����Ǹ�");
		loreList.add("��7����ġ�ٷ� ���� ������ Ȯ���� �� �ֽ��ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a24��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(2 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e�ϼ� ������f(��6Ulmt��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e����Ʈ ��f+ ��eQ ��f]");
		loreList.add("");
		loreList.add("��75ĭ�� ���� 2�ʰ� �����·� �����");
		loreList.add("��7�ڽ��� ���� ü���� 1/3 ��ŭ�� ���ظ� ������");
		loreList.add("��7���� ���ϴ�.");
		loreList.add("");
		loreList.add("��7���� ���ð� : ��a79��");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(3 ,item);
		
		item = new ItemStack(Material.ENCHANTED_BOOK);
		meta = item.getItemMeta();
		meta.setDisplayName("��7[ ��e��Ÿ��f(��6Pasv��f) ��7]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��f[ ��e��� �ߵ� ��f]");
		loreList.add("");
		loreList.add("��7���� ������ ���� ����");
		loreList.add("��72�ʰ� �����°� �˴ϴ�.");
		loreList.add("");
		loreList.add("��c���Ž� ��밡��");
		loreList.add("");
		meta.setLore(loreList);
		item.setItemMeta(meta);
		inven_desc_warrior.setItem(8 ,item);
		
		
		//////������
		
		item_returnBase = new ItemStack(Material.PAPER, 1);
		meta = item_returnBase.getItemMeta();
		meta.setDisplayName("��f[ ��6��ȯ ��f]");
		loreList = new ArrayList<String>(3);
		loreList.add("");
		loreList.add("��7- ��Ŭ���� ������ ��ȯ�մϴ�.");
		loreList.add("");
		meta.setLore(loreList);
		item_returnBase.setItemMeta(meta);
		
		item_none_stone = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_stone.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ���� ��f]");
		item_none_stone.setItemMeta(meta);
		
		item_none_ring = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_ring.getItemMeta();
		meta.setDisplayName("��f[ ��c�������� ��f]");
		item_none_ring.setItemMeta(meta);
		
		item_none_neck = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_neck.getItemMeta();
		meta.setDisplayName("��f[ ��c����� ���� ��f]");
		item_none_neck.setItemMeta(meta);
		
		item_none_tailsman = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		meta = item_none_tailsman.getItemMeta();
		meta.setDisplayName("��f[ ��c���� ���� ��f]");
		item_none_tailsman.setItemMeta(meta);
		
		stone_forest = new ItemStack(Material.INK_SACK, 1, (short)1);
		meta = stone_forest.getItemMeta();
		meta.setDisplayName("��f[ ��6���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��aü�� +8%");
		loreList.add("��7- ��a�̵��ӵ� +4%");
		loreList.add("");
		meta.setLore(loreList);
		stone_forest.setItemMeta(meta);
		
		stone_miracle = new ItemStack(Material.INK_SACK, 1, (short)2);
		meta = stone_miracle.getItemMeta();
		meta.setDisplayName("��f[ ��6������ ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��aü�� +15%");
		loreList.add("");
		meta.setLore(loreList);
		stone_miracle.setItemMeta(meta);
		
		stone_water = new ItemStack(Material.INK_SACK, 1, (short)3);
		meta = stone_water.getItemMeta();
		meta.setDisplayName("��f[ ��6���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��aü�� +25%");
		loreList.add("��7- ��4�ִ����� -1");
		loreList.add("");
		meta.setLore(loreList);
		stone_water.setItemMeta(meta);
		
		stone_fire = new ItemStack(Material.INK_SACK, 1, (short)4);
		meta = stone_fire.getItemMeta();
		meta.setDisplayName("��f[ ��6���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��aü�� +30%");
		loreList.add("��7- ��4�̵��ӵ� -10%");
		loreList.add("");
		meta.setLore(loreList);
		stone_fire.setItemMeta(meta);
		
		ring_speed = new ItemStack(Material.INK_SACK, 1, (short)5);
		meta = ring_speed.getItemMeta();
		meta.setDisplayName("��f[ ��6�ż��� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�̵��ӵ� +4%");
		loreList.add("��7- ��aü�� +6%");
		loreList.add("");
		meta.setLore(loreList);
		ring_speed.setItemMeta(meta);
		
		ring_gravity = new ItemStack(Material.INK_SACK, 1, (short)6);
		meta = ring_gravity.getItemMeta();
		meta.setDisplayName("��f[ ��6�߷��� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�̵��ӵ� +8%");
		loreList.add("");
		meta.setLore(loreList);
		ring_gravity.setItemMeta(meta);
		
		ring_gale = new ItemStack(Material.INK_SACK, 1, (short)7);
		meta = ring_gale.getItemMeta();
		meta.setDisplayName("��f[ ��6��ǳ�� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�̵��ӵ� +10%");
		loreList.add("��7- ��4�ִ����� -1");
		loreList.add("");
		meta.setLore(loreList);
		ring_gale.setItemMeta(meta);
		
		ring_storm = new ItemStack(Material.INK_SACK, 1, (short)8);
		meta = ring_storm.getItemMeta();
		meta.setDisplayName("��f[ ��6��ǳ�� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�̵��ӵ� +15%");
		loreList.add("��7- ��4ü�� -25%");
		loreList.add("");
		meta.setLore(loreList);
		ring_storm.setItemMeta(meta);
		
		neck_iron = new ItemStack(Material.INK_SACK, 1, (short)9);
		meta = neck_iron.getItemMeta();
		meta.setDisplayName("��f[ ��6ö ����� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�ִ����� +1");
		loreList.add("��7- ��aü�� +8%");
		loreList.add("");
		meta.setLore(loreList);
		neck_iron.setItemMeta(meta);
		
		neck_gold = new ItemStack(Material.INK_SACK, 1, (short)10);
		meta = neck_gold.getItemMeta();
		meta.setDisplayName("��f[ ��6�� ����� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�ִ����� +1");
		loreList.add("��7- ��a�̵��ӵ� +8%");
		loreList.add("");
		meta.setLore(loreList);
		neck_gold.setItemMeta(meta);
		
		neck_diamond = new ItemStack(Material.INK_SACK, 1, (short)11);
		meta = neck_diamond.getItemMeta();
		meta.setDisplayName("��f[ ��6���̾Ƹ�� ����� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�ִ����� +2");
		loreList.add("");
		meta.setLore(loreList);
		neck_diamond.setItemMeta(meta);
		
		neck_emerald = new ItemStack(Material.INK_SACK, 1, (short)12);
		meta = neck_emerald.getItemMeta();
		meta.setDisplayName("��f[ ��6���޶��� ����� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��a�ִ����� +3");
		loreList.add("��7- ��4ü�� -25%");
		loreList.add("");
		meta.setLore(loreList);
		neck_emerald.setItemMeta(meta);
		
		tailsman_miracle = new ItemStack(Material.GOLD_NUGGET, 1);
		meta = tailsman_miracle.getItemMeta();
		meta.setDisplayName("��f[ ��6������ ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��7ü���� 0���Ϸ� ������ ��");
		loreList.add("��7  ��750�ۼ�Ʈ Ȯ���� ��� ���� �� ");
		loreList.add("��7  ��78��ŭ ü�� ȸ��");
		loreList.add("��7  ��7���� ���ð� : 120��");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_miracle.setItemMeta(meta);
		
		tailsman_eminent = new ItemStack(Material.INK_SACK, 1, (short)14);
		meta = tailsman_eminent.getItemMeta();
		meta.setDisplayName("��f[ ��6������ ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��7��� ��ų�� ���ط� 20% ����");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_eminent.setItemMeta(meta);
		
		tailsman_heal = new ItemStack(Material.INK_SACK, 1);
		meta = tailsman_heal.getItemMeta();
		meta.setDisplayName("��f[ ��6ġ���� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��7���� óġ�� �� ü�� 20ȸ��");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_heal.setItemMeta(meta);
		
		tailsman_wisdom = new ItemStack(Material.CLAY_BRICK, 1);
		meta = tailsman_wisdom.getItemMeta();
		meta.setDisplayName("��f[ ��6����� ���� ��f]");
		loreList = new ArrayList<String>();
		loreList.add("");
		loreList.add("��7- ��7�ñر� ���� ���ð� 15% ����");
		loreList.add("");
		meta.setLore(loreList);
		tailsman_wisdom.setItemMeta(meta);
	}

	public Team getTeamObject(Player p) {
		if(redTeam.teamList.contains(p.getName()))
			return redTeam;
		else return blueTeam;
	}
	
	public String getTeam(Player p) {

		if(redTeam.teamList.contains(p.getName())){
			return "RED";	
		} else if(blueTeam.teamList.contains(p.getName())){
			return "BLUE";
		} else {
			return "����";
		}		
	}
	
	public String getTeam(String pName) {
		if(playerMap.containsKey(pName)){
			return playerMap.get(pName).team.teamName;
		}
		
		return "����";
	}
	
	public List<String> getEnemyList(String pName) {
		if(getTeam(pName).equalsIgnoreCase("BLUE")) {
			return redTeam.teamList;
		} else if(getTeam(pName).equalsIgnoreCase("RED")) {
			return blueTeam.teamList;
		}
		return ingamePlayer;
	}
	
	
	public void spawn(Player p) {
		if(!ingame) return;
		p.setGameMode(GameMode.SURVIVAL);
		if(playerMap.containsKey(p.getName())) {
			playerMap.get(p.getName()).team.spawn(p);
		} else {
			gameQuitPlayer(p, true, false);
			server.spawn(p);
		}
	}
	
	
	public String getAbiilityNameFromCode(int abCode) {
		String abName = "";
		switch(abCode) {
		case 3: abName = "������"; break;
		case 4: abName = "�ڰ�"; break;
		case 5: abName = "��ɲ�"; break;
		case 6: abName = "�뺴"; break;
		case 7: abName = "�ü�"; break;
		case 12: abName = "������"; break;
		case 13: abName = "������"; break;
		case 14: abName = "����"; break;
		case 15: abName = "���ݼ�"; break;
		
		case 21: abName = "��ȣ��"; break;
		case 22: abName = "������"; break;
		case 23: abName = "���"; break;
		case 24: abName = "������"; break;
		case 25: abName = "����"; break;
		case 30: abName = "������"; break;
		case 31: abName = "����"; break;
		
		case 39: abName = "����"; break;
		case 40: abName = "���ݼ���"; break;
		case 41: abName = "�渶����"; break;
		default: abName = "???"; break;
		}
		return abName;
	}
	
	
	public void hrwHelper(Player p, int slot) {
		switch (slot) {
		case 2:
			if(!playerMap.containsKey(p.getName())) {
				Team team = getTeamObject(p);
				if(team.selectAbilityMap.containsKey(p.getName())){
					int jobCode = team.selectAbilityMap.get(p.getName());
					if(jobCode == 3) {
						p.openInventory(inven_desc_tracer);
					} else if(jobCode == 4) {
						p.openInventory(inven_desc_assasin);
					} else if(jobCode == 5) {
						p.openInventory(inven_desc_hunter);
					} else if(jobCode == 6) {
						p.openInventory(inven_desc_mercenary);
					} else if(jobCode == 7) {
						p.openInventory(inven_desc_archer);
					} else if(jobCode == 12) {
						p.openInventory(inven_desc_wizard);
					} else if(jobCode == 13) {
						p.openInventory(inven_desc_hider);
					} else if(jobCode == 14) {
						p.openInventory(inven_desc_virtuous);
					} else if(jobCode == 15) {
						p.openInventory(inven_desc_marksman);
					} else if(jobCode == 21) {
						p.openInventory(inven_desc_guardian);
					} else if(jobCode == 22) {
						p.openInventory(inven_desc_monk);
					} else if(jobCode == 23) {
						p.openInventory(inven_desc_knight);
					} else if(jobCode == 24) {
						p.openInventory(inven_desc_fighter);
					} else if(jobCode == 25) {
						p.openInventory(inven_desc_monarch);
					} else if(jobCode == 30) {
						p.openInventory(inven_desc_predator);
					} else if(jobCode == 31) {
						p.openInventory(inven_desc_warrior);
					} else if(jobCode == 39) {
						p.openInventory(inven_desc_priest);
					} else if(jobCode == 40) {
						p.openInventory(inven_desc_alchemist);
					} else if(jobCode == 41) {
						p.openInventory(inven_desc_blackMagician);
					} else {
						p.sendMessage(ms+"���� �ڵ带 Ȯ���� �� �����ϴ�. �����ڿ��� �������ּ���.");
					}
				}else {
					p.sendMessage(ms+"���� ������ �������� �����̽��ϴ�.");
					p.closeInventory();
				}
			}else {
				playerMap.get(p.getName()).ability.invenHelper(p);
			}
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;
			
		case 4:
			Team team = getTeamObject(p);
			team.openStatInventory(p);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;
			
		case 6:
			p.openInventory(inven_gameHelper);
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
			return;

		default:
			return;
		}
	}
}
