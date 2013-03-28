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

			Gdx.app.debug("PHTEST", "CONNECTTHREAD():mando pkt welcome");
			btsock.writePkt(new PaccoWelcome("TEST"));
			Pacco p = btsock.readPkt();

			if (p == null || p.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME){
				Gdx.app.debug("PHTEST", "ERRORE PROTOCOLLO WELCOME");
				this.close();
				return;
			}
			Gdx.app.debug("PHTEST", "CONNECTTHREAD():ricevuto pkt welcome");
			try {
				WorldMulti.enemy = new PaccoWelcome(p).getNick();
			} catch (ProtocolException e1) {
				Gdx.app.debug("PHTEST", "ERRORE PROTOCOLLO WELCOME");
				this.close();
				return;
			}
			Gdx.app.debug("PHTEST", "CONNECTTHREAD(): mando pkt start");
			btsock.writePkt(new PaccoStart(10000));

			if (btsock.readPkt().getType() != PROTOCOL_CONSTANTS.PACKET_START){
				Gdx.app.debug("PHTEST", "ERRORE PROTOCOLLO START");
				this.close();
				return;
			}
			//FIXME set seed


			Gdx.app.debug("PHTEST", "CONNECTTHREAD(): ricevuto packet start");
			sem.release();
			new SendThread().start();

			while (true){
				Pacco pkt = btsock.readPkt();
				if (pkt == null || pkt.getType() == PROTOCOL_CONSTANTS.PACKET_END){
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

		} catch (UnknownHostException e) {
			MultiplayerScreen.str = "UNKNOWN HOST EXCEPTION";
			sem.release();
		} catch (IOException e) {
			MultiplayerScreen.str = "IO EXCEPTION";
			sem.release();
		}
	}

	void close(){
		btsock.close();
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class SendThread extends Thread {

		public SendThread () {
			super();
		}

		@Override
		public void run () {
			while(true){
				try {
					Pacco pkt = buf.takePaccoOutBLOCK();
					btsock.writePkt(pkt);
					if (pkt.getType() == PROTOCOL_CONSTANTS.PACKET_END) break;
				} catch (InterruptedException e) { }
			}
		}
	}
}
