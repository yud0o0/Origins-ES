package yud0o0.main.powers.passive;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import yud0o0.main.powers.passive.powers.ESHeartsPowers;

public class ESPassivePowers {
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(new ESHeartsPowers.ESPVPEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new ESHeartsPowers.ESRealDeathEvent());
    }
}
