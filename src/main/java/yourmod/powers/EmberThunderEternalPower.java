package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 烬雷·永恒：每回合第一张攻击牌加入 0 费虚无消耗复制 */
public class EmberThunderEternalPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EmberThunderEternal");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean usedThisTurn = false;

    public EmberThunderEternalPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atStartOfTurn() {
        usedThisTurn = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!usedThisTurn && card.type == AbstractCard.CardType.ATTACK) {
            usedThisTurn = true;
            flash();
            AbstractCard copy = card.makeStatEquivalentCopy();
            copy.cost = 0;
            copy.costForTurn = 0;
            copy.isCostModified = true;
            copy.isEthereal = true;
            copy.exhaust = true;
            addToBot(new MakeTempCardInHandAction(copy, 1));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
