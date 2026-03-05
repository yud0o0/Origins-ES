package yud0o0.main.tome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ESTomeScreen extends Screen {
    private final List<String> books;
    private int page = 0;

    private static final int COLUMNS = 5;
    private static final int ROWS = 2;
    private static final int BOOKS_PER_PAGE = COLUMNS * ROWS;

    private static final int SLOT_SIZE = 32;
    private static final int SPACING = 5; // Отступ между кнопками

    public ESTomeScreen(List<String> books) {
        super(Text.literal("Tome Index"));
        this.books = books;
    }

    @Override
    protected void init() {
        this.clearChildren();

        int centerX = width / 2;
        int centerY = height / 2;

        int totalGridWidth = (COLUMNS * SLOT_SIZE) + ((COLUMNS - 1) * SPACING);
        int startX = centerX - (totalGridWidth / 2);
        int startY = centerY - 40;

        int start = page * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, books.size());

        for (int i = start; i < end; i++) {
            String bookId = books.get(i);
            int indexOnPage = i - start;

            int col = indexOnPage % COLUMNS;
            int row = indexOnPage / COLUMNS;

            int x = startX + (col * (SLOT_SIZE + SPACING));
            int y = startY + (row * (SLOT_SIZE + SPACING));

            ButtonWidget bookButton = new BookIconButton(x, y, bookId, button -> {
                ClientPlayNetworking.send(new ESTomePayload.SelectBookPayload(bookId));
                PatchouliAPI.get().openBookGUI(Identifier.of(bookId));
                this.close();
            });

            bookButton.setTooltip(Tooltip.of(Text.literal(bookId)));
            addDrawableChild(bookButton);
        }

        if (page > 0) {
            addDrawableChild(ButtonWidget.builder(Text.literal("<"), b -> { page--; init(); })
                    .dimensions(startX - 30, centerY - 15, 20, 20).build());
        }
        if (end < books.size()) {
            addDrawableChild(ButtonWidget.builder(Text.literal(">"), b -> { page++; init(); })
                    .dimensions(startX + totalGridWidth + 10, centerY - 15, 20, 20).build());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, this.title, width / 2, 20, 0xffffff);

        String pageInfo = (page + 1) + " / " + ((books.size() - 1) / BOOKS_PER_PAGE + 1);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(pageInfo), width / 2, height / 2 + 35, 0xaaaaaa);
    }

    private class BookIconButton extends ButtonWidget {
        private final ItemStack iconStack = new ItemStack(Items.BOOK);

        public BookIconButton(int x, int y, String bookId, PressAction onPress) {
            super(x, y, SLOT_SIZE, SLOT_SIZE, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderWidget(context, mouseX, mouseY, delta);

            int itemX = getX() + (this.width - 16) / 2;
            int itemY = getY() + (this.height - 16) / 2;
            context.drawItem(iconStack, itemX, itemY);
        }
    }

    @Override
    public boolean shouldPause() { return false; }
}