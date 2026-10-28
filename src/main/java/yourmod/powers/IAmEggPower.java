package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 鎴戞槸铔嬭泲 */
public class IAmEggPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("IAmEgg");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered = false;

    public IAmEggPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (triggered || card == null) {
            return;
        }
        triggered = true;
        int paid = card.costForTurn;
        if (card.freeToPlayOnce) {
            paid = 0;
        }
        if (paid >= 2) {
            flash();
            addToBot(new GainEnergyAction(1));
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}