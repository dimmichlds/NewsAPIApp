package gr.unipi.NewsFXApp;

import exception.NewsAPIException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	static Stage primaryStage;
	static Scene newsScene;

	@Override
	public void start(Stage primaryStage) throws NewsAPIException {
		this.primaryStage = primaryStage;
		SceneCreator newsSceneCreator = new NewsScene(800, 650);
		newsScene = newsSceneCreator.createScene();
		primaryStage.setTitle("News API App");
		primaryStage.setScene(newsScene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch();
	}

}