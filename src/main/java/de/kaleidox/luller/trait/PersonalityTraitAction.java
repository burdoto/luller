package de.kaleidox.luller.trait;

import de.kaleidox.luller.model.BasicEntity;
import de.kaleidox.luller.trait.model.ResponseModel;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.BiConsumer;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public abstract class PersonalityTraitAction extends BasicEntity
        implements BiConsumer<ResponseModel, TraitTriggerData> {}
