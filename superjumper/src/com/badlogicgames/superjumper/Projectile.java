package com.badlogicgames.superjumper;

import com.badlogic.gdx.math.Vector2;

public class Projectile extends DynamicGameObject {
	public static float HEIGHT = 0.3f;
	public static float WIDTH = 0.6f;
	public final float width, height;
	public final float MAXVELOCITY = 10f;
	public int type;
	public Vector2 gravity = new Vector2();
	public int state=0;
	float stateTime;
	private boolean flag = true;
	public static final int TYPE = 0;
	public static final float MISSILE_PULVERIZE_TIME = 0.2f * 3;
	public static final int MISSILE_STATE_PULVERIZING = 1;

	public Projectile (float x, float y, float width, float height) {
		super(x, y, width, height);
		this.width = width;
		this.height = height;
		stateTime = 0;
		this.type = TYPE;
	}

	public void setGravity(float x, float y){
		this.gravity.x = x;
		this.gravity.y = y;
	}

	public void setVelocity(float x, float y){
		this.velocity.x = x;
		this.velocity.y = y;
	}
	public void update (float deltaTime) {
		if (flag) setGravity(0,20);
		//if(state==0)setGravity(0,20);
		//else velocity.y=-20;
		velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
		stateTime += deltaTime;
	}
}