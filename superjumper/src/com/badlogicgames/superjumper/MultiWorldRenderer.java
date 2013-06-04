/**
 * 
 */
package com.badlogicgames.superjumper;

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
			TextureRegion keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, world.bobMulti.position.x -0.65f, world.bobMulti.position.y -1f, 1.3f, 2f);
		}
	}
}
