package com.badlogicgames.superjumper;

import com.badlogic.gdx.math.Vector2;

public class BobMain extends DynamicGameObject {
	public static final int BOB_STATE_JUMP = 0;
	public static final int BOB_STATE_FALL = 1;
	public static final int BOB_STATE_HIT = 2;
	public static final float BOB_JUMP_VELOCITY = 11;
	public static final float BOB_MOVE_VELOCITY = 8;
	public static final float BOB_WIDTH = 0.8f;
	public static final float BOB_HEIGHT = 0.8f;
	public final float MAXVELOCITY = 6f;
	public Vector2 gravity = new Vector2(-1,-1);
	public final int RAGGIO = 130;
	private float totaltime = 0;
	private float provatime = 0;
	public static boolean BOB_DOUBLE_JUMP = false;
	public float rotationcounter=90;//90-180 da sistemare
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

	private boolean OverlapCircleTester(){
		//Gdx.app.debug("OVERLAPCIRCLE", "overlap returns " +(int)Math.sqrt((int)Math.pow((double)MainMenuScreen.centrox - (double)this.position.x,2) + (int)Math.pow((double)MainMenuScreen.centroy - (double)this.position.y,2)));
		return (int)Math.sqrt((int)Math.pow((double)MainMenuScreen.centrox - (double)this.position.x,2) + (int)Math.pow((double)MainMenuScreen.centroy - (double)this.position.y,2)) >= RAGGIO ? true : false;
	}
	/*
	private void updateGravity(){

		final float TESTGRAVITY = 10f;
		Gdx.app.debug("UPDATEGRAVITY","position.x = " + position.x + ", position.y = " + position.y);
		if (position.x > MainMenuScreen.centrox && position.y >  MainMenuScreen.centroy){

			if (OverlapCircleTester()){
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
		} else if (position.x > MainMenuScreen.centrox && position.y <  MainMenuScreen.centroy){

			if (OverlapCircleTester()){
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
		} else if (position.x < MainMenuScreen.centrox && position.y >  MainMenuScreen.centroy){

			if (OverlapCircleTester()){
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

			if (OverlapCircleTester()){
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
		}
		/*if (position.x >= MainMenuScreen.centrox && position.y >= MainMenuScreen.centroy){
			this.gravity.rotate(90);
			} else if (position.x >= MainMenuScreen.centrox && position.y < MainMenuScreen.centroy){
			this.gravity.rotate(360);
			} else if (position.x < MainMenuScreen.centrox && position.y >= MainMenuScreen.centroy){
			this.gravity.rotate(270);
			} else if (position.x < MainMenuScreen.centrox && position.y < MainMenuScreen.centroy){
			this.gravity.rotate(180);
		}
	}*/

	private void setCircularPosition(float deltaTime, int raggio, int x, int y){
		//Gdx.app.debug("SETCIRCULARPOSITION", "deltatime = " + deltaTime);
		totaltime += deltaTime;
		if(totaltime<=0.1f)rotationcounter=0;
		//if (rotationcounter >= 360f) rotationcounter = 0;
		rotationcounter += 0.987f; //da sistemare x la rotazione d BobMain
		//Gdx.app.debug("SETCIRCULARPOSITION","rotationcounter = " + rotationcounter);
		this.position.x = (float) (x + raggio*Math.cos(totaltime));
		this.position.y = (float) (y + raggio*Math.sin(totaltime));
		//this.position.add(x + raggio*(float)Math.cos(totaltime*2*Math.PI),y + raggio*(float)Math.sin(totaltime*2*Math.PI));
		//Gdx.app.debug("SETCIRCULARPOSITION", "position.x = " + position.x + ", position.y = " + position.y + ", totaltime = " + totaltime + ", cos= " + Math.cos(totaltime*2*Math.PI) + ", sin = " + (y + raggio*Math.sin(totaltime*2*Math.PI)));
	}

	public void update(float deltaTime) {
		/*updateGravity();
		velocity.add(gravity.x * deltaTime, gravity.y * deltaTime );
		position.add(velocity.x * deltaTime, velocity.y * deltaTime );*/
		//Modificata la posizione del cerchio x migliorare la rotazione di bob
		setCircularPosition(deltaTime, RAGGIO, MainMenuScreen.centrox-15, MainMenuScreen.centroy-150);
		/*velocity.x += gravity.x * deltaTime;
		velocity.y += gravity.y * deltaTime;
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;*/
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		//if(rotationcounter==360)rotationcounter=0;
		/*FIXME
		else rotationcounter++;*/
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