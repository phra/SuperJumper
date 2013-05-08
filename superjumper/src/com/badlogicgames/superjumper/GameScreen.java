package com.badlogicgames.superjumper;
/*CONTROLLER*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	/*public static boolean attivatraj=false;
	public LinkedList<Vector2> traiettoria;
	private boolean prectouch = false;*/

	public GameScreen (Game game) {
		this.game = game;
		this.buttons = new ArrayList<Button>();
		state = GAME_READY;
		guiCam = new OrthographicCamera(320, 480);
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
		renderer = new WorldRenderer(batcher, world);
		pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
		quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
		missileBounds = new Rectangle(320 - 64, 480 - 320, 64, 64);
		nosBounds = new Rectangle(320 - 64, 480 - 400, 64, 64);
		bubbleBounds = new Rectangle(320 - 64, 12, 64, 64);
		lastScore = 0;
		scoreString = "SCORE: 0";
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

		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if ((!OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) 
				&& (!(OverlapTester.pointInRectangle(nosBounds, touchPoint.x, touchPoint.y)))
				&&	(!OverlapTester.pointInRectangle(bubbleBounds, touchPoint.x, touchPoint.y))
				&& !(OverlapTester.pointInRectangle(missileBounds, touchPoint.x, touchPoint.y)))
				world.ShotProjectile();
			if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Button button = new Button(90,230);
				buttons.add(button);
				Button buttones = new Button(88,180);
				buttons.add(buttones);
				state = GAME_PAUSED;
				return;
			}

			if (OverlapTester.pointInRectangle(nosBounds, touchPoint.x, touchPoint.y)) {
				//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando su");
				world.nosActivate();
				return;
			}
			else if (OverlapTester.pointInRectangle(bubbleBounds, touchPoint.x, touchPoint.y)) {
				//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
				world.bubbleActivate();
				return;
			}
			else if (OverlapTester.pointInRectangle(missileBounds, touchPoint.x, touchPoint.y)) {
				//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
				//attivatraj=true;
				world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, world.charlie));
				return;
			}
		}/* else {
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
		ApplicationType appType = Gdx.app.getType();
		// should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
			//if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
			world.update(deltaTime, Gdx.input.getAccelerometerX());
		} else {
			float accel = 0;
			if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accel = 5f;
			if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accel = -5f;
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
			renderer = new WorldRenderer(batcher, world);
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
		GLCommon gl = Gdx.gl;
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
		controlLockCharacter();

	}   

	private void controlLockCharacter()
	{ 
		if (world.signal2screen==1)
		{
			stampo("+10 ammo");
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
			stampo("Kill!!");
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
		if (state == GAME_RUNNING) state = GAME_PAUSED;
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}