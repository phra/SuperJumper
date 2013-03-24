package com.badlogicgames.superjumper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import com.badlogic.gdx.Gdx;

public class AcceptThread extends Thread {

	ServerSocket ssock ;
	Socket sock;
	int port;
	BTsocket btsock;
	Boolean OK2Send = false;
	FullDuplexBuffer buf;
	Semaphore sem;

	public AcceptThread(int port, FullDuplexBuffer buf, Semaphore sem) {
		super();
		Gdx.app.debug("PHTEST", "ACCEPTTHREAD()");
		this.port = port;
		this.buf = buf;
		this.sem = sem;
	}

	@Override
	public void run () {
		Gdx.app.debug("PHTEST", "ACCEPTTHREAD.RUN");
		try {
			ssock = new ServerSocket(port);
			sock =  ssock.accept();
			btsock = new BTsocket(sock.getInputStream(),sock.getOutputStream());
			OK2Send = true;
			Gdx.app.debug("PHTEST", "rilascio sem (accept thread)");
			MultiplayerScreen.str = "ACCEPT THREAD";
			
			Gdx.app.debug("PHTEST", "CONNECTTHREAD():mando pkt welcome");
			btsock.writePkt(new PaccoWelcome("TEST"));
			Pacco p = btsock.readPkt();
			
			if (p.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME){
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
			Gdx.app.debug("PHTEST", "CONNECTTHREAD():mando pkt start");
			btsock.writePkt(new PaccoStart());

			if (btsock.readPkt().getType() != PROTOCOL_CONSTANTS.PACKET_START){
				Gdx.app.debug("PHTEST", "ERRORE PROTOCOLLO START");
				this.close();
				return;
			}
			Gdx.app.debug("PHTEST", "CONNECTTHREAD():ricevuto pkt start");
			sem.release();
			new SendThread().start();

			while (OK2Send){
				Pacco pkt = btsock.readPkt();
				if (pkt.getType() == PROTOCOL_CONSTANTS.PACKET_END){
					//OK2Send = false;
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
			while(OK2Send){
				try {
					Pacco pkt = buf.takePaccoOutBLOCK();
					btsock.writePkt(pkt);
					if (pkt.getType() == PROTOCOL_CONSTANTS.PACKET_END) break;
				} catch (InterruptedException e) { }
			}
		}
		
	}
}
