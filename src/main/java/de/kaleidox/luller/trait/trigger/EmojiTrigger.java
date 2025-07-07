package de.kaleidox.luller.trait.trigger;

import de.kaleidox.luller.trait.PersonalityTraitTrigger;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class EmojiTrigger extends PersonalityTraitTrigger {
    @Basic String emoji;

    @Override
    public boolean test(TraitTriggerData trigger) {
        return getContentSource().apply(trigger.getMessage()).contains(emoji) || trigger.getMessage()
                .getReactions()
                .stream()
                .map(MessageReaction::getEmoji)
                .map(Emoji::getFormatted)
                .anyMatch(emoji::equals);
    }
}
