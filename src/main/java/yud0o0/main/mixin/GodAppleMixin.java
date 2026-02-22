package yud0o0.main.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yud0o0.main.powers.passive.powers.ESHeartsPowers;

@Mixin(ServerPlayerEntity.class)
public abstract class GodAppleMixin {
    @Inject(method = "consumeItem", at = @At("HEAD"))
    private void onConsumeItem(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ItemStack stack = player.getActiveItem();

        if (stack != null && stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
            ESHeartsPowers.onFinishUsingItem(player, stack);
        }
    }
}