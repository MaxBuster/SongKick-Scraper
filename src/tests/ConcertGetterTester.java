package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import objects.Artist;
import objects.Concert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import concertGetter.ConcertGetter;

public class ConcertGetterTester {
	private ConcertGetter concertGetter;
	
	@Before
	public void setUp() throws Exception {
		String artistPageLink = "/artists/217815-taylor-swift?r=touring_stats";
		String following = "1";
		Artist artist = new Artist(artistPageLink, following);
		LinkedList<Artist> artistList = new LinkedList<Artist>();
		artistList.add(artist);
		this.concertGetter = new ConcertGetter(artistList);
	}

	@After
	public void tearDown() throws Exception {
		concertGetter = null;
	}

	@Test
	public void testNotNull() {
		concertGetter.getConcertLinks();
		assertNotNull(concertGetter.getConcertList());
	}

	@Test
	public void testNotFullList() {
		concertGetter.getConcertLinks();
		LinkedList<Concert> concertLinks = concertGetter.getConcertList();
		assertTrue(concertLinks.size() > 0);
	}

}
