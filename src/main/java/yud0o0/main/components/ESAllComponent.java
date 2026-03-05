package yud0o0.main.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ESAllComponent {

    public static class ESHomeComponent implements ESComponent.teleport_data {
        private final Map<String, GlobalPos> homes = new HashMap<>();
        private UUID targetUuid = null;

        @Override public void setHome(String dim, GlobalPos pos) { homes.put(dim, pos); }
        @Override public GlobalPos getHome(String dim) { return homes.get(dim); }
        @Override public void setTargetUuid(UUID uuid) { this.targetUuid = uuid; }
        @Override public UUID getTargetUuid() { return targetUuid; }

        @Override
        public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
            homes.clear();
            NbtCompound homesTag = tag.getCompound("Homes");
            for (String key : homesTag.getKeys()) {
                NbtCompound homeTag = homesTag.getCompound(key);

                BlockPos pos = new BlockPos(homeTag.getInt("x"), homeTag.getInt("y"), homeTag.getInt("z"));
                Identifier worldId = Identifier.of(homeTag.getString("world"));
                RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, worldId);

                homes.put(key, GlobalPos.create(worldKey, pos));
            }

            if (tag.contains("TargetUUID")) {
                targetUuid = tag.getUuid("TargetUUID");
            }
        }

        @Override
        public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
            NbtCompound homesTag = new NbtCompound();
            homes.forEach((dim, globalPos) -> {
                NbtCompound homeTag = new NbtCompound();
                homeTag.putInt("x", globalPos.pos().getX());
                homeTag.putInt("y", globalPos.pos().getY());
                homeTag.putInt("z", globalPos.pos().getZ());
                homeTag.putString("world", globalPos.dimension().getValue().toString());
                homesTag.put(dim, homeTag);
            });
            tag.put("Homes", homesTag);

            if (targetUuid != null) {
                tag.putUuid("TargetUUID", targetUuid);
            }
        }
    }

    public static class ESAFriendsComponent implements ESComponent.afriend_data {
        private boolean afriend = true;

        @Override public void setafriend(Boolean YorN) { this.afriend = YorN; }

        @Override public boolean getafriend() { return this.afriend; }

        @Override
        public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
            if (tag.contains("IsAFriend")) {
                this.afriend = tag.getBoolean("IsAFriend");
            }
        }

        @Override
        public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
            tag.putBoolean("IsAFriend", this.afriend);
        }
    }
}