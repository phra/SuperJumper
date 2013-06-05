package com.badlogicgames.superjumper;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * @author phra
 *
 */

public class PaccoUpdateBobMulti extends Pacco implements PROTOCOL_CONSTANTS {

	private float deltaTime, accelX, accelY;

	public PaccoUpdateBobMulti (float deltaTime, float accelX, float accelY) {
		super(PROTOCOL_CONSTANTS.PACKET_TYPE_BOB_MULTI);
		byte[] payload = Utils.serializeManyFloat(deltaTime, accelX, accelY);
		this.setSize(payload.length);
		this.setData(payload);
		this.deltaTime = deltaTime;
		this.accelX = accelX;
		this.accelY = accelY;
	}

	public PaccoUpdateBobMulti (Pacco pkt) throws ProtocolException {
		super(pkt.getType(),pkt.getData(),pkt.getSize());
		if (this.getType() != PROTOCOL_CONSTANTS.PACKET_TYPE_BOB_MULTI) {
			throw new ProtocolException("TYPE SBAGLIATO.");
		}
		ByteArrayInputStream bas = new ByteArrayInputStream(pkt.getData());
		DataInputStream ds = new DataInputStream(bas);
		try {
			this.deltaTime = ds.readFloat();
			this.accelX = ds.readFloat();
			this.accelY = ds.readFloat();
		} catch (IOException e) {
			throw new ProtocolException("ERRORE NEL PROTOCOLLO.");
		}
	}

	public float getDeltaTime() {
		return deltaTime;
	}

	public float getAccelX(){
		return accelX;
	}
	
	public float getAccelY(){
		return accelY;
	}

}
