package yud0o0.main.powers.action.entity;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import yud0o0.main.ESComponents;
import yud0o0.main.OriginsES;

import java.util.UUID;

public class ESTeleportActions {

    public static final Identifier SAVE_MARK_ID = Identifier.of(OriginsES.MOD_ID, "tpmarksave");
    public static final Identifier GO_TO_MARK_ID = Identifier.of(OriginsES.MOD_ID, "tpmarkgo");

    public static void register() {
        registerAction(SAVE_MARK_ID, SaveMarkAction.DATA_FACTORY);
        registerAction(GO_TO_MARK_ID, GoToMarkAction.DATA_FACTORY);
    }

    private static <T extends EntityActionType> void registerAction(Identifier id, TypedDataObjectFactory<T> factory) {
        ActionConfiguration<T> config = ActionConfiguration.of(id, factory);

        ActionConfiguration<EntityActionType> castedConfig = (ActionConfiguration<EntityActionType>) config;

        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, id, castedConfig);
    }

    public static class SaveMarkAction extends EntityActionType {
        public static final TypedDataObjectFactory<SaveMarkAction> DATA_FACTORY = TypedDataObjectFactory.simple(
                new SerializableData(),
                data -> new SaveMarkAction(),
                (type, data) -> data.instance()
        );

        @Override
        public void accept(EntityActionContext context) {
            Entity entity = context.entity();
            if (!(entity instanceof ServerPlayerEntity player)) return;
            var storage = ESComponents.TELEPORT_DATA.get(player);

            if (player.isSneaking()) {
                Entity target = getTargetEntity(player, 12);
                if (target != null) {
                    storage.setTargetUuid(target.getUuid());
                    player.sendMessage(Text.translatable("chat.origins-es.info.tptarget_captured").formatted(Formatting.GREEN), true);
                } else {
                    player.sendMessage(Text.translatable("chat.origins-es.info.tptarget_not_found").formatted(Formatting.RED), true);
                }
            } else {
                String dim = player.getWorld().getRegistryKey().getValue().getPath();
                GlobalPos pos = GlobalPos.create(player.getWorld().getRegistryKey(), player.getBlockPos());
                storage.setHome(dim, pos);
                player.sendMessage(Text.translatable("chat.origins-es.info.tphome_set").formatted(Formatting.GREEN), true);
            }
        }

        @Override
        public @NotNull ActionConfiguration<?> getConfig() {
            return ActionConfiguration.of(SAVE_MARK_ID, DATA_FACTORY);
        }
    }

    public static class GoToMarkAction extends EntityActionType {
        public static final TypedDataObjectFactory<GoToMarkAction> DATA_FACTORY = TypedDataObjectFactory.simple(
                new SerializableData(),
                data -> new GoToMarkAction(),
                (type, data) -> data.instance()
        );

        @Override
        public void accept(EntityActionContext context) {
            Entity entity = context.entity();
            if (!(entity instanceof ServerPlayerEntity player)) return;
            var storage = ESComponents.TELEPORT_DATA.get(player);

            if (player.isSneaking()) {
                UUID entityId = storage.getTargetUuid();
                for (ServerWorld world : player.getServer().getWorlds()) {
                    Entity foundEntity = world.getEntity(entityId);
                    if (foundEntity != null) {
                        player.teleport(world, foundEntity.getX(), foundEntity.getY(), foundEntity.getZ(), player.getYaw(), player.getPitch());
                        return;
                    }
                }
                player.sendMessage(Text.translatable("chat.origins-es.info.tpfail_offline").formatted(Formatting.RED), false);
            } else {
                GlobalPos globalPos = storage.getHome(player.getWorld().getRegistryKey().getValue().getPath());
                if (globalPos != null) {
                    ServerWorld targetWorld = player.getServer().getWorld(globalPos.dimension());
                    if (targetWorld != null) {
                        player.teleport(targetWorld, globalPos.pos().getX() + 0.5, globalPos.pos().getY(), globalPos.pos().getZ() + 0.5, player.getYaw(), player.getPitch());
                    }
                } else {
                    player.sendMessage(Text.translatable("chat.origins-es.info.tphome_not_set").formatted(Formatting.RED), false);
                }
            }
        }

        @Override
        public @NotNull ActionConfiguration<?> getConfig() {
            return ActionConfiguration.of(GO_TO_MARK_ID, DATA_FACTORY);
        }
    }

    public static Entity getTargetEntity(ServerPlayerEntity player, double distance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F).multiply(distance);
        Vec3d end = start.add(direction);
        Box box = player.getBoundingBox().stretch(direction).expand(1.0D);
        EntityHitResult hitResult = ProjectileUtil.raycast(player, start, end, box, (e) -> !e.isSpectator() && e.canHit(), distance * distance);
        return (hitResult != null) ? hitResult.getEntity() : null;
    }
}