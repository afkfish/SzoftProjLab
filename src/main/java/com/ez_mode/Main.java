package com.ez_mode;

import com.ez_mode.gui.Menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Main {
    private enum Version {
        SKELETON,
        PROTOTYPE,
        GRAPGHICAL
    }

    public static Version version = Version.GRAPGHICAL;

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Main.class);

        Menu menu = new Menu();

        /*if (version == Version.GRAPGHICAL) {
        	JFrame mainFrame = View.setup();
        }*/
    }
}
