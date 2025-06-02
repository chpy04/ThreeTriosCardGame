package adapters.model;

import java.util.HashMap;
import java.util.Map;

import provider.model.cards.Direction;

/**
 * An adapter between different Directions which represent the same thing in two
 * different implementations.
 */
public class DirectionAdapter implements EnumAdapter<Direction, model.Direction> {
  public static final Map<Direction, model.Direction> DIR_SET;

  static {
    DIR_SET = new HashMap<>();
    DIR_SET.put(Direction.NORTH, model.Direction.UP);
    DIR_SET.put(Direction.SOUTH, model.Direction.DOWN);
    DIR_SET.put(Direction.EAST, model.Direction.RIGHT);
    DIR_SET.put(Direction.WEST, model.Direction.LEFT);
  }

  @Override
  public model.Direction providerToModel(Direction dir) {
    return DIR_SET.get(dir);
  }

  @Override
  public Direction modelToProvider(model.Direction dir) {
    for (Direction key : DIR_SET.keySet()) {
      if (DIR_SET.get(key).equals(dir)) {
        return key;
      }
    }
    throw new IllegalStateException("invalid direction given");
  }
}

