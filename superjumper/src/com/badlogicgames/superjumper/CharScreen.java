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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CharScreen implements Screen {
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle nextBounds;
	Vector3 touchPoint;
	Rectangle backBounds;
	Rectangle character;
	public static int state=1;
	public CharScreen (Game game) {
		this.game = game;

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		nextBounds = new Rectangle(320 - 64, 0, 64, 64);
		backBounds = new Rectangle(0, 0, 64, 64);
		character= new Rectangle(120, 150,120, 150);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		
	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				return;
			}
			else if (OverlapTester.pointInRectangle(backBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
			else if (OverlapTester.pointInRectangle(character, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
			if(state==0)state=1;
			else state=0;
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
		MainMenuScreen.drawGradient(batcher, Assets.rect, 0, 0, 320, 480,Color.BLACK,Color.BLUE, false);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		Assets.font.draw(batcher, "Choose Character", 29,440);
		Assets.font.draw(batcher, "GO", 280,35);
		Assets.font.draw(batcher, "BACK", 3,35);
		if(state==1)
			batcher.draw(Assets.backgroundRegion, 150,200, 25, 35, 120, 150, 1, 1, 180);
		else
			batcher.draw(Assets.backgroundRegion10, 150,200, 25, 35, 120, 150, 1, 1, 180);
		//batcher.draw(Assets.arrow, 320, 0, -64, 64);
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

	public int state() {
		return state;
	}
	
	@Override
	public void show () {
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
		Assets.backgroundmain4.dispose();
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
