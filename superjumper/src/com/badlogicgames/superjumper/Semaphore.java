/**
 * 
 */
package com.badlogicgames.superjumper;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/** <p>
 * @author raelix
 * <p>
 * @Uses
 * {@link Semaphore} that help you to implement a semaphore countdown for start the game.
 * </p>
 * </p> */
public class Semaphore extends Text {
private LinkedList<Text> list;
public boolean theEnd=false;
public static final float DURATION = 1f;
	/**
	 * @param list
	 */
	public Semaphore (LinkedList<Text> list) {
		super(list.getFirst().position.x,list.getFirst().position.y, list.getFirst().string);
		this.list=list;
	}

	public LinkedList<Text> updateCount(float deltaTime){
		if(!list.isEmpty())
		if (list.getFirst().stateTime > DURATION){
			list.removeFirst();
			Assets.playSound(Assets.soundClick);
			Gdx.app.debug("Semaphore", "rimosso elemento");
			return list;
		}
		else if(list.isEmpty()){
			Gdx.app.debug("Semaphore", "lista vuota");
			return null;
		}
		return list;
		
	}

}
