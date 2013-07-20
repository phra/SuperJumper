package com.badlogicgames.superjumper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button extends DynamicGameObject {
	public float stateTime;
	public Texture texture;
	public static float x2=UI.SCREENWIDTH;
	public static float y2=UI.SCREENHEIGHT/2;
	public float x1,y1;

public Vector2 gravity = new Vector2();

public Button (float dstX, float dstY, float srcX, float srcY, Texture texture) {
	super(srcX, srcY, UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
	x1=dstX;	
	y1=dstY;
	this.texture = texture;
	}	

public void update(float deltaTime) {
	velocity.x = (x1 - position.x)*15;
	velocity.y = (y1 - position.y)*15;
	velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
	position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	bounds.x = position.x - bounds.width / 2;
	bounds.y = position.y - bounds.height / 2;
	stateTime += deltaTime;

}

public void draw(SpriteBatch batch) {
	//Gdx.app.debug("DRAWBUTTON", "this.position.x = " + this.position.x + ", this.position.y = " + this.position.y);
	batch.draw(texture,this.position.x,this.position.y,UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
	}

}
