package de.kaleidox.luller.trait.model;

import lombok.Value;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

@Value
public class TraitTriggerData {
    public static TraitTriggerData of(MessageReceivedEvent event) {
        return new TraitTriggerData(event.getMember(), event.getMessage(), event.getChannel(), event.getGuild());
    }

    public static TraitTriggerData of(MessageReactionAddEvent event) {
        return new TraitTriggerData(event.getMember(),
                event.retrieveMessage().complete(),
                event.getChannel(),
                event.getGuild());
    }

    Member              author;
    Message             message;
    MessageChannelUnion channel;
    Guild               guild;
}
