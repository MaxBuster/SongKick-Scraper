package touringArtistGetter;

import io.InitialPageGetter;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.jsoup.nodes.Document;

import objects.Artist;
import sourceCodeGetter.SourceCodeGetter;

public class TouringArtistGetter {
	private LinkedList<String> artistPageLinks;
	private LinkedList<Artist> touringArtists;

	public TouringArtistGetter() {
		super();
		this.artistPageLinks = new InitialPageGetter().readLinksFromFile();
		this.touringArtists = new LinkedList<Artist>();
	}

	public void crawlForTouringArtists() {
		String artistPageLink = null;
		while ((artistPageLink = getArtistLink()) != null) {
			Document artistPageDocument = getPageSource(artistPageLink);
			if (isArtistTouring(artistPageDocument)) {
				String artistFollowing = getArtistFollowing(artistPageDocument);
				addArtist(artistPageLink, artistFollowing);
			} else {
				continue;
			}
		}
	}

	public synchronized String getArtistLink() {
		try {
			String artistLink = artistPageLinks.remove();
			return artistLink;
		} catch (NullPointerException e) {
			return null;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public synchronized void addArtist(Artist artist) {
		touringArtists.add(artist);
	}

	public Document getPageSource(String link) {
		link = "http://www.songkick.com" + link;
		SourceCodeGetter sourceCodeGetter = new SourceCodeGetter(link);
		Document sourceCodeDocument = sourceCodeGetter.getSource();
		return sourceCodeDocument;
	}

	public boolean isArtistTouring(Document pageSource) {
		String onTouringText = pageSource.select(".ontour strong")
				.text();
		if (onTouringText.contains("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public String getArtistFollowing(Document pageSource) {
		String followingText = pageSource.select(".tracking-count strong")
				.text();
		return followingText;
	}

	public synchronized void addArtist(String artistLink,
			String artistFollowing) {
		Artist artist = new Artist(artistLink, artistFollowing);
		touringArtists.add(artist);
	}

	public LinkedList<Artist> getTouringArtists() {
		return touringArtists;
	}
}
