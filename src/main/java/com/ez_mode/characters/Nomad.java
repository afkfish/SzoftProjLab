package com.ez_mode.characters;

import com.ez_mode.Main;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;

/**
 * Class representing a Nomad character. The nomad can break pipes and adjust pumps in order to
 * reroute the water into the desert.
 */
public class Nomad extends Character {
  public Nomad(String name) {
    super(name);
  }

  @Override
  public void setPump(Pipe in, Pipe out) {
    try {
      Pump pump = (Pump) this.standingOn;
      assert in != out : "Input and output pipes must be different.";
      if (pump.getNeighbours().contains(in)) pump.setActiveInput(in);
      else {
        Main.log("\t" + in.getUuid() + " in Pipe not connected to the pump.");
        return;
      }
      if (pump.getNeighbours().contains(out)) pump.setActiveOutput(out);
      else {
        Main.log("\t" + in.getUuid() + " out Pipe is not a neighbour of the pump.");
        return;
      }
      Main.log("\t" + this.getUuid() + " is setting the pump.");
    } catch (ClassCastException e) {
      Main.log("Player " + this.getUuid() + " tried to set a pump on a non-pump object.");
    }
  }

  public void setSlippery() {
    try {
      standingOn.setSurface("slippery", this);
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }
}
