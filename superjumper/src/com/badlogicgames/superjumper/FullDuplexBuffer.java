package com.badlogicgames.superjumper;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author phra
 *
 */

public class FullDuplexBuffer {
	private LinkedBlockingQueue<Pacco> QueueOut, QueueIn;

	public FullDuplexBuffer(){
		this.QueueIn = new LinkedBlockingQueue<Pacco>();
		this.QueueOut = new LinkedBlockingQueue<Pacco>();
	}

	protected void putPaccoInBLOCK(Pacco pkt) throws InterruptedException{
		QueueIn.put(pkt);
	}

	protected void putPaccoOutBLOCK(Pacco pkt) throws InterruptedException{
		QueueOut.put(pkt);
	}

	protected void putPaccoInNOBLOCK(Pacco pkt) {
		if (Integer.MAX_VALUE != QueueIn.size()) try {
			QueueIn.put(pkt);
		} catch (InterruptedException e) { }
	}

	protected void putPaccoOutNOBLOCK(Pacco pkt) {
		if (Integer.MAX_VALUE != QueueOut.size()) try {
			QueueOut.put(pkt);
		} catch (InterruptedException e) { }
	}

	protected Pacco takePaccoInBLOCK() throws InterruptedException{
		return QueueIn.take();
	}

	protected Pacco takePaccoOutBLOCK() throws InterruptedException{
		return QueueOut.take();
	}

	protected Pacco takePaccoInNOBLOCK() {
		if (!QueueIn.isEmpty()) try {
			return QueueIn.take();
		} catch (InterruptedException e) { }
		return null;
	}

	protected Pacco takePaccoOutNOBLOCK() {
		if (!QueueOut.isEmpty()) try {
			return QueueOut.take();
		} catch (InterruptedException e) { }
		return null;
	}

	public boolean selfTest(){
		Pacco tmp1 = new Pacco(1);
		Pacco tmp2 = new Pacco(2);
		Pacco tmp3 = new Pacco(3);
		Pacco tmp4 = new Pacco(4);

		try {
			this.putPaccoOutBLOCK(tmp1);
			this.putPaccoOutNOBLOCK(tmp2);
			this.putPaccoInBLOCK(tmp3);
			this.putPaccoInNOBLOCK(tmp4);
			Pacco tmp7 = this.takePaccoOutBLOCK();
			Pacco tmp8 = this.takePaccoOutNOBLOCK();
			Pacco tmp9 = this.takePaccoInBLOCK();
			Pacco tmp10 = this.takePaccoInNOBLOCK();
			if (tmp7.getType() != 1){
				return false;
			}
			if (tmp8.getType() != 2){
				return false;
			}
			if (tmp9.getType() != 3){
				return false;
			}
			if (tmp10.getType() != 4){
				return false;
			}
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
}