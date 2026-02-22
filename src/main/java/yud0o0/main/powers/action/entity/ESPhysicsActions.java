package yud0o0.main.powers.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.NotNull;
import yud0o0.main.powers.action.ESEntityActionTypes;

public class ESPhysicsActions {

    public static class InvertYAction extends EntityActionType {

        public static final TypedDataObjectFactory<InvertYAction> DATA_FACTORY = TypedDataObjectFactory.simple(
                new SerializableData(),
                data -> new InvertYAction(),
                (type, data) -> data.instance()
        );

        @Override
        public void accept(EntityActionContext context) {
            Entity entity = context.entity();
            if (!(entity instanceof LivingEntity living)) return;

            if (living instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
                if (!player.getInventory().contains(Items.WIND_CHARGE.getDefaultStack())) {
                    if (living.getWorld().getTime() % 40 == 0) {
                        player.sendMessage(Text.translatable("").formatted(Formatting.RED), true);
                    }
                    return;
                }

                if (living.getWorld().getTime() % 20 == 0) {
                    removeItem(player, Items.WIND_CHARGE, 1);
                }
            }


            Vec3d vel = living.getVelocity();

            if (vel.y < -0.1) {
                BlockPos pos = living.getBlockPos();
                double surfaceY = living.getWorld().getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
                double distToGround = living.getY() - surfaceY;

                if (distToGround < 8.0 && distToGround >= 3.0 && vel.y < -1.0) {
                    living.setVelocity(vel.x, vel.y * 0.8, vel.z);
                    living.velocityModified = true;
                }

                if (distToGround < 3.0) {
                    double bounceImpulse = Math.abs(vel.y) * 1.2 + 0.4;
                    living.setVelocity(vel.x, bounceImpulse, vel.z);
                    living.velocityModified = true;

                    if (living.getWorld() instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(ParticleTypes.GUST_EMITTER_LARGE,
                                living.getX(), surfaceY + 0.5, living.getZ(),
                                1, 0.0, 0.0, 0.0, 0.0);
                        serverWorld.playSound(null, living.getX(), living.getY(), living.getZ(),
                                SoundEvents.ENTITY_WIND_CHARGE_WIND_BURST, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                }
            }
        }

        private void removeItem(PlayerEntity player, net.minecraft.item.Item item, int count) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                net.minecraft.item.ItemStack stack = player.getInventory().getStack(i);
                if (stack.getItem() == item) {
                    stack.decrement(count);
                    break;
                }
            }
        }

        @Override
        public @NotNull ActionConfiguration<?> getConfig() {
            return ESEntityActionTypes.INVERT_Y;
        }
    }
}