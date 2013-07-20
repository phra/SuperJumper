/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogicgames.superjumper;

import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class FirstScreen implements Screen {
	Game game;
	Spring ruota;
	LinkedList<Text> testo;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle clickBounds;
	Vector3 touchPoint;
	Texture helpImage;
	TextureRegion helpRegion;

	public FirstScreen (Game game) {
		this.game = game;
		this.testo=new LinkedList<Text>();
		Assets.playSound(Assets.soundRocket);
		guiCam = new OrthographicCamera(UI.SCREENWIDTH,UI.SCREENHEIGHT);
		guiCam.position.set(UI.HALFSCREENWIDTH, UI.HALFSCREENHEIGHT, 0);
		this.ruota=new Spring(UI.RUOTAPOSITIONX,UI.RUOTAPOSITIONY);
		this.testo.offer(new Text(UI.FIRSTEXTX, UI.FIRSTEXTY, UI.SCREENWIDTH/2,UI.SCREENHEIGHT, "Welcome"));
		this.testo.offer(new Text(UI.SECONDTEXTX,UI.SECONDTEXTY,UI.SCREENWIDTH,UI.SCREENHEIGHT/2, "To"));
		this.testo.offer(new Text(UI.THIRDTEXTX,UI.THIRDTEXTY,UI.SCREENWIDTH/2,0,"Game"));
		clickBounds = new Rectangle(0, 0, UI.SCREENWIDTH, UI.SCREENHEIGHT);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
	}

	public void update (float deltaTime) {
		ruota.update(deltaTime);
		for(int i=0;i<testo.size();i++){
			testo.get(i).updateAnim(deltaTime);
		}
			if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(clickBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.welcome, 0, 0, UI.SCREENPOSITIONX,UI.SCREENPOSITIONY);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		ruota.draw(batcher, Assets.ruotaRegion,UI.RUOTASIZE,UI.RUOTASIZE);
		Assets.handfontsmall.scale(-UI.FIRSTSCREENTEXTSCALE);
		Assets.handfontsmall.getRegion().getTexture().setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.MipMapLinearNearest);
		for(int i=0;i<testo.size();i++)
		testo.get(i).drawAnim(batcher);
		Assets.handfontsmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Assets.handfontsmall.scale(UI.FIRSTSCREENTEXTSCALE);
		batcher.end();
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void render (float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
