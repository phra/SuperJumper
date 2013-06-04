/**
 * 
 */
package com.badlogicgames.superjumper;

import java.util.Random;

/**
 * @author phra
 *
 */
public class MultiWorld extends World {

	private float precdelta;
	private float precaccelx;
	protected static FullDuplexBuffer buffer = new FullDuplexBuffer();
	public static String enemy = "";
	public Bob bobMulti;

	/**
	 * @param seed
	 */
	public MultiWorld (int seed) {
		super();
		this.randgenerate = new Random(seed);
		this.bobMulti = new Bob(UI.HALFSCREENWIDTH,0);
	}

	@Override
	public void update(float deltaTime, float accelX) {
		super.update(deltaTime,accelX);
		switch (this.state) {
		case CONSTANTS.GAME_RUNNING:
			Pacco pkt;
			while ((pkt = buffer.takePaccoInNOBLOCK()) != null) {
				switch (pkt.getType()) {
				case PROTOCOL_CONSTANTS.PACKET_TYPE_BOB_MULTI:
					PaccoUpdateBobMulti pktbob;
					try {
						pktbob = new PaccoUpdateBobMulti(pkt);
					} catch (ProtocolException e) {
						System.out.println("PKT FUORI DAL PROTOCOLLO.");
						break;
					}
					this.precdelta = pktbob.getDeltaTime();
					this.precaccelx = pktbob.getAccelX();
					updateBobMulti(this.precdelta,this.precaccelx);
					break;
				case PROTOCOL_CONSTANTS.PACKET_END:
					this.state = CONSTANTS.GAME_LEVEL_END;
				default:
					System.out.println("PKT FUORI DAL PROTOCOLLO.");
					break;
				}
			}
			buffer.putPaccoOutNOBLOCK(new PaccoUpdateBobMulti(deltaTime, accelX));
			if (this.life == 0) {
				buffer.putPaccoOutNOBLOCK(new PaccoEnd());
				this.state = CONSTANTS.GAME_OVER;
			}
		}
	}

	private void updateBobMulti (float deltaTime, float accelX) {
		bobMulti.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
		bobMulti.update(deltaTime);
	}
}
