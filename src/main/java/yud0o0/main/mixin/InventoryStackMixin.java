package yud0o0.main.mixin;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class InventoryStackMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void updateStackSizes(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.age % 10 != 0) return;
        Power power = PowerManager.get(Identifier.of("origins-es", "inventorystack"));
        if (power.isActive(player)) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (!stack.isEmpty()) {
                    if (stack.getMaxCount() < 64) {
                        stack.set(DataComponentTypes.MAX_STACK_SIZE, 64);
                    }
                }
            }
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (!stack.isEmpty()) {
                    if (stack.getCount() > stack.getItem().getMaxCount()) {
                        stack.remove(DataComponentTypes.MAX_STACK_SIZE);
                        player.dropStack(stack.copy());
                        stack.setCount(0);
                    }
                }
            }
        }
    }
}