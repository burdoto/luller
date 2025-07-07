package de.kaleidox.luller.trait.trigger;

import de.kaleidox.luller.model.StringComparison;
import de.kaleidox.luller.trait.PersonalityTraitTrigger;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.comroid.annotations.internal.Annotations;

import java.util.Objects;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class ContainsTrigger extends PersonalityTraitTrigger {
    @Basic String text;
    StringComparison comparison;

    public StringComparison getComparison() {
        return Objects.requireNonNullElseGet(comparison, () -> Annotations.defaultValue(StringComparison.class));
    }

    @Override
    public boolean test(TraitTriggerData trigger) {
        return getComparison().test(getContentSource().apply(trigger.getMessage()), text, String::contains);
    }
}
