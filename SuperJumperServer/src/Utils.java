

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
	
	public static LinkedList<Float> deserializeManyFloat(byte[] buf){
		ByteArrayInputStream stream = new ByteArrayInputStream(buf);
		DataInputStream dis = new DataInputStream(stream);
		LinkedList<Float> list = new LinkedList<Float>();
		try {
			while (list.add(dis.readFloat()));
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
}
