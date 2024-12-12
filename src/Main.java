import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CsvReader csvReader = new CsvReader();
        TablePrinter tablePrinter = new TablePrinter();
        boolean isRunning = true;

        System.out.println("株式取引管理システムを開始します。");

        while (isRunning) {
            System.out.println("\n操作するメニューを選んでください。");
            System.out.println("1. 銘柄マスター一覧表示");
            System.out.println("9. アプリケーションを終了する");
            System.out.print("入力してください：");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    System.out.println("「銘柄マスター一覧表示」が選択されました。");
                    List<Stock> stocks = csvReader.readStocks("src/stocks.csv");
                    tablePrinter.printStocks(stocks);
                    break;

                case "9":
                    System.out.println("アプリケーションを終了します。");
                    isRunning = false;
                    break;

                default:
                    System.out.println("\"" + userInput + "\" に対応するメニューは存在しません。");
                    break;
            }
        }

        scanner.close();
    }
}