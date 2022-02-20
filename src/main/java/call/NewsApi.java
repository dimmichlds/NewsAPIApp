package call;

import services.NewsAPIService;

public class NewsApi {
	// Call the service with attributes
	public static NewsAPIService getNewsAPIService() {
		return new NewsAPIService("https://newsapi.org/", "722b78bc9b184c5e9cf0fd0c931cd6ea",
				"http://ip-api.com/json?fields=countryCode");
	}

}
