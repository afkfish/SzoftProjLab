package com.ez_mode;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Plummer;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.WaterSpring;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is responsible for the map of the game.
 * It contains a HashMap of StandableObjects and the Characters standing on them.
 * It also contains a method to handle the case when a player is lost somehow.
 */
public class Map {
	private static final Logger logger = LogManager.getLogger(Map.class);
	/**
	 * The HashMap representation of the game.
	 * This map contains everyone and every object.
	 */
	private static final HashMap<Node, ArrayList<Character>> gameMap = new HashMap<>();

	/**
	 * The list of all players.
	 */
	private static final ArrayList<Character> players = new ArrayList<>();

	/**
	 * The amount of water, lost by the nomads sabotaging the nodes.
	 */
	public static double waterLost = 0;

	public Map() {
		this.fillMap();
	}

	/**
	 * This method fills the map with the objects
	 * and places the characters to their startiing positions.
	 */
	private void fillMap() {
		// TODO: implement a way to fill the map
		WaterSpring waterSpring = new WaterSpring();
		gameMap.put(waterSpring, new ArrayList<>());

		Plummer plummer = new Plummer("Plummer");
		gameMap.get(waterSpring).add(plummer);
		waterSpring.addCharacter(plummer);

		players.add(plummer);

		Pipe pipe = new Pipe();
		gameMap.put(pipe, new ArrayList<>());

		waterSpring.addNeighbour(pipe);
		pipe.addNeighbour(waterSpring);
		pipe.addSource(waterSpring);
		waterSpring.addAbsorber(pipe);

		for (int i = 0; i < 1; i++) {
			gameMap.keySet().forEach(Tickable::tick);
		}

		plummer.placeTo(waterSpring);

		plummer.moveTo(pipe);
	}

	/**
	 * If a player character lost somehow,
	 * this method will move it to the position it is supposed to be,
	 * or to the start position.
	 * @param character the player who is lost
	 */
	public static void playerLostHandler(Character character) {
		Node playerTruePos = gameMap
				.keySet()
				.stream()
				.filter(standableObject ->
						standableObject
								.getCharactersOn()
								.contains(character)
				).findFirst()
				.orElse(null);

		// TODO: move to start if null
		assert playerTruePos != null;
		character.placeTo(playerTruePos);
	}
}
