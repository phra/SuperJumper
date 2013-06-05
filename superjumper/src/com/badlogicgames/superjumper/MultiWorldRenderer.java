/**
 * 
 */
package com.badlogicgames.superjumper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author phra
 *
 */
public class MultiWorldRenderer extends WorldRenderer {
	private MultiWorld world;

	/**
	 * @param batch
	 * @param world
	 * @param screencam
	 */
	public MultiWorldRenderer (SpriteBatch batch, MultiWorld world, OrthographicCamera screencam) {
		super(batch, world, screencam);
		this.world = world;
	}

	@Override
	public void render () {
		super.render();
		switch (world.state){
		case CONSTANTS.GAME_RUNNING:
			batch.begin();
			batch.enableBlending();
			batch.setProjectionMatrix(cam.combined);
			renderBobMulti();
			renderProjectiles();
			batch.disableBlending();
			batch.end();
		}
	}

	private void renderBobMulti(){
		//Gdx.app.debug("RENDERMULTIBOB", "world.bobMulti.position.x = " + world.bobMulti.position.x + " world.bobMulti.position.y = " + world.bobMulti.position.y);
		//Gdx.app.debug("RENDERBOB", "world.bob.position.x = " + world.bob.position.x + " world.bob.position.y = " + world.bob.position.y);
		TextureRegion keyFrame = Assets.shutmilitAnim.getKeyFrame(world.bobMulti.stateTime, Animation.ANIMATION_LOOPING);
//		batch.draw(Assets.meteorabluRegion, world.bob.position.x -0.65f, world.bob.position.y -1f, 1.3f, 2f);
		batch.draw(keyFrame, world.bobMulti.position.x -0.65f, world.bobMulti.position.y -1f, 1.3f, 2f);
	}
	
	private void renderProjectiles(){
		TextureRegion keyFrame;
		for (Projectile projectile : world.projEnemy) {
			switch (projectile.type) {
			case Projectile.TYPE:
				keyFrame = Assets.projAnim.getKeyFrame(projectile.stateTime, Animation.ANIMATION_LOOPING);
				batch.draw(keyFrame, projectile.position.x -0.07f , projectile.position.y+0.4f, 0.3f,0.6f);
				break;
			case Missile.TYPE:
				keyFrame = Assets.missileRegion;
				batch.draw(keyFrame, projectile.position.x-0.4f , projectile.position.y+0.2f, 1f,1.4f);
				break;
			case SuperMissile.TYPE:
				keyFrame = Assets.missileRegion;
				batch.draw(keyFrame, projectile.position.x-0.4f , projectile.position.y+0.2f, 1f,1.4f);
				break;
			}
		}
	}
}
