package yourmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 鎴戝氨杩欐牱 */
public class JustLikeThisPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JustLikeThis");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public boolean firstCard = true;

    public JustLikeThisPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        UselessPower.gain(owner, owner, amount);
        firstCard = true;
        discountHand();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (firstCard && card != null) {
            card.setCostForTurn(Math.max(0, card.costForTurn - 1));
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (firstCard) {
            firstCard = false;
            if (AbstractDungeon.player != null) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.cost >= 0) {
                        c.setCostForTurn(c.cost);
                    }
                }
            }
        }
    }

    private void discountHand() {
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