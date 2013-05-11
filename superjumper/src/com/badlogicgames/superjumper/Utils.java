package com.badlogicgames.superjumper;
	//rotationcounter = (float)Utils.changeRotationTowards(this, MainMenuScreen.centrox,MainMenuScreen.centroy);
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

	public static void changeGravityTowards(DynamicGameObject dyn, GameObject target){
		//dyn.velocity.lerp(new Vector2(target.position.x,target.position.y), +0.001f);
		dyn.velocity.x = target.position.x - dyn.position.x;
		dyn.velocity.y = target.position.y - dyn.position.y;
	}
	
	public static double changeRotationTowards(DynamicGameObject dyn, double x, double y){
		//float diffx = dyn.position.x - x;
		//float diffy = dyn.position.y - y;

		double diffx = dyn.position.x + x;
		double diffy = dyn.position.y + y;
		//return Math.atan2(diffy, diffx) * 180 / Math.PI;
		return ((Math.atan2(y, x) -  Math.atan2(dyn.position.y, dyn.position.x))* 180 / Math.PI) *2 % 360;
		
		//return Math.toDegrees(Math.acos( (x * dyn.position.x + y * dyn.position.y) / (Math.sqrt(Math.pow(x,2)+Math.pow(y,2)) * Math.sqrt(Math.pow(dyn.position.x,2)+Math.pow(dyn.position.y,2)))));
		
		//double angle = Math.toDegrees(Math.atan2(diffx, diffy));
	   //if(angle < 0) angle += 360;
	    
	   //return angle;
		//return Math.toDegrees(Math.atan2(diffy, diffx));
		//dyn.velocity.lerp(new Vector2(target.position.x,target.position.y), +0.001f);
		//dyn.velocity.x = x - dyn.position.x;
		//dyn.velocity.y = y - dyn.position.y;
	}
	
	
	
	public static double distance(GameObject a, GameObject b){
		return Math.pow(Math.pow(a.position.x - b.position.x, 2) +  Math.pow(a.position.y - b.position.y, 2), (double)1/(double)2);
	}
	
	
	
}
