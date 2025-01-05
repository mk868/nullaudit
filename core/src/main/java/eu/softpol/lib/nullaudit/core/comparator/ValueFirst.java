package eu.softpol.lib.nullaudit.core.comparator;

import java.util.Comparator;
import java.util.Objects;

public class ValueFirst<T> implements Comparator<T> {

  private final T firstValue;
  private final Comparator<T> real;

  @SuppressWarnings("unchecked")
  private ValueFirst(T firstValue, Comparator<? super T> real) {
    Objects.requireNonNull(real);
    this.firstValue = firstValue;
    this.real = (Comparator<T>) real;
  }

  @Override
  public int compare(T a, T b) {
    if (Objects.equals(a, b)) {
      return 0;
    }
    if (Objects.equals(a, firstValue)) {
      return -1;
    } else if (Objects.equals(b, firstValue)) {
      return 1;
    } else {
      return real.compare(a, b);
    }
  }

  public static <T> Comparator<T> valueFirst(T firstValue, Comparator<? super T> real) {
    return new ValueFirst<>(firstValue, real);
  }
}
