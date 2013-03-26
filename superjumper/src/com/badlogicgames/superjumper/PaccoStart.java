package com.badlogicgames.superjumper;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author phra
 *
 */

public class PaccoStart extends Pacco implements PROTOCOL_CONSTANTS {
	
	int seed;
	
	public PaccoStart(int seed) {
		super(PROTOCOL_CONSTANTS.PACKET_START);
		byte[] payload = Utils.serializeInt(seed);
		this.setSize(payload.length);
		this.setData(payload);
		this.seed = seed;
		
	}

	public PaccoStart(Pacco pkt) throws ProtocolException {
		super(pkt.getType(),pkt.getData(),pkt.getSize());
		if (this.getType() != PROTOCOL_CONSTANTS.PACKET_TYPE_BOB_MULTI) {
			throw new ProtocolException("TYPE SBAGLIATO.");
		}
		ByteArrayInputStream bas = new ByteArrayInputStream(pkt.getData());
		DataInputStream ds = new DataInputStream(bas);
		try {
			this.seed = ds.readInt();
		} catch (IOException e) {
			throw new ProtocolException("ERRORE NEL PROTOCOLLO.");
		}
	}


}
