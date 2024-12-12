import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String STOCKS_CSV_FILE = "src/stocks.csv";
    private static final String TRANSACTIONS_CSV_FILE = "src/transactions.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CsvReader csvReader = new CsvReader();
        CsvWriter csvWriter = new CsvWriter(STOCKS_CSV_FILE);
        TransactionCsvWriter transactionCsvWriter = new TransactionCsvWriter(TRANSACTIONS_CSV_FILE);
        TablePrinter tablePrinter = new TablePrinter();
        boolean isRunning = true;

        System.out.println("株式取引管理システムを開始します。");

        while (isRunning) {
            System.out.println("\n操作するメニューを選んでください。");
            System.out.println("1. 銘柄マスター一覧表示");
            System.out.println("2. 銘柄マスタ新規登録");
            System.out.println("3. 取引入力");
            System.out.println("9. アプリケーションを終了する");
            System.out.print("入力してください：");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    List<Stock> stocks = csvReader.readStocks(STOCKS_CSV_FILE);
                    tablePrinter.printStocks(stocks);
                    break;

                case "2":
                    registerNewStock(scanner, csvReader, csvWriter);
                    break;

                case "3":
                    inputTransaction(scanner, csvReader, transactionCsvWriter);
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
        List<Stock> existingStocks = csvReader.readStocks(STOCKS_CSV_FILE);

        csvWriter.addStock(newStock, existingStocks);
    }

    private static void inputTransaction(Scanner scanner, CsvReader csvReader, TransactionCsvWriter transactionCsvWriter) {
        System.out.println("取引情報を入力します。");

        // 銘柄コード入力
        String ticker = promptInput(scanner, "銘柄コード", InputValidator::isValidTicker).toUpperCase();
        List<Stock> stocks = csvReader.readStocks(STOCKS_CSV_FILE);
        Stock stock = stocks.stream()
                .filter(s -> s.getTicker().equals(ticker))
                .findFirst()
                .orElse(null);

        if (stock == null) {
            System.out.println("銘柄コードが存在しません。取引を中止します。");
            return;
        }

        // 取引情報の入力
        LocalDateTime tradedDatetime = inputDatetime(scanner);
        String side = promptInput(scanner, "売買区分（買い / 売り）", InputValidator::isValidSide);
        int quantity = Integer.parseInt(promptInput(scanner, "取引数量（100株単位）", InputValidator::isValidQuantity));
        BigDecimal tradedUnitPrice = new BigDecimal(promptInput(scanner, "取引単価", InputValidator::isValidPrice));

        Transaction transaction = new Transaction(tradedDatetime, ticker, side, quantity, tradedUnitPrice);

        // 取引記録の保存
        transactionCsvWriter.saveTransaction(transaction);
    }

    private static LocalDateTime inputDatetime(Scanner scanner) {
        // 取引日時の入力を行う
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime tradedDatetime = null;

        while (tradedDatetime == null) {
            System.out.print("取引日時（YYYY-MM-DD HH:MM）＞ ");
            String dateTimeInput = scanner.nextLine();

            try {
                tradedDatetime = LocalDateTime.parse(dateTimeInput, formatter);
                if (tradedDatetime.isAfter(LocalDateTime.now())) {
                    System.out.println("取引日時は現在日時より前でなければなりません。");
                    tradedDatetime = null;
                } else if (tradedDatetime.getDayOfWeek().getValue() > 5 || tradedDatetime.getHour() < 9 || tradedDatetime.getHour() > 15 || (tradedDatetime.getHour() == 15 && tradedDatetime.getMinute() > 30)) {
                    System.out.println("取引日時は平日9:00～15:30の間である必要があります。");
                    tradedDatetime = null;
                }
            } catch (Exception e) {
                System.out.println("不正な日時フォーマットです。");
            }
        }

        return tradedDatetime;
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