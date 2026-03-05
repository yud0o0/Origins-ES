package yud0o0.main.components;

import org.ladysnake.cca.api.v3.component.Component;
import net.minecraft.util.math.GlobalPos;
import java.util.UUID;

public class ESComponent {
    public interface teleport_data extends Component {
        void setHome(String dimension, GlobalPos pos);

        GlobalPos getHome(String dimension);

        void setTargetUuid(UUID uuid);

        UUID getTargetUuid();
    }
    public interface afriend_data extends Component {
        void setafriend(Boolean YorN);
        boolean getafriend();
    }
}
