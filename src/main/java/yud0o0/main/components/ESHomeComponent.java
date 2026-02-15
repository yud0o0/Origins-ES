package yud0o0.main.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.GlobalPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ESHomeComponent implements ESComponent {
    private final Map<String, GlobalPos> homes = new HashMap<>();
    private UUID targetUuid = null;

    @Override
    public void setHome(String dim, GlobalPos pos) { homes.put(dim, pos); }

    @Override
    public GlobalPos getHome(String dim) { return homes.get(dim); }

    @Override
    public void setTargetUuid(UUID uuid) { this.targetUuid = uuid; }

    @Override
    public UUID getTargetUuid() { return targetUuid; }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        homes.clear();
        for (String dim : new String[]{"overworld", "nether", "end"}) {
            if (tag.contains("Home_" + dim)) {
            }
        }
        if (tag.contains("TargetUUID")) {
            targetUuid = tag.getUuid("TargetUUID");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (targetUuid != null) {
            tag.putUuid("TargetUUID", targetUuid);
        }
    }
}
