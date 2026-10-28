package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;

import static yourmod.ModFile.makeID;

/** 战场回收 */
public class DamuhouFieldSalvage extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFieldSalvage");

    public DamuhouFieldSalvage() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsAction(p.discardPile.group, 1,
                upgraded ? "从弃牌堆选择 1 张牌加入手牌。" : "从弃牌堆选择 1 张费用不少于 2 的牌加入手牌。",
                false, c -> upgraded || c.cost >= 2, cards -> {
            for (AbstractCard c : cards) {
                p.discardPile.removeCard(c);
                p.hand.addToHand(c);
                if (upgraded || c.type == AbstractCard.CardType.ATTACK) {
                    c.setCostForTurn(Math.max(0, c.costForTurn - 1));
                }
            }
            p.hand.refreshHandLayout();
        }));
    }

    @Override
    public void upp() {
    }
}
