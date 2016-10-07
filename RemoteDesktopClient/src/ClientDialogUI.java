

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClientDialogUI{
	private JFrame chat;
	private JButton send, raisehand;
	private JPanel row1, row2, row3;
	private JScrollPane sessionRoll, msgRoll;
	private JTextArea session;
	private JTextArea msg;
	private Client client;
	public ClientDialogUI(Client c) {
		client = c;
		chat = new JFrame("聊天室");
		send = new JButton("发送");
		raisehand = new JButton("举手");
		row1 = new JPanel();
		row2 = new JPanel();
		row3 = new JPanel(new GridLayout(1, 2));
		msg = new JTextArea(5, 25);
		msg.setTabSize(4);
		msg.setFont(new Font("宋体", Font.BOLD, 16));
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		
		session = new JTextArea(20, 25);
		session.setLineWrap(true);
		session.setWrapStyleWord(true);
		session.setFont(new Font("宋体", Font.BOLD, 16));
		session.append("欢迎来到聊天室\n");
		session.setEditable(false);
		sessionRoll = new JScrollPane(session);
		msgRoll = new JScrollPane(msg);
		
		chat.setSize(250, 560);
		Container con = chat.getContentPane();
		con.setLayout(new BorderLayout());
		row1.add(sessionRoll);
		row2.add(msgRoll);
		row3.add(raisehand);
		row3.add(send);
		con.add(row1, BorderLayout.NORTH);
		con.add(row2, BorderLayout.CENTER);
		con.add(row3, BorderLayout.SOUTH);
		
		raisehand.addActionListener(new Action());
		send.addActionListener(new Action());
		
		chat.setResizable(false);
		chat.setLocation(1050, 200);
		chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat.setVisible(true);
		
		new receiver();
	}
	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == send) {
				if(msg.getText().length() > 0) {
					session.append("Me:" + msg.getText() + "\n");
					client.send(Information.createSession(client.getSelfAddress(), msg.getText()));
					msg.setText(null);
				}
			} else if(e.getSource() == raisehand) {
				session.append("Me:我举了手\n");
				client.send(Information.createRaiseHand(client.getSelfAddress()));
			}
		}
	}
	private class receiver extends Thread {
		public void run() {
			while(true) {
				session.append("服务器 ：" + client.receive().getContent());
			}
		}
	}
}
