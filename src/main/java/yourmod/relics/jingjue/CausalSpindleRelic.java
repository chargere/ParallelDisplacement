package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class CausalSpindleRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjCausalSpindle");

    public CausalSpindleRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CausalSpindleRelic();
    }
}
