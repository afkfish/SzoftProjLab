package com.ez_mode.characters;

import com.ez_mode.objects.Pipe;

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
    System.out.println("\t" + this.getUuid() + " has set " + standingOn.getUuid());
  }
}
