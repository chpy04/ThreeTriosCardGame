package adapters.model;

/**
 * Represents an adapter that converts between two enums representing the same thing.
 * @param <V> the type of the provider's enum
 * @param <T> the type of our model's enum
 */
public interface EnumAdapter<V, T> {

  /**
   * Converts a provider's enum into the equivalent model enum.
   * @param input the provider enum
   * @return out model's enum
   */
  T providerToModel(V input);

  /**
   * Converts out model's enum into the equivalent provider enum.
   * @param input our model's enum
   * @return the provider's enum
   */
  V modelToProvider(T input);
}
