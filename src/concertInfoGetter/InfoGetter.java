package concertInfoGetter;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import objects.Concert;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sourceCodeGetter.SourceCodeGetter;

public class InfoGetter {
	private LinkedList<Concert> infolessConcerts;
	private LinkedList<Concert> completedConcerts;

	public InfoGetter(LinkedList<Concert> infolessConcerts) {
		super();
		this.infolessConcerts = infolessConcerts;
		this.completedConcerts = new LinkedList<Concert>();
	}

	public void getInfo() {
		Concert concert = null;
		while ((concert = getConcert()) != null) {
			String concertLink = concert.getConcertLink();
			Document sourceCodeDocument = getSourceCodeDocument(concertLink);
			String[] info = makeInfoArray(sourceCodeDocument);
			checkInfoForNulls(concert, info);
		}
	}

	public Document getSourceCodeDocument(String concertLink) {
		concertLink = "https://www.songkick.com" + concertLink;
		SourceCodeGetter sourceCodeGetter = new SourceCodeGetter(concertLink);
		Document sourceCodeDocument = sourceCodeGetter.getSource();
		return sourceCodeDocument;
	}

	public String[] makeInfoArray(Document sourceCodeDocument) {
		String[] info = new String[5];
		info[0] = getDate(sourceCodeDocument);
		info[1] = getState(sourceCodeDocument);
		info[2] = getPrimaryPrice(sourceCodeDocument);
		info[3] = getSecondaryPrice(sourceCodeDocument);
		info[4] = getCapacity(sourceCodeDocument);
		return info;
	}

	public void checkInfoForNulls(Concert concert, String[] info) {
		for (String bitOfInfo : info) {
			if (bitOfInfo == null) {
				return;
			}
		}
		addInfoAndPutInFinished(concert, info);
	}

	public synchronized Concert getConcert() {
		try {
			Concert concert = infolessConcerts.remove();
			return concert;
		} catch (NullPointerException e) {
			return null;
		} catch (NoSuchElementException f) {
			return null;
		}
	}

	public void addInfoAndPutInFinished(Concert concert,
			String[] info) {
		concert.setConcertInfo(info);
		addConcertToFinished(concert);
	}

	public synchronized void addConcertToFinished(Concert concert) {
		completedConcerts.add(concert);
	}

	public String getDate(Document sourceCodeDocument) {
		String date = sourceCodeDocument.select(".date-and-name time")
				.attr("datetime");
		if (testForNullData(date)) {
			return null;
		} else {
			date = cutDate(date);
			return date;
		}
	}
	
	public String cutDate(String date) {
		int endIndex = date.indexOf("T");
		date = cutString(date, 0, endIndex);
		return date;
	}

	public String getState(Document sourceCodeDocument) {
		String location = sourceCodeDocument.select("div.location")
				.select(".locality")
				.text();
		if (testForNullData(location)) {
			return null;
		} else {
			String state = getStateFromLocation(location);
			return state;
		}
	}
	
	public String getStateFromLocation(String location) {
		location = location.replace(", US", "");
		int startIndex = location.indexOf(",") + 2;
		int endIndex = location.length();
		String state = cutString(location, startIndex, endIndex);
		return state;
	}

	public String getSecondaryPrice(Document sourceCodeDocument) {
		Elements resellerContainers = sourceCodeDocument
				.select(".ticket-wrapper:has(div.reseller-label)");	
		for (Element reseller : resellerContainers) {
			String price = getSecondaryPriceFromContainer(reseller);
			if (price != null) {
				return price;
			} 
		}
		return null;
	}
	
	public String getSecondaryPriceFromContainer(Element reseller) {
		String price = reseller.select("span.price")
				.text();
		if (testForNullData(price)) {
			return null;
		} else {
			price = cleanUpPrice(price);
			return price;
		}
	}

	public String getPrimaryPrice(Document sourceCodeDocument) {
		Elements primarySellerContainers = getJustPrimarySellerElements(sourceCodeDocument);
		for (Element seller : primarySellerContainers) {
			String price = getPrimaryPriceFromContainer(seller);
			if (price != null) {
				return price;
			}
		}
		return null;
	}
	
	public Elements getJustPrimarySellerElements(Document sourceCodeDocument) {
		Elements allSellerContainers = sourceCodeDocument
				.select(".ticket-wrapper");	
		Elements resellerContainers = allSellerContainers
				.select(":has(div.reseller-label)");
		allSellerContainers.removeAll(resellerContainers);
		return allSellerContainers;
	}
	
	public String getPrimaryPriceFromContainer(Element seller) {
		String price = seller.select("span.price")
				.text();
		if (testForNullData(price)) {
			return null;
		} else {
			price = cleanUpPrice(price);
			return price;
		}
	}

	public String cleanUpPrice(String price) {
		if (price.contains("From US $")) {
			price = price.replace("From US $", "");
		} if (price.contains(" – ")) {
			int startIndex = price.indexOf("$") + 1;
			int endIndex = price.indexOf(" ");
			price = cutString(price, startIndex, endIndex);
		} if (price.contains("US $")){
			price = price.replace("US $", "");
		} if (price.contains(".")) {
			int endIndex = price.indexOf(".");
			price = cutString(price, 0, endIndex);
		}
		return price;
	}

	public String getCapacity(Document sourceCodeDocument) {
		String capacity = sourceCodeDocument.select(".venue-info-details")
				.select(".capacity")
				.text();
		if (testForNullData(capacity)) {
			return null;
		} else {
			capacity = capacity.replace("Capacity: ", "");
			capacity = capacity.replaceAll(",", "");
			return capacity;
		}
	}

	public boolean testForNullData(String info) {
		if (info == null ||
				info.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public LinkedList<Concert> getCompletedConcerts() {
		return completedConcerts;
	}

	public String cutString(String originalString,
			int startIndex,
			int endIndex) {
		if (startIndex != -1 &&
				endIndex != -1 &&
				endIndex > startIndex) {
			String cutString = originalString.substring(startIndex, endIndex);
			return cutString;
		} else {
			return null;
		}
	}
}
