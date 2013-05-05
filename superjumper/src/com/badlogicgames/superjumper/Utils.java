package com.badlogicgames.superjumper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author phra
 *
 */

public abstract class Utils {

	public static byte[] serializeFloat(float x){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeFloat(x);
			w.flush();
			w.close(); //FIXME
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public static byte[] serialize2Float(float x, float y){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeFloat(x);
			w.writeFloat(y);
			w.flush();
			w.close(); //FIXME 
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public static byte[] serializeInt(int x){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeInt(x);
			w.flush();
			w.close(); //FIXME 
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public static boolean overlapCircleTester(GameObject obj, int centrox, int centroy, int raggio) {
		//Gdx.app.debug("OVERLAPCIRCLE", "overlap returns " +(int)Math.sqrt((int)Math.pow((double)MainMenuScreen.centrox - (double)this.position.x,2) + (int)Math.pow((double)MainMenuScreen.centroy - (double)this.position.y,2)));
		return (int)Math.sqrt((int)Math.pow((double)centrox - (double)obj.position.x,2) + (int)Math.pow((double)centroy - (double)obj.position.y,2)) >= raggio ? true : false;
	}

	public static void changeGravityTowards(DynamicGameObject dyn, DynamicGameObject target){
		//dyn.velocity.lerp(new Vector2(target.position.x,target.position.y), +0.001f);
		dyn.velocity.x = (target.position.x - dyn.position.x)/2;
		dyn.velocity.y = (target.position.y - dyn.position.y)/2;
	}
}
