public class Stock {
    private String ticker;
    private String name;
    private String exchangeMarket;
    private long sharesIssued;

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