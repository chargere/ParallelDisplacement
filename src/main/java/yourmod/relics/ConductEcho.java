package yourmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;

import static yourmod.ModFile.makeID;

/**
 * 雷纹回响（罕见）：每次传导事件回复 1 生命。
 * 逻辑在 HongwenHooks.onConduct。
 */
public class ConductEcho extends AbstractEasyRelic {
    public static final String ID = makeID("ConductEcho");

    public ConductEcho() {
        super(ID, RelicTier.UNCOMMON, LandingSound.CLINK, CharacterFile.Enums.TODO_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ConductEcho();
    }
}
