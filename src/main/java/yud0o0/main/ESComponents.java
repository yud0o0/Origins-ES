package yud0o0.main;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;
import yud0o0.main.components.ESAllComponent;
import yud0o0.main.components.ESComponent;

public class ESComponents implements EntityComponentInitializer {
    public static final ComponentKey<ESComponent.teleport_data> TELEPORT_DATA =
            ComponentRegistry.getOrCreate(Identifier.of("origins-es", "teleport_data"), ESComponent.teleport_data.class);
    public static final ComponentKey<ESComponent.afriend_data> AFRIEND_DATA =
            ComponentRegistry.getOrCreate(Identifier.of("origins-es", "afriend_data"), ESComponent.afriend_data.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TELEPORT_DATA, player -> new ESAllComponent.ESHomeComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(AFRIEND_DATA, player -> new ESAllComponent.ESAFriendsComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}