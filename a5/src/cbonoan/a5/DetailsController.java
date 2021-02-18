package cbonoan.a5;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DetailsController {
    // Labels that show the value of bitcoin and etherium
    // BTC - bitcoin   ETH - etherium
    @FXML
    Label labBTC, labETH;

    public void initialize() {
        labBTC.setText("$48,213.00");
        labETH.setText("$1,832.32");
    }

    // Constructor is called first then initializer
    public DetailsController() {
    }
}
