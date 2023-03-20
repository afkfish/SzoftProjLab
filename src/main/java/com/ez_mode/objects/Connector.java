package com.ez_mode.objects;

import com.ez_mode.characters.Character;
import java.util.ArrayList;

/** The Connector class is used to connect two nodes together. */
public class Connector {
  /** The nodes that are connected by this connector. */
  private Node node1, node2 = null;

  private boolean isConnected = false;

  public Connector(Node node1) {
    this.node1 = node1;
    node1.addConnector(this);
  }

  public Connector(Node node1, Node node2) {
    this.node1 = node1;

    node1.addConnector(this);

    connect(node2);
  }

  public Node getNeighbour(Node self) {
    if (self == node1) {
      return node2;
    } else {
      return node1;
    }
  }

  public ArrayList<Character> getReachableCharacters(Node self) {
    if (self == node1 && node2 != null) {
      return node2.getCharactersOn();
    } else if (self == node2 && node1 != null) {
      return node1.getCharactersOn();
    } else {
      return new ArrayList<>();
    }
  }

  public void connect(Node node) {
    this.node2 = node;
    isConnected = true;

    if (node2.flowRate > 0) {
      node1.addFlowRate(node2, node.flowRate);
      node1.addSource(node2);
    } else {
      node1.addAbsorber(node2);
    }
    node2.addConnector(this);
  }

  public void disconnect(Node node) {
    // if the given node is not connected to this connector, throw an exception
    if (node != node1 && node != node2) {
      throw new IllegalArgumentException("Node is not connected to this connector");
    } else if (node == node1) {
      // if the given node is node1, remove this connector from node1's connectors
      this.node1.removeConnector(this);

      // remove node2 from node1's sources or absorbers
      this.node1.removeSource(this.node2);
      this.node1.removeAbsorber(this.node2);
      this.node1 = null;
    } else {
      // if the given node is node2, remove this connector from node2's connectors
      this.node2.removeConnector(this);

      // remove node1 from node2's sources or absorbers
      this.node2.removeSource(this.node1);
      this.node2.removeAbsorber(this.node1);
      node2 = null;
    }
    this.isConnected = false;
  }

  public boolean isConnected() {
    return isConnected;
  }
}
