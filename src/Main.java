import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String CSV_FILE = "src/stocks.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CsvReader csvReader = new CsvReader();
        CsvWriter csvWriter = new CsvWriter(CSV_FILE);
        TablePrinter tablePrinter = new TablePrinter();
        boolean isRunning = true;

        System.out.println("株式取引管理システムを開始します。");

        while (isRunning) {
            System.out.println("\n操作するメニューを選んでください。");
            System.out.println("1. 銘柄マスター一覧表示");
            System.out.println("2. 銘柄マスタ新規登録");
            System.out.println("9. アプリケーションを終了する");
            System.out.print("入力してください：");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    List<Stock> stocks = csvReader.readStocks(CSV_FILE);
                    tablePrinter.printStocks(stocks);
                    break;

                case "2":
                    registerNewStock(scanner, csvReader, csvWriter);
                    break;

                case "9":
                    System.out.println("アプリケーションを終了します。");
                    isRunning = false;
                    break;

                default:
                    System.out.println("不正な入力です。再度入力してください。");
                    break;
            }
        }
        scanner.close();
    }

    private static void registerNewStock(Scanner scanner, CsvReader csvReader, CsvWriter csvWriter) {
        System.out.println("新規株式銘柄マスタを登録します。");

        String name = promptInput(scanner, "銘柄名", InputValidator::isValidName);
        String ticker = promptInput(scanner, "銘柄コード", InputValidator::isValidTicker).toUpperCase();
        String market = promptInput(scanner, "上場市場 (Prime/Standard/Growth)", InputValidator::isValidMarket);
        long sharesIssued = Long.parseLong(promptInput(scanner, "発行済み株式数", InputValidator::isValidSharesIssued));

        Stock newStock = new Stock(ticker, name, market, sharesIssued);
        List<Stock> existingStocks = csvReader.readStocks(CSV_FILE);

        csvWriter.addStock(newStock, existingStocks);
    }

    private static String promptInput(Scanner scanner, String prompt, java.util.function.Predicate<String> validator) {
        String input;
        while (true) {
            System.out.print(prompt + " > ");
            input = scanner.nextLine().trim();
            if (validator.test(input)) {
                break;
            }
            System.out.println(prompt + "が規則に従っていません。再度入力してください。");
        }
        return input;
    }
}