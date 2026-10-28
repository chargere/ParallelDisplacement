package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class SnakeTokenRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjSnakeToken");

    public SnakeTokenRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SnakeTokenRelic();
    }
}
