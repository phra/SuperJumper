/**
 * 
 */
package com.badlogicgames.superjumper;

import java.util.LinkedList;

/**
 * @author phra
 *
 */
public class PaccoProiettile extends Pacco {
	
	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}
	
	private float x, y;
	
	public PaccoProiettile(float x, float y) {
		super(PROTOCOL_CONSTANTS.PACKET_PROJECTILE,Utils.serialize2Float(x, y),Utils.serialize2Float(x, y).length);
		this.x = x;
		this.y = y;
	}
	
	public PaccoProiettile(Pacco pkt) {
		this(Utils.deserializeManyFloat(pkt.getData()).get(0),Utils.deserializeManyFloat(pkt.getData()).get(1));
	}
	
	public LinkedList<Float> deserialize() {
		LinkedList<Float> list =  Utils.deserializeManyFloat(this.payload);
		this.x = list.removeFirst();
		this.y = list.removeFirst();
		return list;
	}

}
