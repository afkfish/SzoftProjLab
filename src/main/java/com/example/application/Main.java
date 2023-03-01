package com.example.application;

import com.formdev.flatlaf.FlatDarkLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(Main.class);
		logger.info("Hello World!");
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
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
