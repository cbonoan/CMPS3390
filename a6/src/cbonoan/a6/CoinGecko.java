package cbonoan.a6;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * CoinGecko class that uses the CoinGecko api
 * From the api we request for the current price of bitcoin and ethereum as well
 * as the history prices of each coin
 * @author Charles Bonoan
 * @version 1.0
 */
public class CoinGecko {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void updatePriceHistory(Coin coin, int days) {
        coin.getHistoricalValues().getData().clear();
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.coingecko.com/api/v3/coins/"
                        +coin.getName() + "/market_chart?vs_currency=usd&days="
                        + days + "&interval=daily"))
                .setHeader("User-agent", "Java 11 HttpClient cbonoan.a6")
                .build();
        try {
            HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            JSONArray priceArr = obj.getJSONArray("prices");

            for(int i=0; i<priceArr.length(); i++) {
                JSONArray tmpArr = priceArr.getJSONArray(i);
                double tmpVal = tmpArr.getDouble(1);
                coin.addHistoricalValue(i - priceArr.length() + 1, tmpVal);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void updateCurrentPrice(Coin coin) {
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://api.coingecko.com/api/v3/simple/price?ids=" + coin.getName() + "&vs_currencies=usd"))
                .setHeader("User-Agent", "Java 11 HttpClient cbonoan.a6")
                .build();

        try {
            HttpResponse<String> response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonObject = new JSONObject(response.body());
            double value = jsonObject.getJSONObject(coin.getName()).getDouble("usd");
            coin.setCurrentPrice(value);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
