package yud0o0.main;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import yud0o0.main.powers.passive.powers.ESHeartsPowers;

public class ESCommands {
    public void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("esmain")
                    .then(CommandManager.literal("takeheart")
                            .executes(new ESHeartsPowers.ESTakeHeartCommand())
                    )
            );
        });
    }
}
