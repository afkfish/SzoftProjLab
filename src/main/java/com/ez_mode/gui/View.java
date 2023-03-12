package com.ez_mode.gui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class View {
	public static JFrame setup() {
		try {
			UIManager.setLookAndFeel( new FlatDarkLaf() );
		} catch( Exception ex ) {
			System.err.println( "Failed to initialize LaF" );
		}


		JFrame frame = new JFrame("Hello World");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int height = size.height;
		int width = size.width;
		frame.setSize(width / 2, height / 2);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.add(panel, BorderLayout.CENTER);

		JButton button = new JButton("Exit");
		button.addActionListener(e -> System.exit(0));
		panel.add(button);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		return frame;
	}
}
