package com.badlogicgames.superjumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class World {
	public interface WorldListener {
		public void jump ();
		public void highJump ();
		public void hit ();
		public void coin ();
		public void life ();
		public void projectile();
	}
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 1000;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public final int PLATFORMS_DISTANCE = 10;
	public final int STARS_DISTANCE = 1;
	public final Bob bob;
	public final List<Bubble> bubbles;
	public final List<Platform> platforms;
	public final List<Star> stars;
	public final List<Spring> springs;
	public final List<Squirrel> squirrels;
	public final List<Projectile> projectiles;
	public final List<Coin> coins;
	public final List<Life> lifes;
	public Castle castle;
	public final WorldListener listener;
	public final Random rand;
	public float heightSoFar;
	public int score;
	public int state;
	public int shot=5;
	public int turbo=1;
	public float bubbletimes;
	private Vector2 gravity = new Vector2(0,15);

	public World (WorldListener listener) {
		this.bob = new Bob(4, 2);
		this.bubbles=new ArrayList<Bubble>();
		this.platforms = new ArrayList<Platform>();
		this.stars = new ArrayList<Star>();
		this.projectiles = new ArrayList<Projectile>();
		this.springs = new ArrayList<Spring>();
		this.squirrels = new ArrayList<Squirrel>();
		this.coins = new ArrayList<Coin>();
		this.lifes = new ArrayList<Life>();
		this.listener = listener;
		this.rand = new Random(5000L);
		this.generateLevel();
		this.setGravity(0, 3);
		this.heightSoFar = 0;
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
		Life life = new Life(0,0);
		lifes.add(life);
		lifes.add(life);
		lifes.add(life);
		lifes.add(life);
		lifes.add(life);
	}

	private void generateLevel () {
		final float k = 5;
		float y = Platform.PLATFORM_HEIGHT / 2;
		//float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * this.gravity.y);
		float maxJumpHeight = this.PLATFORMS_DISTANCE;
		float minJumpHeight = this.STARS_DISTANCE;
		while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
			int type = rand.nextFloat() > 0.5f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
			float x = rand.nextFloat() > 0.5f ? rand.nextFloat() *k : WORLD_WIDTH/ - rand.nextFloat() *k;
			//star generate
			int type_star = Star.STAR_TYPE_STATIC;//star
			float y_star = rand.nextFloat() *20;//star
			float x_star = rand.nextFloat() *10;//star
			Star star = new Star(type_star, x_star, y_star);//star
			stars.add(star);
			//end star generate
			Platform platform = new Platform(type, x, y);
			platforms.add(platform);
			if (rand.nextFloat() > 0.2f && type != Platform.PLATFORM_TYPE_MOVING) {
				Spring spring = new Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT / 2 + Spring.SPRING_HEIGHT / 2);
				springs.add(spring);

				if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
					Squirrel squirrel = new Squirrel(platform.position.x + rand.nextFloat()*k, platform.position.y
						+ Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * k);
					squirrels.add(squirrel);
				}
			}
			if (rand.nextFloat() > 0.5f) {
				/*Coin coin = new Coin(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 5*rand.nextFloat() : -k*rand.nextFloat()), 
					platform.position.y + (rand.nextFloat() > 0.5f ? k*rand.nextFloat() : -k*rand.nextFloat()));
				coins.add(coin);*/
				Bubble bubble = new Bubble(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 5*rand.nextFloat() : -k*rand.nextFloat()), 
					platform.position.y + (rand.nextFloat() > 0.5f ? k*rand.nextFloat() : -k*rand.nextFloat()));
				bubbles.add(bubble);
			}
			y += (maxJumpHeight - 0.5f);
			//y -= rand.nextFloat() * (maxJumpHeight / 3);
			
			
			if (y > WORLD_HEIGHT / 2) {
				Coin coin = new Coin(platform.position.x + (platform.position.x < WORLD_WIDTH /2 ? 5*rand.nextFloat() : -k*rand.nextFloat()), 
					platform.position.y + (rand.nextFloat() > 0.5f ? k*rand.nextFloat() : -k*rand.nextFloat()));
				coins.add(coin);
			}
			
			
			
			
		}
		castle = new Castle(WORLD_WIDTH / 2, y);
	}

	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
		bob.setGravityBob(x, y);
	}

	public void LifeLess(){
		float len = lifes.size();
		int i=0;
		if(i<len && lifes.size() > 1){
			Life life = lifes.get(i);
			lifes.remove(life);
			len = lifes.size();

		}

		else state = WORLD_STATE_GAME_OVER;
	}

	public void LifeMore(){
		float len = lifes.size();
		Life life = new Life(0,0);
		if(len<=4)lifes.add(life);


	}


	public void ShotProjectile()
	{
		if(shot>0){
			Gdx.input.vibrate(new long[] { 1, 20, 10, 20}, -1); 
			Projectile projectile = new Projectile(bob.position.x,bob.position.y);
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

		updateBob(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateBubbles(deltaTime);
		updateSquirrels(deltaTime);
		updateCoins(deltaTime);
		//updateGravityPlanet(deltaTime);
		updateLifes(deltaTime);
		addStarDynamic();
		updateStar( deltaTime);
		updateProjectiles(deltaTime);
		if (rand.nextFloat() > 0.5f) score += (int)bob.velocity.y;
		if (bob.state != Bob.BOB_STATE_HIT) checkCollisions();
		checkRemovePlatform();
		checkRemoveProjectile();
		checkRemoveCoin();
		checkGameOver();
		checkRemoveStars();
		/* checkRemoveBubble();*/

	}
	
	private void updateGravityPlanet(float deltaTime){
		for ( Platform plat : this.platforms) {
			if (plat.type != Platform.PLATFORM_TYPE_MOVING){
				Utils.changeGravityTowards(plat, bob);
			}
		}
	}
	
	private void updateBob (float deltaTime, float accelX) {
		if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform();
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}

	private void addStarDynamic(){
		//star generate
		int type_star = rand.nextFloat()>0.3?Star.STAR_TYPE_STATIC:Star.STAR_TYPE_MOVING;//star light
		float y_star = rand.nextFloat() *10;//star
		float x_star = rand.nextFloat() *10;//star
		Star star = new Star(type_star, x_star, y_star+bob.position.y+13);//star
		stars.add(star);//star
		////end star generate



	}


	private void updateStar(float deltaTime){
		int len = stars.size();
		for (int i = 0; i < len; i++) {
			Star star= stars.get(i);
			star.update(deltaTime);
		}
	}

	private void updatePlatforms (float deltaTime) {
		
		for ( Platform plat : this.platforms) {
			if (plat.type != Platform.PLATFORM_TYPE_MOVING){
				Utils.changeGravityTowards(plat, bob);
			}
			plat.update(deltaTime);
		}
		
		
		/*
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (platform.type != Platform.PLATFORM_TYPE_MOVING){
				Utils.changeGravityTowards(platform, bob);
			}
			platform.update(deltaTime);
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) { //FIXME
				platforms.remove(platform);
				len = platforms.size();
			}
		}*/
	}
	
	private void updateBubbles (float deltaTime) {
		int len = bubbles.size();
		for (int i = 0; i < len; i++) {
			Bubble bubble = bubbles.get(i);
			bubble.update(deltaTime);
			if(bubble.crashtime != 0 && bubble.crashtime<bubble.stateTime-6){
				bubbles.remove(bubble);
			}
				len = bubbles.size();
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

	private void updateLifes (float deltaTime) {
		int len = lifes.size();
		for (int i = 0; i < len; i++) {
			Life life = lifes.get(i);
			life.update(deltaTime);
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
	
	private void checkRemoveBubble() {
		if (!bubbles.isEmpty()) { 
			if (bob.position.y > bubbles.get(0).position.y+8  && bubbles.get(0).state!=Bubble.BUBBLE_STATE_BOB) bubbles.remove(0);
		}
	}

	private void checkRemoveStars() {
		if (!stars.isEmpty()) { 
			if (bob.position.y > stars.get(0).position.y+5  ) stars.remove(0);
		}
	}

	private void checkRemoveCoin() {
		if (!coins.isEmpty()) { 
			if (bob.position.y > coins.get(0).position.y+5  ) coins.remove(0);
		}
	}
	private void checkCollisions () {
		checkPlatformCollisions();
		checkBubbleCollisions ();
		checkDoubleJump();
		checkSquirrelCollisions();
		checkItemCollisions();
		checkCastleCollisions();
		checkVelocity();
		/*  checkProjectileCollisions();*/
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
                    //#FIXME
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
						platform.pulverize();
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
						coin.pulverize(); 
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
					platform.pulverize();
					score += 100;
					listener.jump();
					len = platforms.size();
					break;
				}
			}
		}
	}

	private void checkDoubleJump() {
		if(Bob.BOB_DOUBLE_JUMP){
			Bob.BOB_DOUBLE_JUMP=false;
			bob.hitPlatform();
			listener.hit();
		}
	}

	private void checkBubbleCollisions () {
		int len = bubbles.size();
		for (int i = 0; i < len; i++) {
			Bubble bubble=bubbles.get(i);
			if (OverlapTester.overlapRectangles(bubble.bounds, bob.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 10, 6, 10}, -1);
			
				bubble.state=Bubble.BUBBLE_STATE_BOB;
			bubble.crashtime=bubble.stateTime;
				len = bubbles.size();
				break;
			}
		}
	}
	
	private void checkSquirrelCollisions () {
		int len = squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = squirrels.get(i);
			if (OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
				Gdx.input.vibrate(new long[] { 1, 100, 60, 100}, -1); 
				LifeMore();
				listener.hit(); 
				squirrels.remove(squirrel);
				len = squirrels.size();
				break;
			}
		}
	}

	private void checkItemCollisions () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			int p=0;
		
			Coin coin = coins.get(i);
			if (coin.state != Coin.COIN_STATE_PULVERIZING && OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {

				Gdx.input.vibrate(new long[] { 1, 90, 40, 90},-1); 
				bob.velocity.y=2;
				bob.setGravityBob(0, 3);
				len = coins.size();
				listener.coin();
				coin.pulverize();
				LifeLess();
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

	private void checkGameOver () {
		if (heightSoFar - 7.5f > bob.position.y) {
			state = WORLD_STATE_GAME_OVER;
		}
		int i = 0;
		Life life = lifes.get(i);
		if (i<0){ state = WORLD_STATE_GAME_OVER;}
	}

}
