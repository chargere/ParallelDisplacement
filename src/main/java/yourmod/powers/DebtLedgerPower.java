package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 债册：承受霜偿代价后下回合抽牌；amount=抽数，amount2=每回合最多触发次数 */
public class DebtLedgerPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DebtLedger");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DebtLedgerPower(AbstractCreature owner, int draw, int maxTriggers) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, draw);
        this.amount2 = maxTriggers;
        this.isTwoAmount = true;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        updateDescription();
    }

    public void stackMaxTriggers(int extra) {
        this.amount2 += extra;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + amount2 + strings.DESCRIPTIONS[2];
    }
}
