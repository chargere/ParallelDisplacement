package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;

/** 舱门上的霜：开战 +2 霜寒；承受霜偿时回血（见 DominicHooks） */
public class HatchFrost extends AbstractEasyRelic {
    public static final String ID = makeID("HatchFrost");
    private static final int START_FROST = 2;

    public HatchFrost() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        FrostColdPower.gainFrostImmediate(AbstractDungeon.player, START_FROST);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HatchFrost();
    }
}
