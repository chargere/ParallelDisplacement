package yourmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;

import static yourmod.ModFile.makeID;

/** 观测停笔：承受霜偿时可延迟至下回合（见 FrostPayGateAction） */
public class ObservationHalt extends AbstractEasyRelic {
    public static final String ID = makeID("ObservationHalt");

    public ObservationHalt() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ObservationHalt();
    }
}
