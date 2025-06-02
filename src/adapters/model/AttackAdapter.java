package adapters.model;

import java.util.HashMap;
import java.util.Map;

import model.AttackValue;
import provider.model.cards.Attack;

/**
 * An adapter between AttackValues and Attacks which represent the same thing in two
 * different implementations.
 */
public class AttackAdapter implements EnumAdapter<Attack, model.AttackValue> {
  public static final Map<Attack, model.AttackValue> ATTACK_SET;

  static {
    ATTACK_SET = new HashMap<>();
    ATTACK_SET.put(Attack.ONE, AttackValue.ONE);
    ATTACK_SET.put(Attack.TWO, AttackValue.TWO);
    ATTACK_SET.put(Attack.THREE, AttackValue.THREE);
    ATTACK_SET.put(Attack.FOUR, AttackValue.FOUR);
    ATTACK_SET.put(Attack.FIVE, AttackValue.FIVE);
    ATTACK_SET.put(Attack.SIX, AttackValue.SIX);
    ATTACK_SET.put(Attack.SEVEN, AttackValue.SEVEN);
    ATTACK_SET.put(Attack.EIGHT, AttackValue.EIGHT);
    ATTACK_SET.put(Attack.NINE, AttackValue.NINE);
    ATTACK_SET.put(Attack.TEN, AttackValue.A);
  }

  @Override
  public AttackValue providerToModel(Attack input) {
    return ATTACK_SET.get(input);
  }

  @Override
  public Attack modelToProvider(AttackValue input) {
    for (Attack key : ATTACK_SET.keySet()) {
      if (ATTACK_SET.get(key).equals(input)) {
        return key;
      }
    }
    throw new IllegalStateException("could not convert attack value");
  }
}

