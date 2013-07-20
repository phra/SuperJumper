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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
	public final List<Star> stars;
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
	public final Random rand;
	private	float stateTime=0;
	private static float[] verts = new float[20];

	public MainMenuScreen (Game game) {
		this.game = game;
		this.rand = new Random();
		this.stars = new ArrayList<Star>();
		this.bob = new BobMain(centrox, centroy);
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		sprite= new Sprite();
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(149, 195, 86, 58);
		multiplayerBounds = new Rectangle(37,124,70, 90);
		highscoresBounds = new Rectangle(78, 67, 75, 85);
		helpBounds = new Rectangle(156, 125, 75, 50);
		touchPoint = new Vector3();


	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new CharScreen(game));
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
		int type_star = Star.STAR_TYPE_STATIC;//star
		float y_star = rand.nextFloat() *480;//star
		float x_star = rand.nextFloat() *320;//star
		if(stars.size()<1000){
		Star star = new Star(type_star, x_star, y_star);//star
		stars.add(star);//star
		}

	}

	public void draw (float deltaTime) {
		
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		drawGradient(batcher, Assets.rect, 0, 0, 320, 480,Color.BLACK,Color.BLUE, false);
		//batcher.draw(Assets.backgroundRegionmain, 0, 0, 340, 480);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		int len = stars.size();
		for (int i = 0; i < len; i++) {
			Star star = stars.get(i);
			TextureRegion keyFrame2 = Assets.starRegion;
			batcher.draw(keyFrame2, star.position.x , star.position.y , 5, 5);
		}
		batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 46, 46);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		TextureRegion keyFrame1;
		stateTime=stateTime+0.015f;
		keyFrame1 = Assets.backAnim.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
		if(stateTime>2)stateTime=0;
		TextureRegion keyFrame;
		keyFrame = Assets.bobJump.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
		//Gdx.app.debug("Animation", "position"+bob.position.x+" "+bob.position.y);
		batcher.draw(keyFrame1,0, 0, 320, 480);
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
		Settings.load();
	}

	@Override
	public void dispose () {
	}
	public static void drawGradient(SpriteBatch batch, TextureRegion tex, float x, float y,
		float width, float height, Color a, Color b, boolean horiz) {
		float ca = a.toFloatBits();
		float cb = b.toFloatBits();

		int idx = 0;
		verts[idx++] = x;
		verts[idx++] = y;
		verts[idx++] = horiz ? ca : cb; // bottom left
		verts[idx++] = tex.getU(); //NOTE: texture coords origin is top left
		verts[idx++] = tex.getV2();

		verts[idx++] = x;
		verts[idx++] = y + height;
		verts[idx++] = ca; // top left
		verts[idx++] = tex.getU();
		verts[idx++] = tex.getV();

		verts[idx++] = x + width;
		verts[idx++] = y + height;
		verts[idx++] = horiz ? cb : ca; // top right
		verts[idx++] = tex.getU2();
		verts[idx++] = tex.getV();

		verts[idx++] = x + width;
		verts[idx++] = y;
		verts[idx++] = cb; // bottom right
		verts[idx++] = tex.getU2();
		verts[idx++] = tex.getV2();

		batch.draw(tex.getTexture(), verts, 0, verts.length);
	}
}