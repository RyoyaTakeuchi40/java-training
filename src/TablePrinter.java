import java.util.List;

public class TablePrinter {

    public void printStocks(List<Stock> stocks) {
        if (stocks.isEmpty()) {
            System.out.println("登録された銘柄はありません。");
            return;
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf("| %-6s | %-20s | %-10s | %14s |\n",
                "Ticker", "Name", "Market", "Shares Issued");
        System.out.println("---------------------------------------------------------------");

        for (Stock stock : stocks) {
            System.out.printf("| %-6s | %-20s | %-10s | %,14d |\n",
                    stock.getTicker(),
                    formatName(stock.getName()),
                    stock.getExchangeMarket(),
                    stock.getSharesIssued());
        }

        System.out.println("---------------------------------------------------------------");
    }

    private String formatName(String name) {
        final int maxLen = 18;
        if (name.length() > maxLen) {
            return name.substring(0, maxLen - 2) + "..";
        }
        return name;
    }
}