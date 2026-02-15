package yud0o0.main.components;

import org.ladysnake.cca.api.v3.component.Component;
import net.minecraft.util.math.GlobalPos;
import java.util.UUID;

public interface ESComponent extends Component {
    void setHome(String dimension, GlobalPos pos);
    GlobalPos getHome(String dimension);

    void setTargetUuid(UUID uuid);
    UUID getTargetUuid();
}
