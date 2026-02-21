package yud0o0.main;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static yud0o0.main.OriginsES.RTP;

public class ESItems {
    public static final Item HEARTBOX =
            new Item(new Item.Settings().rarity(Rarity.EPIC));
    public static final Item HEART =
            new Item(new Item.Settings().rarity(Rarity.RARE));
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

    public static void register() {
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
    }
}
