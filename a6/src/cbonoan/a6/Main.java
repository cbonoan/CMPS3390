package cbonoan.a6;

public class Main {

    public static void main(String[] args) {

        Coin bitcoin = new Coin("bitcoin");
        Coin ethereum = new Coin("ethereum");
        CoinGecko.updateCurrentPrice(bitcoin);
        CoinGecko.updateCurrentPrice(ethereum);
        CoinGecko.updatePriceHistory(bitcoin, 7);
        System.out.println(bitcoin);
        System.out.println(ethereum);
    }
}
