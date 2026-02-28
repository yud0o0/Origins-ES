package yud0o0.main;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static yud0o0.main.OriginsES.MOD_ID;

public class ESEnchants {
    public static final RegistryKey<Enchantment> HARPOON_KEY =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "harpoon"));
}
