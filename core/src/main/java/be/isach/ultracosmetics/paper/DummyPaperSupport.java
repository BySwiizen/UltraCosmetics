package be.isach.ultracosmetics.paper;

import org.bukkit.entity.Player;

public class DummyPaperSupport implements PaperSupport {
    @Override
    public boolean hasParticlesDisabled(Player player) {
        return false;
    }
}
