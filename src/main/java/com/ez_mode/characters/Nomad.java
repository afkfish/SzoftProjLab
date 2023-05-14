package com.ez_mode.characters;

import com.ez_mode.Main;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;

/**
 * Class representing a Nomad character. The nomad can break pipes and adjust
 * pumps in order to
 * reroute the water into the desert.
 */
public class Nomad extends Character {
  public Nomad(String name) {
    super(name);
  }

  public void setSlippery() {
    try {
      standingOn.setSurface("slippery", this);
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }
}
