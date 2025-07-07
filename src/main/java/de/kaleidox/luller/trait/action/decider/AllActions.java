package de.kaleidox.luller.trait.action.decider;

import de.kaleidox.luller.trait.PersonalityTraitAction;
import de.kaleidox.luller.trait.model.ActionDecider;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.stream.Stream;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class AllActions extends ActionDecider {
    @Override
    public Stream<PersonalityTraitAction> apply(Stream<PersonalityTraitAction> actions) {
        return actions;
    }
}
