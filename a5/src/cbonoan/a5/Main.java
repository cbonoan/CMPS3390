package cbonoan.a5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main driver class for a5
 * @author Charles Bonoan
 * @version 1.0
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Details.fxml"));
        Parent root = loader.load();
        DetailsController controller = loader.getController();
        Scene scene = new Scene(root, 700, 475);
        primaryStage.setTitle("Coin Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnHidden(e -> controller.stopApp());

    }


    public static void main(String[] args) {
        launch(args);
    }
}
