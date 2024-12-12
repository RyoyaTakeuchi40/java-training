import java.io.BufferedReader;
import java.io.FileReader;
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

    private String mapMarketCode(String code) {
        switch (code.toUpperCase()) {
            case "P": return "Prime";
            case "S": return "Standard";
            case "G": return "Growth";
            default: return "不明な市場";
        }
    }
}