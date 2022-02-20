package gr.unipi.NewsFXApp;


import exception.NewsAPIException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Functions {

	//Clearing all fields
	public static  void clearAll(ComboBox country, TextArea news ,ComboBox category,ComboBox language, TextField text, TextField sText  ) {
		
		try {
			country.setValue(NewsScene.getCountryCodeByIp().toLowerCase());
		} catch (NewsAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		news.clear();
		category.setValue("");
		language.setValue("");
		text.setText("");
		sText.setText("");
	}
	
	
}
