import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TransactionCsvWriter {

    private final String filePath;

    public TransactionCsvWriter(String filePath) {
        this.filePath = filePath;
    }

    public void saveTransaction(Transaction transaction) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            String record = String.format("%s,%s,%s,%s,%d,%.2f,%s",
                    transaction.getTradedDatetime(),
                    transaction.getTicker(),
                    transaction.getStockName(),
                    transaction.getSide(),
                    transaction.getQuantity(),
                    transaction.getTradedUnitPrice(),
                    transaction.getInputDatetime());
            pw.println(record);
            System.out.println("取引をCSVに記録しました。");
        } catch (IOException e) {
            System.out.println("取引データの保存に失敗しました: " + e.getMessage());
        }
    }
}