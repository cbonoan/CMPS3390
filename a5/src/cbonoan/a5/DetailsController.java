package cbonoan.a5;

import cbonoan.a6.Coin;
import cbonoan.a6.UpdateCoinTimerTask;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * When project is ran, this class is called which displays the Details.fxml page to then
 * the user can go to the Chart.fxml or ETH.fxml page
 * This main page will show the current prices of bitcoin and ethereum
 * @author Charles Page
 * @version 1.0
 */
public class DetailsController {
    // Labels that show the value of bitcoin and etherium
    // BTC - bitcoin   ETH - etherium
    @FXML
    Label labBTC, labETH;

    @FXML
    HBox hboxBTC, hboxETH;

    Coin bitcoin, ethereum;

    Timer bitcoinTimer, ethereumTimer;

    public void initialize() {
        this.bitcoin = new Coin("bitcoin");
        this.ethereum = new Coin("ethereum");


        labBTC.textProperty().bind(Bindings.format("$%-10.2f", bitcoin.currentPriceProperty()));
        labETH.textProperty().bind(Bindings.format("$%-10.2f", ethereum.currentPriceProperty()));

        bitcoinTimer = new Timer();
        bitcoinTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new UpdateCoinTimerTask(bitcoin));
            }
        }, 0, 5000);

        ethereumTimer = new Timer();
        ethereumTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new UpdateCoinTimerTask(ethereum));
            }
        }, 0, 5000);
        //labBTC.setText("$" + bitcoin.getCurrentPrice());
        //labETH.setText("$" + ethereum.getCurrentPrice());
    }

    // Constructor is called first then initializer
    public DetailsController() {

    }

    public void onDetailButtonClicked(MouseEvent mouseEvent) throws IOException {
        stopApp();

        Parent root = FXMLLoader.load(getClass().getResource("Chart.fxml"));
        Stage primaryStage = (Stage) hboxBTC.getScene().getWindow();
        primaryStage.setScene(new Scene(root, 700, 475));
    }

    public void stopApp() {
        System.out.println("Closing application... Stopping timers.");
        bitcoinTimer.cancel();
        ethereumTimer.cancel();
    }
}
