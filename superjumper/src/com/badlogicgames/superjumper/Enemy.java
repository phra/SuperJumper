package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends DynamicGameObject {
	public static final int ENEMY_STATE_DIE = 0;
	public static final int ENEMY_STATE_REM = 1;
	public static final int ENEMY_STATE_HIT = 2;
	public static final float BOB_MOVE_VELOCITY = 20;
	public static final float ENEMY_WIDTH = 2f;
	public static final float ENEMY_HEIGHT = 2f;
	public static final float ENEMY_PULVERIZE_TIME = 0.1f * 4;
	public static final int NTYPE = 2;
	Random rand=new Random();
	public float killtime;
	public int life=5;
	public Vector2 gravity = new Vector2();
	 public float enemyshotime;
	int active=0;
	int state;
	float stateTime;
	int type;
	//public static float jumpTime;

	public Enemy (float x, float y) {
		super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT);
		this.type = (int)(rand.nextFloat() * NTYPE);
		state = ENEMY_STATE_HIT ;
		stateTime = 0;
	}

	public void setGravityEnemy(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
	}

	public void update (float deltaTime,DynamicGameObject bob) {
		//Gdx.app.debug("ENEMYUPDATE", "position.x " + this.position.x + ", position.y " + this.position.y);
		//velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width/2;
		bounds.y = position.y - bounds.height/2;
		if(active==1){ //FIXME
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
		//jumpTime += deltaTime;
	}
}