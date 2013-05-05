package com.badlogicgames.superjumper;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * @author phra
 *
 */

public class PaccoUpdateBobMulti extends Pacco implements PROTOCOL_CONSTANTS {

	private float deltaTime, accelX;

	public PaccoUpdateBobMulti (float deltaTime, float accelX) {
		super(PROTOCOL_CONSTANTS.PACKET_TYPE_BOB_MULTI);
		byte[] payload = Utils.serialize2Float(deltaTime, accelX);
		this.setSize(payload.length);
		this.setData(payload);
		this.deltaTime = deltaTime;
		this.accelX = accelX;
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

}
