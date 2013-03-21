package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WorldRendererMulti {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	WorldMulti world;
	OrthographicCamera cam;
	SpriteBatch batch;
	TextureRegion background;

	public WorldRendererMulti (SpriteBatch batch, WorldMulti world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.batch = batch;
	}

	public void render () {
		if (world.bob.position.y+5 > cam.position.y) cam.position.y = world.bob.position.y+5;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
 
		//start the batcher, so we would want to do all of our draw calls between batcher.begin and .end
		batch.begin();
		//batch.draw(Assets.backgroundRegion, 0, 0);
		batch.draw(Assets.backgroundRegion1, 0, 0, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion2, 0.2f, 15, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion3, 0, 30, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion4, 0, 45, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion5, 0, 60, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion2, 0.2f, 75, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion3, 0, 90, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion4, 0, 105, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion5, 0, 120, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion2, 0.2f, 135, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion3, 0, 150, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion4, 0, 165, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion5, 0, 180, FRUSTUM_WIDTH+5, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion2, 0.2f, 195, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion3, 0, 210, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion4, 0, 225, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion5, 0, 240, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion2, 0.2f, 255, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion3, 0, 270, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion4, 0, 285, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.draw(Assets.backgroundRegion5, 0, 300, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		//batch.draw(Assets.backgroundRegion, 0, 0, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		batch.end();
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderBob();
		renderPlatforms();
		renderItems();
		renderLifes();
		renderSquirrels();
		renderCastle();
		renderProjectiles();
		batch.end();
	}

	private void renderBob () {
		TextureRegion keyFrame;
		switch (world.bob.state) {
		case Bob.BOB_STATE_FALL:
			keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case Bob.BOB_STATE_JUMP:
			keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case Bob.BOB_STATE_HIT:
		default:
			keyFrame = Assets.bobHit;
		}

	/*	float side = world.bob.velocity.x < 0 ? -1 : 1;*/
		
		batch.draw(keyFrame, world.bob.position.x -0.65f, world.bob.position.y -1f, 1.3f, 2f);
		batch.draw(keyFrame, world.bobMulti.position.x -0.65f, world.bobMulti.position.y -1f, 1.3f, 2f);
		}

	private void renderPlatforms () {
		int len = world.platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = world.platforms.get(i);
			TextureRegion keyFrame = Assets.platform;
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
				keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
			}

			batch.draw(keyFrame, platform.position.x - 1, platform.position.y - 0.25f, 2, 0.5f);
		}
	}

	private void renderItems () {
		int len = world.springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = world.springs.get(i);
			batch.draw(Assets.spring, spring.position.x - 0.5f, spring.position.y - 0.5f, 1, 1);
		}

		len = world.coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = world.coins.get(i);
			TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
			if (coin.state == Coin.COIN_STATE_PULVERIZING) {
				keyFrame = Assets.breakanim.getKeyFrame(coin.stateTime, Animation.ANIMATION_NONLOOPING);
			}
			batch.draw(keyFrame, coin.position.x - 0.75f, coin.position.y - 0.75f, 1.5f, 1.5f);
		}
	}

	private void renderLifes(){
		int len = world.lifes.size();
		for (int i = 0; i < len; i++) {
			Life life = world.lifes.get(i);
			TextureRegion keyFrame = Assets.lifeAnim.getKeyFrame(life.stateTime, Animation.ANIMATION_LOOPING);	
	batch.draw(keyFrame, cam.position.x - FRUSTUM_WIDTH/2, cam.position.y + i+3, 0.5f, 0.5f);
		
		
		}
	}
	
	private void renderProjectiles(){
		int len = world.projectiles.size();
		for (int i = 0; i < len; i++) {
			Projectile projectile = world.projectiles.get(i);
			TextureRegion keyFrame = Assets.projAnim.getKeyFrame(projectile.stateTime, Animation.ANIMATION_LOOPING);	
			batch.draw(keyFrame, projectile.position.x-0.2f  , projectile.position.y, 0.3f,0.6f);
				}
	}
	
	private void renderSquirrels () {
		int len = world.squirrels.size();
		for (int i = 0; i < len; i++) {
			Squirrel squirrel = world.squirrels.get(i);
			TextureRegion keyFrame = Assets.lifeAnim.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
			float side = squirrel.velocity.x < 0 ? -1 : 1;
			if (side < 0)
				batch.draw(keyFrame, squirrel.position.x + 0.5f, squirrel.position.y - 0.5f, side * 1, 1);
			else
				batch.draw(keyFrame, squirrel.position.x - 0.5f, squirrel.position.y - 0.5f, side * 1, 1);
		}
	}

	private void renderCastle () {
		Castle castle = world.castle;
		batch.draw(Assets.castle, castle.position.x - 1, castle.position.y - 1, 2, 2);
	}
}