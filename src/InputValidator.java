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
}