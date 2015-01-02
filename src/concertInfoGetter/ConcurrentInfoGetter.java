package concertInfoGetter;

import java.util.LinkedList;

import objects.Concert;

public class ConcurrentInfoGetter {
	private InfoGetter infoGetter;

	public ConcurrentInfoGetter(LinkedList<Concert> concerts) {
		super();
		this.infoGetter = new InfoGetter(concerts);
	}

	public LinkedList<Concert> getConcertInfo() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		Thread[] arrayOfThreads = new Thread[numThreads];
		for (int i=0; i<numThreads; i++) {
			Thread newThread = newThread();
			arrayOfThreads[i] = newThread;
		}
		joinThreads(arrayOfThreads);
		return getConcertList();
	}
	
	public Thread newThread() {
		Thread newThread = new Thread() {
			public void run() {
				infoGetter.getInfo();
			}
		};
		newThread.start();
		return newThread;
	}
	
	public void joinThreads(Thread[] arrayOfThreads) {
		for (Thread thread : arrayOfThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public LinkedList<Concert> getConcertList() {
		return infoGetter.getCompletedConcerts();
	}
}
