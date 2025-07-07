package de.kaleidox.luller.trait.model;

import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ResponseModel {
    @Nullable String               content;
    @Nullable EmbedBuilder         embed;
    @Nullable List<@NotNull Emoji> reactions;

    public MessageCreateData createResponseMessage() {
        var msg = new MessageCreateBuilder();
        if (content != null) msg.setContent(content);
        else if (embed != null) msg.addEmbeds(embed.build());
        else throw new IllegalStateException("No message content was defined");
        return msg.build();
    }

    public RestAction<?> apply(TraitTriggerData trigger) {
        return RestAction.allOf(Stream.concat(Stream.of(content, embed)
                                .filter(Objects::nonNull)
                                .findAny()
                                .map($ -> createResponseMessage())
                                .map(trigger.getChannel()::sendMessage)
                                .stream(),
                        Stream.ofNullable(reactions).flatMap(Collection::stream).map(trigger.getMessage()::addReaction))
                .toList());
    }
}
