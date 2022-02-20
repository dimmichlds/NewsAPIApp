package gr.unipi.NewsFXApp;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import call.NewsApi;
import exception.NewsAPIException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import model.NewsInfo;
import services.NewsAPIService;

public class NewsScene extends SceneCreator implements EventHandler<ActionEvent> {

	FlowPane buttonFlowPane;

	Button searchBtn, clearBtn;

	GridPane inputFieldsPane, allBtnsPane;

	ComboBox<String> categoryBox, countryBox, languageBox, sortByBox;

	Label categoryLbl, countryLbl, languageLbl, sortByLbl, qLbl, sourceLbl, fromLbl, toLbl;

	TextField qText, sourcesText;

	DatePicker from, to;

	ObservableList<URI> urlHistoryList = FXCollections.observableArrayList();

	BorderPane pane;

	RadioButton topHeadlines, allNews;

	final Menu menu;

	MenuBar exit;

	Hashtable<String, String> query;

	TextArea resultsNews;

	LocalDate today;

	ListView<URI> historyList;

	SplitPane centerBox;

	public NewsScene(double width, double height) throws NewsAPIException {
		super(width, height);
		// initialize
		pane = new BorderPane();
		buttonFlowPane = new FlowPane();
		query = new Hashtable<>();
		resultsNews = new TextArea();
		inputFieldsPane = new GridPane();
		allBtnsPane = new GridPane();
		historyList = new ListView<URI>();

		// initialize labels
		categoryLbl = new Label("Category");
		countryLbl = new Label("Country");
		languageLbl = new Label("Language");
		sortByLbl = new Label("Sort By");
		qLbl = new Label("Keyword/Phrase");
		sourceLbl = new Label("Source");
		fromLbl = new Label("From Date");
		toLbl = new Label("To Date");

		// initialize-set up-fill in all combo boxes
		categoryBox = new ComboBox<String>();
		countryBox = new ComboBox<String>();
		languageBox = new ComboBox<String>();
		sortByBox = new ComboBox<String>();
		categoryBox.setMinSize(120, 30);
		countryBox.setMinSize(120, 30);
		languageBox.setMinSize(120, 30);
		sortByBox.setMinSize(120, 30);
		countryBox.getItems().addAll("", "ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz",
				"de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma",
				"mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk",
				"th", "tr", "tw", "ua", "us", "ve", "za");
		categoryBox.getItems().addAll(" ", "business", "entertainment", "general", "health", "science", "sports",
				"technology");

		languageBox.getItems().addAll("", "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "se", "ud",
				"zh");
		sortByBox.getItems().addAll("", "relevancy", "popularity", "publishedAt");
		// giving the country code from IP API
		countryBox.setValue(getCountryCodeByIp().toLowerCase());
		categoryBox.setValue("");
		languageBox.setValue("");
		sortByBox.setValue("");

		// initialize text fields
		qText = new TextField();
		sourcesText = new TextField();
		qText.setText("");
		sourcesText.setText("");

		// initialize and set up buttons
		searchBtn = new Button("Search");
		clearBtn = new Button("Clear");
		searchBtn.setMinSize(100, 40);
		clearBtn.setMinSize(50, 40);
		buttonFlowPane.setAlignment(Pos.CENTER_LEFT);
		buttonFlowPane.getChildren().addAll(searchBtn, clearBtn);
		buttonFlowPane.setHgap(0);
		buttonFlowPane.autosize();

		// initialize and set up Radio buttons
		final ToggleGroup group = new ToggleGroup();
		topHeadlines = new RadioButton();
		allNews = new RadioButton();
		topHeadlines.setText("Top Headline");
		allNews.setText("All News");
		topHeadlines.setToggleGroup(group);
		allNews.setToggleGroup(group);
		allNews.setSelected(true);

		// create exit menu
		exit = new MenuBar();
		menu = new Menu("File");
		exit.getMenus().add(menu);
		exit.getContextMenu();
		MenuItem exitApp = new MenuItem();
		menu.getItems().add(exitApp);
		exitApp.setText("Exit");
		exitApp.setOnAction(e -> App.primaryStage.close());

		// initialize and set up with local date the date pickers
		from = new DatePicker();
		to = new DatePicker();
		from.setValue(LocalDate.now());
		to.setValue(LocalDate.now());
		// Functions.dateFormat(from);
		// Functions.dateFormat(to);

		to.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		from.valueProperty().addListener(LocalDate -> {
			AlertWindow.displayAlert("date", "Set a range of Dates \n" + " Fill the date " + "'To'" + "field");
		});

		// Setting up grid pane
		inputFieldsPane.setAlignment(Pos.TOP_RIGHT);
		inputFieldsPane.setVgap(20);
		inputFieldsPane.setHgap(20);
		inputFieldsPane.add(topHeadlines, 0, 1);
		inputFieldsPane.add(allNews, 1, 1);
		inputFieldsPane.add(categoryLbl, 0, 2);
		inputFieldsPane.add(categoryBox, 1, 2);
		inputFieldsPane.add(countryLbl, 0, 3);
		inputFieldsPane.add(countryBox, 1, 3);
		inputFieldsPane.add(languageLbl, 0, 4);
		inputFieldsPane.add(languageBox, 1, 4);
		inputFieldsPane.add(sortByLbl, 0, 5);
		inputFieldsPane.add(sortByBox, 1, 5);
		inputFieldsPane.add(qLbl, 0, 6);
		inputFieldsPane.add(qText, 1, 6);
		inputFieldsPane.add(sourceLbl, 0, 7);
		inputFieldsPane.add(sourcesText, 1, 7);
		inputFieldsPane.add(fromLbl, 0, 8);
		inputFieldsPane.add(from, 1, 8);
		inputFieldsPane.add(toLbl, 0, 9);
		inputFieldsPane.add(to, 1, 9);
		inputFieldsPane.add(searchBtn, 0, 10);
		inputFieldsPane.add(clearBtn, 1, 10);

		// Listener to history list
		HistoryListChangeListener history = new HistoryListChangeListener(resultsNews, historyList,
				NewsApi.getNewsAPIService());
		historyList.setMaxHeight(120);
		historyList.getSelectionModel().selectedItemProperty().addListener(history);

		// Setting up Border Pane
		pane.setRight(inputFieldsPane);
		BorderPane.setAlignment(inputFieldsPane, Pos.CENTER_RIGHT);
		BorderPane.setMargin(inputFieldsPane, new Insets(12, 10, 10, 10));
		pane.setCenter(resultsNews);
		pane.setTop(exit);
		pane.setBottom(historyList);

		// Attach events
		searchBtn.setOnAction(this);
		topHeadlines.setOnAction(this);
		allNews.setOnAction(this);
		clearBtn.setOnAction(this);
		disableAllKeys();
	}

	@Override
	Scene createScene() {
		return new Scene(pane, width, height);
	}

	@Override
	public void handle(ActionEvent event) {
		resultsNews.clear();
		if (event.getSource() == searchBtn) {
			// checking the input data
			if (topHeadlines.isSelected() == true) {
				query.clear();
				String q = qText.getText();
				String category = categoryBox.getValue();
				String country = countryBox.getValue();
				if (!q.isBlank())
					query.put("q", q);
				if (!category.isBlank())
					query.put("category", category);
				if (!country.isBlank())
					query.put("country", country);
				// Built and saving uri query
				URI url = null;
				try {
					url = NewsApi.getNewsAPIService().setURIquery("top-headlines", query,
							NewsApi.getNewsAPIService().API_URL, NewsApi.getNewsAPIService().API_KEY);
				} catch (NewsAPIException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				urlHistoryList.add(url);
				System.out.println(urlHistoryList);
				historyList.setItems(urlHistoryList);
				/// List<NewsInfo> to List<String> for Top Headlines
				List<NewsInfo> list = null;
				try {
					list = newsTopHeadlinesResults(query);
				} catch (NewsAPIException e) {
					AlertWindow.displayAlert("Invalid request", "Please place another request");
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				List<String> strings = list.stream().map(NewsInfo -> Objects.toString(NewsInfo, null))
						.collect(Collectors.toList());
				for (String li : strings) {
					resultsNews.appendText(li);
				}
				// checking the input data
			} else if (allNews.isSelected() == true) {
				query.clear();
				String q = qText.getText();
				String sources = sourcesText.getText();
				String sortBy = sortByBox.getValue();
				String language = languageBox.getValue();
				String fromString = from.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String toString = to.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				if (!q.isBlank())
					query.put("q", q);
				if (!sources.isBlank())
					query.put("sources", sources);
				if (!sortBy.isBlank())
					query.put("sortBy", sortBy);
				if (!language.isBlank())
					query.put("language", language);
				if (!fromString.isBlank())
					query.put("from", fromString);
				if (!toString.isBlank())
					query.put("to", toString);
				// Built and saving uri query
				URI url = null;
				try {
					url = NewsApi.getNewsAPIService().setURIquery("top-headlines", query,
							NewsApi.getNewsAPIService().API_URL, NewsApi.getNewsAPIService().API_KEY);
				} catch (NewsAPIException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				urlHistoryList.add(url);
				System.out.println(urlHistoryList);
				historyList.setItems(urlHistoryList);
				/// List<NewsInfo> to List<String> for Top Headlines
				List<NewsInfo> list = null;
				try {
					System.out.println(query);
					list = newsAllResults(query);
				} catch (NewsAPIException e) {

					e.printStackTrace();
					AlertWindow.displayAlert("Invalid query", "Please place another query");
				} catch (URISyntaxException e) {

					e.printStackTrace();
					AlertWindow.displayAlert("Invalid query", "Please place another query");
				}
				List<String> strings = list.stream().map(NewsInfo -> Objects.toString(NewsInfo, null))
						.collect(Collectors.toList());
				for (String li : strings) {
					resultsNews.appendText(li);
				}

			}

		}

		if (topHeadlines.isSelected() == true) {
			disableKeysForAll();
			enableKeysForTop();
		} else if (topHeadlines.isSelected() == false) {
			disableKeysForTop();
			enableKeysForAll();
		}
		if (event.getSource() == topHeadlines) {

			Functions.clearAll(countryBox, resultsNews, categoryBox, languageBox, qText, sourcesText);
		}
		if (event.getSource() == allNews) {

			Functions.clearAll(countryBox, resultsNews, categoryBox, languageBox, qText, sourcesText);
		}
		if (event.getSource() == clearBtn) {
			Functions.clearAll(countryBox, resultsNews, categoryBox, languageBox, qText, sourcesText);
		}
	}

	// method to call NewsAPIService for top headlines
	public List<NewsInfo> newsTopHeadlinesResults(Hashtable<String, String> res)
			throws NewsAPIException, URISyntaxException {

		final NewsAPIService topHeadLines = NewsApi.getNewsAPIService();
		List<NewsInfo> results = null;
		try {
			results = topHeadLines.getTopHeadlines(res);
		} catch (NewsAPIException | URISyntaxException e) {
			e.printStackTrace();
			AlertWindow.displayAlert("Invalid query", "Please place another query");
		}
		if (results.isEmpty())
			AlertWindow.displayAlert("No news",
					"There are not any news about this query. \n          Please place another request.");
		return results;

	}

	// method to call NewsAPIService for all news(everything)
	public List<NewsInfo> newsAllResults(Hashtable<String, String> res) throws NewsAPIException, URISyntaxException {

		final NewsAPIService allNews = NewsApi.getNewsAPIService();
		List<NewsInfo> results = null;
		try {
			results = allNews.getAllnews(res);
		} catch (NewsAPIException e) {
			e.printStackTrace();
			AlertWindow.displayAlert("Invalid query", "Please place another query");
		}
		if (results.isEmpty())
			AlertWindow.displayAlert("No news",
					"There are not any news about this query. \n          Please place another request.");

		return results;

	}

	// method to call NewsAPIService for getting CoutryCode by IP API
	public static String getCountryCodeByIp() throws NewsAPIException {
		String result = NewsApi.getNewsAPIService().getCountryCodeByIp();
		return result;
	}

	public void disableKeysForAll() {
		sortByBox.setDisable(true);
		languageBox.setDisable(true);
		from.setDisable(true);
		to.setDisable(true);
		sourcesText.setDisable(true);
	}

	public void enableKeysForAll() {
		sortByBox.setDisable(false);
		languageBox.setDisable(false);
		from.setDisable(false);
		to.setDisable(false);
		sourcesText.setDisable(false);
		qText.setDisable(false);
	}

	public void disableKeysForTop() {
		categoryBox.setDisable(true);
		countryBox.setDisable(true);

	}

	public void enableKeysForTop() {
		categoryBox.setDisable(false);
		countryBox.setDisable(false);
		qText.setDisable(false);

	}

	public void disableAllKeys() {
		this.categoryBox.setDisable(true);
		this.countryBox.setDisable(true);
		this.sortByBox.setDisable(true);
		this.languageBox.setDisable(true);
		this.from.setDisable(true);
		this.to.setDisable(true);
		this.sourcesText.setDisable(true);
		this.qText.setDisable(true);
	}

}
