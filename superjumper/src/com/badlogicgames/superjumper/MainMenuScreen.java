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
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
	Sprite sprite;
	Game game;
	public final BobMain bob;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle multiplayerBounds;
	Rectangle highscoresBounds;
	Rectangle helpBounds;
	Vector3 touchPoint;
	final static int centrox = 320/2;
	final static int centroy = 480/2;


	public MainMenuScreen (Game game) {
		this.game = game;
		this.bob = new BobMain(centrox, centroy);
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		sprite= new Sprite();
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(147, 232, 79, 47);
		multiplayerBounds = new Rectangle(38,163,68, 69);
		highscoresBounds = new Rectangle(77, 102, 69, 66);
		helpBounds = new Rectangle(154, 152, 69, 60);
		touchPoint = new Vector3();

	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
			} else if (OverlapTester.pointInRectangle(multiplayerBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MultiplayerScreen(game));
			} else if (OverlapTester.pointInRectangle(highscoresBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HighscoresScreen(game));
			} else if (OverlapTester.pointInRectangle(helpBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HelpScreen(game));
			} else if (OverlapTester.pointInRectangle(soundBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.soundEnabled = !Settings.soundEnabled;
				if (Settings.soundEnabled)
					Assets.music.play();
				else
					Assets.music.pause();
			}

		}
		updatebob(deltaTime);

	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.backgroundRegionmain, 0, 0, 340, 480);

		batcher.end();
		batcher.enableBlending();
		batcher.begin();

		//TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.deltaTime, Animation.ANIMATION_LOOPING);

		//batcher.draw(keyFrame,10, 20, 1.5f, 1.5f);

		/*batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);*/
		//batcher.draw(Assets.mainMenu, 10, 200 - 110, 300, 240);
		batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 54, 44);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		TextureRegion keyFrame;

		keyFrame = Assets.bobJump.getKeyFrame(0.2f, Animation.ANIMATION_LOOPING);
		//Gdx.app.debug("Animation", "position"+bob.position.x+" "+bob.position.y);

		/*if(bob.position.y>50) {

			batcher.draw(keyFrame, bob.position.x,bob.position.y, 25, 35, 50, 70, 1, 1, bob.rotationcounter);
		}
		else batcher.draw(keyFrame, bob.position.x, bob.position.y, 50, 70);*/
		batcher.draw(keyFrame, bob.position.x,bob.position.y, 25, 35, 50, 70, 1, 1, bob.rotationcounter);

		batcher.end();
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

	private void updatebob(float deltaTime){
		//	bob.setGravityBob(0,0);
		//if(bob.position.y>480/10)bob.velocity.y=100;
		//bob.velocity.set(13, 13);
		bob.update(deltaTime);
	}

	@Override
	public void hide () {
	}

	@Override
	public void pause () {
		Settings.save();
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}