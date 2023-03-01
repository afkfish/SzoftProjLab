package com.example.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Main {
	private enum Version {
		SKELETON, PROTOTYPE, GRAPGHICAL
	}
	public static Version version = Version.SKELETON;
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(Main.class);
		logger.info("Hello World!");


		if (version == Version.GRAPGHICAL) {
			JFrame mainFrame = View.setup();
		}
	}
}
