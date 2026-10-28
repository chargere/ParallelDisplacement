package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class HourglassShardRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjHourglassShard");

    public HourglassShardRelic() {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
        counter = 0;
    }

    @Override
    public void onPlayerEndTurn() {
        counter++;
        if (counter >= 3) {
            counter = 0;
            flash();
            JingjueCombatState.gainHourglass(1);
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HourglassShardRelic();
    }
}
