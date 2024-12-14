import java.math.BigDecimal;
import java.math.RoundingMode;

public class Position {
    private String ticker;
    private String stockName;
    private int quantity;
    private BigDecimal avgUnitPrice;
    private BigDecimal realizedPnL;
    private BigDecimal valuation;
    private BigDecimal unrealizedPnL;

    public Position(String ticker, String stockName) {
        this.ticker = ticker;
        this.stockName = stockName;
        this.quantity = 0;
        this.avgUnitPrice = BigDecimal.ZERO;
        this.realizedPnL = BigDecimal.ZERO;
        this.valuation = null;
        this.unrealizedPnL = null;
    }

    public void buy(int quantity, BigDecimal price) {
        BigDecimal totalCost = avgUnitPrice.multiply(BigDecimal.valueOf(this.quantity))
                .add(price.multiply(BigDecimal.valueOf(quantity)));
        this.quantity += quantity;
        avgUnitPrice = totalCost.divide(BigDecimal.valueOf(this.quantity), 2, RoundingMode.HALF_UP);
    }

    public void sell(int quantity, BigDecimal price) {
        if (this.quantity >= quantity) {
            BigDecimal proceeds = price.multiply(BigDecimal.valueOf(quantity));
            BigDecimal cost = avgUnitPrice.multiply(BigDecimal.valueOf(quantity));
            realizedPnL = realizedPnL.add(proceeds.subtract(cost));
            this.quantity -= quantity;
        }
    }

    public void updateValuation(BigDecimal marketPrice) {
        if (marketPrice != null && quantity > 0) {
            valuation = marketPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
            unrealizedPnL = valuation.subtract(avgUnitPrice.multiply(BigDecimal.valueOf(quantity)))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            valuation = null;
            unrealizedPnL = null;
        }
    }

    // Getter methods
    public String getTicker() { return ticker; }

    public String getStockName() { return stockName; }

    public int getQuantity() { return quantity; }

    public BigDecimal getAvgUnitPrice() { return avgUnitPrice; }

    public BigDecimal getRealizedPnL() { return realizedPnL; }

    public BigDecimal getValuation() { return valuation; }

    public BigDecimal getUnrealizedPnL() { return unrealizedPnL; }
}