package yud0o0.main;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import yud0o0.main.powers.passive.powers.ESHeartsPowers;
import yud0o0.main.powers.passive.powers.ESWaterBomb;

import static yud0o0.main.OriginsES.RTP;

public class ESItems {
    public static final Item HEARTBOX =
            new ESHeartsPowers.LifeLocatorItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
    public static final Item HEART =
            new ESHeartsPowers.HeartItem(new Item.Settings().rarity(Rarity.RARE));
    public static final Item HEARTSHARD =
            new Item(new Item.Settings().rarity(Rarity.RARE));
    public static final ToolMaterial LIFE = new ToolMaterial() {
        @Override
        public int getDurability() {
            return 3000;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return 5;
        }

        @Override
        public float getAttackDamage() {
            return 9;
        }

        @Override
        public TagKey<Block> getInverseTag() {
            return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        }

        @Override
        public int getEnchantability() {
            return 100 ;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(HEART);
        }
    };
    public static final Item LIFESTEALER =
            new SwordItem(
                    LIFE,
                    new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(LIFE, 2, -1.8f)).rarity(Rarity.EPIC)
                    );
    public static final Potion RTPPOT =
            new Potion(
                    new StatusEffectInstance(RTP, 1, 0)
            );
    public static final Potion ABSOLUTERTPPOT =
            new Potion(
                    new StatusEffectInstance(RTP, 1, 1)
            );
    public static final Item RTPINGREDIENTSBAGI =
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON));
    public static final Item RTPINGREDIENTSBAGII =
            new Item(new Item.Settings().rarity(Rarity.EPIC));
    public static final Item OBSIDIAN_ROD =
            new Item(new Item.Settings().rarity(Rarity.RARE));
    public static final Item WATER_BOMB =
            new ESWaterBomb.WaterBombItem(new Item.Settings().maxCount(16));
    public static final Item NIMBUS = new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)) {
        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!world.isClient) {
                ServerWorld serverWorld = (ServerWorld) world;
                if (!world.isRaining()) {
                    serverWorld.setWeather(0, 2400, true, false);
                    world.playSound(null, user.getX(), user.getY(), user.getZ(),
                            SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    if (!user.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                    user.getItemCooldownManager().set(this, 200);
                    return TypedActionResult.consume(itemStack);
                } else {
                    user.sendMessage(Text.translatable("chat.origins-es.info.arained").formatted(Formatting.RED), true);
                    return TypedActionResult.fail(itemStack);
                }
            }
            return TypedActionResult.success(itemStack, world.isClient());
        }
        @Override
        public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
            // Если предмет в руке (selected) и идет дождь
            if (selected && world.isRaining() && entity instanceof LivingEntity) {
                if (!world.isClient && world.getTime() % 5 == 0) { // Раз в 5 тиков, чтобы не лагало
                    ServerWorld serverWorld = (ServerWorld) world;
                    serverWorld.spawnParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY() + 0.1, entity.getZ(), 10, 0.3, 0.3, 0.3, 0);
                }
            }
        }
    };

    public static void register() {

        // --------------ITEMS-----------------

        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "heartbox"),
                HEARTBOX
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "heart"),
                HEART
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "heartshard"),
                HEARTSHARD
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "lifestealer"),
                LIFESTEALER
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "rtpingredientsbag1"),
                RTPINGREDIENTSBAGI
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "rtpingredientsbag2"),
                RTPINGREDIENTSBAGII
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "obsidian_rod"),
                OBSIDIAN_ROD
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "water_bomb"),
                WATER_BOMB
        );
        Registry.register(
                Registries.ITEM,
                Identifier.of("origins-es", "nimbus"),
                NIMBUS
        );



        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EntityRendererRegistry.register(ESEntities.WATER_BOMB_TYPE, (context) ->
                    new FlyingItemEntityRenderer<>(context));
        }

        // --------------GROUPS-----------------

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.NETHER_STAR, ESItems.HEARTBOX, ESItems.HEART, ESItems.HEARTSHARD, ESItems.RTPINGREDIENTSBAGI, ESItems.RTPINGREDIENTSBAGII);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.BREEZE_ROD, ESItems.OBSIDIAN_ROD);
        });
        ItemStack afterStack = PotionContentsComponent.createStack(Items.TIPPED_ARROW, Potions.STRONG_HARMING);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.addAfter(Items.NETHERITE_SWORD, ESItems.LIFESTEALER);
            content.addAfter(
                    afterStack,
                    PotionContentsComponent.createStack(Items.TIPPED_ARROW, OriginsES.RTPPOT),
                    PotionContentsComponent.createStack(Items.TIPPED_ARROW, OriginsES.ABSOLUTERTPPOT));
            content.addAfter(Items.WIND_CHARGE, ESItems.WATER_BOMB, ESItems.NIMBUS);
        });
        ItemStack afterStack1 = PotionContentsComponent.createStack(Items.POTION, Potions.STRONG_HARMING);
        ItemStack afterStack2 = PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.STRONG_HARMING);
        ItemStack afterStack3 = PotionContentsComponent.createStack(Items.LINGERING_POTION, Potions.STRONG_HARMING);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
            content.addAfter(
                    afterStack1,
                    PotionContentsComponent.createStack(Items.POTION, OriginsES.RTPPOT),
                    PotionContentsComponent.createStack(Items.POTION, OriginsES.ABSOLUTERTPPOT));
            content.addAfter(
                    afterStack2,
                    PotionContentsComponent.createStack(Items.SPLASH_POTION, OriginsES.RTPPOT),
                    PotionContentsComponent.createStack(Items.SPLASH_POTION, OriginsES.ABSOLUTERTPPOT));
            content.addAfter(
                    afterStack3,
                    PotionContentsComponent.createStack(Items.LINGERING_POTION, OriginsES.RTPPOT),
                    PotionContentsComponent.createStack(Items.LINGERING_POTION, OriginsES.ABSOLUTERTPPOT));
        });



        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    Ingredient.ofItems(RTPINGREDIENTSBAGI),
                    OriginsES.RTPPOT
            );
        });
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    OriginsES.RTPPOT,
                    Ingredient.ofItems(RTPINGREDIENTSBAGII),
                    OriginsES.ABSOLUTERTPPOT
            );
        });



    }
}
