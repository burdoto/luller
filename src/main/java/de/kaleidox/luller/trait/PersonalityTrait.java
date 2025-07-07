package de.kaleidox.luller.trait;

import de.kaleidox.luller.model.BasicEntity;
import de.kaleidox.luller.trait.model.ActionDecider;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class PersonalityTrait extends BasicEntity {
    long guildId;
    @ManyToMany Set<PersonalityTraitTrigger> triggers;
    @ManyToMany List<PersonalityTraitAction> actions;
    @ManyToMany List<ActionDecider>          deciders;
}
