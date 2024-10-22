package com.ez_mode.map_tests;

import com.ez_mode.Map;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.WaterSpring;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerMovementTest {
  WaterSpring waterSpring1 = new WaterSpring(0, 0);
  Pipe pipe1 = new Pipe(0, 1);
  Plumber plumber = new Plumber("Plumber");
  Nomad nomad = new Nomad("Nomad");

  @Before
  public void setUp() {
    Map.addPlayer(plumber, waterSpring1);
  }

  @Test
  public void testInvalidMovement() {
    // test if a player can move to a node
    // that is not connected to the node he is standing on
    Assert.assertThrows(InvalidPlayerMovementException.class, () -> plumber.moveTo(pipe1));
  }

  @Test
  public void testValidMovement() throws ObjectFullException, InvalidPlayerMovementException {
    // connecting the two nodes
    try {
      pipe1.connect(waterSpring1);
    } catch (ObjectFullException e) {
      throw new RuntimeException(e);
    }

    plumber.moveTo(pipe1);
  }

  @Test
  public void testMovingToFullNode() {
    // connecting the two nodes
    try {
      pipe1.connect(waterSpring1);
    } catch (ObjectFullException e) {
      throw new RuntimeException(e);
    }

    // add player2 to the map
    Map.addPlayer(nomad, pipe1);

    // test if a player can move to a node that is full
    Assert.assertThrows(ObjectFullException.class, () -> plumber.moveTo(pipe1));
  }
}
