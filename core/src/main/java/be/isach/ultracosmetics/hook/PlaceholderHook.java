package be.isach.ultracosmetics.hook;

import be.isach.ultracosmetics.UltraCosmetics;
import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.suits.ArmorSlot;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.player.UltraPlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * PlaceholderAPI hook.
 *
 * @author RadBuilder
 * @since 2.5
 */
public class PlaceholderHook extends PlaceholderExpansion {

    private UltraCosmetics ultraCosmetics;

    public PlaceholderHook(UltraCosmetics ultraCosmetics) {
        this.ultraCosmetics = ultraCosmetics;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        UltraPlayer ultraPlayer = ultraCosmetics.getPlayerManager().getUltraPlayer(player);
        if (identifier.startsWith("ammo_")) {
            String gadget = identifier.substring(5);
            GadgetType type = CosmeticType.valueOf(Category.GADGETS, gadget);
            if (type == null) return null;
            return String.valueOf(ultraPlayer.getAmmo(type));
        }
        switch (identifier) {
            // Current cosmetics
            case "current_gadget":
                return ultraPlayer.getCurrentGadget() == null ? "None" : ultraPlayer.getCurrentGadget().getTypeName();
            case "current_mount":
                return ultraPlayer.getCurrentMount() == null ? "None" : ultraPlayer.getCurrentMount().getTypeName();
            case "current_particleeffect":
                return ultraPlayer.getCurrentParticleEffect() == null ? "None" : ultraPlayer.getCurrentParticleEffect().getTypeName();
            case "current_pet":
                return ultraPlayer.getCurrentPet() == null ? "None" : ultraPlayer.getCurrentPet().getTypeName();
            case "current_morph":
                return ultraPlayer.getCurrentMorph() == null ? "None" : ultraPlayer.getCurrentMorph().getTypeName();
            case "current_hat":
                return ultraPlayer.getCurrentHat() == null ? "None" : ultraPlayer.getCurrentHat().getTypeName();
            case "current_emote":
                return ultraPlayer.getCurrentEmote() == null ? "None" : ultraPlayer.getCurrentEmote().getTypeName();
            case "current_suit_helmet":
                return ultraPlayer.getCurrentSuit(ArmorSlot.HELMET) == null ? "None" : ultraPlayer.getCurrentSuit(ArmorSlot.HELMET).getTypeName();
            case "current_suit_chestplate":
                return ultraPlayer.getCurrentSuit(ArmorSlot.CHESTPLATE) == null ? "None" : ultraPlayer.getCurrentSuit(ArmorSlot.CHESTPLATE).getTypeName();
            case "current_suit_leggings":
                return ultraPlayer.getCurrentSuit(ArmorSlot.LEGGINGS) == null ? "None" : ultraPlayer.getCurrentSuit(ArmorSlot.LEGGINGS).getTypeName();
            case "current_suit_boots":
                return ultraPlayer.getCurrentSuit(ArmorSlot.BOOTS) == null ? "None" : ultraPlayer.getCurrentSuit(ArmorSlot.BOOTS).getTypeName();

            // Keys, and user-specific settings
            case "keys":
                return "" + ultraPlayer.getKeys();
            case "gadgets_enabled":
                return "" + ultraPlayer.hasGadgetsEnabled();
            case "morph_selfview":
                return "" + ultraPlayer.canSeeSelfMorph();
            case "treasurechest_active":
                return "" + (ultraPlayer.getCurrentTreasureChest() != null);
        }
        return null;
    }

    @Override
    public String getIdentifier() {
        return "ultracosmetics";
    }

    @Override
    public String getAuthor() {
        return ultraCosmetics.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return ultraCosmetics.getDescription().getVersion();
    }
}
