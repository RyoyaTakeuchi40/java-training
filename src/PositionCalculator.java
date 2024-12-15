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

    // 時価情報の更新
    public void updateMarketPrices(List<Position> positions, Map<String, BigDecimal> marketPrices) {
        for (Position position : positions) {
            BigDecimal marketPrice = marketPrices.get(position.getTicker());
            position.updateValuation(marketPrice);
        }
    }

    // ポジション表示
    public void printPositions(List<Position> positions) {
        String borderLine = "-".repeat(100);
        System.out.println(borderLine);
        System.out.printf("| %-6s | %-18s | %14s | %14s | %14s | %14s |\n",
                "Ticker", "Name", "Quantity", "Avg Price", "Valuation", "Unrealized PnL");
        System.out.println(borderLine);

        positions.stream()
                .sorted(Comparator.comparing(Position::getTicker))
                .forEach(pos -> System.out.printf("| %-6s | %-18s | %,14d | %,14.2f | %14s | %14s |\n",
                        pos.getTicker(),
                        formatName(pos.getStockName()),
                        pos.getQuantity(),
                        pos.getAvgUnitPrice(),
                        formatCurrency(pos.getValuation()),
                        formatCurrency(pos.getUnrealizedPnL())
                ));
        System.out.println(borderLine);
    }

    // ユーティリティメソッド
    private String formatCurrency(BigDecimal value) {
        return value == null ? "N/A" : String.format("%,.2f", value);
    }

    private String formatName(String name) {
        return name == null ? "N/A" : name;
    }
}