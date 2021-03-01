package cbonoan.a5;

import cbonoan.a6.Coin;
import cbonoan.a6.CoinGecko;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;

/**
 * Displays a chart of the price of bitcoin over the span of given days
 * @author Charles Bonoan
 * @version 1.0
 */
public class ChartController {
    @FXML
    ImageView btnBackArrow;

    @FXML
    LineChart<Number, Number> priceChart;

    @FXML
    ComboBox cbDateRange;

    @FXML
    ComboBox cbCoinSelector;

    @FXML
    ImageView imgViewCoinLogo;

    File btcFile, ethFile;
    Image btcImg, ethImg;
    Coin bitcoin, ethereum;
    public void initialize() throws FileNotFoundException {
        priceChart.setAnimated(false);

        // Initialize logos of coins in order to change the
        // logo of chart whenever changed
        //btcFile = new File("assets/Bitcoin.png");
        btcImg = new Image(new FileInputStream("src/cbonoan/a5/assets/Bitcoin.png"));
        //ethFile = new File("/home/student/CMPS3390/a5/src/cbonoan/a5/assets/Ethereum.png");
        ethImg = new Image(new FileInputStream("src/cbonoan/a5/assets/Ethereum.png"));
        imgViewCoinLogo = new ImageView();
        imgViewCoinLogo.setImage(btcImg);

        bitcoin = new Coin("bitcoin");
        ethereum = new Coin("ethereum");

        CoinGecko.updatePriceHistory(bitcoin, 365);
        CoinGecko.updatePriceHistory(ethereum, 365);

        priceChart.getData().add(bitcoin.getHistoricalValues());
    }

    public void onBackArrowClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Details.fxml"));
        Stage primaryStage = (Stage) btnBackArrow.getScene().getWindow();
        primaryStage.setScene(new Scene(root, 700, 475));
    }

    public void onDateRangeChange(ActionEvent actionEvent) throws InterruptedException {
        int days = 0;
        switch((String)cbDateRange.getValue()) {
            case "Year":
                days = 365;
                break;
            case "90 Days":
                days = 90;
                break;
            case "60 Days":
                days = 60;
                break;
            case "30 Days":
                days = 30;
                break;
            case "Week":
                days = 7;
                break;
        }
        CoinGecko.updatePriceHistory(bitcoin, days);
        CoinGecko.updatePriceHistory(ethereum, days);
        updateChart();
    }

    public void onCoinSelectionChange(ActionEvent actionEvent) throws InterruptedException {
        updateChart();
    }

    private void updateChart() throws InterruptedException {
        priceChart.getData().clear();
        switch((String) cbCoinSelector.getValue()) {
            case "Bitcoin":
                imgViewCoinLogo.setImage(btcImg);
                priceChart.getData().add(bitcoin.getHistoricalValues());
                break;
            case "Ethereum":
                imgViewCoinLogo.setImage(ethImg);
                priceChart.getData().add(ethereum.getHistoricalValues());
                break;
            case "All":
                priceChart.getData().add(bitcoin.getHistoricalValues());
                priceChart.getData().add(ethereum.getHistoricalValues());
                break;
        }

    }

}
