package yud0o0.main.tome;

import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.registry.Registries;
import vazkii.patchouli.api.PatchouliAPI;
import yud0o0.main.OriginsES;

import java.util.ArrayList;
import java.util.List;

public class ESTome extends Item {

    public ESTome(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack tomeStack = user.getStackInHand(hand);
        List<String> books = new ArrayList<>(tomeStack.getOrDefault(OriginsES.ACQUIRED_BOOKS, List.of()));
        String activeId = tomeStack.get(OriginsES.ACTIVE_BOOK);

        if (user.isSneaking() && !books.isEmpty()) {
            if (world.isClient) {
                openScreen(books);
            }
            return TypedActionResult.success(tomeStack);
        }

        if (!world.isClient) {
            ItemStack offHandStack = user.getOffHandStack();
            Identifier patchouliComponentId = Identifier.of("patchouli", "book");
            ComponentType<Identifier> patchouliType = (ComponentType<Identifier>) Registries.DATA_COMPONENT_TYPE.get(patchouliComponentId);

            if (patchouliType != null && offHandStack.getComponents().contains(patchouliType)) {
                Identifier bookIdentifier = offHandStack.get(patchouliType);
                if (bookIdentifier != null) {
                    String bookId = bookIdentifier.toString();

                    if (!books.contains(bookId)) {
                        books.add(bookId);
                        tomeStack.set(OriginsES.ACQUIRED_BOOKS, books);

                        if (activeId == null) {
                            tomeStack.set(OriginsES.ACTIVE_BOOK, bookId);
                        }

                        user.sendMessage(Text.translatable("chat.origins-es.info.tome_added", bookId).formatted(Formatting.GREEN), true);
                    } else {
                        user.sendMessage(Text.translatable("chat.origins-es.info.tome_exists").formatted(Formatting.RED), true);
                    }
                    return TypedActionResult.success(tomeStack);
                }
            }

            if (activeId != null) {
                PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, Identifier.of(activeId));
            } else {
                user.sendMessage(Text.translatable("chat.origins-es.info.tome_empty").formatted(Formatting.GRAY, Formatting.ITALIC), true);
            }
        }

        return TypedActionResult.success(tomeStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        String active = stack.get(OriginsES.ACTIVE_BOOK);
        List<String> allBooks = stack.get(OriginsES.ACQUIRED_BOOKS);

        if (active != null && !active.isEmpty()) {
            tooltip.add(Text.translatable("chat.origins-es.info.tooltip_tome_active", active));
        } else {
            tooltip.add(Text.translatable("chat.origins-es.info.tooltip_tome_empty").formatted(Formatting.DARK_GRAY));
        }

        if (allBooks != null && !allBooks.isEmpty()) {
            tooltip.add(Text.translatable("chat.origins-es.info.tooltip_tome_total", allBooks.size()));
        }
    }
    private void openScreen(List<String> books) {
        net.minecraft.client.MinecraftClient.getInstance().setScreen(new ESTomeScreen(books));
    }
}