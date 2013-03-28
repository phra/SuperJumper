package com.badlogicgames.superjumper;

import com.badlogic.gdx.math.Vector2;

public class BobMain extends DynamicGameObject {
	public static final int BOB_STATE_JUMP = 0;
	public static final int BOB_STATE_FALL = 1;
	public static final int BOB_STATE_HIT = 2;
	public static final float BOB_JUMP_VELOCITY = 11;
	public static final float BOB_MOVE_VELOCITY = 20;
	public static final float BOB_WIDTH = 0.8f;
	public static final float BOB_HEIGHT = 0.8f;
	public final float MAXVELOCITY = 6f;
	public Vector2 gravity = new Vector2();

	public static boolean BOB_DOUBLE_JUMP = false;
	public int rotationcounter=0;
	int state;
	float stateTime;
	public static float jumpTime;

	public BobMain (float x, float y) {
		super(x, y, BOB_WIDTH, BOB_HEIGHT);
		state = BOB_STATE_FALL;
		stateTime = 0;
	}

	public void setGravityBob(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
	}


	public void update(float deltaTime) {
		int centrox = 320/2;
		int centroy = 480/2;

		if (position.x >= centrox && position.y >= centroy){
			this.gravity.x--;
			this.gravity.y--;
		} else if (position.x >= centrox && position.y < centroy){
			this.gravity.x--;
			this.gravity.y++;
		} else if (position.x < centrox && position.y >= centroy){
			this.gravity.x++;
			this.gravity.y--;
		} else if (position.x < centrox && position.y < centroy){
			this.gravity.x++;
			this.gravity.y++;
		}

		velocity.add(gravity.x * deltaTime, gravity.y * deltaTime );
		position.add((velocity.x * 50) * deltaTime, (velocity.y * 50) * deltaTime );

		//velocity.x = gravity.x * deltaTime;
		//velocity.y = gravity.y * deltaTime;

		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		if(rotationcounter==360)rotationcounter=0;
		else rotationcounter++;
		if (velocity.y > 0 && state != BOB_STATE_HIT) {
			if (state != BOB_STATE_JUMP) {
				state = BOB_STATE_JUMP;
				stateTime = 0;
			}
		}

		if (velocity.y < 0 && state != BOB_STATE_HIT) {
			if (state != BOB_STATE_FALL) {
				state = BOB_STATE_FALL;
				stateTime = 0;
			}
		}

		if (position.x < 0) position.x = 320;
		if (position.x > 320) position.x = 0;

		stateTime += deltaTime;
		jumpTime += deltaTime;
	}

	public void hitSquirrel () {
		velocity.set(0, 0);
		state = BOB_STATE_HIT;
		stateTime = 0;
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

}