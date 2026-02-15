package yud0o0.main;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;
import yud0o0.main.components.ESHomeComponent;
import yud0o0.main.components.ESComponent;

public class ESComponents implements EntityComponentInitializer {
    public static final ComponentKey<ESComponent> TELEPORT_DATA =
            ComponentRegistry.getOrCreate(Identifier.of("origins-es", "teleport_data"), ESComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(TELEPORT_DATA, player -> new ESHomeComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}