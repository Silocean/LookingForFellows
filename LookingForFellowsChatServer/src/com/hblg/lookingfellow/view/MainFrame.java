package com.hblg.lookingfellow.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.hblg.lookingfellow.model.Server;

public class MainFrame extends JFrame implements ActionListener {
	
	JPanel panel1;
	JPanel panel2;
	
	JButton startBut;
	
	public static JTextArea ta;
	JScrollPane sp;
	
	public MainFrame() {
		startBut = new JButton("Æô¶¯·þÎñÆ÷");
		startBut.addActionListener(this);
		panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
		panel1.add(startBut);
		panel2 = new JPanel();
		ta = new JTextArea();
		sp = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel2.setLayout(new BorderLayout());
		panel2.add(sp, BorderLayout.CENTER);
		this.add(panel1, BorderLayout.NORTH);
		this.add(panel2, BorderLayout.CENTER);
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		new MainFrame();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		new ServerThread().start();
		startBut.setEnabled(false);
	}
	
	private class ServerThread extends Thread {
		public void run() {
			new Server();
		}
	}
	
}
