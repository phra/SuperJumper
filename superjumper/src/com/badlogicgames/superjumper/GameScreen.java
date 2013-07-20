package com.badlogicgames.superjumper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogicgames.superjumper.World.WorldListener;

public class GameScreen implements Screen, CONSTANTS {

	GestureDetector gestureDetector ;
	FingerControl control = new FingerControl();
	Game game;
	int state;
	OrthographicCamera guiCam;
	Vector3 touchPoint;
	SpriteBatch batcher;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	Rectangle missileBounds;
	Rectangle nosBounds;
	Rectangle bubbleBounds;
	int lastScore;
	float statexplosion=0;
	String scoreString;
	private Random rand =  new Random(); //FIXME
	private boolean missileON = false;


	public GameScreen (final Game game) {
		this.game = game;
		state = GAME_READY;
		guiCam = new OrthographicCamera(UI.SCREENWIDTH, UI.SCREENHEIGHT);
		guiCam.position.set(UI.HALFSCREENWIDTH, UI.HALFSCREENHEIGHT, 0);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		world = new World();
		renderer = new WorldRenderer(batcher, world, guiCam);
		pauseBounds = new Rectangle(UI.POSITIONPAUSEX - UI.INDICATORSIZE/2 , UI.POSITIONPAUSEY  - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		resumeBounds = new Rectangle(UI.BUTTONRESUMEPOSITIONX - UI.BUTTONWIDTH/2, UI.BUTTONRESUMEPOSITIONY - UI.BUTTONHEIGHT/2, UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
		quitBounds = new Rectangle(UI.BUTTONQUITPOSITIONX - UI.BUTTONWIDTH/2 , UI.BUTTONQUITPOSITIONY - UI.BUTTONHEIGHT/2, UI.BUTTONWIDTH, UI.BUTTONHEIGHT);
		missileBounds = new Rectangle(UI.MISSILEPOSITIONX - UI.INDICATORSIZE/2, UI.MISSILEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		nosBounds = new Rectangle(UI.SUPERMISSILEPOSITIONX - UI.INDICATORSIZE/2, UI.SUPERMISSILEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		bubbleBounds = new Rectangle(UI.BUBBLEPOSITIONX - UI.INDICATORSIZE/2, UI.BUBBLEPOSITIONY - UI.INDICATORSIZE/2, UI.INDICATORSIZE, UI.INDICATORSIZE);
		scoreString = "SCORE: 0";
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
				guiCam.unproject(touchPoint.set(x, y, 0));
				Gdx.app.debug("TAP", "touchPoint.x = " + touchPoint.x + "touchPoint.y = " + touchPoint.y + ", worldstate = " + world.state);
				switch (world.state) {

				case CONSTANTS.GAME_RUNNING:
					if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) {
						pause();
					}
					if (world.supermissiles > 0 && OverlapTester.pointInRectangle(nosBounds, touchPoint.x, touchPoint.y)) {
						Assets.playSound(Assets.soundRocket);
						if (--world.supermissiles <= 0) world.supermissileButton = false;
						if (!world.enemies.isEmpty()) world.projectiles.add(new SuperMissile(world.bob.position.x, world.bob.position.y, SuperMissile.WIDTH, SuperMissile.HEIGHT, world.enemies.peek(),world.projectiles,world.enemies));

					}
					else if (world.bubbleButton == true && OverlapTester.pointInRectangle(bubbleBounds, touchPoint.x, touchPoint.y)) {
						Assets.playSound(Assets.soundBubble);
						world.bob.enablebubble = true;
						world.bubbleButton = false;
						world.bob.bubbletime = world.bob.stateTime;
					}
					else if (world.missiles > 0 && OverlapTester.pointInRectangle(missileBounds, touchPoint.x, touchPoint.y)) {
						//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
						Assets.playSound(Assets.soundRocket);
						if (--world.missiles <= 0) world.activemissile = false;
						if (!world.enemies.isEmpty()) world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, Missile.WIDTH, Missile.HEIGHT, world.enemies.peek()));

					}  else {
						world.ShotProjectile();
					}
					break;

				case CONSTANTS.GAME_OVER:
					if (world.score > Settings.highscores[4]){
						world.scoretext.update(0, "NEW HIGHSCORE: " + world.score);
						Settings.addScore(world.score);
						Settings.save();
					}
					Gdx.input.setInputProcessor(null);
					game.setScreen(new MainMenuScreen(game));
					break;

				case CONSTANTS.GAME_LEVEL_END:
					world = new World();
					renderer = new WorldRenderer(batcher, world, guiCam);
					world.state = GAME_READY;
					break;

				case CONSTANTS.GAME_PAUSED:
					guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
					for (Button b : world.buttons) {
						Assets.playSound(Assets.soundRocket);
						if (OverlapTester.pointInRectangle(b.bounds, touchPoint.x, touchPoint.y)) {
//							Assets.playSound(Assets.soundClick);
							if (b.texture == Assets.resume){
								Gdx.app.debug("TAP", "RESUME");
								world.state = GAME_RUNNING;
							} else if (b.texture == Assets.quit){
								world.state = CONSTANTS.GAME_RUNNING;
								game.setScreen(new MainMenuScreen(game));
							}
						}
					}
					break;

				case CONSTANTS.GAME_READY:
					break;
				

				}
				return true;
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
				if (world.state == CONSTANTS.GAME_RUNNING) {
					if(Math.abs(velocityX)>Math.abs(velocityY)){
						if(velocityX > 20) {
							Gdx.app.debug("fling", "trascino sx");

						} else if (velocityX < -20) {
							Gdx.app.debug("fling", "trascino dx");
						}	
					} else {
						if(velocityY > 20) {
							Gdx.app.debug("fling", "trascino giu");
							Assets.playSound(Assets.soundBulletime);
							if(!world.decrementonos){
								world.texts.offer(new FloatingText("BULLET TIME!",0.5f));
								world.freezeON = true;
								world.decremento=true;}
							if(world.decrementonos)
							{
								world.decrementonos=false;
								world.TurboLess();
							}
						} else if (velocityY < -20) {
							Gdx.app.debug("fling", "trascino su");
							if(!world.freezeON){
								world.decrementonos=true;
								Assets.playSound(Assets.soundRocket);
								world.texts.offer(new FloatingText("NOS!",2f));
								}
							world.freezeON = false;
							world.decremento=false;
						}
						// Ignore the input, because we don't care about up/down swipes.
					}
				}
				return true;
			}
		});
		Gdx.input.setInputProcessor(gestureDetector);
	}

	public void update (float deltaTime) {
//		if (deltaTime > 0.1f) deltaTime = 0.1f;
		switch (world.state) {

		case GAME_READY:
			world.update(deltaTime, Gdx.input.getAccelerometerX());
			break;

		case GAME_RUNNING:
			if(!world.buttons.isEmpty())
			for (int i=0;i<world.buttons.size();i++) {
			world.buttons.remove(i);}
			ApplicationType appType = Gdx.app.getType();
			//if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
			if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
				world.update(deltaTime, Gdx.input.getAccelerometerX());
			} else {
				float accel = 0;
				if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A)) accel = 5f;
				if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) accel = -5f;
				if (Gdx.input.isKeyPressed(Keys.SPACE)) world.ShotProjectile();
				world.update(deltaTime, accel);
			}
			break;

		case GAME_PAUSED:
			for(int i=0;i<world.buttons.size();i++)
				world.buttons.get(i).update(deltaTime);
			break;

		case GAME_LEVEL_END:

			break;
		case GAME_OVER:
			break;
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render();
		guiCam.update();
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
		Assets.RectBlack();
		Assets.RectWhite();
		Assets.load();
		render(0);
}

	@Override
	public void hide () {
		//Settings.addScore(world.score);
		//Settings.save();
	}

	@Override
	public void pause () {
		world.buttons.add(new Button(UI.BUTTONRESUMEPOSITIONX ,UI.BUTTONRESUMEPOSITIONY ,UI.SCREENWIDTH,UI.SCREENHEIGHT/2-50 ,Assets.resume));
		world.buttons.add(new Button(UI.BUTTONQUITPOSITIONX,UI.BUTTONQUITPOSITIONY ,UI.SCREENWIDTH,UI.SCREENHEIGHT/2-50 ,Assets.quit));
		Assets.playSound(Assets.clickSound);
		world.state = CONSTANTS.GAME_PAUSED;
	}

	@Override
	public void resume () {
		Assets.RectBlack();
		Assets.RectWhite();
	}

	@Override
	public void dispose () {
	}
}