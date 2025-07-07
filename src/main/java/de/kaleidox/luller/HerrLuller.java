package de.kaleidox.luller;

import de.kaleidox.luller.repo.PersonalityTraitRepository;
import de.kaleidox.luller.security.SecurityConfig;
import de.kaleidox.luller.trait.model.ResponseModel;
import de.kaleidox.luller.trait.model.TraitTriggerData;
import de.kaleidox.luller.util.ApplicationContextProvider;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.comroid.annotations.Description;
import org.comroid.api.config.ConfigurationManager;
import org.comroid.api.func.ext.Context;
import org.comroid.api.func.util.Command;
import org.comroid.api.func.util.Event;
import org.comroid.api.io.FileFlag;
import org.comroid.api.io.FileHandle;
import org.jetbrains.annotations.Nullable;
import org.mariadb.jdbc.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.io.File;
import java.util.Locale;

import static de.kaleidox.luller.util.ApplicationContextProvider.*;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackageClasses = {
        ApplicationContextProvider.class, SecurityConfig.class, PersonalityTraitRepository.class
})
public class HerrLuller {
    public static final File COMMAND_PURGE_FILE = new File("./purge_commands");

    public static void main(String[] args) {
        SpringApplication.run(HerrLuller.class, args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> bean(JDA.class).shutdown()));
    }

    @Command(permission = "8")
    @Description("Shutdown the Bot")
    public static String shutdown(
            User user, @Command.Arg(value = "purgecommands", required = false) @Description(
                    "Whether to purge and recreate all commands on this restart cycle") @Nullable Boolean purgeCommands
    ) {
        if (Boolean.TRUE.equals(purgeCommands)) FileFlag.enable(COMMAND_PURGE_FILE);
        System.exit(0);
        return "Goodbye";
    }

    @Lazy @Autowired PersonalityTraitRepository traits;

    @Bean
    public Locale locale() {
        var locale = Locale.GERMAN;
        Locale.setDefault(locale);
        return locale;
    }

    @Bean
    public FileHandle configDir() {
        return new FileHandle("/srv/discord/luller", true);
    }

    @Bean
    public FileHandle configFile(@Autowired FileHandle configDir) {
        return configDir.createSubFile("config.json");
    }

    @Bean
    public ConfigurationManager<BotConfig> configManager(@Autowired Context context, @Autowired FileHandle configFile) {
        return new ConfigurationManager<>(context, BotConfig.class, configFile.getAbsolutePath());
    }

    @Bean
    @Order
    public BotConfig config(@Autowired ConfigurationManager<BotConfig> configManager) {
        configManager.initialize();
        return configManager.getConfig();
    }

    @Bean
    public JDA jda(@Autowired BotConfig config) {
        return JDABuilder.create(config.getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build();
    }

    @Bean
    public DataSource database(@Autowired BotConfig config) {
        return DataSourceBuilder.create()
                .driverClassName(Driver.class.getCanonicalName())
                .url(config.getDatabase().getUri())
                .username(config.getDatabase().getUsername())
                .password(config.getDatabase().getPassword())
                .build();
    }

    @Bean
    public Command.Manager cmdr() {
        var cmdr = new Command.Manager();
        cmdr.addChild(this);

        cmdr.register(this);

        return cmdr;
    }

    @Bean
    public Command.Manager.Adapter$JDA cmdrJdaAdapter(@Autowired Command.Manager cmdr, @Autowired JDA jda)
    throws InterruptedException {
        try {
            var adp = cmdr.new Adapter$JDA(jda.awaitReady());
            adp.setPurgeCommands(FileFlag.consume(COMMAND_PURGE_FILE));
            return adp;
        } finally {
            cmdr.initialize();
        }
    }

    @Bean
    public Event.Bus<GenericEvent> eventBus(@Autowired JDA jda) {
        var bus = new Event.Bus<GenericEvent>();

        bus.register(this);
        jda.addEventListener((EventListener) bus::accept);
        bus.start();

        return bus;
    }

    @Event.Subscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        var data = TraitTriggerData.of(event);
        handleTraitTrigger(data);
    }

    @Event.Subscriber
    public void onMessageReceived(MessageReactionAddEvent event) {
        var data = TraitTriggerData.of(event);
        handleTraitTrigger(data);
    }

    private void handleTraitTrigger(TraitTriggerData data) {
        for (var trait : traits.findAll()) {
            if (trait.getTriggers().stream().noneMatch(trigger -> trigger.test(data))) continue;

            var traitActions = trait.getActions();
            var decidedActions = trait.getDeciders()
                    .stream()
                    .flatMap(decider -> decider.apply(traitActions.stream()))
                    .toList();

            final var model = new ResponseModel();
            for (var action : decidedActions)
                action.accept(model, data);

            if (model.isApplicable()) model.apply(data).queue();
        }
    }
}
