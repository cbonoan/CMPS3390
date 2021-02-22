package cbonoan.a5;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailsController {
    // Labels that show the value of bitcoin and etherium
    // BTC - bitcoin   ETH - etherium
    @FXML
    Label labBTC, labETH;

    @FXML
    HBox hboxBTC, hboxETH;

    public void initialize() {
        labBTC.setText("$48,213.00");
        labETH.setText("$1,832.32");
    }

    // Constructor is called first then initializer
    public DetailsController() {

    }

    public void onDetailButtonClicked(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getSource() == hboxBTC) {
            System.out.println("Changed to BTC");

            Parent root = FXMLLoader.load(getClass().getResource("BTC.fxml"));
            Stage primaryStage = (Stage) hboxBTC.getScene().getWindow();
            primaryStage.setScene(new Scene(root, 700, 475));
        } else if(mouseEvent.getSource() == hboxETH) {
            System.out.println("Changed to ETH");

            Parent root = FXMLLoader.load(getClass().getResource("ETH.fxml"));
            Stage primaryStage = (Stage) hboxETH.getScene().getWindow();
            primaryStage.setScene(new Scene(root, 700, 475));
        }
    }
}
