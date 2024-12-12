import java.util.List;
import java.util.Map;
import java.util.Comparator;

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

    public void printHoldings(Map<String, Integer> holdingQuantities, Map<String, String>holdingStocks) {
        if (holdingStocks.isEmpty()) {
            System.out.println("保有ポジションはありません。");
            return;
        }

        String borderLine = "-".repeat(48);
        System.out.println(borderLine);
        System.out.printf("| %-6s | %-18s | %-14s |\n",
                "Ticker", "Name", "Quantity");
        System.out.println(borderLine);
        // 銘柄コードの昇順で並べ替えて表示
        holdingQuantities.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(new TickerComparator()))
                .forEach(entry -> {
                    String ticker = entry.getKey();
                    String stockName = formatName(holdingStocks.get(ticker));
                    int quantity = entry.getValue();
                    System.out.printf("| %-6s | %-18s | %,14d |\n",
                            ticker, stockName, quantity);
                });
        System.out.println(borderLine);
    }

    private String formatName(String name) {
        final int maxLen = 18;
        if (name.length() > maxLen) {
            return name.substring(0, maxLen - 2) + "..";
        }
        return name;
    }
}


