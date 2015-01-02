package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import io.InitialPageGetter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InitialLinkGetterTester {
	private InitialPageGetter pageGetter;
	
	@Before
	public void setUp() throws Exception {
		this.pageGetter = new InitialPageGetter();
	}

	@After
	public void tearDown() throws Exception {
		this.pageGetter = null;
	}

	@Test
	public void test() {
		LinkedList<String> pageLinks = pageGetter.readLinksFromFile();
		assertTrue(pageLinks.size() > 0);
		for (String link : pageLinks) {
			assertNotNull(link);
		}
	}

}
