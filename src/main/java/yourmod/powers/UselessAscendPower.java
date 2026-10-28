package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 姣涚敤椋炲崌锛氳垂鐢?1锛屽洖鍚堢粨鏉熸竻绌烘病鐢ㄥ苟鎶? */
public class UselessAscendPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("UselessAscend");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public UselessAscendPower(AbstractCreature owner, int turns) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, turns);
    }

    @Override
    public void onInitialApplication() {
        discountAll();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card != null && card.costForTurn >= 0) {
            card.setCostForTurn(Math.max(0, card.costForTurn - 1));
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        discountAll();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        flash();
        UselessPower.spendAll(owner);
        addToBot(new DrawCardAction(2));
        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    private void discountAll() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn >= 0) {
                c.setCostForTurn(Math.max(0, c.costForTurn - 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}