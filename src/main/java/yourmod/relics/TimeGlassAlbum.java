package yourmod.relics;

import yourmod.ComingSoonTimeCharacter;

import static yourmod.ModFile.makeID;

/**
 * 时璃相簿：时间生命占位初始遗物（效果敬请期待）。
 */
public class TimeGlassAlbum extends AbstractEasyRelic {
    public static final String ID = makeID("TimeGlassAlbum");

    public TimeGlassAlbum() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, ComingSoonTimeCharacter.Enums.TIME_COLOR);
    }
}
