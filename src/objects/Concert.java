package objects;

public class Concert {
	private String concertLink;
	private String[] concertInfo;
	
	public Concert(String concertLink) {
		super();
		this.concertLink = concertLink;
		this.concertInfo = new String[6];
	}	
	
	public String getConcertLink() {
		return concertLink;
	}
	
	public String[] getConcertInfo() {
		return concertInfo;
	}
	
	public void setArtistFollowing(String following) {
		concertInfo[0] = following;
	}
	
	public void setConcertInfo(String[] info) {
		for (int i=0; i<5; i++) {
			concertInfo[i+1] = info[i];
		}
	}
}
