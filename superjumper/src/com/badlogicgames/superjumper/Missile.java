package com.badlogicgames.superjumper;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Missile extends Projectile {
	
	GameObject target;
	//LinkedList<Vector2> path;
	
	public Missile (float x, float y, GameObject target) {
		super(x, y);
		this.target = target;
		//this.path = path;
	}
	
	@Override
	public void update (float deltaTime) {
		//Gdx.app.debug("MISSILEUPDATE", "init");
	/*	if (!this.path.isEmpty()) {
			Gdx.app.debug("MISSILEUPDATE", "prendo posizione dalla lista");
			Vector2 tmp = this.path.remove();
			this.position.x = tmp.x;
			this.position.y = tmp.y;
			//position.add(tmp.x * deltaTime, tmp.y * deltaTime);
		} else*/ if (target != null) {
			//velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
			//Gdx.app.debug("MISSILEUPDATE", "genero la posizione");
			Utils.changeGravityTowards(this,target);
			position.add(velocity.x * deltaTime, (velocity.y+10f) * deltaTime);
			//position.add(0,20);
		} else {
			//Gdx.app.debug("MISSILEUPDATE", "lista vuota e nemico null");
			return;
		}
		
		//Gdx.app.debug("MISSILEUPDATE", "position.x " + this.position.x + ", position.y " + this.position.y);
		
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
		stateTime += deltaTime;
	}
}


