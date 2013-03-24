

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author phra
 *
 */

public class Pacco {
	private int type, len;
	byte[] payload;

	public Pacco(int type, byte[] payload, int len){
		this.type = type;
		this.payload = payload;
		this.len = len;
	}

	Pacco(int type, int len){
		this.type = type;
		this.len = len;
		this.payload = null;
	}

	Pacco(int type){
		this.type = type;
		this.len = 0;
		this.payload = null;
	}

	public byte[] getData(){
		return payload;
	}

	void setData(byte[] payload){
		this.payload = payload;
	}

	public int getType(){
		return type;
	}

	void setType(int type){
		this.type = type;
	}

	public int getSize(){
		return len;
	}

	void setSize(int size){
		this.len = size;
	}

	public byte[] getSerializedHeader() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);

		try {
			w.writeInt(len);
			w.writeInt(type);
			w.flush();
			w.close(); //FIXME
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

	public byte[] getSerialized() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);

		try {
			w.writeInt(len);
			w.writeInt(type);
			w.write(payload);
			w.flush();
			w.close(); //FIXME 
		} catch (IOException e) {
			return null;
		}
		return baos.toByteArray();
	}

}
