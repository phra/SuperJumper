import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;


public class MatchThread extends Thread implements PROTOCOL_CONSTANTS {
	Socket sock1, sock2;
	BTsocket btsock1, btsock2;
	public int OK = 0;
	private FullDuplexBuffer buffer;
	private CountDownLatch latch = new CountDownLatch(4);
	private CountDownLatch latchParent;
	
	public MatchThread(Socket sock1, Socket sock2, CountDownLatch latchParent) {
		super();
		System.out.println("MatchThread()");
		this.sock1 = sock1;
		this.sock2 = sock2;
		this.latchParent = latchParent;
		this.buffer = new FullDuplexBuffer();
		try {
			this.btsock1 = new BTsocket(sock1.getInputStream(),sock1.getOutputStream());
			this.btsock2 = new BTsocket(sock2.getInputStream(),sock2.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.close();
		}
	}
	
	@Override
	public void start() {
		System.out.println("MatchThread: start()");
		Pacco pkt1 = btsock1.readPkt();
		Pacco pkt2 = btsock2.readPkt();
		PaccoWelcome pkt3,pkt4;
		Send1Thread send1;
		Recv1Thread recv1;
		Send2Thread send2;
		Recv2Thread recv2;
		if ((pkt1.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME) || (pkt2.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME)){
			System.out.println("MatchThread: ERRORE PROTOCOLLO");
			this.close();
			return;
		}
		try {
			pkt3 = new PaccoWelcome(pkt1);
			pkt4 = new PaccoWelcome(pkt2);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("MatchThread: ERRORE PROTOCOLLO");
			this.close();
			return;
		}
		btsock2.writePkt(pkt3);
		btsock1.writePkt(pkt4);
		int id1 = SuperJumperServer.getID(), id2 = SuperJumperServer.getID();
		SuperJumperServer.users.put(id1,new User(pkt3.getNick(),id1));
		SuperJumperServer.users.put(id2,new User(pkt4.getNick(),id2));
		
		OK = 2;
		send1 = new Send1Thread();
		recv1 = new Recv1Thread();
		send2 = new Send2Thread();
		recv2 = new Recv2Thread();
		send1.start();
		recv1.start();
		send2.start();
		recv2.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SuperJumperServer.users.remove(id1);
		SuperJumperServer.users.remove(id2);
		this.close();
	}
	
	public void close(){
		System.out.println("MatchThread: close()");
		btsock1.close();
		btsock2.close();
		try {
			sock1.close();
			sock2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		latchParent.countDown();
	}
	
	private class Send1Thread extends Thread {

		public Send1Thread () {
			super();
			System.out.println("Send1Thread()");
		}

		@Override
		public void run () {
			System.out.println("Send1Thread: run()");
			//btsock1.writePkt(new PaccoStart());
			while(OK > 0){
				try {
					Pacco tmp = buffer.takePaccoInBLOCK();
					System.out.println("Send1Thread: writePkt()");
					btsock1.writePkt(buffer.takePaccoOutBLOCK());
					if (tmp.getType() == PROTOCOL_CONSTANTS.PACKET_END){
						OK--;
						latch.countDown();
						return;
					}
				} catch (InterruptedException e) {
					latch.countDown();
					return;
				}
			}
			latch.countDown();
			return;
		}
	}
	
	private class Recv1Thread extends Thread {
		
		public Recv1Thread () {
			super();
			System.out.println("Recv1Thread()");
		}

		@Override
		public void run () {
			System.out.println("Recv1Thread: start()");
			while(OK > 0){
				System.out.println("Recv1Thread: readPkt()");
				Pacco pkt = btsock1.readPkt();
				if (pkt == null){
					latch.countDown();
					return;
				}
				try {
					buffer.putPaccoInBLOCK(pkt);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					latch.countDown();
					return;
				}
			}
			latch.countDown();
			return;
		}
	}
	
	private class Send2Thread extends Thread {
		
		public Send2Thread () {
			super();
			System.out.println("Send2Thread()");
		}

		@Override
		public void run () {
			System.out.println("Send2Thread: writePkt()");
			//btsock1.writePkt(new PaccoStart());
			while(OK > 0){
				try {
					Pacco tmp = buffer.takePaccoInBLOCK();
					System.out.println("Send2Thread: writePkt()");
					btsock2.writePkt(tmp);
					if (tmp.getType() == PROTOCOL_CONSTANTS.PACKET_END){
						OK--;
						latch.countDown();
						return;
					}

				} catch (InterruptedException e) {
					latch.countDown();
					return;
				}
			}
			latch.countDown();
			return;
		}
	}
	
	private class Recv2Thread extends Thread {
		
		private FullDuplexBuffer buffer;

		public Recv2Thread () {
			super();
			System.out.println("Recv2Thread()");
		}

		@Override
		public void run () {
			while(OK > 0){
				System.out.println("Recv2Thread: readPkt");
				Pacco pkt = btsock2.readPkt();
				if (pkt == null) {
					latch.countDown();
					return;
				}
				try {
					buffer.putPaccoOutBLOCK(pkt);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					latch.countDown();
					return;
				}
			}
			latch.countDown();
			return;
		}
	}

}
