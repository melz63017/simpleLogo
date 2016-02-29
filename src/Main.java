import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public Main() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void start(Stage primaryStage) {

		ArrayList<String> viewlist = new ArrayList<String>();
		viewlist.add("History");
		viewlist.add("Console");
		viewlist.add("SavedVar");
		viewlist.add("SavedMethod");
		viewlist.add("Agent");
		Workspace UI = new Workspace(viewlist);
		Scene myScene = UI.init();
		myScene.getStylesheets().add("resources/style/style.css");
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }

}