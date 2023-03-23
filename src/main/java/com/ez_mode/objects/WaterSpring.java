package com.ez_mode.objects;

import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * WaterSpring is a special type of node that can be used to refill a cistern. It generates 1 unit
 * of water per tick. It is not possible to break or repair a water spring. It has no maximum
 * capacity and unlimited water.
 */
public class WaterSpring extends Node {
    public WaterSpring() {
        super(Integer.MAX_VALUE, 4);
        this.flowRate = 1;
    }

    @Override
    public void addCharacter(Character character) {
        this.charactersOn.add(character);
    }

    @Override
    public void repairNode(Character character) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException(
                String.format("Player <%s> tried to repair a cistern.", character.getName()));
    }

    @Override
    public void breakNode(Character character) throws InvalidPlayerActionException {
        throw new InvalidPlayerActionException(
                String.format("Player <%s> tried to break a cistern.", character.getName()));
    }

    @Override
    public void tick() {
        this.connectors.stream()
                .filter(Connector::isConnected)
                .forEach(connector -> connector.getNeighbour(this).addFlowRate(this, 1));

        this.logger.debug("Flow rate is at {}", this.flowRate);
    }
}
