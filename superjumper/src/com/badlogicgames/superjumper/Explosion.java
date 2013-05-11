package com.badlogicgames.superjumper;

public class Explosion extends DynamicGameObject {
	
	public float stateTime = 0, width, height;

	public static final float EXPLOSION_TIME = 1f;
	
	public Explosion (float x, float y, float width, float height) {
		super(x, y, width, height);
		this.width = width;
		this.height = height;
		// TODO Auto-generated constructor stub
	}
	
	public void update(float deltaTime) {
		this.stateTime += deltaTime;
	}

}
