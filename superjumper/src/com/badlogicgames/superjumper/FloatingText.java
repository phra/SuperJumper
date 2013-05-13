package com.badlogicgames.superjumper;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FloatingText extends Text {
	public static final float DURATION = 1f;
	private static final Random constrand = new Random();


	public FloatingText (String string, float duration) {
		super( Gdx.graphics.getWidth()/2f + (((Gdx.graphics.getWidth()/4f)*constrand.nextFloat())* (constrand.nextFloat() > 0.5f ? 1 : -1)), 
			Gdx.graphics.getHeight()/2f + (((Gdx.graphics.getHeight()/4f)*constrand.nextFloat()) * (constrand.nextFloat() > 0.5f ? 1 : -1)), string);
		if (duration > 0) this.duration = duration;
		else this.duration = DURATION;
	}

	public FloatingText (float x, float y, String string, float duration) {
		super(x, y, string);
		if (duration > 0) this.duration = duration;
		else this.duration = DURATION;
	}

	@Override
	public void draw(SpriteBatch batch) {
		Assets.handfontsmall.scale(this.stateTime);
		super.draw(batch);
		Assets.handfontsmall.scale(-this.stateTime);
//		Gdx.app.debug("drawtext", "position.x = " + position.x + ", position.y = " + position.y);
	}

}
