package yud0o0.main.tome;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import yud0o0.main.OriginsES;

import java.util.List;

public class ESTomePayload {

    public record SelectBookPayload(String bookId) implements CustomPayload {
        public static final Id<SelectBookPayload> ID = new Id<>(Identifier.of("origins-es", "select_book"));

        public static final PacketCodec<PacketByteBuf, SelectBookPayload> CODEC = PacketCodec.tuple(
                PacketCodecs.STRING, SelectBookPayload::bookId,
                SelectBookPayload::new
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void register() {

        PayloadTypeRegistry.configurationC2S().register(SelectBookPayload.ID, SelectBookPayload.CODEC); // Для фазы конфигурации если нужно, но лучше сразу в PLAY:
        PayloadTypeRegistry.playC2S().register(SelectBookPayload.ID, SelectBookPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SelectBookPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                var player = context.player();
                var stack = player.getMainHandStack(); // Предполагаем, что книга в главной руке

                if (stack.getItem() instanceof ESTome) {
                    List<String> books = stack.getOrDefault(OriginsES.ACQUIRED_BOOKS, List.of());
                    String selected = payload.bookId();

                    if (books.contains(selected)) {
                        stack.set(OriginsES.ACTIVE_BOOK, selected);
                        player.sendMessage(Text.translatable("chat.origins-es.info.tome_selected", selected), true);
                    }
                }
            });
        });
    }
}
