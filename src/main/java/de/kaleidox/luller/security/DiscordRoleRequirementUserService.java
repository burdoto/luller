package de.kaleidox.luller.security;

import de.kaleidox.luller.BotConfig;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscordRoleRequirementUserService extends DefaultOAuth2UserService {
    @Autowired BotConfig config;
    @Autowired JDA       jda;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        var user = super.loadUser(userRequest);

        var roleRequirements = config.getOAuth().getRoleRequirements();
        var roles = roleRequirements.stream()
                .map(BotConfig.DiscordOAuth2Info.GuildRoleRequirement::getRole)
                .map(jda::getRoleById)
                .flatMap(Stream::ofNullable)
                .collect(Collectors.toUnmodifiableSet());

        if (roleRequirements.stream()
                .map(BotConfig.DiscordOAuth2Info.GuildRoleRequirement::getGuild)
                .map(jda::getGuildById)
                .flatMap(Stream::ofNullable)
                .flatMap(guild -> {
                    var id = user.getAttribute("id");
                    return id == null ? Stream.empty() : Stream.ofNullable(guild.getMemberById(String.valueOf(id)));
                })
                .noneMatch(member -> member.getUnsortedRoles().stream().anyMatch(roles::contains)))
            throw new OAuth2AuthenticationException("User does not have any of the required Discord roles");

        return user;
    }
}
