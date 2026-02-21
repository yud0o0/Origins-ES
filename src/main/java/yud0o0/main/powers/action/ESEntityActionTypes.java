package yud0o0.main.powers.action;

import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import yud0o0.main.OriginsES;
import yud0o0.main.powers.action.entity.ESPhysicsActions.InvertYAction;
import yud0o0.main.powers.action.entity.ESTeleportActions.SaveMarkAction;
import yud0o0.main.powers.action.entity.ESTeleportActions.GoToMarkAction;

public class ESEntityActionTypes {

    public static final ActionConfiguration<SaveMarkAction> SAVE_MARK = register(
            ActionConfiguration.of(Identifier.of("origins-es", "tpmarksave"), SaveMarkAction.DATA_FACTORY)
    );

    public static final ActionConfiguration<GoToMarkAction> GO_TO_MARK = register(
            ActionConfiguration.of(Identifier.of("origins-es", "tpmarkgo"), GoToMarkAction.DATA_FACTORY)
    );

    public static final ActionConfiguration<InvertYAction> INVERT_Y = register(
            ActionConfiguration.of(Identifier.of("origins-es", "invert-y"), InvertYAction.DATA_FACTORY)
    );

    @SuppressWarnings("unchecked")
    public static <T extends EntityActionType> ActionConfiguration<T> register(ActionConfiguration<T> configuration) {
        ActionConfiguration<EntityActionType> casted = (ActionConfiguration<EntityActionType>) configuration;
        Registry.register(ApoliRegistries.ENTITY_ACTION_TYPE, casted.id(), casted);
        return configuration;
    }

    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        if (SAVE_MARK != null) {
            registered = true;
            OriginsES.LOGGER.info("Custom Entity Actions registered successfully.");
        }
    }
}