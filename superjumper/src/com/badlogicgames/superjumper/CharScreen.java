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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class CharScreen implements Screen {
	Game game;
	float velX, velY;
	boolean flinging = false;
	float initialScale = 1;
	OrthographicCamera guiCam;
	public final Bob bob;
	public final Bob bobfem;
	public final Bob bobmil;
	public final List<Button> buttons;
	SpriteBatch batcher;
	Rectangle nextBounds;
	Vector3 touchPoint;
	Rectangle backBounds;
	GestureDetector gestureDetector;
	Rectangle character;
	public static int state=1;
	public  int choose=0;
	public  int swipedeactive=0;
	private	float stateTime=0;
	public static float punteggio=0;
	public static float centrox=320/2-40;
	public static float centroy=480/2-60;
	public static float finex=300;
	public static float finey=240;
	public static float iniziox=0;
	public static float inizioy=240;
	public float swipestate=0;

	public CharScreen (Game game) {
		this.game = game;
		this.bob = new Bob(centrox,centroy);
		this.bobfem = new Bob(iniziox-100,inizioy);
		this.bobmil = new Bob(iniziox-150,inizioy);
		this.buttons = new ArrayList<Button>();
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		nextBounds = new Rectangle(320 - 64, 0, 64, 64);
		backBounds = new Rectangle(0, 0, 64, 64);
		character= new Rectangle(120, 150,120, 150);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		this.bob.CHARSCREENUSE=1;
		this.bobfem.CHARSCREENUSE=1;
		this.bobmil.CHARSCREENUSE=1;

		gestureDetector = new GestureDetector(20, 0.5f, 2, 0.15f, new GestureListener() {

			@Override
			public boolean zoom (float initialDistance, float distance) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown (float x, float y, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean tap (float x, float y, int count, int button) {
				//guiCam.zoom=0.2f;
				return false;
			}

			@Override
			public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean pan (float x, float y, float deltaX, float deltaY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean longPress (float x, float y) {
				//guiCam.zoom=1f;
				return false;
			}

			@Override
			public boolean fling (float velocityX, float velocityY, int button) {
				// TODO Auto-generated method stub
				//Gdx.app.debug("x"+velocityX, "y"+velocityY);
				if(Math.abs(velocityX)>Math.abs(velocityY)){
					if(velocityX>0)
					{//Gdx.app.debug("UPDATEGRAVITY", "x="+bob.position.x);
						if(bob.position.x>centrox-20 && bob.position.x<centrox+20) {
							swipedeactive=1;
							swipestate=1;
						}
						if(bob.position.x<=iniziox ) {
							swipestate=2;
						}
						if(bob.position.x>=finex )	{
							swipestate=5;
						}
					}
					else if (velocityX<0) {
						//Gdx.app.debug("UPDATEGRAVITY", "x="+bob.position.x);

						if(bob.position.x>=finex ) {
							swipestate=4;
						}
						if(bobfem.position.x>=finex )	{
							swipestate=6;
						}
					}

					else {
						// Do nothing.
					}
				}else{

					// Ignore the input, because we don't care about up/down swipes.
				}
				return true; 

			}
		});
		Gdx.input.setInputProcessor(gestureDetector);

	}

	public void update (float deltaTime) {

		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y) && state==0 && punteggio>20000 
				|| (OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y) && state==1)||
				(OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y) && state==2 && punteggio>40000 )) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				return;
			}
			else if (OverlapTester.pointInRectangle(backBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
			/*else if (OverlapTester.pointInRectangle(character, touchPoint.x, touchPoint.y)) {
				//Assets.playSound(Assets.clickSound);
				if(state==0)state=1;
				else state=0;
				return;
			}*/
		}
		bob.update(deltaTime);
		bobfem.update(deltaTime);
		bobmil.update(deltaTime);
		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button=buttons.get(i);
			button.update(deltaTime);
		}
		//updateScore();//controllo quanti punti sono stati conquistati
		if(bob.position.x<320&&bob.position.x>0)state=1;
		else if(bobfem.position.x<320&&bobfem.position.x>0)state=0;
		else if(bobmil.position.x<320&&bobmil.position.x>0)state=2;
		if(swipestate==1)
		{  
			sposto(bob,finex+100,finey);
			sposto(bobfem,centrox,centroy);
		}
		else if(swipestate==2)
		{
			sposto(bob,centrox,centroy);
		}
		else if(swipestate==4)
		{
			sposto(bob,centrox,centroy);
			sposto(bobfem,iniziox-100,inizioy);
		}
		else if(swipestate==5)
		{ 
			sposto(bobfem,finex+100,finey);
			sposto(bobmil,centrox-30,centroy);
		}
		else if(swipestate==6)
		{
			sposto(bobfem,centrox,centroy);
			sposto(bobmil,iniziox-150,inizioy);
		}
	}
	public void sposto(DynamicGameObject alien,float x,float y){
		alien.velocity.x=(x-alien.position.x)*10;
		alien.velocity.y=(y-alien.position.y)*10;
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.choose,0,0,512,512);
		//MainMenuScreen.drawGradient(batcher, Assets.rect, 0, 0, 320, 480,Color.BLACK,Color.BLUE, false);
		batcher.end();
		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.icontext,275,10,45,45);
		batcher.draw(Assets.icontextback,0,10,45,45);
		//int len = buttons.size();
		/*
		for (int i = 0; i < len; i++) {
			Button button = buttons.get(i);
			Texture keyFrame =Assets.lock;
			if(i==1)keyFrame=Assets.locked;
			//batcher.draw(keyFrame,button.position.x,button.position.y,145,145);
		}*/

		for (Button button : buttons) {
			button.draw(batcher);
		}

		Assets.fontsmall.draw(batcher, "GO", 285,40);
		stateTime=stateTime+0.020f;
		TextureRegion keyFrame1 = Assets.swipeAnim.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);
		if(stateTime>4)stateTime=0;
		if(swipedeactive==0)
		{
			batcher.draw(Assets.swipetext,10,0,320,256);
			batcher.draw(keyFrame1,10,0,320,256);
		}
		batcher.draw(Assets.backgroundRegion,bob.position.x ,bob.position.y ,130,130);
		batcher.draw(Assets.backgroundRegion10,bobfem.position.x ,bobfem.position.y ,130,130);
		batcher.draw(Assets.backgroundRegion11,bobmil.position.x-20 ,bobmil.position.y ,170,170);
		if(punteggio<20000 && state==0)
		{
			batcher.draw(Assets.lock,bobfem.position.x-10 ,bobfem.position.y+30 ,100,100);
			batcher.draw(Assets.locked,bobfem.position.x-45,bobfem.position.y-70 ,170,150);
			Assets.fontsmall.draw(batcher, "need 20000 scores", guiCam.position.x-90,guiCam.position.y-150);
		}
		if(punteggio<40000 && state==2)
		{
			batcher.draw(Assets.lock,bobmil.position.x+26 ,bobmil.position.y+30 ,85,100);
			batcher.draw(Assets.locked,bobmil.position.x-18,bobmil.position.y-70 ,170,150);
			Assets.fontsmall.draw(batcher, "need 40000 scores", guiCam.position.x-90,guiCam.position.y-150);
		}

		//batcher.draw(Assets.backgroundRegion,bob.position.x ,bob.position.y ,25, 35, 120, 150, 1, 1, 180);
		//batcher.draw(Assets.backgroundRegion10,bobfem.position.x ,bobfem.position.y ,25, 35, 120, 150, 1, 1, 180);
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
	/*
	private void updateScore(){
		for (int i = 0; i < 5; i++) {
			if (punteggio<Settings.highscores[i])punteggio=Settings.highscores[i];
		}
	}*/


}
