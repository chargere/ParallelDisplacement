package yourmod.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DominicCharacter;
import yourmod.actions.ThreeDayHatchRetainAction;

import static yourmod.ModFile.makeID;

/** 三天的舱门：前3回合结束各可选1张手牌保留 */
public class ThreeDayHatch extends AbstractEasyRelic {
    public static final String ID = makeID("ThreeDayHatch");
    private int turnsRemaining = 0;

    public ThreeDayHatch() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    @Override
    public void atBattleStart() {
        turnsRemaining = 3;
    }

    @Override
    public void onPlayerEndTurn() {
        if (turnsRemaining <= 0 || AbstractDungeon.player.hand.isEmpty()) {
            return;
        }
        turnsRemaining--;
        flash();
        AbstractDungeon.actionManager.addToBottom(new ThreeDayHatchRetainAction());
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ThreeDayHatch();
    }
}
