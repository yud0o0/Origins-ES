package yud0o0.main.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yud0o0.main.ESEnchants;

@Mixin(TridentEntity.class)
public abstract class TridentHarpoonMixin extends PersistentProjectileEntity {

    protected TridentHarpoonMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void pullOwnerTick(CallbackInfo ci) {
        if (this.inGround && !this.getWorld().isClient) {
            Entity owner = this.getOwner();
            ItemStack stack = this.getItemStack();

            if (owner instanceof PlayerEntity player) {
                ServerWorld world = (ServerWorld) this.getWorld();
                var registry = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
                var harpoonEntry = registry.getEntry(ESEnchants.HARPOON_KEY);

                if (harpoonEntry.isPresent() && EnchantmentHelper.getLevel(harpoonEntry.get(), stack) > 0) {
                    Vec3d tridentPos = this.getPos();
                    Vec3d playerPos = player.getPos();
                    double distance = tridentPos.distanceTo(playerPos);

                    if (distance > 3.0 && distance < 64.0) {
                        Vec3d diff = tridentPos.subtract(playerPos);

                        double pullPower = 0.25;
                        Vec3d velocity = diff.normalize().multiply(pullPower);

                        player.addVelocity(velocity.x, velocity.y * 1.1, velocity.z);

                        if (player.getVelocity().y < 0.2) {
                            player.addVelocity(0, 0.04, 0);
                        }

                        player.velocityModified = true;
                        player.fallDistance = 0;
                    }
                }
            }
        }
    }
}