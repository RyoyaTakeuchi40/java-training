import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TransactionInput {

    private static Scanner scanner = new Scanner(System.in);

    public static void inputTransaction(List<Stock> stockList) {
        System.out.println("取引情報を入力してください");

        // 銘柄選択
        System.out.print("銘柄コード＞ ");
        String ticker = scanner.nextLine();

        Stock stock = stockList.stream()
                .filter(s -> s.getTicker().equals(ticker))
                .findFirst()
                .orElse(null);

        if (stock == null) {
            System.out.println("不正な銘柄コードです。取引を中止します。");
            return;
        }

        // 取引日時入力
        LocalDateTime tradedDatetime = inputDatetime();

        // 売買区分選択
        System.out.print("売買区分（買い / 売り）＞ ");
        String side = scanner.nextLine();
        if (!side.equalsIgnoreCase("買い") && !side.equalsIgnoreCase("売り")) {
            System.out.println("不正な売買区分です。");
            return;
        }

        // 数量入力
        System.out.print("取引数量（100株単位）＞ ");
        int quantity = Integer.parseInt(scanner.nextLine());

        // 取引単価入力
        System.out.print("取引単価（小数点以下2位まで）＞ ");
        BigDecimal tradedUnitPrice = new BigDecimal(scanner.nextLine());

        // 取引を記録
        Transaction transaction = new Transaction(tradedDatetime, ticker, side, quantity, tradedUnitPrice);
        recordTransaction(transaction);
    }

    private static LocalDateTime inputDatetime() {
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

    private static void recordTransaction(Transaction transaction) {
        // ここでは取引を記録する処理を行います。例えば、CSVに書き込みます。
        System.out.println("取引が記録されました: " + transaction.getTradedDatetime() + ", " + transaction.getTicker());
    }
}