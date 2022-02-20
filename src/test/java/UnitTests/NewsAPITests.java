package UnitTests;



import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import call.NewsApi;
import exception.NewsAPIException;
import model.NewsInfo;
import services.NewsAPIService;

public class NewsAPITests {

	

	@Test
	public void testTopHeadLinesByCountry() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("country", "us");
		final NewsAPIService topHeadLinesByCountry = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = topHeadLinesByCountry.getTopHeadlines(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testTopHeadLinesCategory() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("category", "sports");
		final NewsAPIService topHeadLinesCategory = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = topHeadLinesCategory.getTopHeadlines(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testAllNewsByKeyword() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("q", "bitcoin");
		final NewsAPIService allNewsByKeyword = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = allNewsByKeyword.getAllnews(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testAllNewsBySource() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("sources", "the-washington-post");
		final NewsAPIService allNewsBySource = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = allNewsBySource.getAllnews(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testAllNewsKeywordLanguage() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("q", "covid");
		testparam.put("language", "es");
		final NewsAPIService allNewsKeywordLanguage = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = allNewsKeywordLanguage.getAllnews(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

	@Test
	public void testAllNewsKeywordLanguageDate() throws NewsAPIException, URISyntaxException {
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("q", "tesla");
		testparam.put("language", "de");
		testparam.put("from", "2022-02-12");
		testparam.put("to", "2022-02-15");
		final NewsAPIService allNewsKeywordLanguageDate = NewsApi.getNewsAPIService();
		final List<NewsInfo> results = allNewsKeywordLanguageDate.getAllnews(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}
	
	@Test
	public void testTopHeadLinesByIp() throws NewsAPIException, URISyntaxException {
		final NewsAPIService topHeadLinesByIp = NewsApi.getNewsAPIService();
		Hashtable<String, String> testparam = new Hashtable<>();
		testparam.put("country",topHeadLinesByIp.getCountryCodeByIp() );
		final List<NewsInfo> results = topHeadLinesByIp.getTopHeadlines(testparam);
		Assert.assertFalse(results.isEmpty());
		results.forEach(System.out::println);
	}

}
