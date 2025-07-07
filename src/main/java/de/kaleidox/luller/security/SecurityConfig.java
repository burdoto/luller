package de.kaleidox.luller.security;

import de.kaleidox.luller.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(
            @Autowired HttpSecurity http,
            @Autowired DiscordRoleRequirementUserService discordUserService
    ) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(oAuth -> oAuth.userInfoEndpoint(userInfo -> userInfo.userService(discordUserService)))
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(@Autowired BotConfig config) {
        var info = config.getOAuth();
        return new InMemoryClientRegistrationRepository(ClientRegistration.withRegistrationId("discord")
                .clientId(info.getClientId())
                .clientSecret(info.getSecret())
                .scope("identify")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://discord.com/oauth2/authorize")
                .redirectUri(info.getRedirectUrl())
                .tokenUri("https://discord.com/api/oauth2/token")
                .userInfoUri("https://discord.com/api/users/@me")
                .userNameAttributeName("id")
                .build());
    }
}
