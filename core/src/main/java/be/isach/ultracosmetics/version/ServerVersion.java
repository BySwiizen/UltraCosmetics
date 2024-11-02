package be.isach.ultracosmetics.version;

import me.gamercoder215.mobchip.abstraction.ChipUtil;
import org.bukkit.Bukkit;

/**
 * Created by Sacha on 6/03/16.
 */
public enum ServerVersion {

    // Do not supply a mapping version when there is no NMS module.
    // 1.17 is the first version to support Java 17
    v1_17(17, 1),
    v1_18(18, 2),
    v1_19(19, 4),
    v1_20(20, 6, "ee13f98a43b9c5abffdcc0bb24154460", 4),
    v1_21(21, 3, "61a218cda78417b6039da56e08194083", 2),
    NEW("???"),
    ;

    private final String name;
    private final int majorVer;
    private final int minorVer;
    // mappingsVersion is a random string that is changed whenever NMS changes
    // which is more often than actual NMS revisions happen. You can find this
    // value by checking the source code of this method:
    // org.bukkit.craftbukkit.util.CraftMagicNumbers#getMappingsVersion
    // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/browse/src/main/java/org/bukkit/craftbukkit/util/CraftMagicNumbers.java#229
    // getMappingsVersion was added in 1.13.2, earlier versions don't have it.
    private final String mappingsVersion;
    // The NMS revision the corresponding module is built for, or 0 for no module.
    private final int nmsRevision;

    // Dummy constructor for placeholder versions
    private ServerVersion(String name) {
        this.name = name;
        this.majorVer = 0;
        this.minorVer = 0;
        this.mappingsVersion = null;
        this.nmsRevision = 0;
    }

    private ServerVersion(int majorVer, int minorVer) {
        this(majorVer, minorVer, null, 0);
    }

    private ServerVersion(int majorVer, int minorVer, String mappingsVersion, int nmsRevision) {
        this.name = "1." + majorVer + (minorVer > 0 ? "." + minorVer : "");
        this.majorVer = majorVer;
        this.minorVer = minorVer;
        this.mappingsVersion = mappingsVersion;
        this.nmsRevision = nmsRevision;
    }

    public String getName() {
        return name;
    }

    public int getMajorVer() {
        return majorVer;
    }

    public int getMinorVer() {
        return minorVer;
    }

    public String getMappingsVersion() {
        return mappingsVersion;
    }

    public int getNMSRevision() {
        return nmsRevision;
    }

    public static ServerVersion earliest() {
        return values()[0];
    }

    public static ServerVersion byId(int id) {
        if (id == 0) return null;
        for (ServerVersion version : values()) {
            if (id == version.majorVer) {
                return version;
            }
        }
        return null;
    }

    public boolean isAtLeast(ServerVersion version) {
        return this.compareTo(version) >= 0;
    }

    public boolean isMobChipAvailable() {
        if (isMobchipEdgeCase()) {
            return false;
        }
        try {
            ChipUtil.getWrapper();
        } catch (IllegalStateException e) {
            return false;
        }
        return true;
    }

    public boolean isNmsSupported() {
        return nmsRevision > 0;
    }

    public String getNmsVersion() {
        //noinspection UnnecessaryToStringCall
        return toString() + "_R" + nmsRevision;
    }

    public static boolean isMobchipEdgeCase() {
        return getMinecraftVersion().equals("1.19");
    }

    public static String getMinecraftVersion() {
        String rawVersion = Bukkit.getVersion();
        return rawVersion.substring(rawVersion.lastIndexOf(" ") + 1, rawVersion.length() - 1);
    }

    /**
     * Returns the latest supported version. NEW does not count
     */
    public static ServerVersion latest() {
        return values()[values().length - 2];
    }
}
