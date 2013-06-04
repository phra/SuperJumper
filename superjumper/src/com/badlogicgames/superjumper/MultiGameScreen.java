/**
 * 
 */
package com.badlogicgames.superjumper;

import com.badlogic.gdx.Game;

/**
 * @author phra
 *
 */
public class MultiGameScreen extends GameScreen {

	/**
	 * @param game
	 */
	public MultiGameScreen (Game game, int seed) {
		super(game);
		this.world = new MultiWorld(seed);
		this.renderer = new WorldRenderer(batcher, world, guiCam);
	}

}
