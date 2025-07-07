package de.kaleidox.luller.trait.action;

import de.kaleidox.luller.trait.PersonalityTraitAction;
import de.kaleidox.luller.trait.model.ResponseModel;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class StaticPlaintextMessageAction extends PersonalityTraitAction {
    String text;

    @Override
    public ResponseModel apply(ResponseModel model, TraitTriggerData trigger) {
        return model.setContent(text);
    }
}
