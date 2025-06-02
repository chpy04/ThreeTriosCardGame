package model;

/**
 * represents a direction on a compass.
 */
public enum Direction {
  UP {
    @Override
    public Direction getOppositeDirection() {
      return DOWN;
    }
  },
  DOWN {
    @Override
    public Direction getOppositeDirection() {
      return UP;
    }
  },
  RIGHT {
    @Override
    public Direction getOppositeDirection() {
      return LEFT;
    }
  },
  LEFT {
    @Override
    public Direction getOppositeDirection() {
      return RIGHT;
    }
  };

  /**
   * Returns the direction opposite to this one (i.e. north's opposite is south).
   * @return the opposite direction
   */
  public abstract Direction getOppositeDirection();
}
