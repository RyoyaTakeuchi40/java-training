import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final LocalDateTime tradedDatetime;  // 取引日時
    private final String ticker;                // 銘柄
    private final String stockName;             // 株式銘柄
    private final String side;                  // 売買区分 (買い / 売り)
    private final int quantity;                 // 数量
    private final BigDecimal tradedUnitPrice;   // 取引単価
    private final LocalDateTime inputDatetime;  // 入力日時

    public Transaction(LocalDateTime tradedDatetime, String ticker, String stockName, String side, int quantity, BigDecimal tradedUnitPrice) {
        this.tradedDatetime = tradedDatetime;
        this.ticker = ticker;
        this.stockName = stockName;
        this.side = side;
        this.quantity = quantity;
        this.tradedUnitPrice = tradedUnitPrice;
        this.inputDatetime = LocalDateTime.now();  // 現在の時刻を入力日時として設定
    }

    // Getters and Setters
    public LocalDateTime getTradedDatetime() {
        return tradedDatetime;
    }

    public String getTicker() {
        return ticker;
    }

    public String getStockName() {
        return stockName;
    }

    public String getSide() {
        return side;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTradedUnitPrice() {
        return tradedUnitPrice;
    }

    public LocalDateTime getInputDatetime() {
        return inputDatetime;
    }
}