package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogicgames.superjumper.Assets;
import com.badlogicgames.superjumper.DynamicGameObject;

public class Text extends DynamicGameObject {
	public static final float DURATION = -1f;
	public String string;
	public float stateTime;
	public float duration;
	public float dstX,dstY;
	public Vector2 gravity = new Vector2();

	public Text (float x, float y, String string) {
		super(x, y, 1, 1);
		this.string = string;
		this.stateTime = 0;
		this.duration = DURATION;
	}
	
	public Text (float dstX, float dstY,float srcX ,float srcY, String string) {
		super(srcX, srcY, 1, 1);
		this.dstX=dstX;
		this.dstY=dstY;
		this.string = string;
		this.stateTime = 0;
		this.duration = DURATION;
	}

	public void draw(SpriteBatch batch) {
		Assets.handfontsmall.scale(UI.TEXTSCALE);
		//Assets.handfontsmall.setScale(UI.TEXTSCALEX,UI.TEXTSCALEY);
		 Assets.handfontsmall.getBounds(string);
		float width = Assets.handfontsmall.getBounds(string).width; //Get the width of the text we draw using the current font
		float height = Assets.handfontsmall.getBounds(string).height; //Get the height of the text we draw using the current font
		Assets.handfontsmall.draw(batch,string,position.x-width/2,position.y-height/2);
		//Gdx.app.debug("drawtext", "position.x = " + position.x + ", position.y = " + position.y + ", width = " + width + ", height = " + height);
		Assets.handfontsmall.scale(-UI.TEXTSCALE);
		//Assets.handfontsmall.setScale(-UI.TEXTSCALEX,-UI.TEXTSCALEY);
	}
	
	public void drawAnim(SpriteBatch batch){
		Assets.handfontsmall.draw(batch,string,position.x,position.y);
	}

	public void update(float deltaTime) {
		this.stateTime += deltaTime;
	}

	public void update(float deltaTime, String newtext) {
		this.update(deltaTime);
		this.string = newtext;
	}
	
	public void updateAnim(float deltaTime) {
		velocity.x = (dstX - position.x)*8;
		velocity.y = (dstY - position.y)*8;
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		//velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
		
		this.update(deltaTime);
	}
	}

