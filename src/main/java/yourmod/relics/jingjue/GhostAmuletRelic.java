package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class GhostAmuletRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjGhostAmulet");
    public static boolean usedThisCombat;

    public GhostAmuletRelic() {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        usedThisCombat = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GhostAmuletRelic();
    }
}
