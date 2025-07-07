package de.kaleidox.luller.trait;

import de.kaleidox.luller.model.BasicEntity;
import de.kaleidox.luller.model.MessageContentSource;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.comroid.annotations.internal.Annotations;

import java.util.Objects;
import java.util.function.Predicate;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public abstract class PersonalityTraitTrigger extends BasicEntity implements Predicate<TraitTriggerData> {
    MessageContentSource contentSource;

    public MessageContentSource getContentSource() {
        return Objects.requireNonNullElseGet(contentSource,
                () -> Annotations.defaultValue(MessageContentSource.class));
    }
}
