package yud0o0.main;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yud0o0.main.powers.action.ESEntityActionTypes;

public class OriginsES implements ModInitializer {
	public static final String MOD_ID = "origins-es";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RegistryEntry<StatusEffect> RTP = Registry.registerReference(
			Registries.STATUS_EFFECT,
			Identifier.of(MOD_ID, "rtp"),
			new ESEffect(StatusEffectCategory.BENEFICIAL, 0x9370DB)
	);
	public static final RegistryEntry<Potion> RTPPOT = Registry.registerReference(
			Registries.POTION,
			Identifier.of(MOD_ID, "rtp_potion"),
			new Potion(new StatusEffectInstance(RTP, 1, 0))
	);

	public static final RegistryEntry<Potion> ABSOLUTERTPPOT = Registry.registerReference(
			Registries.POTION,
			Identifier.of(MOD_ID, "absolute_rtp_potion"),
			new Potion(new StatusEffectInstance(RTP, 1, 1))
	);

	@Override
	public void onInitialize() {
		ESEntityActionTypes.register();
		ESItems.register();
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
					PotionContentsComponent.createStack(Items.TIPPED_ARROW, RTPPOT),
					PotionContentsComponent.createStack(Items.TIPPED_ARROW, ABSOLUTERTPPOT));
		});
		ItemStack afterStack1 = PotionContentsComponent.createStack(Items.POTION, Potions.STRONG_HARMING);
		ItemStack afterStack2 = PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.STRONG_HARMING);
		ItemStack afterStack3 = PotionContentsComponent.createStack(Items.LINGERING_POTION, Potions.STRONG_HARMING);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
			content.addAfter(
					afterStack1,
					PotionContentsComponent.createStack(Items.POTION, RTPPOT),
					PotionContentsComponent.createStack(Items.POTION, ABSOLUTERTPPOT));
			content.addAfter(
					afterStack2,
					PotionContentsComponent.createStack(Items.SPLASH_POTION, RTPPOT),
					PotionContentsComponent.createStack(Items.SPLASH_POTION, ABSOLUTERTPPOT));
			content.addAfter(
					afterStack3,
					PotionContentsComponent.createStack(Items.LINGERING_POTION, RTPPOT),
					PotionContentsComponent.createStack(Items.LINGERING_POTION, ABSOLUTERTPPOT));
		});
		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(
					Potions.AWKWARD,
					Ingredient.ofItems(ESItems.RTPINGREDIENTSBAGI),
					RTPPOT
			);
		});
		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(
					RTPPOT,
					Ingredient.ofItems(ESItems.RTPINGREDIENTSBAGII),
					ABSOLUTERTPPOT
			);
		});

		new ESCommands().init();
		LOGGER.info("Origins-ES was instalized");
	}
}