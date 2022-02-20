package model;

import model.thenewsdb.Article;

public class NewsInfo {

	private String title;
	private String description;
	private String date;
	private String url;

	public NewsInfo(String title, String description, String date, String url) {
		this.title = title;
		this.description = description;
		this.date = date;
		this.url = url;
	}

	public NewsInfo(Article theArticle) {
		this.title = theArticle.getTitle();
		this.description = theArticle.getDescription();
		this.date = theArticle.getPublishedAt();
		this.url = theArticle.getUrl();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {

		return "===================================================================" + "\n" + "Title: " + title + "\n"
				+ "Description: " + description + "\n" + "Date :" + date + "\n" + "url :" + url + "\n";
	}

}
