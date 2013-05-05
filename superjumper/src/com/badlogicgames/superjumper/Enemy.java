package com.badlogicgames.superjumper;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicGameObject {
	public static final int ENEMY_STATE_DIE = 0;
	public static final int ENEMY_STATE_REM = 1;
	public static final int ENEMY_STATE_HIT = 2;
	public static final float BOB_MOVE_VELOCITY = 20;
	public static final float BOB_WIDTH = 2f;
	public static final float BOB_HEIGHT = 2f;
	public static final float ENEMY_PULVERIZE_TIME = 0.1f * 4;
	public float killtime;
	public int life=5;
	public Vector2 gravity = new Vector2();

	public static boolean BOB_DOUBLE_JUMP = false;
 int active=0;
float  pulverizetime;
	int state;
	float stateTime;
	public static float jumpTime;

	public Enemy (float x, float y) {
		super(x, y, BOB_WIDTH, BOB_HEIGHT);
		state = ENEMY_STATE_HIT ;
		stateTime = 0;
	}

	public void setGravityEnemy(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
	}

	public void update (float deltaTime,DynamicGameObject bob) {
		//velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width;
		bounds.y = position.y - bounds.height;
		if(active==1){
			if(position.y-bob.position.y<10)velocity.y+=0.5f;
			if(position.y-bob.position.y>8.5f)velocity.y-=0.5f;
			if(position.x-bob.position.x<1f)velocity.x+=0.5f;
			if(position.x-bob.position.x>-1f)velocity.x-=0.5f;
			if(position.y<14)velocity.y+=0.5;
			if(velocity.x>8)velocity.x=2;
			if(position.x>10)velocity.x-=1;
			if(position.x<0)velocity.x+=1;
			if(bob.position.x==position.x)velocity.x=-5;
		}
		
		stateTime += deltaTime;
		jumpTime += deltaTime;
	}


	public void hitPlatform () {
		//velocity.y = BOB_JUMP_VELOCITY;
		//state = BOB_STATE_JUMP;
		stateTime = 0;
	}

	public void hitSpring () {
		//velocity.y = BOB_JUMP_VELOCITY * 1.5f;
		//state = BOB_STATE_JUMP;
		stateTime = 0;
	}
	public void pulverize () {
		state = ENEMY_STATE_DIE;
		pulverizetime=stateTime;
		velocity.y = 0;
	}
}