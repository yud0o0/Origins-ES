package yud0o0.main;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ESSounds {
    public static final SoundEvent WATER_BOMB_THROW = register("water_bomb_throw");
    public static final SoundEvent WATER_BOMB_EXPLODE = register("water_bomb_explode");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.of("origins-es", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {}
}
