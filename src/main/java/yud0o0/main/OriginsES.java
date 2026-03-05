package yud0o0.main;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yud0o0.main.powers.action.ESEntityActionTypes;
import yud0o0.main.powers.passive.ESPassivePowers;
import yud0o0.main.tome.ESTomePayload;

import java.util.List;

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

	public static final ComponentType<List<String>> ACQUIRED_BOOKS = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of("origins-es", "acquired_books"),
			ComponentType.<List<String>>builder()
					.codec(Codec.STRING.listOf())
					.packetCodec(PacketCodecs.STRING.collect(PacketCodecs.toList()))
					.cache()
					.build()
	);

	public static final ComponentType<String> ACTIVE_BOOK = Registry.register(
			Registries.DATA_COMPONENT_TYPE,
			Identifier.of("origins-es", "active_book"),
			ComponentType.<String>builder()
					.codec(Codec.STRING)
					.packetCodec(PacketCodecs.STRING)
					.build()
	);

	@Override
	public void onInitialize() {
		ESEntityActionTypes.register();
		ESEntities.register();
		ESSounds.register();
		ESItems.register();
		ESPassivePowers.register();
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {ESfck.register();}
		ESTomePayload.register();
		new ESCommands().init();
		LOGGER.info("Origins-ES was instalized");

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {

			ServerPlayerEntity player = handler.player;

			int timeplay = player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME));

			if (timeplay >= 10) {
				server.sendMessage(Text.literal("Игрок " + player.getNameForScoreboard() + " зашел впервые!"));
				server.getCommandManager().executeWithPrefix(server.getCommandSource(), "origin set " + player.getNameForScoreboard() + " origins:origin origins:human");
			}
		});
	}
}