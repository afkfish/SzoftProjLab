package com.ez_mode;

import com.ez_mode.gui.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private enum Version {
        SKELETON,
        PROTOTYPE,
        GRAPGHICAL
    }

    public static Version version = Version.GRAPGHICAL;

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(Main.class);

        if (version == Version.SKELETON) {
            new Map();
        } else if (version == Version.GRAPGHICAL) {
            new Menu();
        }
    }
}
