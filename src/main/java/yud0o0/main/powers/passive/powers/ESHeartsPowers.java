package yud0o0.main.powers.passive.powers;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.github.apace100.apoli.power.PowerManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.BannedPlayerList;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import yud0o0.main.ESItems;

import java.util.Date;
import java.util.List;

public class ESHeartsPowers {
    public static class ESPVPEvent  implements ServerEntityCombatEvents.AfterKilledOtherEntity {
        @Override
        public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
            if (entity instanceof ServerPlayerEntity player && killedEntity instanceof ServerPlayerEntity victim) {
                if (PowerManager.get(Identifier.of("origins-es", "glassheart")).isActive(victim)) {
                    double x = victim.getX();
                    double y = victim.getY();
                    double z = victim.getZ();
                    world.spawnEntity(new ItemEntity(world, x, y, z, ESItems.HEARTSHARD.getDefaultStack()));
                }
                if (player.isHolding(ESItems.LIFESTEALER)) {
                    double x = victim.getX();
                    double y = victim.getY();
                    double z = victim.getZ();
                    world.spawnEntity(new ItemEntity(world, x, y, z, ESItems.HEART.getDefaultStack()));
                    EntityAttributeInstance attribute = victim.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    attribute.setBaseValue(attribute.getValue()-2);
                    heartlessban(victim);
                }
            }
        }
    }
    public static class ESTakeHeartCommand implements Command<ServerCommandSource> {
        public int run(CommandContext<ServerCommandSource> context) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            if (attribute.getValue() - 2 == 0) {
                player.sendMessage(Text.translatable("chat.origins-es.info.commandheartless").formatted(Formatting.RED));
            } else {
                attribute.setBaseValue(attribute.getValue() - 2);
                player.giveItemStack(ESItems.HEART.getDefaultStack());
            }
            return 1;
        }
    }
    public static class HeartItem extends Item {
        public HeartItem(Settings settings) {
            super(settings);
        }
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);

            if (!world.isClient) {
                EntityAttributeInstance health = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                health.setBaseValue(health.getBaseValue() + 2.0);
                user.heal(2.0f);
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                return TypedActionResult.success(itemStack);
            }
            return TypedActionResult.pass(itemStack);
        }
    }
    public static class ESRealDeathEvent implements ServerLivingEntityEvents.AfterDeath {
        @Override
        public void afterDeath(LivingEntity entity, DamageSource damageSource) {
            if (entity instanceof ServerPlayerEntity victim) {
                if (PowerManager.get(Identifier.of("origins-es", "glassheart")).isActive(victim)) {
                    EntityAttributeInstance attribute = victim.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                    attribute.setBaseValue(attribute.getValue() - 2);
                    heartlessban(victim);
                }
            }
        }
    }
    public static class LifeLocatorItem extends Item {
        public LifeLocatorItem(Settings settings) {
            super(settings);
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack mainHandStack = user.getStackInHand(hand);

            if (!world.isClient) {
                ItemStack offHandStack = user.getOffHandStack();

                if (offHandStack.isOf(Items.PLAYER_HEAD) && offHandStack.contains(DataComponentTypes.PROFILE)) {
                    ProfileComponent profile = offHandStack.get(DataComponentTypes.PROFILE);

                    if (profile != null && profile.name().isPresent()) {
                        String targetName = profile.name().get();

                        ServerPlayerEntity targetPlayer = world.getServer().getPlayerManager().getPlayer(targetName);

                        if (targetPlayer != null) {
                            int x = targetPlayer.getBlockX();
                            int y = targetPlayer.getBlockY();
                            int z = targetPlayer.getBlockZ();

                            String dim = targetPlayer.getWorld().getRegistryKey().getValue().getPath();
                            user.sendMessage(
                                    Text.translatable("chat.origins-es.info.radarprefix")
                                            .append(Text.literal(targetName).formatted(Formatting.AQUA))
                                            .append(Text.translatable("chat.origins-es.info.radarsuffix"))
                                    , false
                            );
                            user.sendMessage(Text.literal("§7 X: §c" + x + " §7Y: §c" + y + " §7Z: §c" + z + " §7(" + dim + ")"), false);
                            if (!user.isCreative()) {
                                mainHandStack.decrement(1);
                                offHandStack.decrement(1);
                            }

                            return TypedActionResult.success(mainHandStack);
                        } else {
                            user.sendMessage(
                                    Text.translatable("chat.origins-es.info.radarprefix")
                                            .append(Text.literal(targetName).formatted(Formatting.AQUA))
                                            .append(Text.translatable("chat.origins-es.info.radarwrongsuffix")),
                                    false
                            );
                        }
                    }
                } else {
                    user.sendMessage(Text.translatable("chat.origins-es.info.offhandheadtip").formatted(Formatting.YELLOW), true);
                }
            }
            return TypedActionResult.pass(mainHandStack);
        }

        @Override
        public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
            tooltip.add(Text.translatable("tooltip.origins-es.life_locator.description").formatted(Formatting.GRAY));
        }
    }
    public static void onFinishUsingItem(LivingEntity entity, ItemStack stack) {
        if (entity instanceof ServerPlayerEntity player) {
            if (PowerManager.get(Identifier.of("origins-es", "godapple")).isActive(player)) {
                EntityAttributeInstance attr = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (attr != null) {
                    if (attr.getValue() < 30) {
                        attr.setBaseValue(attr.getBaseValue() + 2.0);
                    } else {
                        player.sendMessage(Text.translatable("chat.origins-es.info.more30").formatted(Formatting.GOLD));
                    }
                }
            }
        }
    }
    public static void heartlessban(ServerPlayerEntity victim) {
        EntityAttributeInstance attribute = victim.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attribute.getBaseValue() <= 0) {
            BannedPlayerList banList = victim.getServer().getPlayerManager().getUserBanList();

            BannedPlayerEntry entry = new BannedPlayerEntry(
                    victim.getGameProfile(),
                    new Date(),
                    "Server",
                    null,
                    Text.translatable("chat.origins-es.info.heartbankick").toString()
            );

            banList.add(entry);
            victim.networkHandler.disconnect(Text.translatable("chat.origins-es.info.heartbankick"));
        }
    }
}
