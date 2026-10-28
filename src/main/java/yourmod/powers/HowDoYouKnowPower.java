package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 你怎么知道赢不了：首次降至半血以下触发一次 */
public class HowDoYouKnowPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("HowDoYouKnow");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final int blockAmount;
    private boolean wasHPAboveHalf = true;

    public HowDoYouKnowPower(AbstractCreature owner, int frostAmount, int blockAmount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, frostAmount);
        this.blockAmount = blockAmount;
        this.amount2 = blockAmount;
        this.isTwoAmount = true;
        if (owner.currentHealth <= owner.maxHealth / 2) {
            wasHPAboveHalf = false;
        }
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount <= 0 || DominicCombatState.howDoYouKnowTriggeredThisCombat || !wasHPAboveHalf) {
            return;
        }
        if (owner.currentHealth <= owner.maxHealth / 2) {
            DominicCombatState.howDoYouKnowTriggeredThisCombat = true;
            wasHPAboveHalf = false;
            flash();
            FrostColdPower.gainFrost(owner, amount);
            addToBot(new DrawCardAction(owner, 2));
            addToBot(new GainBlockAction(owner, owner, blockAmount));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
                + blockAmount + strings.DESCRIPTIONS[2];
    }
}
