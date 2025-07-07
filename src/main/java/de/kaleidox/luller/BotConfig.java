package de.kaleidox.luller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.comroid.api.data.seri.DataNode;

@Data
@NoArgsConstructor
public class BotConfig implements DataNode {
    final DatabaseInfo      database = new DatabaseInfo();
    final DiscordOAuth2Info oAuth    = new DiscordOAuth2Info();
    String token;

    @Data
    @NoArgsConstructor
    public static class DatabaseInfo {
        String uri;
        String username;
        String password;
    }

    @Data
    @NoArgsConstructor
    public static class DiscordOAuth2Info {
        String                 clientId;
        String                 secret;
        String                 redirectUrl;
        GuildRoleRequirement[] roleRequirements;

        @Data
        @NoArgsConstructor
        public static class GuildRoleRequirement {
            long guild;
            long role;
        }
    }
}
