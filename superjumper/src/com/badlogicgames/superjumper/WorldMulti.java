package com.badlogicgames.superjumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class WorldMulti implements PROTOCOL_CONSTANTS {
	public interface WorldListener {
		public void jump ();
		public void highJump ();
		public void hit ();
		public void coin ();
		public void life ();
		public void projectile();
	}

	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public final int PLATFORMS_DISTANCE = 10;
	public final Bob bob;
	public final Bob bobMulti;
	public final List<Platform> platforms;
	public final List<Spring> springs;
	public final List<Squirrel> squirrels;
	public final List<Projectile> projectiles;
	public final List<Coin> coins;
	public Castle castle;
	public final WorldListener listener;
	public Random rand;
	public float heightSoFar;
	public int score;
	public int state;
	public int shot=5;
	public int turbo=1;
	private Vector2 gravity = new Vector2(0,15);
	protected static FullDuplexBuffer buffer = new FullDuplexBuffer();
	public static String enemy = "";
	private float precdelta = 0, precaccelx = 0;

	public WorldMulti (WorldListener listener, int seed) {
		this.bob = new Bob(4, 2);
		this.bobMulti = new Bob(4, 2);
		this.platforms = new ArrayList<Platform>();
		this.projectiles = new ArrayList<Projectile>();
		this.springs = new ArrayList<Spring>();
		this.squirrels = new ArrayList<Squirrel>();
		this.coins = new ArrayList<Coin>();
		this.listener = listener;
		this.rand = new Random();
		this.generateLevel();
		this.setGravity(0, 3);
		this.heightSoFar = 0;
		this.score = 0;
		this.rand = new Random(seed);
		this.state = WORLD_STATE_RUNNING;

	}

	private void generateLevel () {
		float y = Platform.PLATFORM_HEIGHT / 2;
		//float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * this.gravity.y);
		float maxJumpHeight = this.PLATFORMS_DISTANCE;


		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {

			int type = rand.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
			//float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;
			float random = rand.nextFloat();
			float x = rand.nextFloat() > 0.5f ? random *3 : WORLD_WIDTH/ - random *3;
			Platform platform = new Platform(type, x, y);
			platforms.add(platform);
			if (rand.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING) {
				Spring spring = new Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2 + Spring.SPRING_HEIGHT / 2);
				springs.add(spring);
			}
			if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
				Squirrel squirrel = new Squirrel(platform.position.x + rand.nextFloat(), platform.position.y
					+ Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2);
				squirrels.add(squirrel);
			}
			if (rand.nextFloat() > 0.3f) {
				random = rand.nextFloat();
				Coin coin = new Coin(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 3*random : -3*random), 
					platform.position.y + (rand.nextFloat() > 0.5f ? 3*random : -3*random));
				coins.add(coin);
			}
			y += (maxJumpHeight - 0.5f);
			y -= rand.nextFloat() * (maxJumpHeight / 3);
		}
		castle = new Castle(WORLD_WIDTH / 2, y);
	}

	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
		bob.setGravityBob(x, y);
		bobMulti.setGravityBob(x,y);
	}
	
 

	public void ShotProjectile()
	{
		if(shot>0){
			Gdx.input.vibrate(new long[] { 1, 20, 10, 20}, -1); 
			Projectile projectile = new Projectile(bob.position.x,bob.position.y,Projectile.WIDTH,Projectile.HEIGHT);
			projectile.setVelocity(0,15);
			projectiles.add(projectile);
			shot=shot-1;}

	}
	public Vector2 getGravity(){
		return gravity;
	}

	public void Turbo(){
		if(turbo>=1){
			bob.velocity.y=10;
			turbo-=1;
		}
	}

	public void update (float deltaTime, float accelX) {

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
			default:
				System.out.println("PKT FUORI DAL PROTOCOLLO.");
				break;
			}
		} /*else {
            //this.precdelta /= 2;
            //this.precaccelx /= 2;
            updateBobMulti(this.precdelta,this.precaccelx);
        }*/

		//updateBobMulti(deltaTime,accelX * 1.2f);
		buffer.putPaccoOutNOBLOCK(new PaccoUpdateBobMulti(deltaTime, accelX));
		updateBob(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateSquirrels(deltaTime);
		updateCoins(deltaTime);
		updateProjectiles(deltaTime);
		score += (int)bob.velocity.y / 2;
		if (bob.state != Bob.BOB_STATE_HIT) checkCollisions();
		checkRemovePlatform();
		checkRemoveProjectile();
		checkRemoveCoin();
		checkGameOver();
		//checkGameOverMulti();

	}

	private void updateBob (float deltaTime, float accelX) {
		if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform();
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}

	private void updateBobMulti(float deltaTime, float accelX){
		bobMulti.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
		bobMulti.update(deltaTime);
	}

	private void updatePlatforms (float deltaTime) {
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
				platforms.remove(platform);
				len = platforms.size();
			}
		}
	}

	private void updateSquirrels (float deltaTime) {
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			squirrel.update(deltaTime);
		}
	}

	private void updateCoins (float deltaTime) 
	{
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			coin.update(deltaTime);
			if (coin.state == Coin.COIN_STATE_PULVERIZING && coin.stateTime > Coin.COIN_PULVERIZE_TIME) {
				coins.remove(coin);
				len = coins.size();

			}
		}
	}
	private void updateProjectiles (float deltaTime) {
		int len = projectiles.size();
		for (int i = 0; i < len; i++) {
			Projectile projectile = projectiles.get(i);
			projectile.update(deltaTime);
		}
	}


	
	private void checkRemoveProjectile(){
		int i = 0;
		if (!projectiles.isEmpty()) {
			if ( projectiles.get(i).position.y > bob.position.y+14 ) 
				projectiles.remove(i);
		}
	}

	private void checkRemovePlatform() {
		if (!platforms.isEmpty()) { 
			if (bob.position.y > platforms.get(0).position.y+5  ) platforms.remove(0);
		}
	}
	private void checkRemoveCoin() {
		if (!coins.isEmpty()) { 
			if (bob.position.y > coins.get(0).position.y+5  ) coins.remove(0);
		}
	}
	private void checkCollisions () {
		checkPlatformCollisions();
		checkPlatformCollisionsMulti();
		checkSquirrelCollisions();
		checkSquirrelCollisionsMulti();
		checkItemCollisions();
		checkItemCollisionsMulti();
		checkCastleCollisions();
		checkCastleCollisionsMulti();
		checkVelocity();
		checkVelocityMulti();
		checkProjectileCollisions();
		checkProjectileWorldCollisions();

	}
	private void checkProjectileCollisions(){
		int i = 0, j = 0;
		if (!projectiles.isEmpty() && !platforms.isEmpty()){

			/* Platform platformstmp;
        int len= platforms.size();
        int len1 = projectiles.size();
       if (platforms.get(0).position.y > projectiles.get(0).position.y)
                return;
            else if (OverlapTester.overlapRectangles(platforms.get(0).bounds, projectiles.get(0).bounds)){
                platforms.get(0).pulverize();
                projectiles.remove(0);
                return;
            }
            else {
                Platform primaplatform = platforms.get(i);
                for (; i < projectiles.size(); i++) {
                    for (platformstmp = platforms.get(0); projectiles.get(j).position.y > primaplatform.position.y; j++){
                        if (OverlapTester.overlapRectangles(platformstmp.bounds, projectiles.get(j).bounds)) {
                          platforms.get(j).pulverize();
                           projectiles.remove(i);
                        }
                    }
                }
            }
			 */


			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<platforms.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Platform platform=platforms.get(j);
					if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 100, 60, 100}, -1); 
						projectiles.remove(i);
						i--;
				
						/*platforms.remove(j);*/

						break;
					}

				}
			}
		}
	}

	private void checkProjectileCollisionsMulti(){
		int i = 0, j = 0;
		if (!projectiles.isEmpty() && !platforms.isEmpty()){

			/* Platform platformstmp;
        int len= platforms.size();
        int len1 = projectiles.size();
       if (platforms.get(0).position.y > projectiles.get(0).position.y)
                return;
            else if (OverlapTester.overlapRectangles(platforms.get(0).bounds, projectiles.get(0).bounds)){
                platforms.get(0).pulverize();
                projectiles.remove(0);
                return;
            }
            else {
                Platform primaplatform = platforms.get(i);
                for (; i < projectiles.size(); i++) {
                    for (platformstmp = platforms.get(0); projectiles.get(j).position.y > primaplatform.position.y; j++){
                        if (OverlapTester.overlapRectangles(platformstmp.bounds, projectiles.get(j).bounds)) {
                          platforms.get(j).pulverize();
                           projectiles.remove(i);
                        }
                    }
                }
            }
			 */


			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<platforms.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Platform platform=platforms.get(j);
					if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && OverlapTester.overlapRectangles(platform.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 100, 60, 100}, -1); 
						projectiles.remove(i);
						i--;
					
						/*platforms.remove(j);*/

						break;
					}

				}
			}
		}
	}

	private void checkProjectileWorldCollisions(){
		int i = 0, j = 0;
		if (!projectiles.isEmpty() && !coins.isEmpty()){

			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<coins.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Coin coin=coins.get(j);
					if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(coin.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
						coins.remove(j);
						projectiles.remove(i);



						i--;

						/*platforms.remove(j);*/

						break;
					}

				}
			}
		}
	}

	private void checkProjectileWorldCollisionsMulti(){
		int i = 0, j = 0;
		if (!projectiles.isEmpty() && !coins.isEmpty()){

			for(i=0;i<projectiles.size();i++)
			{
				for(j=0;j<coins.size();j++)
				{
					Projectile projectile=projectiles.get(i);
					Coin coin=coins.get(j);
					if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(coin.bounds, projectile.bounds)) {
						Gdx.input.vibrate(new long[] { 1, 20, 40, 20}, -1); 
						coins.remove(j);
						projectiles.remove(i);



						i--;

						/*platforms.remove(j);*/

						break;
					}

				}
			}
		}
	}

	private void checkVelocity () {
		if (bob.velocity.y > bob.MAXVELOCITY){
			bob.setGravityBob(0, 0);

		}
	}

	private void checkVelocityMulti () {
		if (bobMulti.velocity.y > bobMulti.MAXVELOCITY){
			bobMulti.setGravityBob(0, 0);

		}
	}

	private void checkPlatformCollisionsMulti () {
		//if (bob.velocity.y > 0) return;
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (bobMulti.position.y > platform.position.y) {
				if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && OverlapTester.overlapRectangles(bobMulti.bounds, platform.bounds)) {
					bobMulti.hitPlatform();
					Turbo();
					Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
					turbo=turbo+1;
					shot=shot+5;
					
					score += 100;
					listener.jump();
					len = platforms.size();
					break;
				}
			}
		}
	}

	private void checkPlatformCollisions () {
		//if (bob.velocity.y > 0) return;
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (bob.position.y > platform.position.y) {
				if (platform.state != Platform.PLATFORM_STATE_PULVERIZING && OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
					bob.hitPlatform();
					Turbo();
					Gdx.input.vibrate(new long[] { 1, 20,10, 5}, -1); 
					turbo=turbo+1;
					shot=shot+5;
				
					score += 100;
					listener.jump();
					len = platforms.size();
					break;
				}
			}
		}
	}

	private void checkSquirrelCollisions () {
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 100, 60, 100}, -1); 
			//	LifeMore();
				listener.hit(); 
				squirrels.remove(squirrel);
				len = squirrels.size();
				break;
			}
		}
	}

	private void checkSquirrelCollisionsMulti () {
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, bobMulti.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 100, 60, 100}, -1); 
				//LifeMore();
				listener.hit(); 
				squirrels.remove(squirrel);
				len = squirrels.size();
				break;
			}
		}
	}

	private void checkItemCollisionsMulti () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(bobMulti.bounds, coin.bounds)) {

				Gdx.input.vibrate(new long[] { 1, 90, 40, 90},-1); 
				bobMulti.velocity.y=2;
				bobMulti.setGravityBob(0, 3);
				len = coins.size();
				listener.coin();
				//LifeLess();
				score -= 300;
				coins.remove(coin);
				break;
			}
		}
		if (bobMulti.velocity.y > 0) return;
		len = springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = springs.get(i);
			if (bobMulti.position.y > spring.position.y) {
				if (OverlapTester.overlapRectangles(bobMulti.bounds, spring.bounds)) {
					bobMulti.hitSpring();
					listener.highJump();
					break;
				}
			}
		}
	}

	private void checkItemCollisions () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {

				Gdx.input.vibrate(new long[] { 1, 90, 40, 90},-1); 
				bob.velocity.y=2;
				bob.setGravityBob(0, 3);
				len = coins.size();
				listener.coin();
				//LifeLess();
				score -= 300;
				coins.remove(coin);
				break;
			}
		}
		if (bob.velocity.y > 0) return;
		len = springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = springs.get(i);
			if (bob.position.y > spring.position.y) {
				if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
					bob.hitSpring();
					listener.highJump();
					break;
				}
			}
		}
	}

	private void checkCastleCollisions () {
		/*if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds))*/ 
		if(bob.position.y>castle.position.y){
			state = WORLD_STATE_NEXT_LEVEL;
		}
	}

	private void checkCastleCollisionsMulti () {
		/*if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds))*/ 
		if(bob.position.y>castle.position.y){
			state = WORLD_STATE_NEXT_LEVEL;
		}
	}

	@SuppressWarnings("static-access")
	private void checkGameOver () {
		if (heightSoFar - 7.5f > bob.position.y) {
			state = WORLD_STATE_GAME_OVER;
			this.buffer.putPaccoOutNOBLOCK(new PaccoEnd());
		}
	}
	private void checkGameOverMulti () {
		if (heightSoFar - 7.5f > bobMulti.position.y) {
			state = WORLD_STATE_GAME_OVER;
		}
		/*
        int i = 0;
        Life life = lifes.get(i);
        if (i<0){ state = WORLD_STATE_GAME_OVER;}*/
	}

}