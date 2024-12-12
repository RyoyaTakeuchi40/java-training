import java.math.BigDecimal;
import java.util.regex.Pattern;

public class InputValidator {

    public static boolean isValidTicker(String ticker) {
        return Pattern.matches("^[0-9][A-Z0-9][0-9][A-Z0-9]$", ticker.toUpperCase());
    }

    public static boolean isValidName(String name) {
        return Pattern.matches("^[A-Za-z0-9 .()]+$", name);
    }

    public static boolean isValidMarket(String market) {
        return market.equalsIgnoreCase("Prime") ||
                market.equalsIgnoreCase("Standard") ||
                market.equalsIgnoreCase("Growth");
    }

    public static boolean isValidSharesIssued(String sharesIssued) {
        return Pattern.matches("^\\d{1,9}$", sharesIssued);
    }

    // 売買区分（買い/売り）のバリデーション
    public static boolean isValidSide(String input) {
        return input.equals("買い") || input.equals("売り");
    }

    // 取引数量のバリデーション（100株単位）
    public static boolean isValidQuantity(String input) {
        try {
            int quantity = Integer.parseInt(input);
            return quantity % 100 == 0 && quantity > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 取引単価のバリデーション（小数点以下2桁）
    public static boolean isValidPrice(String input) {
        try {
            BigDecimal price = new BigDecimal(input);
            return price.compareTo(BigDecimal.ZERO) > 0 && input.matches("\\d+(\\.\\d{1,2})?");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}