package com.badlogicgames.superjumper;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectThread extends Thread {

	Socket sock;
	BTsocket btsock;
	int port;
	String dest;
	
	public ConnectThread(String dest, int port) {
		super();
		this.dest = dest;
		this.port = port;
	}
	
	
	@Override
	public void run () {
		String test = "TEST";
		try {
			sock = new Socket(dest,port);
			btsock = new BTsocket(sock.getInputStream(),sock.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				Pacco pkt = new Pacco(1,test.getBytes(),test.getBytes().length);
				btsock.writePkt(pkt);
				MultiplayerScreen.str = "SEND OK!";
				btsock.close();
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
