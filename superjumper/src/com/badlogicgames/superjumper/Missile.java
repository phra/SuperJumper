package com.badlogicgames.superjumper;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Missile extends Projectile {
	
	GameObject target;
	LinkedList<Vector2> path;
	
	public Missile (float x, float y, GameObject target, LinkedList<Vector2> path) {
		super(x, y);
		this.target = target;
		this.path = path;
	}
	
	@Override
	public void update (float deltaTime) {

		if (this.path.isEmpty()) {
			//velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			Gdx.app.debug("MISSILEUPDATE", "genero la posizione");
			Utils.changeGravityTowards(this,target);
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		} else {
			Gdx.app.debug("MISSILEUPDATE", "prendo posizione dalla lista");
			Vector2 tmp = this.path.remove();
			position.add(tmp.x * deltaTime, tmp.y * deltaTime);
		}
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
		stateTime += deltaTime;
	}
}
