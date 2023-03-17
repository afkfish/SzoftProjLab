package com.ez_mode;

import com.ez_mode.gui.View;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.swing.*;

public class Main {
	private enum Version {
		SKELETON, PROTOTYPE, GRAPGHICAL
	}
	public static Version version = Version.SKELETON;
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(Main.class);

		Map map = new Map();


		if (version == Version.GRAPGHICAL) {
			JFrame mainFrame = View.setup();
		}
	}
}
