package objects;

public class Artist {
	private String pageLink;
	private String following;
	
	public Artist(String pageLink, 
			String following) {
		super();
		this.pageLink = pageLink;
		this.following = following;
	}
	
	public String getLink() {
		return pageLink;
	}
	
	public String getFollowing() {
		return following;
	}
}
