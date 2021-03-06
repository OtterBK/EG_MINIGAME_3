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
		/*Toolkit tk = Toolkit.getDefaultToolkit(); //사용자의 화면 크기값을 얻기위한 툴킷 클래스 
		
		frame = new JFrame();
		frame.setLayout(new BorderLayout(5,5));
		frame.setBounds((int) tk.getScreenSize().getWidth() / 2 - frameWidth/2, (int) tk.getScreenSize().getHeight() / 2 - frameHeight/2, frameWidth, frameHeight);
		frame.setTitle("EG 플러그인 매니저"); //프레임 제목 설정
		frame.setResizable(true); //프레임 크기 조정 가능
		frame.setBackground(Color.LIGHT_GRAY); //배경색 설정
		frame.add(new JPanel(), BorderLayout.NORTH); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.SOUTH); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.EAST); //깔끔한 GUI를 위해 추가
		frame.add(new JPanel(), BorderLayout.WEST); //깔끔한 GUI를 위해 추가
		//this.setUndecorated(true); 윈도우 틀 없애기용, 사용안함
	
		logArea = new JTextArea();
		//logArea.setPreferredSize(new Dimension(480,680));
		logArea.setFont(logArea.getFont().deriveFont(15f));
		logArea.setAutoscrolls(true);
		logArea.setEditable(false);
		logArea.setLineWrap(true);   //꽉차면 다음줄로 가게 해줌.
		sc = new JScrollPane(logArea);
		sc.setAutoscrolls(true);
		frame.add(sc, BorderLayout.CENTER);
		
		frame.setVisible(true);*/ //리눅스에서는 못씀
		
	}
	
	public void printLog(String str) {
		//SimpleDateFormat format1 = new SimpleDateFormat ( "[yy년MM월dd일 HH:mm:ss] ");
		//String format_time1 = format1.format (System.currentTimeMillis());
		//logArea.append(format_time1+str+"\n");
		//logArea.setCaretPosition(logArea.getDocument().getLength());
		Bukkit.getLogger().info("[ 로그 ] "+str);
	}

	
}
