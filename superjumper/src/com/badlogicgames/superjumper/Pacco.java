package com.badlogicgames.superjumper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class Pacco {
	private int type, len;
	private byte[] payload;
	
	public Pacco(int type, byte[] payload, int len){
		this.type = type;
		this.payload = payload;
		this.len = len;
	}
	
	public byte[] getData(){
		return payload;
	}
	
	public int getType(){
		return type;
	}
	
	public int getSize(){
		return len;
	}

	public byte[] getSerializedHeader() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream w = new DataOutputStream(baos);
		
		try {
			w.writeInt(len);
			w.writeInt(type);
			w.flush();
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
	} catch (IOException e) {
		return null;
	}
	return baos.toByteArray();
}
	
/*
	public byte[] getSerialized(){
		byte[] bytes;
		ByteBuffer bb = ByteBuffer.allocate(payload.length+8);
		bb.putInt(len).putInt(type).put(payload).flip();
		bytes = new byte[bb.remaining()];
		bb.get(bytes);
		return bytes;
	}
*/
}
