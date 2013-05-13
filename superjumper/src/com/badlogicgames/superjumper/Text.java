package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogicgames.superjumper.Assets;
import com.badlogicgames.superjumper.DynamicGameObject;

public class Text extends DynamicGameObject {
	public static final float DURATION = -1f;
	public String string;
	public float stateTime;
	public float duration;


	public Text (float x, float y, String string) {
		super(x, y, 1, 1);
		this.string = string;
		this.stateTime = 0;
		this.duration = DURATION;
	}

	public void draw(SpriteBatch batch) {
		Assets.handfontsmall.scale(UI.TEXTSCALE);
		float width = Assets.handfontsmall.getBounds(string).width; //Get the width of the text we draw using the current font
		float height = Assets.handfontsmall.getBounds(string).height; //Get the height of the text we draw using the current font
		Assets.handfontsmall.draw(batch,string,position.x-width/2,position.y-height/2);
		//Gdx.app.debug("drawtext", "position.x = " + position.x + ", position.y = " + position.y + ", width = " + width + ", height = " + height);
		Assets.handfontsmall.scale(-UI.TEXTSCALE);
	}

	public void update(float deltaTime) {
		this.stateTime += deltaTime;
	}

	public void update(float deltaTime, String newtext) {
		this.update(deltaTime);
		this.string = newtext;
	}
}
