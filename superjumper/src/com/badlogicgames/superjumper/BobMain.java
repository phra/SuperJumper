package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
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
	public Vector2 gravity = new Vector2(0,0);
	public final int RAGGIO = 50;
	

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

	private int OverlapCircleTester(){
		//try 2 invert
		//int distx = (int)this.position.x - MainMenuScreen.centrox;
		//int disty = (int)this.position.y - MainMenuScreen.centroy;
		int distx = MainMenuScreen.centrox - (int)this.position.x;
		int disty = MainMenuScreen.centroy - (int)this.position.y;
		//Gdx.app.debug("OVERLAPCIRCLE", "overlap returns " + (+(((int)this.position.x^2 + (int)this.position.y^2 + (int)this.position.x*MainMenuScreen.centrox + (int)this.position.y*MainMenuScreen.centroy) - RAGGIO^2)));
		//return +(((int)this.position.x^2 + (int)this.position.y^2 + (int)this.position.x*MainMenuScreen.centrox + (int)this.position.y*MainMenuScreen.centroy) - RAGGIO^2);
		Gdx.app.debug("OVERLAPCIRCLE", "overlap w/ distx=" + distx + "e disty= " + disty + " returns " + (+(distx^2 + disty^2 - RAGGIO^2)));
		return +(distx^2 + disty^2 - RAGGIO^2);
	}
	
	private void updateGravity(){

		final float TESTGRAVITY = 5f;
		/*
		if (position.x >= MainMenuScreen.centrox && position.y >=  MainMenuScreen.centroy){
			
			if (OverlapCircleTester() >= 0){
				Gdx.app.debug("UPDATEGRAVITY", "ALTO DX FUORI");
				this.gravity.x = -TESTGRAVITY;
				this.gravity.y = -TESTGRAVITY;
				//this.gravity.add(-TESTGRAVITY, -TESTGRAVITY);
			} else {
				Gdx.app.debug("UPDATEGRAVITY", "ALTO DX DENTRO");
				this.gravity.x = +TESTGRAVITY;
				this.gravity.y = +TESTGRAVITY;
				//this.gravity.add(+TESTGRAVITY, +TESTGRAVITY);
			}
		} else if (position.x >= MainMenuScreen.centrox && position.y <  MainMenuScreen.centroy){
			
			if (OverlapCircleTester() >= 0){
				Gdx.app.debug("UPDATEGRAVITY", "BASSO DX FUORI");
				this.gravity.x = -TESTGRAVITY;
				this.gravity.y = +TESTGRAVITY;
				//this.gravity.add(-TESTGRAVITY, +TESTGRAVITY);
			} else {
				Gdx.app.debug("UPDATEGRAVITY", "BASSO DX DENTRO");
				this.gravity.x = +TESTGRAVITY;
				this.gravity.y = -TESTGRAVITY;
				//this.gravity.add(+TESTGRAVITY, -TESTGRAVITY);
			}
		} else if (position.x < MainMenuScreen.centrox && position.y >=  MainMenuScreen.centroy){
			
			if (OverlapCircleTester() >= 0){
				Gdx.app.debug("UPDATEGRAVITY", "ALTO SX FUORI");
				this.gravity.x = +TESTGRAVITY;
				this.gravity.y = -TESTGRAVITY;
				//this.gravity.add(+TESTGRAVITY, -TESTGRAVITY);
			} else {
				Gdx.app.debug("UPDATEGRAVITY", "ALTO SX DENTRO");
				this.gravity.x = -TESTGRAVITY;
				this.gravity.y = +TESTGRAVITY;
				//this.gravity.add(-TESTGRAVITY, +TESTGRAVITY);
			}
		} else if (position.x < MainMenuScreen.centrox && position.y <  MainMenuScreen.centroy){
			
			if (OverlapCircleTester() >= 0){
				Gdx.app.debug("UPDATEGRAVITY", "BASSO SX FUORI");
				this.gravity.x = +TESTGRAVITY;
				this.gravity.y = +TESTGRAVITY;
				//this.gravity.add(+TESTGRAVITY, +TESTGRAVITY);
			} else {
				Gdx.app.debug("UPDATEGRAVITY", "BASSO SX DENTRO");
				this.gravity.x = -TESTGRAVITY;
				this.gravity.y = -TESTGRAVITY;
				//this.gravity.add(-TESTGRAVITY, -TESTGRAVITY);
			}
		}*/
		if (position.x >= MainMenuScreen.centrox && position.y >= MainMenuScreen.centroy){
			this.gravity.rotate(90);
			} else if (position.x >= MainMenuScreen.centrox && position.y < MainMenuScreen.centroy){
			this.gravity.rotate(360);
			} else if (position.x < MainMenuScreen.centrox && position.y >= MainMenuScreen.centroy){
			this.gravity.rotate(270);
			} else if (position.x < MainMenuScreen.centrox && position.y < MainMenuScreen.centroy){
			this.gravity.rotate(180);
		}
	}
	
	public void update(float deltaTime) {
		//updateGravity();
		
		
		velocity.add(gravity.x * deltaTime, gravity.y * deltaTime );
		position.add(velocity.x * deltaTime, velocity.y * deltaTime );

		/*velocity.x += gravity.x * deltaTime;
		velocity.y += gravity.y * deltaTime;
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;*/

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