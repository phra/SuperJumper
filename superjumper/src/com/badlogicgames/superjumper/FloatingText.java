package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FloatingText extends Text {
	public static final float DURATION = 1f;
	

	public FloatingText (String string, float duration) {
		super(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4, string);
		if (duration > 0) this.duration = duration;
		else this.duration = DURATION;
	}

	@Override
	public void draw(SpriteBatch batch) {
		Assets.handfontsmall.scale(this.stateTime);
		super.draw(batch);
		Assets.handfontsmall.scale(-this.stateTime);
	}

}
