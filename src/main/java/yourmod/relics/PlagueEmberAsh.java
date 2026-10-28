package yourmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;

import static yourmod.ModFile.makeID;

/**
 * 荒疫残烬（稀有）：本场首次施加烬火或静电时，额外再施加等量层数。
 * 逻辑在 HongwenHooks.tryPlagueDouble*。
 */
public class PlagueEmberAsh extends AbstractEasyRelic {
    public static final String ID = makeID("PlagueEmberAsh");

    public PlagueEmberAsh() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, CharacterFile.Enums.TODO_COLOR);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PlagueEmberAsh();
    }
}
