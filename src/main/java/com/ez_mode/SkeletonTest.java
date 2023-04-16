package com.ez_mode;

import com.ez_mode.characters.*;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.*;
import com.ez_mode.objects.*;

public class SkeletonTest {
  public void SetPumpTest() {
    Character character = new Plumber("Character");
    Pump pump = new Pump();
    Pipe absorber = new Pipe();
    Pipe source = new Pipe();
    System.out.println("SkeletonTest:SetPumpTest param: -");
    try {
      pump.connect(absorber);
      System.out.println("\tpump:connect param: absorber");
      pump.connect(source);
      System.out.println("\tpump:connect param: source");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    character.placeTo(pump);
    System.out.println("\tcharacter:placeTo param: pump");
    character.SetPump();
    System.out.println("\tcharacter:SetPump param: -");
  }

  public void PlumberMovesToPumpTest() {
    Plumber plumber = new Plumber("Plumber");
    Pipe pipe = new Pipe();
    Pump pump = new Pump();
    System.out.println("SkeletonTest:PlumberMovesToPumpTest param: -");
    try {
      pump.connect(pipe);
      System.out.println("\tpump:connect param: -");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    plumber.placeTo(pipe);
    try {
      plumber.moveTo(pump);
      System.out.println("\tplumber:moveTo param: pump");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {
      System.out.println(ipme.getMessage());
    }
  }

  public void PlumberMovesToPipeTest() {
    Plumber plumber = new Plumber("Plumber");
    Pipe pipe = new Pipe();
    Pump pump = new Pump();
    System.out.println("SkeletonTest:PlumberMovesToPipeTest param: -");
    try {
      pipe.connect(pump);
      System.out.println("\tpump:connect param: pipe");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    plumber.placeTo(pump);
    System.out.println("\tplumber:placeTo param: pump");
    try {
      plumber.moveTo(pipe);
      System.out.println("\tplumber:moveTo param: pipe");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {

      System.out.println(ipme.getMessage());
    }
  }

  public void NomadBreaksPipeTest() {
    Nomad nomad = new Nomad("Nomad");
    Pipe pipe = new Pipe();
    System.out.println("SkeletonTest:NomadBreaksPipeTest param: -");
    nomad.placeTo(pipe);
    System.out.println("\tnomad:placeTo param: pipe");
    nomad.breakNode();
    System.out.println("\tnomad:breakNode param: -");
    System.out.println(nomad.getUuid() + " has broken " + pipe.getUuid());
  }

  public void NomadMovesToPipeTest() {
    Nomad nomad = new Nomad("Nomad");
    Pipe pipe = new Pipe();
    Pump pump = new Pump();
    System.out.println("SkeletonTest:NomadMovesToPipeTest param: -");
    try {
      pipe.connect(pump);
      System.out.println("\tpipe:connect param: pump");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    nomad.placeTo(pump);
    System.out.println("\tnomad:placeTo param: pump");
    try {
      nomad.moveTo(pipe);
      System.out.println("\tnomad:moveTo param: pipe");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {

      System.out.println(ipme.getMessage());
    }
  }

  public void CharacterMovesTest() {
    Character character = new Nomad("Character");
    Node node1 = new Pump();
    Node node2 = new Pipe();
    System.out.println("SkeletonTest:CharacterMovesTest param: -");
    try {
      node2.connect(node1);
      System.out.println("\tnode:connect param: node");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    character.placeTo(node1);
    System.out.println("\tcharacter:placeTo param: node");
    try {
      character.moveTo(node2);
      System.out.println("\tcharacter:moveTo param: node");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {
      System.out.println(ipme.getMessage());
    }
  }

  public void CharacterMovesToPumpTest() {
    Character character = new Plumber("Character");
    Pipe pipe = new Pipe();
    Pump pump = new Pump();
    System.out.println("SkeletonTest:CharacterMovesToPumpTest param: -");
    try {
      pump.connect(pipe);
      System.out.println("\tpump:connect param: pipe");
    } catch (ObjectFullException oe) {
      System.out.println(oe.getMessage());
    }
    character.placeTo(pipe);
    System.out.println("\tcharacter:placeTo param: pipe");
    try {
      character.moveTo(pump);
      System.out.println("\tcharacter:moveTo param: pump");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {

      System.out.println(ipme.getMessage());
    }
  }

  public void PlumberRepairsPipeTest() {
    Pipe pipe = new Pipe();
    System.out.println("SkeletonTest:PlumberRepairsPipeTest param: -");
    try {
      pipe.breakNode(new Nomad("Testnomad"));
      System.out.println("\tpipe:breakNode param: nomad");
    } catch (InvalidPlayerActionException e) {
      System.out.println(e.getMessage());
    }
    Plumber p1 = new Plumber("Testplumber");
    p1.placeTo(pipe);
    System.out.println("\tplumber:placeTo param: pipe");
    p1.repair();
    System.out.println("\tplumber:repair param: -");
  }

  public void PlumberRepairsPumpTest() {
    Pump pumpa = new Pump();
    System.out.println("SkeletonTest:PlumberRepairsPumpTest param: -");
    try {
      pumpa.breakNode(new Nomad("Testnomad"));
      System.out.println("\tpump:breakNode param: nomad");
    } catch (InvalidPlayerActionException e) {
      System.out.println(e.getMessage());
    }
    Plumber p1 = new Plumber("Testplumber");
    p1.placeTo(pumpa);
    System.out.println("\tplumber:placeTo param: pump");
    p1.repair();
    System.out.println("\tplumber:repair param: -");
    System.out.println(p1.getUuid() + " has repaired " + pumpa.getUuid());
  }

  public void PlumberPicksUpPipe() {
    Cistern c1 = new Cistern();
    Plumber p1 = new Plumber("Plumber1");
    System.out.println("SkeletonTest:PlumberPicksUpPipe param: -");
    p1.placeTo(c1);
    System.out.println("\tplumber:placeTo param: cistern");
    c1.MakePipe();
    System.out.println("\tcistern:MakePipe param: -");
    p1.PickupPipe();
    System.out.println("\tplumber:PickupPipe param: -");
    System.out.println(p1.getUuid() + " has picked up a new pipe.");
  }

  public void PlumberPicksUpPump() {
    Cistern c1 = new Cistern();
    Plumber p1 = new Plumber("Plumber1");
    System.out.println("SkeletonTest:PlumberPicksUpPump param: -");
    p1.placeTo(c1);
    System.out.println("\tplumber:placeTo param: cistern");
    c1.MakePump();
    System.out.println("\tcistern:MakePump param: -");
    p1.PickupPump();
    System.out.println("\tplumber:PickupPump param: -");
    System.out.println(p1.getUuid() + " has picked up a new pump.");
  }

  public void PlumberDeploysPump() {
    Cistern c1 = new Cistern();
    Plumber p1 = new Plumber("Plumber1");
    System.out.println("SkeletonTest:PlumberDeploysPump param: -");
    p1.placeTo(c1);
    System.out.println("\tplumber:placeTo param: cistern");
    c1.MakePump();
    System.out.println("\tcistern:MakePump param: -");
    p1.PickupPump();
    System.out.println("\tplumber:PickupPump param: -");
    Pipe Pipe1 = new Pipe();
    try {
      Pipe1.connect(c1);
      System.out.println("\tpipe:connect param: cistern");
    } catch (ObjectFullException ofe) {
      System.out.println(ofe.getMessage());
    }
    p1.placeTo(Pipe1);
    System.out.println("\tplumber:placeTo param: pipe");
    /* try {
      p1.moveTo(c1);
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {
      System.out.println(ipme.getMessage());
    }*/
    p1.PlacePump();
    System.out.println("\tplumber:placePump param: -");
  }

  public void PlumberMovesToCistern() {
    Cistern c1 = new Cistern();
    Pipe Pipe1 = new Pipe();
    Plumber p1 = new Plumber("Plumber1");
    System.out.println("SkeletonTest:PlumberMovesToCistern param: -");
    try {
      c1.connect(Pipe1);
      System.out.println("\tpipe:connect param: cistern");
    } catch (ObjectFullException ofe) {
      System.out.println(ofe.getMessage());
    }
    p1.placeTo(Pipe1);
    System.out.println("\tplumber:placeTo param: pipe");
    try {
      p1.moveTo(c1);
      System.out.println("\tplumber:moveTo param: cistern");
    } catch (InvalidPlayerMovementException | ObjectFullException ipme) {

      System.out.println(ipme.getMessage());
    }
  }
}
