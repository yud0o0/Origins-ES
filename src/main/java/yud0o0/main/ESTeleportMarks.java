package yud0o0.main;

//PIZDEC

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.UUID;

public class ESTeleportMarks {
    public void main() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("esmain")
                    .then(CommandManager.literal("tpmarksave")
                            .executes(commandContext -> {
                                ServerCommandSource source = commandContext.getSource();
                                var player = source.getPlayer();
                                if (player!=null) {
                                    Identifier powerId = Identifier.of("origins-es", "savetpmark");
                                    boolean hasPower = PowerHolderComponent.hasPowerType(player, PowerManager.get(powerId).getType().getClass());
                                    if (hasPower) {
                                        var storage = ESComponents.TELEPORT_DATA.get(player);
                                        if (player.isSneaking()) {
                                            Entity target = getTargetEntity(player, 12);
                                            if (target != null) {
                                                UUID targetId = target.getUuid();
                                                storage.setTargetUuid(targetId);
                                            }
                                        } else {
                                            World world = player.getWorld();
                                            String dim = world.getRegistryKey().getValue().getPath();
                                            GlobalPos pos = GlobalPos.create(world.getRegistryKey(), player.getBlockPos());
                                            storage.setHome(dim, pos);
                                        }
                                    } else {
                                        String playername = player.getName().toString();
                                        EntityType.LIGHTNING_BOLT.spawn(source.getWorld(), player.getBlockPos(), SpawnReason.COMMAND);
                                        player.sendMessage(Text.of("§4U try to use power that does not belong to you?"));
                                    }
                                }
                                return 1;
                                })
                    )
                    .then(CommandManager.literal("tpmarkgo")
                            .executes(commandContext -> {
                                ServerCommandSource source = commandContext.getSource();
                                var player = source.getPlayer();
                                if (player!=null) {
                                    Identifier powerId = Identifier.of("origins-es", "tptotpmark");
                                    boolean hasPower = PowerHolderComponent.hasPowerType(player, PowerManager.get(powerId).getType().getClass());
                                    if (hasPower) {
                                        var storage = ESComponents.TELEPORT_DATA.get(player);
                                        if (player.isSneaking()) {
                                            Entity foundEntity = null;
                                            ServerWorld world = null;
                                            UUID entityId = storage.getTargetUuid();
                                            for (ServerWorld iworld : source.getServer().getWorlds()) {
                                                foundEntity = iworld.getEntity(entityId);
                                                world = iworld;
                                                if (foundEntity != null) {
                                                    double x = foundEntity.getX();
                                                    ;
                                                    double y = foundEntity.getY();
                                                    double z = foundEntity.getZ();
                                                    ;
                                                    float yaw = player.getYaw();
                                                    float pitch = player.getPitch();

                                                    player.teleport(world, x, y, z, yaw, pitch);
                                                    break;
                                                }
                                            }
                                            if (foundEntity == null) {
                                                source.sendError(Text.literal("Цель не найдена или чанк не прогружен."));
                                            }
                                        } else {
                                            GlobalPos globalPos = storage.getHome(source.getWorld().getRegistryKey().getValue().getPath());

                                            ServerWorld targetWorld = source.getServer().getWorld(globalPos.dimension());
                                            double x = globalPos.pos().getX() + 0.5;
                                            ;
                                            double y = globalPos.pos().getY();
                                            double z = globalPos.pos().getZ() + 0.5;
                                            ;
                                            float yaw = player.getYaw();
                                            float pitch = player.getPitch();

                                            player.teleport(targetWorld, x, y, z, yaw, pitch);
                                        }
                                    }else {
                                        String playername = player.getName().toString();
                                        EntityType.LIGHTNING_BOLT.spawn(source.getWorld(), player.getBlockPos(), SpawnReason.COMMAND);
                                        player.sendMessage(Text.of("§4U try to use power that does not belong to you?"));
                                    }
                                }
                                return 1;
                                })
                    )
            );
        });
    }
    public static Entity getTargetEntity(ServerPlayerEntity player, double distance) {
        Vec3d start = player.getCameraPosVec(1.0F);
        Vec3d direction = player.getRotationVec(1.0F).multiply(distance);
        Vec3d end = start.add(direction);

        Box box = player.getBoundingBox().stretch(direction).expand(1.0D);

        EntityHitResult hitResult = ProjectileUtil.raycast(
                player,
                start,
                end,
                box,
                (entity) -> !entity.isSpectator() && entity.canHit(),
                distance * distance
        );

        return (hitResult != null) ? hitResult.getEntity() : null;
    }
}
