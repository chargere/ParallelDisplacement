package yourmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;

import static yourmod.ModFile.makeID;

/** 合上的星图：首场成功取消霜偿时抽2（见 DominicHooks） */
public class ClosedStarChart extends AbstractEasyRelic {
    public static final String ID = makeID("ClosedStarChart");

    public ClosedStarChart() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ClosedStarChart();
    }
}
