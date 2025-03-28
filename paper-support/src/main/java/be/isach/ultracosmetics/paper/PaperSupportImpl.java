package be.isach.ultracosmetics.paper;

import com.destroystokyo.paper.ClientOption;
import org.bukkit.entity.Player;

public class PaperSupportImpl implements PaperSupport {
    public PaperSupportImpl() {
        // Fail early if we're not actually on recent Paper
        ClientOption.ParticleVisibility.class.getName();
    }

    @Override
    public boolean hasParticlesDisabled(Player player) {
        return player.getClientOption(ClientOption.PARTICLE_VISIBILITY) == ClientOption.ParticleVisibility.MINIMAL;
    }
}
