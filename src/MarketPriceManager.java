import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MarketPriceManager {

    // 時価情報の読み込み
    public Map<String, BigDecimal> loadMarketPrices(String filePath, Map<String, String> stockMap) {
        Map<String, BigDecimal>  marketPrices = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // ヘッダーの読み飛ばし
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    String ticker = data[0].trim();
                    BigDecimal marketPrice = new BigDecimal(data[1].trim());
                    if (stockMap.containsKey(ticker)) {
                        marketPrices.put(ticker, marketPrice);
                    }
                }
            }
            System.out.println("時価情報の読み込みが完了しました。");
        } catch (Exception e) {
            System.out.println("時価情報の読み込み中にエラーが発生しました: " + e.getMessage());
        }
        return marketPrices;
    }
}