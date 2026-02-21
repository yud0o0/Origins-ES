package yud0o0.main.mixin;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class MaceShieldMixin {
	@Inject(method = "attack", at = @At("HEAD"))
	private void onAttack(Entity target, CallbackInfo ci) {
		PlayerEntity attacker = (PlayerEntity) (Object) this;
		Power power = PowerManager.get(Identifier.of("origins-es", "shield_crush"));
		if (power.isActive(attacker) && attacker.getMainHandStack().isOf(Items.MACE)) {
			if (target instanceof PlayerEntity victim) {
				if (victim.isBlocking()) {
					victim.getItemCooldownManager().set(victim.getActiveItem().getItem(), 100);
					victim.stopUsingItem();
					attacker.getWorld().sendEntityStatus(victim, (byte) 30);
				}
			}
		}
	}
}