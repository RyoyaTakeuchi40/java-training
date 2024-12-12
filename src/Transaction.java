import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private LocalDateTime tradedDatetime;  // 取引日時
    private String ticker;                // 銘柄
    private String side;                  // 売買区分 (買い / 売り)
    private int quantity;                 // 数量
    private BigDecimal tradedUnitPrice;   // 取引単価
    private LocalDateTime inputDatetime;  // 入力日時

    public Transaction(LocalDateTime tradedDatetime, String ticker, String side, int quantity, BigDecimal tradedUnitPrice) {
        this.tradedDatetime = tradedDatetime;
        this.ticker = ticker;
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