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
	Boolean OK2Send = false;
	FullDuplexBuffer buf;

	public AcceptThread(int port, FullDuplexBuffer buf) {
		super();
		this.port = port;
		this.buf = buf;
	}

	@Override
	public void run () {
		try {
			ssock = new ServerSocket(port);
			sock =  ssock.accept();
			btsock = new BTsocket(sock.getInputStream(),sock.getOutputStream());
			OK2Send = true;
			new SendThread().start();

			while (true){
				Pacco pkt = btsock.readPkt();
				if (pkt.getType() == 0){
					OK2Send = false;
					break;
				}
				try {
					buf.putPaccoInBLOCK(pkt);
				} catch (InterruptedException e) { }
			}
			/*
			Pacco pkt = btsock.readPkt();
			MultiplayerScreen.str = "RECV OK! " + new String(pkt.getData());
			 */
			btsock.close();
			sock.close();
		} catch (UnknownHostException e) {
			MultiplayerScreen.str = "UNKNOWN HOST EXCEPTION";
		} catch (IOException e) {
			MultiplayerScreen.str = "IO EXCEPTION";
		}
	}

	private class SendThread extends Thread {

		public SendThread () {
			super();
		}

		@Override
		public void run () {
			while(OK2Send){
				try {
					btsock.writePkt(buf.takePaccoOutBLOCK());
				} catch (InterruptedException e) { }
			}
		}
	}
}
