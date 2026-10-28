package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/** 未竟的工图：复制一张非状态/诅咒手牌，复制品本回合费用降低。 */
public class BlueprintCopyAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final int costReduce;
    private boolean opened;

    public BlueprintCopyAction(AbstractPlayer p, int costReduce) {
        this.p = p;
        this.costReduce = costReduce;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            java.util.ArrayList<AbstractCard> choices = new java.util.ArrayList<>();
            for (AbstractCard c : p.hand.group) {
                if (c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE) {
                    choices.add(c);
                }
            }
            if (choices.isEmpty()) {
                isDone = true;
                return;
            }
            if (choices.size() == 1) {
                makeCopy(choices.get(0));
                isDone = true;
                return;
            }
            opened = true;
            AbstractDungeon.handCardSelectScreen.open("选择一张牌复制", 1, false, false, false, false);
            // Filter isn't built-in; player could pick status if in hand - we excluded in resolve
            tickDuration();
            return;
        }
        if (opened && !AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                p.hand.addToTop(c);
                if (c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE) {
                    makeCopy(c);
                }
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            isDone = true;
        }
        tickDuration();
    }

    private void makeCopy(AbstractCard src) {
        AbstractCard copy = src.makeStatEquivalentCopy();
        int newCost = src.costForTurn - costReduce;
        if (copy.cost >= 0) {
            copy.setCostForTurn(Math.max(0, newCost));
        }
        copy.freeToPlayOnce = false;
        if (p.hand.size() < 10) {
            p.hand.addToTop(copy);
        } else {
            p.createHandIsFullDialog();
            p.discardPile.addToTop(copy);
        }
        copy.lighten(false);
        p.hand.refreshHandLayout();
    }
}
