/**
 * 
 */
package com.badlogicgames.superjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

/**
 * @author phra
 *
 */
public class MultiGameScreen extends GameScreen {
	/**
	 * @param game
	 */
	public MultiGameScreen (final Game game, int seed) {
		super(game);
		this.world = new MultiWorld(seed);
		this.renderer = new MultiWorldRenderer(batcher, (MultiWorld)world, guiCam);

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
					/*	pause();*/
						break;
					}
					if (world.supermissiles > 0 && OverlapTester.pointInRectangle(nosBounds, touchPoint.x, touchPoint.y)) {
						//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando su");
						if (--world.supermissiles <= 0) world.supermissileButton = false;
						if (!world.enemies.isEmpty()) world.projectiles.add(new SuperMissile(world.bob.position.x, world.bob.position.y, SuperMissile.WIDTH, SuperMissile.HEIGHT, world.enemies.peek(),world.projectiles,world.enemies));

					}
					else if (world.bubbleButton == true && OverlapTester.pointInRectangle(bubbleBounds, touchPoint.x, touchPoint.y)) {
						//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
						world.bob.enablebubble = true;
						world.bubbleButton = false;
						world.bob.bubbletime = world.bob.stateTime;
					}
					else if (world.missiles > 0 && OverlapTester.pointInRectangle(missileBounds, touchPoint.x, touchPoint.y)) {
						//Gdx.app.debug("UPDATEGRAVITY", "sto cliccando giu");
						if (--world.missiles <= 0) world.activemissile = false;
						if (!world.enemies.isEmpty()) world.projectiles.add(new Missile(world.bob.position.x, world.bob.position.y, Missile.WIDTH, Missile.HEIGHT, world.enemies.peek()));

					}  else {
						world.ShotProjectile();
						MultiWorld.buffer.putPaccoOutNOBLOCK(new PaccoProiettile(world.bob.position.x,world.bob.position.y));
						Gdx.app.debug("MultiGameScreenTap", "world.bob.position.x= "+world.bob.position.x+" world.bob.position.y"+world.bob.position.y);
						

					}
					break;

				case CONSTANTS.GAME_OVER:
					if (world.score > Settings.highscores[4]){
						world.scoretext.update(0, "NEW HIGHSCORE: " + world.score);
						Settings.addScore(world.score);
						Settings.save();
					}
					world.state = CONSTANTS.GAME_RUNNING;
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
						if (OverlapTester.pointInRectangle(b.bounds, touchPoint.x, touchPoint.y)) {
							Assets.playSound(Assets.clickSound);
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
					state = GAME_RUNNING;
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
	@Override 
	public void pause () {
	MultiWorld.buffer.putPaccoOutNOBLOCK(new PaccoEnd());
	game.setScreen(new MainMenuScreen(game));
	}
	@Override
	public void dispose () {
		Assets.load();
	
	}
}
