package gr.unipi.NewsFXApp;

import javafx.scene.Scene;

public abstract class SceneCreator {

	double width;
	double height;
	public SceneCreator(double width, double height) {
		super();
		this.width = width;
		this.height = height;
	}
	
	abstract Scene createScene();
}
