package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.dominic.AbstractDominicCard;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

/** 同路余温：本回合每打出一张带霜偿的牌，获得格挡 */
public class SharedWarmthPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SharedWarmth");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SharedWarmthPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, amount);
    }

    @Override
    public void onInitialApplication() {
        DominicCombatState.sharedWarmthBlockPerCard = amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractDominicCard) {
            AbstractDominicCard dc = (AbstractDominicCard) card;
            if (dc.frostPayCost > 0) {
                flash();
                addToBot(new GainBlockAction(owner, owner, amount));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            DominicCombatState.sharedWarmthBlockPerCard = 0;
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
