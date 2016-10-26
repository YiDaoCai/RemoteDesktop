package DesktopServerUI;

import java.awt.*;

import javax.swing.*;

public class DesktopServerRaiseHandFrame {
	private JFrame frm;
	private JLabel text;
	private int width, height;
	private Dimension screenSize;

	private static DesktopServerRaiseHandFrame raisehandframe;
	
	public static DesktopServerRaiseHandFrame getFrame() {
		if(raisehandframe == null) {
			try {
				raisehandframe = new DesktopServerRaiseHandFrame();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return raisehandframe;
	}
	
	private DesktopServerRaiseHandFrame() throws InterruptedException {
		width = 200;
		height = 80;
		frm = new JFrame();
		text = new JLabel();
		// 加上这一句就可以显示一个仅有关闭，最小化，最大化的按钮的Frame了
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// 再加上这一句就可以显示一个在左上角，拥有指定大小的Frame了
		frm.setSize(width, height);
		frm.setLocation(screenSize.width - width, screenSize.height - height
				- 40);
		frm.setResizable(false);
		
		// 如果没有这一句，在点击关闭Frame的时候程序其实还是在执行状态中的，加上这一句才算是真正的把资源释放掉了
		//frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Container c = frm.getContentPane();

		JPanel panel = new JPanel();
		c.add(panel, BorderLayout.CENTER);
		// panel.setVisible(true);
		text.setFont(new Font("宋体", Font.BOLD, 24));
		panel.add(text);
	}

	public void setText(String value) throws InterruptedException {
		text.setText(value);
		width = value.length() * 30;
		reSize();
		frm.setVisible(true);
		Thread.sleep(3000);
		frm.setVisible(false);
	}

	private void reSize() {
		frm.setSize(width, height);
		frm.setLocation(screenSize.width - width, screenSize.height - height
				- 40);
	}

	/*public static void main(String[] args) {
		// 现在创建了一个对象，不过什么都显示不出来
		try {
			DesktopServerRaiseHandFrame d = new DesktopServerRaiseHandFrame();
			d.setText("有同学举手");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

}