package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;

public class Explosion extends DynamicGameObject {
	
	public float stateTime, width, height, duration;

	public static final float EXPLOSION_TIME = 0.2f;
	
	public Explosion (float x, float y, float width, float height, float duration) {
		super(x, y, width, height);
		this.width = width;
		this.height = height;
		if (duration > 0) this.duration = duration;
		else this.duration = EXPLOSION_TIME;
	}
	
	public void update(float deltaTime) {
		//Gdx.app.debug("explosionupdate", "statetime = " + stateTime + ", deltatime = " + deltaTime);
		this.stateTime += deltaTime;
		//duration -= deltaTime;
	}

}
