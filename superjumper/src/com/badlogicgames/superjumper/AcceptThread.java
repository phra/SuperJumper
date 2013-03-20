package com.badlogicgames.superjumper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class AcceptThread extends Thread {
	
	ServerSocket ssock ;
	Socket sock;
	int port;
	BTsocket btsock;
	
	public AcceptThread(int port) {
		super();
		this.port = port;
	}
	
	
	@Override
	public void run () {
		
		try {
			ssock = new ServerSocket(port);
			sock =  ssock.accept();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				btsock = new BTsocket(sock.getInputStream(),sock.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Pacco pkt = btsock.readPkt();
				MultiplayerScreen.str = "RECV OK! " + new String(pkt.getData());
				btsock.close();
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
