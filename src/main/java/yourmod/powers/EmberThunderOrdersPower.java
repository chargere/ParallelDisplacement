package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;

/** 焚雷军令：回合开始预见 3；弃攻击得临时力量，弃技能得格挡 */
public class EmberThunderOrdersPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EmberThunderOrders");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EmberThunderOrdersPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 3);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ScryAction(amount));
    }

    /** 由 DiscardHookPatch 调用 */
    public void onDiscardedCard(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
            addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, 1), 1));
        } else if (card.type == AbstractCard.CardType.SKILL) {
            flash();
            addToBot(new GainBlockAction(owner, owner, 3));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
