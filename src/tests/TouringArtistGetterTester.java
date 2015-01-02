package tests;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import touringArtistGetter.TouringArtistGetter;

public class TouringArtistGetterTester {
	private TouringArtistGetter touringArtistGetter;
	
	@Before
	public void setUp() throws Exception {
		this.touringArtistGetter = new TouringArtistGetter();		
	}

	@After
	public void tearDown() throws Exception {
		this.touringArtistGetter = null;
	}

	@Test
	public void testForSourceGetter() {
		String artistLink = "/artists/29315-foo-fighters";
		Document pageDocument = touringArtistGetter.getPageSource(artistLink);
		assertNotNull(pageDocument);
	}

	@Test
	public void testForTouring() {
		String artistLink = "/artists/29315-foo-fighters";
		Document pageDocument = touringArtistGetter.getPageSource(artistLink);
		assertTrue(touringArtistGetter.isArtistTouring(pageDocument));
	}

	@Test
	public void testForArtistFollowing() {
		String artistLink = "/artists/29315-foo-fighters";
		Document pageDocument = touringArtistGetter.getPageSource(artistLink);
		assertNotNull(touringArtistGetter.getArtistFollowing(pageDocument));
	}

}
