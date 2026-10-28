package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** Famous effect */
public class FamousEffectPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FamousEffect");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public FamousEffectPower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || timesThisTurn >= amount) {
            return;
        }
        String n = card.name == null ? "" : card.name;
        if (n.contains("\u5e9f") || n.contains("\u6ca1\u7528") || n.contains("\u6bdb")) {
            timesThisTurn++;
            flash();
            addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
