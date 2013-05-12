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

public class GameScreen implements Screen {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	public final List<Button> buttons;
	GestureDetector gestureDetector ;
	FingerControl control=new FingerControl();
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
	/*public static boolean attivatraj=false;
	public LinkedList<Vector2> traiettoria;
	private boolean prectouch = false;*/

	public GameScreen (Game game) {
		this.game = game;
		this.buttons = new ArrayList<Button>();
		state = GAME_READY;
		guiCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		guiCam.position.set(320 / 2, 480 / 2, 0);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		worldListener = new WorldListener() {
			@Override
			public void jump () {
				Assets.playSound(Assets.jumpSound);
			}

			@Override
			public void highJump () {
				Assets.playSound(Assets.highJumpSound);
			}

			@Override
			public void hit () {
				Assets.playSound(Assets.hitSound);
			}

			@Override
			public void coin () {
				Assets.playSound(Assets.coinSound);
			}

			@Override
			public void life () {
				// TODO Auto-generated method stub

			}

			@Override
			public void projectile () {
				// TODO Auto-generated method stub

			}


		};
		world = new World(worldListener);
		renderer = new WorldRenderer(batcher, world, guiCam);
		pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
		quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
		missileBounds = new Rectangle(320 - 64, 480 - 320, 64, 64);
		nosBounds = new Rectangle(320 - 64, 480 - 400, 64, 64);
		bubbleBounds = new Rectangle(320 - 64, 12, 64, 64);
		lastScore = 0;
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
				if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) {
					pause();
				}
				if (world.supermissiles > 0 && OverlapTester.pointInRectangle(nosBounds, touchPoint.x, touchPoint.y)) {
					//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando su");
					//world.nosActivate();
					if (--world.supermissiles <= 0) world.supermissileButton = false;
					if (!world.enemies.isEmpty()) world.projectiles.add(new SuperMissile(world.bob.position.x, world.bob.position.y, SuperMissile.WIDTH, SuperMissile.HEIGHT, world.enemies.peek(),world.projectiles,world.enemies));

				}
				else if (world.bubbleButton == true && OverlapTester.pointInRectangle(bubbleBounds, touchPoint.x, touchPoint.y)) {
					//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
					//world.bubbleActivate();
					world.bob.enablebubble = true;
					world.bubbleButton = false;
					world.bob.bubbletime = world.bob.stateTime;
				}
				else if (world.missiles > 0 && OverlapTester.pointInRectangle(missileBounds, touchPoint.x, touchPoint.y)) {
					//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
					//attivatraj=true;
					//this.missileON = true;
					if (--world.missiles <= 0) world.activemissile = false;
					if (!world.enemies.isEmpty()) world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, Missile.WIDTH, Missile.HEIGHT, world.enemies.peek()));

				} /*else if (this.missileON) {
						int i = 0;
						if (OverlapTester.pointInRectangle(world.charlie.bounds, touchPoint.x, touchPoint.y)){
							world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, world.charlie));
							this.missileON = false;
						} else {
							for (Platform plat : world.platforms){
								if ((OverlapTester.pointInRectangle(plat.bounds, touchPoint.x, touchPoint.y))) {
									world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, plat));
									this.missileON = false;
									break;
								}
							}
						}
					}*/ else {
						world.ShotProjectile();
					}


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
					if(velocityX > 0) {
						Gdx.app.debug("fling", "trascino sx");

					} else {
						Gdx.app.debug("fling", "trascino dx");
					}	
				} else {
					if(velocityY > 20) {
						Gdx.app.debug("fling", "trascino giù");
						
						
						if(!world.decrementonos){
							world.signal2screen=14;
							world.freezeON = true;
							world.decremento=true;}
						if(world.decrementonos)
						{
							world.decrementonos=false;
							world.TurboLess();
						}
					} else if (velocityY < 20) {
						Gdx.app.debug("fling", "trascino su");
						if(!world.freezeON)world.decrementonos=true;
						world.freezeON = false;
						world.decremento=false;
					}
					// Ignore the input, because we don't care about up/down swipes.
				}
				return true;
			}
		});
		Gdx.input.setInputProcessor(gestureDetector);
		//traiettoria = new LinkedList<Vector2>();
	}

	public void update (float deltaTime) {
		if (deltaTime > 0.1f) deltaTime = 0.1f;
		switch (state) {
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused(deltaTime);
			break;
		case GAME_LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateReady () {
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
			//World.setGravity(0, 1);
		}
	}

	private void updateRunning (float deltaTime) {

		/* else {
			if (attivatraj && Gdx.input.isTouched()) { 
				this.prectouch = true;
				traiettoria.offer(new Vector2((Gdx.input.getX()*World.WORLD_WIDTH)/320,((Gdx.input.getY()*World.WORLD_HEIGHT)/480)+world.bob.position.y));
				Gdx.app.debug("ISTOUCHED", "position.y: " + Gdx.input.getY() + " position.x: " + Gdx.input.getX());
			} else if (this.prectouch == true) {
				attivatraj = false;
				this.prectouch = false;
				//FIXME projectiles o rockets?
				world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, world.charlie , traiettoria));
				traiettoria = new LinkedList<Vector2>();
			}
		}*/
		//FIXME
		
		ApplicationType appType = Gdx.app.getType();
		// should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
			//if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
			world.update(deltaTime, Gdx.input.getAccelerometerX());
		} else {
			float accel = 0;
			if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A)) accel = 5f;
			if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) accel = -5f;
			if (Gdx.input.isKeyPressed(Keys.SPACE)) world.ShotProjectile();
			world.update(deltaTime, accel);
		}
		if (world.score != lastScore) {
			lastScore = world.score;
			scoreString = "SCORE: " + lastScore;
		}
		if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
			state = GAME_LEVEL_END;
		}
		if (world.state == World.WORLD_STATE_GAME_OVER) {
			state = GAME_OVER;
			if (lastScore >= Settings.highscores[4])
				scoreString = "NEW HIGHSCORE: " + lastScore;
			else
				scoreString = "SCORE:" + lastScore;
			Settings.addScore(lastScore);
			Settings.save();
		}
	}

	private void updatePaused (float deltaTime) {
		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button=buttons.get(i);
			button.update(deltaTime);
		}
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				state = GAME_RUNNING;
				return;
			}
			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	private void updateLevelEnd () {
		if (Gdx.input.justTouched()) {
			world = new World(worldListener);
			renderer = new WorldRenderer(batcher, world, guiCam);
			world.score = lastScore;
			state = GAME_READY;
		}
	}

	private void updateGameOver () {
		if (Gdx.input.justTouched()) {
			game.setScreen(new MainMenuScreen(game));

		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl20;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render();
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.enableBlending();
		batcher.begin();
		switch (state) {
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_LEVEL_END:
			presentLevelEnd();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		batcher.end();
	}

	private void presentReady () {
		Assets.handfontsmall.draw(batcher, "R E A D Y ?", 160-105, 240-18);
		//batcher.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
	}

	private void presentRunning () {
		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button=buttons.get(i);
			buttons.remove(button);
			len = buttons.size();
		}
	
		
		batcher.draw(Assets.pause, 320 - 49, 480 - 53, 44, 44);
		{
			batcher.draw(Assets.tmprectwhite, 20, 48, 12, 90);
			batcher.draw(Assets.tmprectwhite, 36.5f, 48, 12, 90);
			batcher.draw(Assets.tmprectblack, 20, 48, 12, 5.6f*world.level.constant);
			batcher.draw(Assets.tmprectblack, 36.5f, 48, 12,  5.6f*world.levelnos.constant);
			}
		batcher.draw(Assets.level, -53, 480 - 479, 190, 170);
		//Assets.fontsmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//Assets.fontsmall.scale(3f);explosion text 
		batcher.draw(Assets.tubo, 0, 225, 250, 280);
		Assets.handfontsmaller.draw(batcher, scoreString, 63, 480 - 22);
		String scoreproj;
		scoreproj = world.shot+"x ";
		Assets.handfontsmaller.draw(batcher, scoreproj, 282, 480 - 145);
		batcher.draw(Assets.portaproj, 276, 480 - 150, 35, 35);
		String scorelife;
		scorelife = world.life+"x ";
		Assets.handfontsmaller.draw(batcher, scorelife, 282, 480 - 90);
		batcher.draw(Assets.portalife, 276, 480 - 95, 35, 35);
		//controlLockCharacter();

	}   

	private void controlLockCharacter()
	{ 
		if (world.signal2screen==1)
		{
			stampo("+30 ammo");
		}
		else if (world.signal2screen==2)
		{
			stampo("+1 life");
		}
		else    if (world.signal2screen==3)
		{
			stampo("-1 warning");
		}
		else if (world.signal2screen==4)
		{
			stampo("slow!");
			if(statexplosion==0)
				world.signal2screen=10;
		}
		else    if (world.signal2screen==5)
		{
			stampo("good!");
			if(statexplosion==0)
				world.signal2screen=11;
		}
		else    if (world.signal2screen==6)
		{
			stampo("fast!");
			if(statexplosion==0)
				world.signal2screen=12;
		}
		else    if (world.signal2screen==7)
		{
			stampo("excellent!");
			if(statexplosion==0)
				world.signal2screen=13;
		}
		else    if (world.signal2screen==8 )
		{
			stampo("new alien");
			world.print1times=2;
		}
		else    if (world.signal2screen==9 )
		{
			stampo("new alien");
			world.print1times=3;
		}
		else    if (world.signal2screen==10)
		{
			stampo("+100");
		}
		else    if (world.signal2screen==11)
		{
			stampo("+200");
		}
		else    if (world.signal2screen==12)
		{
			stampo("+500");
		}
		else    if (world.signal2screen==13)
		{
			stampo("+1000");
		}
		else    if (world.signal2screen==14)
		{
			stampo("Time freeze!!");
			world.print1times=1;
		}

	}
	private void stampo(String explosion)
	{
		Assets.handfontsmall.scale(0.03f);
		Assets.handfontsmall.draw(batcher, explosion, guiCam.position.x-75,guiCam.position.y);
		statexplosion+=1f;
		if(statexplosion==43 )
		{
			Assets.handfontsmall.scale(-0.03f*43);
			statexplosion=0;
			world.signal2screen=0;
		}
	}

	private void presentPaused () {
		batcher.disableBlending();
		//MainMenuScreen.drawGradient(batcher, Assets.rect, 0, 0, 320, 480,Color.BLACK,Assets.colore, false);
		batcher.draw(Assets.welcomepaused,0,0,512,512);
		batcher.enableBlending();
		//Assets.font.draw(batcher, "R e s u m e",160 - 85, 265);
		//Assets.font.draw(batcher, "Q u i t",160 - 45, 230 );
		//batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 240 - 96 / 2, 192, 96);
		//Assets.font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//Assets.font.draw(batcher, scoreString, 18, 480 - 10);
		Assets.handfontsmall.scale(-0.4f);
		Assets.handfontsmall.draw(batcher, scoreString, 150, 480);
		int len = buttons.size();
		for (int i = 0; i < len; i++) {
			Button button = buttons.get(i);
			Texture keyFrame =Assets.resume;
			if(i==1)keyFrame=Assets.quit;
			batcher.draw(keyFrame,button.position.x,button.position.y,145,145);
		}
		Assets.handfontsmall.scale(0.4f);
	}

	private void presentLevelEnd () {

		String topText = "your friends ...";
		String bottomText = "... aren't here!";
		float topWidth = Assets.font.getBounds(topText).width;
		float bottomWidth = Assets.font.getBounds(bottomText).width;
		Assets.handfontsmall.draw(batcher, topText, 160 - topWidth / 2, 480 - 40);
		Assets.handfontsmall.draw(batcher, bottomText, 160 - bottomWidth / 2, 40);

	}

	private void presentGameOver () {
		Assets.handfontsmall.scale(-0.3f);
		Assets.handfontsmall.draw(batcher, "G A M E  O V E R",160 - 200 / 2, 300);
		//batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
		float scoreWidth = Assets.font.getBounds(scoreString).width;
		Assets.handfontsmall.draw(batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);
		Assets.handfontsmall.scale(0.3f);
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
		//Settings.addScore(world.score);
		//Settings.save();
	}

	@Override
	public void pause () {
		Settings.addScore(world.score);
		Settings.save();
		Assets.playSound(Assets.clickSound);
		Button buttone = new Button(90,230);
		buttons.add(buttone);
		Button buttones = new Button(88,180);
		buttons.add(buttones);
		state = GAME_PAUSED;
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