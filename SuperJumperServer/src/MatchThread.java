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
		Pacco pkt1 = btsock1.readPkt();
		Pacco pkt2 = btsock2.readPkt();
		PaccoWelcome pkt3,pkt4;
		Send1Thread send1;
		Recv1Thread recv1;
		Send2Thread send2;
		Recv2Thread recv2;
		if (pkt1.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME){
			this.close();
			return;
		} else if (pkt2.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME){
			this.close();
			return;
		}
		try {
			pkt3 = new PaccoWelcome(pkt1);
			pkt4 = new PaccoWelcome(pkt2);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.close();
			return;
		}
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
		}

		@Override
		public void run () {
			btsock1.writePkt(new PaccoStart());
			while(OK > 0){
				try {
					Pacco tmp = buffer.takePaccoInBLOCK();
					if (tmp.getType() == PROTOCOL_CONSTANTS.PACKET_END){
						OK--;
						latch.countDown();
						return;
					}
					btsock1.writePkt(buffer.takePaccoOutBLOCK());
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
		}

		@Override
		public void run () {
			while(OK > 0){
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
		}

		@Override
		public void run () {
			btsock1.writePkt(new PaccoStart());
			while(OK > 0){
				try {
					Pacco tmp = buffer.takePaccoInBLOCK();
					if (tmp.getType() == PROTOCOL_CONSTANTS.PACKET_END){
						OK--;
						latch.countDown();
						return;
					}
					btsock2.writePkt(tmp);
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
		}

		@Override
		public void run () {
			while(OK > 0){
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
