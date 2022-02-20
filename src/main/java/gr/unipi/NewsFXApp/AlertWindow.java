package gr.unipi.NewsFXApp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertWindow {

	// Using this method to display mesagges in catches
	public static void displayAlert(String title, String message) {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(350);

		Label messageLbl = new Label();
		messageLbl.setText(message);
		Button okBtn = new Button("Ok");
		okBtn.setOnAction(e -> window.close());

		VBox layout = new VBox(10);
		layout.getChildren().addAll(messageLbl, okBtn);
		layout.setAlignment(Pos.CENTER);

		Scene alertScene = new Scene(layout);
		window.setScene(alertScene);
		window.showAndWait();

	}

}
