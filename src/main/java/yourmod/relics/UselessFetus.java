package yourmod.relics;

import yourmod.FvvEggCharacter;

import static yourmod.ModFile.makeID;

/**
 * 胎没用了呀：fvv蛋蛋初始遗物（纯玩笑，无效果）。
 */
public class UselessFetus extends AbstractEasyRelic {
    public static final String ID = makeID("UselessFetus");

    public UselessFetus() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, FvvEggCharacter.Enums.FVV_COLOR);
    }
}
