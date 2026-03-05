package yud0o0.main.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import io.github.apace100.origins.badge.BadgeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemDecayMixin {

    @ModifyVariable(
            method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private int multiplyEntropyDamage(int amount, @Local(argsOnly = true) ServerPlayerEntity player) {
        if (player != null) {
            Identifier powerId = Identifier.of("origins-es", "entropy");
            Power power = PowerManager.get(Identifier.of("origins-es", "antiair"));
            if (power.isActive(player)) {
                return amount * 3;
            }
        }
        return amount;
    }
}