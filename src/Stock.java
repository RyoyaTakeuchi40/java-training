public class Stock {
    private final String ticker;
    private final String name;
    private final String exchangeMarket;
    private final long sharesIssued;

    public Stock(String ticker, String name, String exchangeMarket, long sharesIssued) {
        this.ticker = ticker;
        this.name = name;
        this.exchangeMarket = exchangeMarket;
        this.sharesIssued = sharesIssued;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public String getExchangeMarket() {
        return exchangeMarket;
    }

    public long getSharesIssued() {
        return sharesIssued;
    }
}