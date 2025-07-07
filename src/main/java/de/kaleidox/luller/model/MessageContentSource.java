package de.kaleidox.luller.model;

import net.dv8tion.jda.api.entities.Message;
import org.comroid.annotations.Default;

import java.util.function.Function;

public enum MessageContentSource implements Function<Message, String> {
    @Default ContentDisplay {
        @Override
        public String apply(Message message) {
            return message.getContentDisplay();
        }
    }, ContentStripped {
        @Override
        public String apply(Message message) {
            return message.getContentStripped();
        }
    }, ContentRaw {
        @Override
        public String apply(Message message) {
            return message.getContentRaw();
        }
    }
}
