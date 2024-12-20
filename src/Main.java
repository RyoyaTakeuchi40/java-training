import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

    private static final String STOCKS_CSV_FILE = "src/stocks.csv";
    private static final String TRANSACTIONS_CSV_FILE = "src/transactions.csv";
    private static final String MARKET_PRICE_CSV_FILE = "src/market_prices.csv";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CsvReader csvReader = new CsvReader();
        CsvWriter csvWriter = new CsvWriter(STOCKS_CSV_FILE);
        TransactionCsvWriter transactionCsvWriter = new TransactionCsvWriter(TRANSACTIONS_CSV_FILE);
        TablePrinter tablePrinter = new TablePrinter();
        PositionCalculator positionCalculator = new PositionCalculator();
        MarketPriceManager marketPriceManager = new MarketPriceManager();

        boolean isRunning = true;

        System.out.println("株式取引管理システムを開始します。");

        List<Stock> stocks = csvReader.readStocks(STOCKS_CSV_FILE);
        // 検索を早くするためにtickerとnameをMap型にする
        Map<String, String> stockMap = createStocksMap(stocks);
        List<Transaction> transactions = csvReader.readTransactions(TRANSACTIONS_CSV_FILE);
        Map<String, BigDecimal> marketPrices = marketPriceManager.loadMarketPrices(MARKET_PRICE_CSV_FILE, stockMap);

        while (isRunning) {
            System.out.println("\n操作するメニューを選んでください。");
            System.out.println("1. 銘柄マスター一覧表示");
            System.out.println("2. 銘柄マスタ新規登録");
            System.out.println("3. 取引入力");
            System.out.println("4. 取引一覧の表示");
            System.out.println("5. 保有ポジション表示");
            System.out.println("6. 時価情報更新");
            System.out.println("9. アプリケーションを終了する");
            System.out.print("入力してください：");

            String userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    tablePrinter.printStocks(stocks);
                    break;

                case "2":
                    registerNewStock(scanner, csvWriter, stockMap);
                    // stocks情報更新
                    stocks = csvReader.readStocks(STOCKS_CSV_FILE);
                    stockMap = createStocksMap(stocks);
                    tablePrinter.printStocks(stocks);
                    break;

                case "3":
                    inputTransaction(scanner, transactionCsvWriter, transactions, stockMap);
                    // transactions情報更新
                    transactions = csvReader.readTransactions(TRANSACTIONS_CSV_FILE);
                    tablePrinter.printTransactions(transactions);
                    break;

                case "4":
                    tablePrinter.printTransactions(transactions);
                    break;

                case "5":
                    List<Position> positions = positionCalculator.calculatePositions(transactions);
                    tablePrinter.printPositions(positions);

                    break;

                case "6":
                    marketPrices = marketPriceManager.loadMarketPrices(MARKET_PRICE_CSV_FILE, stockMap);
                    tablePrinter.printMarketPrices(marketPrices, stockMap);
                    break;

                case "9":
                    System.out.println("アプリケーションを終了します。");
                    isRunning = false;
                    break;

                default:
                    System.out.println("不正な入力です。再度入力してください。");
                    break;
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void registerNewStock(Scanner scanner, CsvWriter csvWriter, Map<String, String> stockMap) {
        System.out.println("新規株式銘柄マスタを登録します。");

        String name = promptInput(scanner, "銘柄名", InputValidator::isValidName);
        String ticker;
        while (true) {
            ticker = promptInput(scanner, "銘柄コード", InputValidator::isValidTicker).toUpperCase();
            if (!stockMap.containsKey(ticker)) {
                break; // 重複なしなら次へ進む
            }
            System.out.println("既に登録されている銘柄コードです。もう一度入力してください。");
        }
        String market = promptInput(scanner, "上場市場 (Prime/Standard/Growth)", InputValidator::isValidMarket);
        long sharesIssued = Long.parseLong(promptInput(scanner, "発行済み株式数", InputValidator::isValidSharesIssued));

        Stock newStock = new Stock(ticker, name, market, sharesIssued);
        csvWriter.addStock(newStock);
    }

    private static void inputTransaction(Scanner scanner, TransactionCsvWriter transactionCsvWriter, List<Transaction> transactions, Map<String, String> stockMap) {
        PositionCalculator positionCalculator = new PositionCalculator();
        System.out.println("取引情報を入力します。");

        String ticker = null;
        String stockName = null;
        LocalDateTime tradedDatetime;
        int quantity;
        // 銘柄コード入力
        while(stockName == null) {
            ticker = promptInput(scanner, "銘柄コード", InputValidator::isValidTicker).toUpperCase();
            if (!stockMap.containsKey(ticker)) {
                System.out.println("銘柄コードが存在しません。");
            }
            stockName = stockMap.get(ticker);
        }
        // 取引情報の入力
        while (true) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            tradedDatetime = LocalDateTime.parse(promptInput(scanner, "取引日時", InputValidator::isValidDatetime), formatter);
            LocalDateTime lastTransactionDatetime = lastTransactionDatetime(transactions, ticker);
            if (lastTransactionDatetime != null && !lastTransactionDatetime.isBefore(tradedDatetime)) {
                System.out.println("同一銘柄の最終取引日時より新しい日時を指定してください。。");
                System.out.printf("最終取引日時:%s\n", lastTransactionDatetime);
                continue;
            }
            break;
        }
        // 売買区分の入力
        String side = promptInput(scanner, "売買区分（買い / 売り）", InputValidator::isValidSide);
        // 取引情報の入力
        while (true) {
            quantity = Integer.parseInt(promptInput(scanner, "取引数量（100株単位）", InputValidator::isValidQuantity));
            Map<String, Integer> holdingQuantity = positionCalculator.getHoldingQuantity(transactions);
            int updatedQuantity = calculateUpdatedQuantity(holdingQuantity, ticker, side, quantity);
            if (updatedQuantity < 0) {
                System.out.println("保有数量がマイナスになる取引は登録できません。");
                System.out.printf("現在の保有数量:%d\n", holdingQuantity.get(ticker));
                continue;
            }
            break;
        }
        // 取引単価の入力
        BigDecimal tradedUnitPrice = new BigDecimal(promptInput(scanner, "取引単価", InputValidator::isValidPrice));

        Transaction transaction = new Transaction(tradedDatetime, ticker, stockMap.get(ticker), side, quantity, tradedUnitPrice);

        // 取引記録の保存
        transactionCsvWriter.saveTransaction(transaction);
    }

    private static Map<String, String> createStocksMap(List<Stock> stocks) {
        Map<String, String> stocksMap = new HashMap<>();
        for (Stock stock : stocks) {
            stocksMap.put(stock.getTicker(), stock.getName());
        }
        return stocksMap;
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

    private static String promptInput(Scanner scanner, String prompt, Function<String, String> validator) {
        String input;
        String validationMessage;
        while (true) {
            System.out.print(prompt + " > ");
            input = scanner.nextLine().trim();
            validationMessage = validator.apply(input);
            if (validationMessage == null) {
                break;
            }
            System.out.println(validationMessage);  // エラーメッセージを表示
        }
        return input;
    }

    private static int calculateUpdatedQuantity(Map<String, Integer> holdings, String ticker, String side, int quantity) {
        int currentQuantity = holdings.getOrDefault(ticker, 0);
        return side.equals("買い") ? currentQuantity + quantity : currentQuantity - quantity;
    }

    private static LocalDateTime lastTransactionDatetime(List<Transaction> transactions, String ticker) {
        return transactions.stream()
                .filter(t -> t.getTicker().equals(ticker))
                .map(Transaction::getTradedDatetime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
}