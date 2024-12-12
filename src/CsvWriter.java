import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {

    private final String filePath;

    public CsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public boolean addStock(Stock stock, List<Stock> existingStocks) {
        // 銘柄コードの重複チェック
        if (existingStocks.stream().anyMatch(s -> s.getTicker().equalsIgnoreCase(stock.getTicker()))) {
            System.out.println("既に登録されている銘柄コードです。登録に失敗しました。");
            return false;
        }

        // CSVファイルへの追記
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String record = String.format("%s,%s,%s,%d",
                    stock.getTicker(),
                    stock.getName(),
                    mapMarketCode(stock.getExchangeMarket()),
                    stock.getSharesIssued());
            pw.println(record);
            System.out.println(stock.getName() + "を新規銘柄として登録しました。");
            return true;
        } catch (IOException e) {
            System.out.println("CSVファイルへの登録に失敗しました: " + e.getMessage());
            return false;
        }
    }

    private String mapMarketCode(String market) {
        switch (market.toLowerCase()) {
            case "prime": return "P";
            case "standard": return "S";
            case "growth": return "G";
            default: return "不明";
        }
    }
}