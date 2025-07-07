package de.kaleidox.luller.trait.action.decider;

import de.kaleidox.luller.trait.PersonalityTraitAction;
import de.kaleidox.luller.trait.model.ActionDecider;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;
import java.util.stream.Stream;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class SingularRandom extends ActionDecider {
    private static final Random RNG = new Random();

    @Override
    public Stream<PersonalityTraitAction> apply(Stream<PersonalityTraitAction> actions) {
        var list = actions.toList();
        return Stream.of(list.get(RNG.nextInt(list.size())));
    }
}
