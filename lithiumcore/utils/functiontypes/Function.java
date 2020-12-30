package lithiumcore.utils.functiontypes;

public interface Function<T, R> {
  public R apply(T t);
}