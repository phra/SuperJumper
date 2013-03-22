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
}
