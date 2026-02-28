package yud0o0.main.powers.passive.powers;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yud0o0.main.ESEntities;
import yud0o0.main.ESItems;
import yud0o0.main.ESSounds;

public class ESWaterBomb {
    public static class WaterBombEntity extends ThrownItemEntity {
        public WaterBombEntity(EntityType<? extends WaterBombEntity> entityType, World world) {
            super(entityType, world);
        }

        public WaterBombEntity(World world, LivingEntity owner) {
            super(ESEntities.WATER_BOMB_TYPE, owner, world);
        }

        @Override
        protected Item getDefaultItem() {
            return ESItems.WATER_BOMB;
        }

        @Override
        protected void onCollision(HitResult hitResult) {
            super.onCollision(hitResult);

            if (!this.getWorld().isClient) {
                World world = this.getWorld();
                BlockPos pos = BlockPos.ofFloored(this.getPos());
                for (BlockPos targetPos : BlockPos.iterate(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
                    if (world.getBlockState(targetPos).isAir() || world.getBlockState(targetPos).isReplaceable()) {
                        world.setBlockState(targetPos, Blocks.WATER.getDefaultState());
                    }
                }

                world.playSound(null, pos, ESSounds.WATER_BOMB_EXPLODE, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.SPLASH,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            20, 0.5, 0.5, 0.5, 0.2);
                }

                this.discard();
            }
        }
    }
    public static class WaterBombItem extends Item {
        public WaterBombItem(Settings settings) {
            super(settings);
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);

            world.playSound(null, user.getX(), user.getY(), user.getZ(),
                    ESSounds.WATER_BOMB_THROW, SoundCategory.NEUTRAL, 0.5F, 1.0F);

            if (!world.isClient) {
                WaterBombEntity bomb = new WaterBombEntity(world, user);
                bomb.setItem(itemStack);
                bomb.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
                world.spawnEntity(bomb);
            }

            if (!user.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return TypedActionResult.success(itemStack, world.isClient());
        }
    }
}
