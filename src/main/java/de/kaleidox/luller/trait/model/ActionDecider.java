package de.kaleidox.luller.trait.model;

import de.kaleidox.luller.model.BasicEntity;
import de.kaleidox.luller.trait.PersonalityTraitAction;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Function;
import java.util.stream.Stream;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public abstract class ActionDecider extends BasicEntity
        implements Function<Stream<PersonalityTraitAction>, Stream<PersonalityTraitAction>> {}
