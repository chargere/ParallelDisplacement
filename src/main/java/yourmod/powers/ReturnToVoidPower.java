package yourmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 归墟：本回合接下来 X 张打出的牌消耗。
 */
public class ReturnToVoidPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ReturnToVoid");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ReturnToVoidPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount <= 0) {
            return;
        }
        flash();
        com.megacrit.cardcrawl.powers.AbstractPower lastLine =
                owner.getPower(LastLineOfChartPower.POWER_ID);
        if (lastLine instanceof LastLineOfChartPower
                && yourmod.util.DominicCombatState.lastLineChartRemaining > 0) {
            ((LastLineOfChartPower) lastLine).consumeUse();
        } else {
            action.exhaustCard = true;
        }
        if (owner instanceof com.megacrit.cardcrawl.characters.AbstractPlayer) {
            com.megacrit.cardcrawl.powers.AbstractPower burn =
                    owner.getPower(MemoryBurnoutPower.POWER_ID);
            if (burn instanceof MemoryBurnoutPower) {
                ((MemoryBurnoutPower) burn).addAshCard(card);
            }
        }
        amount--;
        if (amount <= 0) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
