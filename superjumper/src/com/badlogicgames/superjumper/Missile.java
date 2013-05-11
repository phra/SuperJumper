package com.badlogicgames.superjumper;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Missile extends Projectile {
	public static float HEIGHT = 1f;
	public static float WIDTH = 1f;
	
	GameObject target;
	//LinkedList<Vector2> path;
	public static final int TYPE = 1;
	
	
	public Missile (float x, float y, float width, float height, GameObject target) {
		super(x, y, width, height);
		this.target = target;
		this.type = TYPE;
	}
	
	@Override
	public void update (float deltaTime) {
		if (target != null) {
			//Gdx.app.debug("MISSILEUPDATE", "genero la posizione");
			Utils.changeGravityTowards(this,target);
			position.add(velocity.x * deltaTime, (velocity.y+11f) * deltaTime);
			bounds.x = position.x - bounds.width / 2;
			bounds.y = position.y - bounds.height / 2;
		}
		//Gdx.app.debug("MISSILEUPDATE", "position.x " + this.position.x + ", position.y " + this.position.y);
		stateTime += deltaTime;
	}
}


