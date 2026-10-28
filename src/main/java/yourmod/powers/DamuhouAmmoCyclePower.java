package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.damuhou.AbstractDamuhouCard;

import static yourmod.ModFile.makeID;

public class DamuhouAmmoCyclePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouAmmoCycle");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouAmmoCyclePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("energized");
    }

    @Override
    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        this.amount = Math.max(this.amount, stackAmount);
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        int combo = card instanceof AbstractDamuhouCard ? ((AbstractDamuhouCard) card).effectivePrintedCombo() : 1;
        if (combo <= amount) {
            flash();
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
