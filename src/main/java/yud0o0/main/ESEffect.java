package yud0o0.main;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public class ESEffect extends StatusEffect {
    public ESEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        this.applyInstantEffect(null, null, entity, amplifier, 1.0);
        return true;
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        if (!target.getWorld().isClient) {
            World world = target.getWorld();
            double x, y, z;

            if (amplifier == 0) {
                Random random = target.getRandom();
                x = target.getX() + (random.nextDouble() - 0.5) * 24; // разброс 12 в каждую сторону
                y = target.getY() + (random.nextInt(5) - 2); // чуть-чуть по вертикали
                z = target.getZ() + (random.nextDouble() - 0.5) * 24;
            } else {
                Random random = target.getRandom();
                x = (random.nextDouble() - 0.5) * world.getWorldBorder().getSize();
                z = (random.nextDouble() - 0.5) * world.getWorldBorder().getSize();
                y = (random.nextDouble() - 0.5) * 118;
            }

            BlockPos targetPos = new BlockPos((int)x, (int)y, (int)z);

            ChunkPos chunkPos = new ChunkPos(targetPos);
            target.getServer().getOverworld().getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, target.getId());
            target.getServer().getOverworld().getChunk(chunkPos.x, chunkPos.z);

            targetPos = findInterestingTeleportPos(target.getServer().getOverworld(), targetPos.getX(), targetPos.getZ(), targetPos.getY());

            Set<PositionFlag> flags = EnumSet.noneOf(PositionFlag.class);

            target.teleport(target.getServer().getOverworld(), targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5, flags, target.getYaw(), target.getPitch());
            world.playSound(null, targetPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }
    private BlockPos findInterestingTeleportPos(ServerWorld world, double x, double z, int startY) {
        BlockPos.Mutable mutablePos = new BlockPos.Mutable((int)x, startY, (int)z);
        int bottomY = world.getBottomY();

        for (int y = startY; y > bottomY + 2; y--) {
            mutablePos.setY(y);

            if (world.getBlockState(mutablePos).isAir() &&
                    world.getBlockState(mutablePos.up()).isAir() &&
                    world.getBlockState(mutablePos.down()).isSolid()) {

                return mutablePos.toImmutable();
            }
        }

        int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int)x, (int)z);

        if (topY <= bottomY) {
            topY = 100;
        }

        return new BlockPos((int)x, topY, (int)z);
    }
}
