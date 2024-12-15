import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TablePrinter {

    public void printStocks(List<Stock> stocks) {
        if (stocks.isEmpty()) {
            System.out.println("登録された銘柄はありません。");
            return;
        }

        String borderLine = "-".repeat(61);
        System.out.println(borderLine);
        System.out.printf("| %-6s | %-18s | %-10s | %14s |\n",
                "Ticker", "Name", "Market", "Shares Issued");
        System.out.println(borderLine);
        for (Stock stock : stocks) {
            System.out.printf("| %-6s | %-18s | %-10s | %,14d |\n",
                    stock.getTicker(),
                    formatName(stock.getName()),
                    stock.getExchangeMarket(),
                    stock.getSharesIssued());
        }
        System.out.println(borderLine);
    }

    public void printTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("取引履歴はありません。");
            return;
        }

        transactions.sort(Comparator.comparing(Transaction::getTradedDatetime).reversed());
        String borderLine = "-".repeat(92);
        System.out.println(borderLine);
        System.out.printf("| %-16s | %-6s | %-18s | %-4s | %-14s | %-15s |\n",
                "TradedDatetime", "Ticker", "Name", "Side","Quantity","TradedUnitPrice");
        System.out.println(borderLine);
        for (Transaction transaction : transactions) {
            System.out.printf("| %-16s | %-6s | %-18s | %-3s | %-14d | %15.2f |\n",
                    transaction.getTradedDatetime(),
                    transaction.getTicker(),
                    formatName(transaction.getStockName()),
                    transaction.getSide(),
                    transaction.getQuantity(),
                    transaction.getTradedUnitPrice());
        }
        System.out.println(borderLine);
    }

    public void printPositions(List<Position> positions) {
        if (positions.isEmpty()) {
            System.out.println("保有ポジションはありません。");
            return;
        }

        positions.sort(Comparator.comparing(Position::getTicker));
        String borderLine = "-".repeat(100);
        System.out.println(borderLine);
        System.out.printf("| %-6s | %-18s | %14s | %14s | %14s | %14s |\n",
                "Ticker", "Name", "Quantity", "Avg Price", "Valuation", "Unrealized PnL");
        System.out.println(borderLine);
        for (Position position : positions) {
            System.out.printf("| %-6s | %-18s | %,14d | %,14.2f | %14s | %14s |\n",
                    position.getTicker(),
                    formatName(position.getStockName()),
                    position.getQuantity(),
                    position.getAvgUnitPrice(),
                    formatCurrency(position.getValuation()),
                    formatCurrency(position.getUnrealizedPnL())
            );
        }
        System.out.println(borderLine);
    }

    public void printMarketPrices(Map<String, BigDecimal> marketPrices, Map<String, String> stockMap) {
        String borderLine = "-".repeat(44);
        System.out.println(borderLine);
        System.out.printf("| %-6s | %-18s | %10s |\n", "Ticker", "Name", "Price");
        System.out.println(borderLine);
        for (Map.Entry<String, BigDecimal> entry : marketPrices.entrySet()) {
            String ticker = entry.getKey();
            BigDecimal price = entry.getValue();
            System.out.printf("| %-6s | %-18s | %,10.2f |\n",
                    ticker, formatName(stockMap.get(ticker)), price);
        }
        System.out.println(borderLine);
    }

    private String formatName(String name) {
        final int maxLen = 18;
        if (name.length() > maxLen) {
            return name.substring(0, maxLen - 2) + "..";
        }
        return name;
    }

    private String formatCurrency(BigDecimal value) {
        return value == null ? "N/A" : String.format("%,.2f", value);
    }

}


