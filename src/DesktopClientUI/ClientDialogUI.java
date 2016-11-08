package DesktopClientUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import util.DesktopRemoteType;
import util.Information;

import DesktopClientProcess.Client;

public class ClientDialogUI {
	private JFrame chat;
	private JButton send, raisehand;
	private JPanel row1, row2, row3;
	private JPanel row2_1;
	private JComboBox object;
	private JButton sendfile;
	private JScrollPane sessionRoll, msgRoll;
	private JTextArea session;
	private JTextArea msg;
	private Client client;

	private JFileChooser fc;

	public Socket getSocket() {
		return client.getSocket();
	}

	public String getInetAddress() {
		return client.getSelfAddress();
	}

	public ClientDialogUI(Client c) {
		client = c;
		chat = new JFrame("聊天室");
		send = new JButton("发送");
		raisehand = new JButton("举手");
		row1 = new JPanel();
		row2 = new JPanel(new BorderLayout());
		row3 = new JPanel(new GridLayout(1, 2));
		row2_1 = new JPanel(new FlowLayout());
		msg = new JTextArea(5, 25);
		msg.setTabSize(4);
		msg.setFont(new Font("宋体", Font.BOLD, 16));
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);

		fc = new JFileChooser();

		session = new JTextArea(20, 27);
		session.setLineWrap(true);
		session.setWrapStyleWord(true);
		session.setFont(new Font("宋体", Font.BOLD, 16));
		session.append("欢迎来到聊天室\n");
		session.setEditable(false);
		sessionRoll = new JScrollPane(session);
		msgRoll = new JScrollPane(msg);
		object = new JComboBox();
		object.addItem("至全体");
		object.addItem("至老师");
		object.setPreferredSize(new Dimension(115, 20));
		Image image = (new ImageIcon("./bin/image/tranFile.png")).getImage();
		Image smallImage = image.getScaledInstance(20, 20, Image.SCALE_FAST);
		sendfile = new JButton("交作业", new ImageIcon(smallImage));
		sendfile.setPreferredSize(new Dimension(115, 20));
		sendfile.setContentAreaFilled(false);
		sendfile.setUI(new BasicButtonUI());
		sendfile.setMargin(new Insets(0, 0, 0, 0));

		sendfile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (fc.showOpenDialog(chat) == JFileChooser.APPROVE_OPTION) {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							File file = fc.getSelectedFile();
							try {
								Socket filesocket = new Socket(Client
										.getServerAddress(),
										DesktopRemoteType.ServerFile.getPort());
								DataInputStream fis = new DataInputStream(
										new BufferedInputStream(
												new FileInputStream(file
														.getAbsolutePath())));
								DataOutputStream ps = new DataOutputStream(
										filesocket.getOutputStream());
								ps.writeUTF(file.getName());
								ps.flush();
								ps.writeLong((long) file.length());
								ps.flush();

								int bufferSize = 8192;
								byte[] buf = new byte[bufferSize];

								while (true) {
									int read = 0;
									if (fis != null) {
										read = fis.read(buf);
									}

									if (read <= 0) {
										break;
									}
									ps.write(buf, 0, read);
								}
								System.out.println("文件发送完成");
								addSession("文件\"" + file.getName() + "\"发送完成");
								ps.flush();
								ps.close();
								fis.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}).start();
				}
			}
		});
		row2_1.add(object);
		row2_1.add(sendfile);
		row2_1.setAlignmentX(0);
		row2_1.setPreferredSize(new Dimension(0, 25));
		chat.setSize(250, 600);
		Container con = chat.getContentPane();
		con.setLayout(new BorderLayout());
		row1.add(sessionRoll);
		// row2.setPreferredSize(new Dimension(250, 90));
		row2.add(row2_1, BorderLayout.NORTH);
		row2.add(msgRoll, BorderLayout.CENTER);
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

		// setDesktop();
	}

	public void addSession(String value) {
		session.append(value + "\r\n");
		JScrollBar jsb = sessionRoll.getVerticalScrollBar();
		jsb.setValue(jsb.getMaximum() + 1);
	}

	public class Action implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == send) {
				if (msg.getText().length() < 1) return;
				if(object.getSelectedItem().equals("至全体")) {
					addSession("[Me to all]" + msg.getText());
					client.send(new Information("session",client.getSelfAddress(), msg.getText(),"all"));
				} else {
					addSession("[Me to Server]" + msg.getText());
					client.send(new Information("session",client.getSelfAddress(), msg.getText(),"server"));
				}
				msg.setText(null);
			} else if (e.getSource() == raisehand) {
				addSession("[Me]我举了手");
				client.send(Information.createRaiseHand(client.getSelfAddress()));
			}
		}
	}

}
