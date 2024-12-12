import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public List<Stock> readStocks(String filePath) {
        List<Stock> stocks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // ヘッダー行の読み飛ばし

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 4) {
                    System.out.println("不正なデータ形式: " + line);
                    continue;
                }

                try {
                    String ticker = data[0].trim();
                    String name = data[1].trim();
                    String market = mapMarketCode(data[2].trim());
                    long sharesIssued = Long.parseLong(data[3].trim().replace(",", ""));

                    stocks.add(new Stock(ticker, name, market, sharesIssued));
                } catch (NumberFormatException e) {
                    System.out.println("数値形式エラー: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("CSVファイルの読み込み中にエラーが発生しました: " + e.getMessage());
        }

        return stocks;
    }

    public List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); // 取引日時のフォーマット

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // ヘッダー行の読み飛ばし

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length != 7) {
                    System.out.println("不正なデータ形式: " + line);
                    continue;
                }

                try {
                    // CSVから各データを取得
                    LocalDateTime tradedDatetime = LocalDateTime.parse(data[0].trim(), datetimeFormatter); // 取引日時
                    String ticker = data[1].trim();  // 銘柄コード
                    String stockName = data[2].trim(); // 銘柄名
                    String side = data[3].trim();  // 売買区分 (買い / 売り)
                    int quantity = Integer.parseInt(data[4].trim().replace(",", ""));  // 数量
                    BigDecimal tradedUnitPrice = new BigDecimal(data[5].trim()); // 取引単価

                    // Transactionオブジェクトを作成し、リストに追加
                    transactions.add(new Transaction(tradedDatetime, ticker, stockName, side, quantity, tradedUnitPrice));
                } catch (Exception e) {
                    System.out.println("データ形式エラー: " + line);
                }
            }
        } catch (Exception e) {
            System.out.println("CSVファイルの読み込み中にエラーが発生しました: " + e.getMessage());
        }

        return transactions;
    }

    private String mapMarketCode(String code) {
        return switch (code.toUpperCase()) {
            case "P" -> "Prime";
            case "S" -> "Standard";
            case "G" -> "Growth";
            default -> "不明な市場";
        };
    }
}