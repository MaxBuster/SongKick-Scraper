package concertGetter;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import objects.Artist;
import objects.Concert;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sourceCodeGetter.SourceCodeGetter;

public class ConcertGetter {
	private LinkedList<Artist> artists;
	private LinkedList<Concert> concerts;

	public ConcertGetter(LinkedList<Artist> links) {
		super();
		this.artists = links;
		this.concerts = new LinkedList<Concert>();
	}

	public void getConcertLinks() {
		Artist artist = null;
		while ((artist = getArtist()) != null) {
			String artistLink = artist.getLink();
			artistLink = artistLink.replace("?r=touring_stats", "/calendar");
			artistLink = "https://www.songkick.com" + artistLink;

			SourceCodeGetter sourceCodeGetter = new SourceCodeGetter(artistLink);
			Document sourceCodeDocument = sourceCodeGetter.getSource();
			LinkedList<String> concertLinks = getLinksFromDocument(sourceCodeDocument);

			createConcertsAndAddToList(concertLinks, artist);
		}
	}

	public synchronized Artist getArtist() {
		try {
			Artist artist = artists.remove();
			return artist;
		} catch (NullPointerException e) {
			return null;
		} catch (NoSuchElementException f) {
			return null;
		}
	}

	public void createConcertsAndAddToList(LinkedList<String> concertLinks,
			Artist artist) {
		String following = artist.getFollowing();
		for (String concertLink : concertLinks) {
			Concert concert = new Concert(concertLink);
			concert.setArtistFollowing(following);
			addConcertToList(concert);
		}
	}

	public synchronized void addConcertToList(Concert concert) {
		concerts.add(concert);
	}

	public LinkedList<String> getLinksFromDocument(Document sourceCodeDocument) {
		Elements linkContainers = sourceCodeDocument.select("[itemtype=http://schema.org/Event]");
		LinkedList<String> listOfLinks = new LinkedList<String>();
		for (Element linkContainer : linkContainers) {
			String link = getLinkFromContainer(linkContainer);
			boolean isAGoodConcert = checkForGoodConcert(link, linkContainer);
			if (isAGoodConcert) {
				listOfLinks.add(link);
			} 
		}
		return listOfLinks;
	}
	
	public boolean checkForGoodConcert(String link, Element linkContainer) {
		boolean isAConcert = checkForConcert(link);
		boolean isInUS = checkForInUS(linkContainer);
		if (isAConcert && isInUS) {
			return true;
		} else {
			return false;
		}
	}

	public String getLinkFromContainer(Element linkContainer) {
		String link = linkContainer.select(".artists.summary")
				.select("a")
				.attr("href");
		return link;
	}

	public boolean checkForConcert(String link) {
		if (link.contains("concerts")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkForInUS(Element linkContainer) {
		String location = linkContainer.select(".location")
				.text();
		if (location.contains(", US")) {
			return true;
		} else {
			return false;
		}
	}

	public LinkedList<Concert> getConcertList() {
		return concerts;
	}
}
