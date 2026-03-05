package yud0o0.main;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import yud0o0.main.powers.passive.powers.ESHeartsPowers;

public class ESCommands {
    public void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("esmain")
                    .then(CommandManager.literal("takeheart")
                            .executes(new ESHeartsPowers.ESTakeHeartCommand())
                    )
                    .then(CommandManager.literal("init")
                            .requires(source -> source.hasPermissionLevel(2))
                            .executes(context -> {
                                for (String player : context.getSource().getServer().getPlayerNames()) {
                                    context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource().getServer().getCommandSource(), "origin set " + player + " origins:origin origins-es:" + player.toLowerCase());
                                    int wb = 29999984;
                                    int wb15 = wb/15;
                                    context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource().getServer().getCommandSource(), "/worldborder set " + wb + " " + wb15);
                                }
                                return 1;
                            })
                    )
                    .then(CommandManager.literal("afriend")
                            .then(CommandManager.literal("Yes")
                                    .executes(context -> {
                                        var otherStorage = ESComponents.AFRIEND_DATA.get(context.getSource().getPlayer());
                                        otherStorage.setafriend(true);
                                    return 1;
                                    })
                            )
                            .then(CommandManager.literal("No")
                                    .executes(context -> {
                                        var otherStorage = ESComponents.AFRIEND_DATA.get(context.getSource().getPlayer());
                                        otherStorage.setafriend(false);
                                        return 1;
                                    })
                            )
                    )
            );
        });
    }
}
