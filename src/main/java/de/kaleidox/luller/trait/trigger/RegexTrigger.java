package de.kaleidox.luller.trait.trigger;

import de.kaleidox.luller.trait.PersonalityTraitTrigger;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.BiPredicate;
import java.util.regex.Pattern;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public final class RegexTrigger extends PersonalityTraitTrigger {
    Pattern   pattern;
    MatchType matchType;

    @Override
    public boolean test(TraitTriggerData traitTriggerData) {
        var content = getContentSource().apply(traitTriggerData.getMessage());
        return matchType.test(this, content);
    }

    public enum MatchType implements BiPredicate<RegexTrigger, String> {
        ExactMatch {
            @Override
            public boolean test(RegexTrigger trigger, String text) {
                return trigger.pattern.matcher(text).matches();
            }
        }, OneMatch {
            @Override
            public boolean test(RegexTrigger trigger, String text) {
                var matcher = trigger.pattern.matcher(text);
                return matcher.find() && !matcher.find();
            }
        }, MultipleMatches {
            @Override
            public boolean test(RegexTrigger trigger, String text) {
                var matcher = trigger.pattern.matcher(text);
                return matcher.find() && matcher.find();
            }
        }, AnyMatch {
            @Override
            public boolean test(RegexTrigger trigger, String text) {
                return trigger.pattern.matcher(text).find();
            }
        }
    }
}
