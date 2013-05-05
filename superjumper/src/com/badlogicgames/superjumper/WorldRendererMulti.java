package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
		//Gradient Background 
		batch.begin();
		batch.disableBlending();
		WorldRenderer.drawGradient(batch, Assets.rect, 0, 0, 10, 110,Color.BLACK,Assets.colore, false);
		batch.enableBlending();
		batch.end();
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderBob();
		renderPlatforms();
		renderItems();
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
			TextureRegion keyFrame ;
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
				keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
			}
			else keyFrame = Assets.platform.getKeyFrame(platform.stateTime, Animation.ANIMATION_LOOPING);
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
			//	keyFrame = Assets.breakanim.getKeyFrame(coin.stateTime, Animation.ANIMATION_NONLOOPING);
			}
			batch.draw(keyFrame, coin.position.x - 0.75f, coin.position.y - 0.75f, 1.5f, 1.5f);
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