package services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.NewsAPIException;
import model.NewsInfo;
import model.thenewsdb.Article;
import model.thenewsdb.ErrorResponse;
import model.thenewsdb.NewsResult;
import model.thenewsdb.IpAddress;

public class NewsAPIService {
	public final String API_URL;
	public final String API_KEY;
	private final String API_IP;

	public NewsAPIService(String aPI_URL, String aPI_KEY, String aPI_IP) {
		super();
		API_URL = aPI_URL;
		API_KEY = aPI_KEY;
		API_IP = aPI_IP;
	}

	// get top headlines with attributes
	// https://newsapi.org/v2/top-headlines?
	public List<NewsInfo> getTopHeadlines(Hashtable<String, String> params)
			throws NewsAPIException, URISyntaxException {
		URI uri = setURIquery("top-headlines", params, API_URL, API_KEY);
		NewsResult result = getAPIData(uri);
		List<NewsInfo> newsInfoList = new ArrayList<>(result.getArticles().size());
		for (Article theArticle : result.getArticles()) {
			newsInfoList.add(new NewsInfo(theArticle));
		}
		return newsInfoList;
	}

	// get all news with attributes
	// https://newsapi.org/v2/everything?
	public List<NewsInfo> getAllnews(Hashtable<String, String> params) throws NewsAPIException, URISyntaxException {
		URI uri = setURIquery("everything", params, API_URL, API_KEY);
		System.out.println(uri);
		NewsResult result = getAPIData(uri);
		List<NewsInfo> newsInfoList = new ArrayList<>(result.getArticles().size());
		for (Article theArticle : result.getArticles()) {
			newsInfoList.add(new NewsInfo(theArticle));
		}
		return newsInfoList;
	}

	// Get countryCode by IP address from IP API
	public String getCountryCodeByIp() throws NewsAPIException {

		IpAddress ip = getAPIIp(API_IP);
		String res = ip.getCountryCode().toString();
		return res;
	}

	// Getting the results from a URI(used for the search history)
	public List<NewsInfo> getResultFromUri(URI historyUrl) throws NewsAPIException, URISyntaxException {
		NewsResult result = getAPIData(historyUrl);
		List<NewsInfo> newsInfoList = new ArrayList<>(result.getArticles().size());
		for (Article theArticle : result.getArticles()) {
			newsInfoList.add(new NewsInfo(theArticle));
		}
		return newsInfoList;

	}

	// Adding attributes to the URI and builds it
	public URI setURIquery(String apiFunction, Hashtable<String, String> params, String API_URL, String API_KEY)
			throws NewsAPIException, URISyntaxException {

		final URIBuilder uriBuilder = new URIBuilder(API_URL)// https://newsapi.org/
				.setPathSegments("v2", apiFunction)// https://newsapi.org/v2/everything
				.addParameter("apiKey", API_KEY);// https://newsapi.org/v2/everything?apiKey=722b78bc9b184c5e9cf0fd0c931cd6ea
		if (!params.isEmpty()) {
			switch (apiFunction) {
			case "everything":
				params.forEach((k, v) -> {
					uriBuilder.addParameter(k, v);
				});
				break;

			case "top-headlines":
				params.forEach((k, v) -> {
					uriBuilder.addParameter(k, v);
				});
				break;
			}
		}
		final URI uri = uriBuilder.build();
		return uri;
	}

	// Using http client to call NEWS API with the given URI and return the results
	public NewsResult getAPIData(URI uri) throws NewsAPIException, URISyntaxException {
		final HttpGet getRequest = new HttpGet(uri);
		final CloseableHttpClient httpclient = HttpClients.createDefault();
		try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
			final HttpEntity entity = response.getEntity();
			final ObjectMapper mapper = new ObjectMapper();
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				ErrorResponse errorResponse = mapper.readValue(entity.getContent(), ErrorResponse.class);
				if (errorResponse.getStatus() != null)
					throw new NewsAPIException("Error occurred on API call: " + errorResponse.getStatus());
			}
			return mapper.readValue(entity.getContent(), NewsResult.class);
		} catch (IOException e) {
			throw new NewsAPIException("Error requesting data from theNewsDB API.", e);
		}
	}

	// Using http client to call IP API and get IP's details(CountryCode)
	private IpAddress getAPIIp(String API_IP) throws NewsAPIException {
		try {
			final URIBuilder uriBuilder = new URIBuilder(API_IP);
			final URI uri = uriBuilder.build();
			final HttpGet getRequest = new HttpGet(uri);
			final CloseableHttpClient httpclient = HttpClients.createDefault();
			try (CloseableHttpResponse response = httpclient.execute(getRequest)) {
				final HttpEntity entity = response.getEntity();
				final ObjectMapper mapper = new ObjectMapper();
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					ErrorResponse errorResponse = mapper.readValue(entity.getContent(), ErrorResponse.class);
					if (errorResponse.getStatus() != null)
						throw new NewsAPIException("Error occurred on API call: " + errorResponse.getStatus());
				}
				return mapper.readValue(entity.getContent(), IpAddress.class);
			} catch (IOException e) {
				throw new NewsAPIException("Error requesting data from theNewsDB API.", e);
			}
		} catch (URISyntaxException e) {
			throw new NewsAPIException("Unable to create request URI.", e);
		}

	}

}
