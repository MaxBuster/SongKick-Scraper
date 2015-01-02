package concertGetter;

import java.util.LinkedList;

import objects.Artist;
import objects.Concert;

public class ConcurrentConcertGetter {
	private ConcertGetter concertGetter;

	public ConcurrentConcertGetter(LinkedList<Artist> artists) {
		super();
		this.concertGetter = new ConcertGetter(artists);
	}

	public LinkedList<Concert> getConcerts() {
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
				concertGetter.getConcertLinks();
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
		return concertGetter.getConcertList();
	}
}
