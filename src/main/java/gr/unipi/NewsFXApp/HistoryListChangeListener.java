package gr.unipi.NewsFXApp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import exception.NewsAPIException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.NewsInfo;
import services.NewsAPIService;

public class HistoryListChangeListener implements ChangeListener<URI> {
	private TextArea resultsNews;
	private ListView<URI> historyList;
	private NewsAPIService service;

	public HistoryListChangeListener(TextArea resultsNews, ListView<URI> historyList, NewsAPIService service) {
		super();
		this.resultsNews = resultsNews;
		this.historyList = historyList;
		this.service = service;

	}

	@Override
	// calling again the query from history List
	public void changed(ObservableValue<? extends URI> observable, URI oldValue, URI newValue) {

		this.resultsNews.clear();
		List<NewsInfo> list = null;
		try {
			list = this.service.getResultFromUri(this.historyList.getSelectionModel().getSelectedItem());
		} catch (NewsAPIException e) {
			AlertWindow.displayAlert("Invalid request", "Please place another request");
			e.printStackTrace();
		} catch (URISyntaxException e) {

			e.printStackTrace();
		}
		List<String> strings = list.stream().map(NewsInfo -> Objects.toString(NewsInfo, null))
				.collect(Collectors.toList());
		for (String li : strings) {
			this.resultsNews.appendText(li);
		}

	}

}
