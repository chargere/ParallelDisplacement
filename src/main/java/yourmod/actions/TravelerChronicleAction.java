package yourmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.util.DominicCombatState;

import java.util.ArrayList;

/** 从本回合消耗 / 消耗堆选一张回手并减费 */
public class TravelerChronicleAction extends AbstractGameAction {
    private final int costReduce;

    public TravelerChronicleAction(int costReduce) {
        this.costReduce = costReduce;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> opts = new ArrayList<>();
        for (AbstractCard c : DominicCombatState.exhaustedThisTurn) {
            if (p.exhaustPile.contains(c) && !opts.contains(c)) {
                opts.add(c);
            }
        }
        if (opts.isEmpty()) {
            opts.addAll(p.exhaustPile.group);
        }
        if (opts.isEmpty()) {
            isDone = true;
            return;
        }
        addToTop(new SelectCardsCenteredAction(opts, 1, "选择一张牌返回手牌", cards -> {
            for (AbstractCard c : cards) {
                if (p.exhaustPile.contains(c)) {
                    p.exhaustPile.removeCard(c);
                    p.hand.addToHand(c);
                    c.unhover();
                    c.lighten(false);
                    c.setCostForTurn(Math.max(0, c.costForTurn - costReduce));
                    p.hand.refreshHandLayout();
                }
            }
        }));
        isDone = true;
    }
}
