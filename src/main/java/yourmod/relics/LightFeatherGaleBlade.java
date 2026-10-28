package yourmod.relics;

import yourmod.ComingSoonWindCharacter;

import static yourmod.ModFile.makeID;

/**
 * 光羽岚刃：风光占位初始遗物（效果敬请期待）。
 */
public class LightFeatherGaleBlade extends AbstractEasyRelic {
    public static final String ID = makeID("LightFeatherGaleBlade");

    public LightFeatherGaleBlade() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, ComingSoonWindCharacter.Enums.WIND_COLOR);
    }
}
