import java.math.BigDecimal;

public class MarketPrice {

    private String ticker;         // 銘柄コード
    private String name;           // 銘柄名
    private BigDecimal marketPrice; // 時価

    // コンストラクタ
    public MarketPrice(String ticker, String name, BigDecimal marketPrice) {
        this.ticker = ticker;
        this.name = name;
        this.marketPrice = marketPrice;
    }

    // ゲッターとセッター
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    // 表示用のメソッド
    @Override
    public String toString() {
        return String.format("| %-6s | %-20s | %,10.2f |", ticker, name, marketPrice);
    }
}