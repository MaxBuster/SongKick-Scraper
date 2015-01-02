package mainCrawler;

import io.ExcelWriter;

import java.util.LinkedList;

import objects.Artist;
import objects.Concert;
import concertGetter.ConcurrentConcertGetter;
import concertInfoGetter.ConcurrentInfoGetter;
import touringArtistGetter.ConcurrentTouringArtistGetter;

/**
 * This program uses jsoup to crawl SongKick.com for concert links, and then scrapes them for info.
 * It has three components: one crawler to check a list of artist pages for the touring artists,
 * one crawler to retrieve the concert links from the touring artists, and one scraper that retrieves
 * concert info from all of the concert pages. The overall result is an excel document containing
 * concerts with complete info.
 */
public class Main {

	public void runAllMethods() {
		ConcurrentTouringArtistGetter concurrentTouringArtistGetter = new ConcurrentTouringArtistGetter();
		LinkedList<Artist> touringArtists = concurrentTouringArtistGetter.getArtists();

		ConcurrentConcertGetter concurrentConcertGetter = new ConcurrentConcertGetter(touringArtists);
		LinkedList<Concert> concertList = concurrentConcertGetter.getConcerts();

		ConcurrentInfoGetter concurrentInfoGetter = new ConcurrentInfoGetter(concertList);
		LinkedList<Concert> concertInfo = concurrentInfoGetter.getConcertInfo();
		
		ExcelWriter excelWriter = new ExcelWriter(concertInfo);
		excelWriter.writeOutToExcel();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.runAllMethods();
	}
}
