package com.badlogicgames.superjumper;

import java.util.Random;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

/** <p>
 * @author raelix
 * <p>
 * @Uses
 * {@link Shake} that help you to vibrate your {@link OrthographicCamera}. This allows an application to easily shake your cam.
 * </p>
 * <p>
 * OrthographicCamera has x,y coordinate.You must call this Class with duration  <strong>Float duration</strong> , it will shake your guiCam for duration.
 * screen is set.
 * </p> */
public class Shake {
	private float   stateTime=0;
	public float   duration=0;
	public static float DURATION=2f;
	public  Random rand=new Random();
	
	public void shakethis(float duration){
		if(duration<0)this.duration=DURATION;
		else this.duration=duration;
		stateTime=0;
	}
	public void update(OrthographicCamera cam){
		if(stateTime>=this.duration)cam.position.x=5;
		else{
			cam.position.x=rand.nextFloat()>0.5f? 5+0.12f:5-0.12f;
			}
		stateTime+=0.05f;
	}
}
	



