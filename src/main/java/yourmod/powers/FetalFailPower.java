package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 鑳庢暀澶辫触 */
public class FetalFailPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FetalFail");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean gainedThisTurn = false;
    private boolean lostHpThisTurn = false;

    public FetalFailPower(AbstractCreature owner, int extra) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, extra);
    }

    public void onUselessGained() {
        if (gainedThisTurn) {
            return;
        }
        gainedThisTurn = true;
        flash();
        UselessPower.gainSilent(owner, amount);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && !lostHpThisTurn) {
            lostHpThisTurn = true;
            flash();
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void atStartOfTurn() {
        gainedThisTurn = false;
        lostHpThisTurn = false;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}