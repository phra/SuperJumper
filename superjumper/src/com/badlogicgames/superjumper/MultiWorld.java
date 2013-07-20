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
	int position;
	float myTime = 0;
	private float precdelta, precaccelx, precaccely;
	protected static FullDuplexBuffer buffer = new FullDuplexBuffer();
	public static String enemy = "";
	public Bob bobMulti;
	public LinkedList<Projectile> projEnemy;
	private boolean flag = true;
	public Text positionText = new Text(SCOREPOSITIONX*0.2f,SCOREPOSITIONY*0.92f,"P.");
	public boolean win = false;
	

	/**
	 * @param seed
	 */
	public MultiWorld (int seed) {
		super();
		buffer.clear();
		this.randgenerate = new Random(seed);
		this.bobMulti = new Bob(WORLD_WIDTH/2,0);
		this.projEnemy = new LinkedList<Projectile>();
		positionText.update(0,"P." + position);
		this.texts.offer(positionText);
		scoretext.update(0, "TIME = " + myTime);
		this.texts.offer(ammotext);
		this.texts.offer(lifetext);
		
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
					Gdx.app.debug("Ricezione: Position Nemico", " precdelta= "+ this.precdelta + "posx= "+ bobMulti.position.x +" posy= "+bobMulti.position.y);
					Gdx.app.debug("Ricezione: Position Mio", "deltatime= "+ deltaTime + "posx= "+ bob.position.x +" posy= "+bob.position.y);
					bobMulti.update(this.precdelta,this.precaccelx,this.precaccely);
					flag = false;
					break;
				case PROTOCOL_CONSTANTS.PACKET_END:
					this.win = true;
					System.out.println("HO RICEVUTO IL PACCO END E SPOSTO SU GAME OVER");
					buffer.putPaccoOutNOBLOCK(new PaccoEnd());
					System.out.println("HO RICEVUTO IL PACCO END E SPOSTO SU GAME OVER");
					state = CONSTANTS.GAME_OVER;
					break;
				case PROTOCOL_CONSTANTS.PACKET_PROJECTILE:
					PaccoProiettile paccoproj = new PaccoProiettile(pkt);
					paccoproj.deserialize();
					Gdx.app.debug("Update.Multiworld", "paccoproj.getX()= "+paccoproj.getX()+" paccoproj.getY()"+paccoproj.getY());
					Projectile projectile = new Projectile(paccoproj.getX(), paccoproj.getY(), Projectile.WIDTH, Projectile.HEIGHT);
					projectile.setVelocity(0,25);
					projEnemy.offer(projectile);
					break;
				default:
					System.out.println("PKT FUORI DAL PROTOCOLLO.");
					break;
				}
			}
			buffer.putPaccoOutNOBLOCK(new PaccoUpdateBobMulti(deltaTime, bob.position.x, bob.position.y));
			System.out.println("position.x = "+bob.gravity.x);
			myTime+=deltaTime;
			checkGameOver () ;
			if(bob.position.y > bobMulti.position.y)position=1;
			else position = 2;
			for (int i = 0; i < projEnemy.size() && i >= 0; i++) {
				Projectile projectile = projEnemy.get(i);
				projectile.update(deltaTime);
				for(int j=0;j<platforms.size() && j >= 0;j++) {
					Platform platform=platforms.get(j);
					if (OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 20}, -1); 
						score += 100;
						projEnemy.remove(i--);
						explosions.offer(new Explosion(platform.position.x-Platform.PLATFORM_WIDTH/2, platform.position.y-Platform.PLATFORM_HEIGHT/2,Platform.PLATFORM_WIDTH*2,Platform.PLATFORM_HEIGHT*2,0));
						platforms.remove(j--);
						break;
					}
					else if (OverlapTester.overlapRectangles(bob.bounds, projectile.bounds)){
						projEnemy.remove(i--);
						this.LifeLess();
						break;
					}
				}
				if (projectile.position.y > bobMulti.position.y+20 && i >= 0){ 
					projEnemy.remove(i--);
					break;
				}
			}/*
			if(OverlapTester.overlapRectangles(bob.bounds, bobMulti.bounds)){
				bob.hitPlatform();
				Gdx.input.vibrate(new long[] { 1, 20}, -1); 
				bob.gravity.x=-10;
				bobMulti.gravity.x=10;
			}*/
			for(int i=0;i<projectiles.size() && i >= 0;i++){
				Projectile projectile=projectiles.get(i);
				if (OverlapTester.overlapRectangles(bobMulti.bounds, projectile.bounds)) {
					Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
					score += 100;
					if((projectile.type==1||projectile.type==2)){
						explosions.offer(new Explosion(bobMulti.position.x, bobMulti.position.y,Platform.PLATFORM_WIDTH,Platform.PLATFORM_HEIGHT,0));
						projectiles.remove(i--);
					}
					else if (projectile.type==0) projectiles.remove(projectile);
					break;
				}
			}
			break;
		case GAME_OVER:
			if (this.flag) {
				Gdx.app.debug("GameOver Case MultiWorld ","pacco end");
				buffer.putPaccoOutNOBLOCK(new PaccoEnd());
				this.state = CONSTANTS.GAME_OVER;
				this.flag = false;
			}
			break;
		}
	}
/*
	private void updateBobMulti (float deltaTime, float accelX, float accelY) {
//		bobMulti.position.x=accelX ;
//		bobMulti.position.y=accelY ;
		Gdx.app.debug("updatebobmultiNemico","deltatime="+deltaTime+"positionX="+ bobMulti.position.x+"positionY="+ bobMulti.position.y);
		Gdx.app.debug("updatebobmultiNemicoGravity","deltatime="+deltaTime+"gravityX="+ bobMulti.gravity.x+"positionY="+ bobMulti.gravity.y);
		 bobMulti.velocity.add(accelX * deltaTime, accelY * deltaTime);
		 bobMulti.position.add(bobMulti.velocity.x * deltaTime, bobMulti.velocity.y * deltaTime);
		 bobMulti.bounds.x = bobMulti.position.x - bobMulti.bounds.width / 2;
		 bobMulti.bounds.y = bobMulti.position.y - bobMulti.bounds.height / 2;
			
	}
*/
	@Override
	public void updateTexts(float deltaTime) {
		ammotext.update(deltaTime, shot + "x");
		lifetext.update(deltaTime, life + "x");
		positionText.update(deltaTime,"P."+position);
		scoretext.update(deltaTime, "TIME = " + (int)myTime);
		for (int i = 0; i < this.texts.size() && i >= 0; i++) {
			Text text = this.texts.get(i);
			text.update(deltaTime);
			if (text.duration != -1 && text.stateTime > text.duration)
				texts.remove(i--);
		}
	}

	@Override
	public void checkGameOver () {

		if (life<=0){ 
			Gdx.app.debug("CheckGameOver ","mando il pacco end");
			buffer.putPaccoOutNOBLOCK(new PaccoEnd());
	
			this.state = CONSTANTS.GAME_OVER;}
	}
	
	@Override
	public void LifeLess(){
		if (--life > 0) {
			if (life == 1) this.texts.offer(new FloatingText("WARNING!", 0));
		} else {
			System.out.println("Sono dentro lifeLess override");
			this.win = false;
			buffer.putPaccoOutNOBLOCK(new PaccoEnd());
			state = CONSTANTS.GAME_OVER;
		
		}
	}


}
