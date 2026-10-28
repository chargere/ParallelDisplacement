package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.damuhou.AbstractDamuhouCard;

import static yourmod.ModFile.makeID;

public class DamuhouKeepMovingPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouKeepMoving");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean longComboDrawsExtra;

    public DamuhouKeepMovingPower(AbstractCreature owner, boolean longComboDrawsExtra) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, longComboDrawsExtra ? 2 : 1);
        this.longComboDrawsExtra = longComboDrawsExtra;
        useVanillaIcon("draw");
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (stackAmount >= 2) {
            longComboDrawsExtra = true;
        }
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        int combo = card instanceof AbstractDamuhouCard
                ? ((AbstractDamuhouCard) card).effectivePrintedCombo() : 1;
        int draw = longComboDrawsExtra && combo >= 3 ? 2 : 1;
        flash();
        addToBot(new DrawCardAction(owner, draw));
    }

    @Override
    public void updateDescription() {
        description = longComboDrawsExtra ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}
