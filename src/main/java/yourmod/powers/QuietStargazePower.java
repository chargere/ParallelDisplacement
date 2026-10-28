package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicHooks;

import static yourmod.ModFile.makeID;

/** 静观星象：霜寒减半后若霜寒≤3则抽牌 */
public class QuietStargazePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("QuietStargaze");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public QuietStargazePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        this.priority = 5;
    }

    @Override
    public void atStartOfTurn() {
        if (owner instanceof AbstractPlayer) {
            DominicHooks.processDebtLedgerDraw((AbstractPlayer) owner);
        }
        if (FrostColdPower.getFrost(owner) <= 3) {
            flash();
            addToBot(new DrawCardAction(owner, amount));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
