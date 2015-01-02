package touringArtistGetter;

import java.util.LinkedList;

import objects.Artist;

public class ConcurrentTouringArtistGetter  {
	private TouringArtistGetter touringArtistGetter;

	public ConcurrentTouringArtistGetter() {
		super();
		this.touringArtistGetter = new TouringArtistGetter();
	}

	public LinkedList<Artist> getArtists() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		Thread[] arrayOfThreads = new Thread[numThreads];
		for (int i=0; i<numThreads; i++) {
			Thread newThread = newThread();
			arrayOfThreads[i] = newThread;
		}
		joinThreads(arrayOfThreads);
		return getTouringArtistList();
	}
	
	public Thread newThread() {
		Thread newThread = new Thread() {
			public void run() {
				touringArtistGetter.crawlForTouringArtists();
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
	
	public LinkedList<Artist> getTouringArtistList() {
		return touringArtistGetter.getTouringArtists();
	}
}
