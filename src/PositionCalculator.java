import java.math.BigDecimal;
import java.util.*;

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

    // ポジション計算メソッド
    public List<Position> calculatePositions(List<Transaction> transactions) {
        Map<String, Position> positionMap = new HashMap<>();

        for (Transaction transaction : transactions) {
            String ticker = transaction.getTicker();
            positionMap.putIfAbsent(ticker, new Position(ticker, transaction.getStockName()));

            Position position = positionMap.get(ticker);
            BigDecimal price = transaction.getTradedUnitPrice();
            int quantity = transaction.getQuantity();

            if (transaction.getSide().equals("買い")) {
                position.buy(quantity, price);
            } else if (transaction.getSide().equals("売り")) {
                position.sell(quantity, price);
            }
        }

        return new ArrayList<>(positionMap.values());
    }
}