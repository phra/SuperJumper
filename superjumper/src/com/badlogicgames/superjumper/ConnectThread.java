package com.badlogicgames.superjumper;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Gdx;

public class ConnectThread extends Thread {

	Socket sock;
	BTsocket btsock;
	int port;
	String dest;
	Boolean OK2Send = false;
	FullDuplexBuffer buf;
	Semaphore sem;

	public ConnectThread(String dest, int port, FullDuplexBuffer buf, Semaphore sem) {
		super();
		Gdx.app.debug("PHTEST", "CONNECTTHREAD()");
		this.dest = dest;
		this.port = port;
		this.buf = buf;
		this.sem = sem;
	}

	@Override
	public void run () {
		String test = "TEST";
		Gdx.app.debug("PHTEST", "CONNECTTHREAD.RUN");
		try {
			sock = new Socket(dest,port);
			btsock = new BTsocket(sock.getInputStream(),sock.getOutputStream());
			OK2Send = true;
			Gdx.app.debug("PHTEST", "rilascio sem (connect thread)");
			MultiplayerScreen.str = "CONNECT THREAD";
			sem.release();
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
			Pacco pkt = new Pacco(1,test.getBytes(),test.getBytes().length);
			btsock.writePkt(pkt);
			MultiplayerScreen.str = "SEND OK!";
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
