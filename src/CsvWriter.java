import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

    private final String filePath;

    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void addStock(Stock stock) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String record = String.format("%s,%s,%s,%d",
                    stock.getTicker(),
                    stock.getName(),
                    mapMarketCode(stock.getExchangeMarket()),
                    stock.getSharesIssued());
            pw.println(record);
            System.out.println(stock.getName() + "を新規銘柄として登録しました。");
        } catch (IOException e) {
            System.out.println("CSVファイルへの登録に失敗しました: " + e.getMessage());
        }
    }

    private String mapMarketCode(String market) {
        return switch (market.toLowerCase()) {
            case "prime" -> "P";
            case "standard" -> "S";
            case "growth" -> "G";
            default -> "不明";
        };
    }
}