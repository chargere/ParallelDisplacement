package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 骞宠　璋冩暣锛氳嚜鎹熸敼鍥炲 */
public class BalanceHealPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("BalanceHeal");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BalanceHealPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info != null && info.owner == owner) {
            flash();
            addToTop(new HealAction(owner, owner, damageAmount));
            return 0;
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}