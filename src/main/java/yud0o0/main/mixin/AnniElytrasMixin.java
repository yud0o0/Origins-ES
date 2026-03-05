package yud0o0.main.mixin;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yud0o0.main.ESComponents;

@Mixin(PlayerEntity.class)
public class AnniElytrasMixin {
    @Inject(method = "startFallFlying", at = @At("HEAD"), cancellable = true)
    public void cancelElytraNearEnemies(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        Power power = PowerManager.get(Identifier.of("origins-es", "antiair"));
        if (power.isActive(player)) {
            Vec3d center = player.getPos();
            Box box = new Box(
                    center.x - 40, center.y - 40, center.z - 40,
                    center.x + 40, center.y + 40, center.z + 40
            );
            for (Entity e : player.getWorld().getOtherEntities(player, box)) {
                if (e instanceof PlayerEntity otherPlayer) {
                    var otherStorage = ESComponents.AFRIEND_DATA.get(otherPlayer);
                    if (!otherStorage.getafriend()) {
                        ci.cancel();
                        player.stopFallFlying();
                        return;
                    }
                }
            }
        }
    }
}