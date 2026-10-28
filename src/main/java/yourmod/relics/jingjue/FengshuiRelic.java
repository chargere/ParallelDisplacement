package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class FengshuiRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjFengshui");

    public FengshuiRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atTurnStartPostDraw() {
        flash();
        addToBot(new ScryAction(3));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FengshuiRelic();
    }
}
