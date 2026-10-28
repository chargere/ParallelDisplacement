package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class CamelSkinRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjCamelSkin");
    public static final int HEAL = 3;

    public CamelSkinRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CamelSkinRelic();
    }
}
