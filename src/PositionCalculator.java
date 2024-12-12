import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionCalculator {

    public Map<String, Integer> getHoldingQuantity(List<Transaction> transactions) {
        Map<String, Integer> holdings = new HashMap<>();

        for (Transaction transaction : transactions) {
            int quantity = transaction.getQuantity();
            String ticker = transaction.getTicker();

            // 「買い」取引の場合、数量を加算
            if (transaction.getSide().equals("買い")) {
                holdings.put(ticker, holdings.getOrDefault(ticker, 0) + quantity);
            }
            // 「売り」取引の場合、数量を減算
            else if (transaction.getSide().equals("売り")) {
                holdings.put(ticker, holdings.getOrDefault(ticker, 0) - quantity);
            }
        }

        return holdings;
    }

    public Map<String, String> getHoldingStocks(List<Transaction> transactions) {
        Map<String, String> holdings = new HashMap<>();

        for (Transaction transaction : transactions) {
            holdings.put(transaction.getTicker(), transaction.getStockName());
        }

        return holdings;
    }
}