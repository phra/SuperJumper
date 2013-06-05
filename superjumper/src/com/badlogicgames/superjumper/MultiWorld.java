/**
 * 
 */
package com.badlogicgames.superjumper;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;

/**
 * @author phra
 *
 */
public class MultiWorld extends World {

	private float precdelta, precaccelx, precaccely;
	protected static FullDuplexBuffer buffer = new FullDuplexBuffer();
	public static String enemy = "";
	public Bob bobMulti;
	public LinkedList<Projectile> projEnemy;
	/**
	 * @param seed
	 */
	public MultiWorld (int seed) {
		super();
		this.randgenerate = new Random(seed);
		this.bobMulti = new Bob(UI.HALFSCREENWIDTH,0);
		this.projEnemy = new LinkedList<Projectile>();
	}

	@Override
	public void update(float deltaTime, float accelX) {
		super.update(deltaTime,accelX);
		switch (this.state) {
		case CONSTANTS.GAME_RUNNING:
			Pacco pkt;
			boolean flag = true;
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
					this.precaccely = pktbob.getAccelY();
				//Gdx.app.debug("pkt component", "precdelta= "+ this.precdelta + "accelx= "+ this.precaccelx +" accely= "+this.precaccely);
					updateBobMulti(this.precdelta,this.precaccelx,this.precaccely);
					flag = false;
					break;
				case PROTOCOL_CONSTANTS.PACKET_END:
					this.state = CONSTANTS.GAME_LEVEL_END;
				case PROTOCOL_CONSTANTS.PACKET_PROJECTILE:
					PaccoProiettile paccoproj = (PaccoProiettile) pkt;
					paccoproj.deserialize();
					projEnemy.offer(new Projectile(paccoproj.getX(), paccoproj.getY(), Projectile.WIDTH, Projectile.HEIGHT));
				default:
					System.out.println("PKT FUORI DAL PROTOCOLLO.");
					break;
				}
			}
			//if (flag) bobMulti.update(deltaTime);
			//Gdx.app.debug("pkt component2", "precdelta= "+ deltaTime + "accelx= "+ accelX +" accely= " + this.bob.velocity.y);
			buffer.putPaccoOutNOBLOCK(new PaccoUpdateBobMulti(deltaTime, bob.position.x, bob.position.y));
			if (this.life == 0) {
				buffer.putPaccoOutNOBLOCK(new PaccoEnd());
				this.state = CONSTANTS.GAME_OVER;
			}

			for (int i = 0; i < projEnemy.size(); i++) {
				Projectile projectile = projEnemy.get(i);
				projectile.update(deltaTime);
				for(int j=0;j<platforms.size();j++) {
					Platform platform=platforms.get(j);
					if (OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						bob.hitPlatform();
						Gdx.input.vibrate(new long[] { 1, 20}, -1); 
						score += 100;
						projEnemy.remove(i--);
						explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
						platforms.remove(j--);
						break;
					}
					if (OverlapTester.overlapRectangles(bob.bounds, projectile.bounds)){
						projEnemy.remove(i--);
						this.LifeLess();
					}
				}
				if (projectile.position.y > bobMulti.position.y+11){ 
					projEnemy.remove(i--);
				}
			}/*
			if(OverlapTester.overlapRectangles(bob.bounds, bobMulti.bounds)){
				bob.hitPlatform();
				Gdx.input.vibrate(new long[] { 1, 20}, -1); 
				bob.gravity.x=-10;
				bobMulti.gravity.x=10;
			}*/
			for(int i=0;i<projectiles.size();i++){
				Projectile projectile=projectiles.get(i);
				if (OverlapTester.overlapRectangles(bobMulti.bounds, projectile.bounds)) {
					Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
					score += 100;
					if((projectile.type==1||projectile.type==2)){
						explosions.offer(new Explosion(bobMulti.position.x, bobMulti.position.y,Platform.PLATFORM_WIDTH,Platform.PLATFORM_HEIGHT,0));
						projectiles.remove(projectile);
					}
					else if(projectile.type==0)projectiles.remove(projectile);
					break;
				}
			}
			break;
		}
	}


	private void updateBobMulti (float deltaTime, float accelX, float accelY) {
//		bobMulti.position.add(((-accelX / 10) * Bob.BOB_MOVE_VELOCITY)* deltaTime, accelY * deltaTime);
		bobMulti.position.x=accelX ;
			bobMulti.position.y=accelY ;
			Gdx.app.debug("updatebobmulti","deltatime="+deltaTime+"accX="+accelX+"accY="+accelY);
		Gdx.app.debug("updatebobomulti", "bobMulti.position.y = " + bobMulti.position.y + " bob.position.y = " + bob.position.y);

	}
}
