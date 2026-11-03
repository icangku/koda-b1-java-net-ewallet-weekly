package dana.ewallet.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class CurrencyFormatter {
  private static final DecimalFormat FORMAT;
  static {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
    symbols.setGroupingSeparator('.');
    FORMAT = new DecimalFormat("#,###", symbols);
  }

  private CurrencyFormatter() {
  }

  public static String rupiah(long amount) {
    return "Rp" + FORMAT.format(amount);
  }
}