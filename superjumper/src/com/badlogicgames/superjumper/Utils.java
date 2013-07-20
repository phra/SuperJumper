/**
 * 
 */
package com.badlogicgames.superjumper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author phra
 *
 */
public abstract class Utils {

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	public static String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;");
	}

	/**
	 * @param x
	 * @return a byte array
	 */
	public static byte[] serializeFloat(float x){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeFloat(x);
			w.flush();
			w.close();
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	/**
	 * @param x
	 * @param y
	 * @return a byte array
	 */
	public static byte[] serialize2Float(float x, float y){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeFloat(x);
			w.writeFloat(y);
			w.flush();
			w.close();
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public static byte[] serializeManyFloat(float... numbers) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		for (float n : numbers) {
			try {
				w.writeFloat(n);
			} catch (IOException e) {
				return null;
			} 
		}
		try {
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	//FIXME
	public static LinkedList<Float> deserializeManyFloat(byte[] buf){
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(stream);
		LinkedList<Float> list = new LinkedList<Float>();
		try {
			while (list.offer(dis.readFloat()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return list;
	}

	/**
	 * @param x
	 * @return a byte array
	 */
	public static byte[] serializeInt(int x){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		try {
			w.writeInt(x);
			w.flush();
			w.close();
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	/**
	 * @param list
	 * @return a byte array
	 */
	public static byte[] serializeManyInt(int... list){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);

		for (int x : list) {
			try {
				w.writeInt(x);
				w.flush();
			} catch (IOException e) {
				return null;
			}
		}
		try {
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * @param list
	 * @return a byte array
	 */
	public static byte[] serializeManyStrings(String... list){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);

		for (String x : list) {
			try {
				w.writeUTF(x);
			} catch (IOException e) {
				return null;
			}
		}
		try {
			w.flush();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * @param list
	 * @return a byte array
	 */
	public static byte[] seralizeManyObjects(LinkedList<? extends MySerializable> list) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(Utils.serializeInt(list.size()));
			for (MySerializable obj : list)
				outputStream.write(obj.serialize());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}

	/*
	public static <T> LinkedList<T> deserializeManyObjects(Class<? extends MySerializable> c, byte[] buf) {
		LinkedList<T> list = new LinkedList<T>();

		return null;
	}*/

	/**
	 * @param bytes
	 * @return a byte array
	 */
	public static byte[] concatBytes(byte[]... bytes) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (byte[] obj : bytes) {
			try {
				outputStream.write(obj);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outputStream.toByteArray();
	}

	/**
	 * @param bytes
	 * @return an int
	 */
	public static int deserializeInt(byte[] bytes) {
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		DataInputStream dis = new DataInputStream(stream);
		try {
			return dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}


	public static boolean overlapCircleTester(GameObject obj, int centrox, int centroy, int raggio) {
		return (int)Math.sqrt((int)Math.pow((double)centrox - (double)obj.position.x,2) + (int)Math.pow((double)centroy - (double)obj.position.y,2)) >= raggio ? true : false;
	}

	public static void changeGravityTowards(DynamicGameObject dyn, GameObject target){
		dyn.velocity.x = target.position.x - dyn.position.x;
		dyn.velocity.y = target.position.y - dyn.position.y;
	}
	
	public static void changeGravityTowards(DynamicGameObject dyn,float x, float y){
		dyn.velocity.x = x - dyn.position.x;
		dyn.velocity.y = y - dyn.position.y;
	}


	public static double changeRotationTowards(DynamicGameObject dyn, double x, double y){
//float diffx = dyn.position.x - x;
//float diffy = dyn.position.y - y;

		double diffx = dyn.position.x + x;
		double diffy = dyn.position.y + y;
//return Math.atan2(diffy, diffx) * 180 / Math.PI;
		return ((Math.atan2(y, x) - Math.atan2(dyn.position.y, dyn.position.x))* 180 / Math.PI) *2 % 360;

//return Math.toDegrees(Math.acos( (x * dyn.position.x + y * dyn.position.y) / (Math.sqrt(Math.pow(x,2)+Math.pow(y,2)) * Math.sqrt(Math.pow(dyn.position.x,2)+Math.pow(dyn.position.y,2)))));

//double angle = Math.toDegrees(Math.atan2(diffx, diffy));
//if(angle < 0) angle += 360;

//return angle;
//return Math.toDegrees(Math.atan2(diffy, diffx));
//dyn.velocity.lerp(new Vector2(target.position.x,target.position.y), +0.001f);
//dyn.velocity.x = x - dyn.position.x;
//dyn.velocity.y = y - dyn.position.y;
	}

	private void setCircularPosition(DynamicGameObject obj, DynamicGameObject dst, float raggio, float stateTime){
		obj.position.x = (float) (dst.position.x + raggio*Math.cos(stateTime));
		obj.position.y = (float) (dst.position.y + raggio*Math.sin(stateTime));
	}


	public static double distance(GameObject a, GameObject b){
		return Math.pow(Math.pow(a.position.x - b.position.x, 2) + Math.pow(a.position.y - b.position.y, 2), (double)1/(double)2);
	}

}
