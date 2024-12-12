import java.util.Comparator;

public class TickerComparator implements Comparator<String> {

    @Override
    public int compare(String t1, String t2) {
        if (t1 == null || t2 == null) {
            return t1 == null ? (t2 == null ? 0 : -1) : 1;
        }

        // 銘柄コードの昇順比較 (英数字混在の比較)
        return t1.compareTo(t2);
    }
}
