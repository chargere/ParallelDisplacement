package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.relics.UselessFetus;

import static yourmod.ModFile.makeID;

/** 鑳庨噷甯︽潵 */
public class BornWithItPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("BornWithIt");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean convertedThisTurn = false;
    private boolean battleStartDone = false;

    public BornWithItPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    public boolean tryConvertToDraw() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(UselessFetus.ID)) {
            return false;
        }
        if (convertedThisTurn) {
            return false;
        }
        convertedThisTurn = true;
        flash();
        addToTop(new DrawCardAction(1));
        return true;
    }

    @Override
    public void atStartOfTurn() {
        convertedThisTurn = false;
    }

    @Override
    public void onInitialApplication() {
        tryBattleStartBonus();
    }

    private void tryBattleStartBonus() {
        if (battleStartDone || AbstractDungeon.player == null) {
            return;
        }
        battleStartDone = true;
        if (!AbstractDungeon.player.hasRelic(UselessFetus.ID)) {
            flash();
            UselessPower.gain(owner, owner, 5);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}