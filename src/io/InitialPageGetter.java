package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class InitialPageGetter {
	private String fileName;
	private LinkedList<String> listOfLinks;

	public InitialPageGetter() {
		super();
		this.fileName = "artistLinks.txt";
		this.listOfLinks = new LinkedList<String>();
	}

	public LinkedList<String> readLinksFromFile() {
		BufferedReader buffReaderForLinksFile = createBufferedReader();
		while (readNextLine(buffReaderForLinksFile) != false) {
		}
		closeBufferedReader(buffReaderForLinksFile);
		return listOfLinks;
	}

	public BufferedReader createBufferedReader() {
		try {
			File fileContainingLinks = new File(fileName);
			FileReader readerForFileLinks = new FileReader(fileContainingLinks);
			BufferedReader buffReaderForFileLinks = new BufferedReader(readerForFileLinks);
			return buffReaderForFileLinks;
		} catch (FileNotFoundException e) {
			return null;
		} 
	}

	public boolean readNextLine(BufferedReader buffReaderForFileLinks) {
		try {
			String nextLink = buffReaderForFileLinks.readLine();
			boolean linkIsValid = checkIfLinkIsValid(nextLink);
			if (linkIsValid) {
				addLinkToList(nextLink);
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	public boolean checkIfLinkIsValid(String nextLink) {
		if (nextLink == null) {
			return false;
		} else {
			return true;
		}
	}

	public void addLinkToList(String link) {
		listOfLinks.add(link);
	}

	public void closeBufferedReader(BufferedReader buffReaderForFileLinks) {
		try {
			buffReaderForFileLinks.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
