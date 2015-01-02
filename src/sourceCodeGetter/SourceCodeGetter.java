package sourceCodeGetter;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SourceCodeGetter {
	private String link;

	public SourceCodeGetter(String link) {
		super();
		this.link = link;
	}

	public Document getSource() {
		try {
			Document sourceCodeDocument = Jsoup.connect(link)
					.userAgent("Mozilla")
					.timeout(5000)
					.get();
			return sourceCodeDocument;
		} catch (IOException e) {
			Document sourceCodeDocument = null;
			return sourceCodeDocument;
		} 
	}
}
