package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class DapuAmberRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjDapuAmber");
    public static final int BONUS = 20;

    public DapuAmberRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DapuAmberRelic();
    }
}
