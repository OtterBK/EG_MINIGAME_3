package EGServer.ServerManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EGPluginManager {

	JavaPlugin server;
	
	JFrame frame;
	JScrollPane sc;
	JTextArea logArea;
	int frameWidth = 500;
	int frameHeight = 700;
	
	public EGPluginManager(JavaPlugin server) {
		/*Toolkit tk = Toolkit.getDefaultToolkit(); //������� ȭ�� ũ�Ⱚ�� ������� ��Ŷ Ŭ���� 
		
		frame = new JFrame();
		frame.setLayout(new BorderLayout(5,5));
		frame.setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth/2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		frame.setTitle("EG �÷����� �Ŵ���"); //������ ���� ����
		frame.setResizable(true); //������ ũ�� ���� ����
		frame.setBackground(Color.LIGHT_GRAY); //���� ����
		frame.add(new JPanel(), BorderLayout.NORTH); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.SOUTH); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.EAST); //����� GUI�� ���� �߰�
		frame.add(new JPanel(), BorderLayout.WEST); //����� GUI�� ���� �߰�
		//this.setUndecorated(true); ������ Ʋ ���ֱ��, ������
	
		logArea = new JTextArea();
		//logArea.setPreferredSize(new Dimension(480,680));
		logArea.setFont(logArea.getFont().deriveFont(15f));
		logArea.setAutoscrolls(true);
		logArea.setEditable(false);
		logArea.setLineWrap(true);   //������ �����ٷ� ���� ����.
		sc = new JScrollPane(logArea);
		sc.setAutoscrolls(true);
		frame.add(sc, BorderLayout.CENTER);
		
		frame.setVisible(true);*/ //������������ ����
		
	}
	
	public void printLog(String str) {
		//SimpleDateFormat format1 = new SimpleDateFormat ( "[yy��MM��dd�� HH:mm:ss] ");
		//String format_time1 = format1.format (System.currentTimeMillis());
		//logArea.append(format_time1+str+"\n");
		//logArea.setCaretPosition(logArea.getDocument().getLength());
		Bukkit.getLogger().info("[ �α� ] "+str);
	}

	
}
