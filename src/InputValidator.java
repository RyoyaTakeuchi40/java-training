import java.math.BigDecimal;
import java.util.regex.Pattern;

public class InputValidator {

    // 銘柄コードのバリデーション
    public static String isValidTicker(String ticker) {
        if (!Pattern.matches("^[0-9][A-Z0-9][0-9][A-Z0-9]$", ticker.toUpperCase())) {
            return "銘柄コードは4桁の半角英数字（例: 1A2B）で入力してください。";
        }
        return null;
    }

    // 銘柄名のバリデーション
    public static String isValidName(String name) {
        if (!Pattern.matches("^[A-Za-z0-9 .()]+$", name)) {
            return "銘柄名は英数字、空白、丸括弧、ピリオドのみ使用できます。";
        }
        return null;
    }

    // 市場のバリデーション
    public static String isValidMarket(String market) {
        if (!(market.equalsIgnoreCase("Prime") ||
                market.equalsIgnoreCase("Standard") ||
                market.equalsIgnoreCase("Growth"))) {
            return "市場は Prime、Standard、Growth のいずれかを入力してください。";
        }
        return null;
    }

    // 発行済み株式数のバリデーション
    public static String isValidSharesIssued(String sharesIssued) {
        if (!Pattern.matches("^\\d{1,9}$", sharesIssued)) {
            return "発行済み株式数は1〜9桁の整数で入力してください。";
        }
        return null;
    }

    // 売買区分（買い/売り）のバリデーション
    public static String isValidSide(String input) {
        if (!(input.equals("買い") || input.equals("売り"))) {
            return "売買区分を「買い」または「売り」として入力してください。";
        }
        return null;
    }

    // 取引数量のバリデーション（100株単位）
    public static String isValidQuantity(String input) {
        try {
            int quantity = Integer.parseInt(input);
            if (quantity % 100 != 0 || quantity <= 0) {
                return "取引数量は100株単位の正の整数で入力してください。";
            }
        } catch (NumberFormatException e) {
            return "取引数量は数値で入力してください。";
        }
        return null;
    }

    // 取引単価のバリデーション（小数点以下2桁）
    public static String isValidPrice(String input) {
        try {
            BigDecimal price = new BigDecimal(input);
            if (price.compareTo(BigDecimal.ZERO) <= 0 || !input.matches("\\d+(\\.\\d{1,2})?")) {
                return "取引単価は正の数値で小数点以下2桁までで入力してください。";
            }
        } catch (NumberFormatException e) {
            return "取引単価は数値で入力してください。";
        }
        return null;
    }
}