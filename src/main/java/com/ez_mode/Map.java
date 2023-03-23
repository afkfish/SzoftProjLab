package com.ez_mode;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.WaterSpring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This class is responsible for the map of the game. It contains a HashMap of StandableObjects and
 * the Characters standing on them. It also contains a method to handle the case when a player is
 * lost somehow.
 */
public class Map implements Tickable {
    private final Logger logger = LogManager.getLogger(Map.class);
    /**
     * The ArrayList representation of the game. This map contains every object. TODO: store the
     * objects with their coordinates
     */
    private static final ArrayList<Node> gameMap = new ArrayList<>();

    /** The list of all players. */
    private static final ArrayList<Character> players = new ArrayList<>();

    /** The amount of water, lost by the nomads sabotaging the nodes. */
    public static double waterLost = 0;
    /** The amount of water, arrived to the cisterns. */
    public static double waterArrived = 0;

    public Map() {
        this.fillMap();
    }

    /**
     * This method fills the map with the objects and places the characters to their startiing
     * positions.
     */
    private void fillMap() {
        // TODO: implement a way to fill the map
        WaterSpring waterSpring1 = new WaterSpring();
        gameMap.add(waterSpring1);

        WaterSpring waterSpring2 = new WaterSpring();
        gameMap.add(waterSpring2);

        Pipe pipe = new Pipe();
        gameMap.add(pipe);

        Plumber plumber = new Plumber("Plummer");
        players.add(plumber);

        try {
            pipe.connect(waterSpring1);
            pipe.connect(waterSpring2);
        } catch (ObjectFullException e) {
            this.logger.error("The pipe is full, but it shouldn't be.");
        }
        for (int i = 0; i < 4; i++) {
            this.tick();
        }

        plumber.placeTo(waterSpring1);

        try {
            plumber.moveTo(pipe);
            plumber.moveTo(waterSpring2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPlayer(Character player, Node node) {
        players.add(player);
        player.placeTo(node);
    }

    /**
     * If a player character lost somehow, this method will move it to the position it is supposed
     * to be, or to the start position.
     *
     * @param character the player who is lost
     */
    public static void playerLostHandler(Character character) {
        Node playerTruePos =
                gameMap.stream()
                        .filter(node -> node.getCharacters().contains(character))
                        .findFirst()
                        .orElse(null);

        // TODO: move to start if null
        assert playerTruePos != null;
        character.placeTo(playerTruePos);
    }

    @Override
    public void tick() {
        gameMap.forEach(Tickable::tick);
        this.logger.debug("Current water loss: {}", Map.waterLost);
    }
}
