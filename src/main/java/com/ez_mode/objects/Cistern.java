package com.ez_mode.objects;

import com.ez_mode.Map;

public class Cistern extends Node {
	protected Cistern() {
		super(Integer.MAX_VALUE, 5);
	}

	@Override
	public void tick(){
		super.tick();

		if(sources.size() < maxConnections)
			sources.add(MakePipe());

		// this.sources.forEach(node -> Map.waterLost += node.flowRate);
		for (Node nodi: this.sources) {
			Map.waterArrived += nodi.flowRate;
		}
	}
	public Pipe MakePipe(){
		//Stakeholder
		return(new Pipe());
	}
}
