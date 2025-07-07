package de.kaleidox.luller.trait.action.decider;

import de.kaleidox.luller.trait.PersonalityTraitAction;
import de.kaleidox.luller.trait.model.ActionDecider;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;
import java.util.stream.Stream;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class RandomChance extends ActionDecider {
    private static final Random RNG = new Random();

    @Basic double chance;

    @Override
    public Stream<PersonalityTraitAction> apply(Stream<PersonalityTraitAction> actions) {
        return actions.filter($ -> RNG.nextDouble() < chance);
    }
}
