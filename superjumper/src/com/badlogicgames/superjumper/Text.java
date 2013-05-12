package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Text extends DynamicGameObject {
	public String string;
	public float stateTime;
	public static final float DURATION = 1000f;
	public final float duration;
	private final GameObject obj;
	

	public Text (float x, float y, String string, float duration, GameObject obj) {
		super(x, y, 0, 0);
		this.string = string;
		this.stateTime = 0;
		if (duration > 0) this.duration = duration;
		else this.duration = DURATION;
		this.obj = obj;
	}

	public void draw(SpriteBatch batch) {
		Assets.handfontsmall.scale(-0.5f);
		float width = Assets.handfontsmall.getBounds(string).width; //Get the width of the text we draw using the current font
		float height = Assets.handfontsmall.getBounds(string).height; //Get the height of the text we draw using the current font
		Assets.handfontsmall.draw(batch,string,position.x-width/2,position.y-height/2);
		Gdx.app.debug("drawtext", "position.x = " + position.x + ", position.y = " + position.y + ", width = " + width + ", height = " + height);
		//Assets.handfontsmall.draw(batch, this.string, this.position.x, this.position.y);
		Assets.handfontsmall.scale(0.5f);

		
	}

	public void update(float deltaTime) {
		stateTime += deltaTime/100;
		//this.position.x = this.obj.position.x;
		this.position.y = this.obj.position.y + 50f;
	}
}
