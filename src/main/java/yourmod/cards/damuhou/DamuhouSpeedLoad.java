package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;

import static yourmod.ModFile.makeID;

/** 快速装弹 */
public class DamuhouSpeedLoad extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouSpeedLoad");

    public DamuhouSpeedLoad() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsAction(p.discardPile.group, 1, "从弃牌堆选择 1 张攻击牌加入手牌。",
                false, c -> c.type == AbstractCard.CardType.ATTACK, cards -> {
            for (AbstractCard c : cards) {
                p.discardPile.removeCard(c);
                p.hand.addToHand(c);
                int combo = c instanceof AbstractDamuhouCard ? ((AbstractDamuhouCard) c).effectivePrintedCombo() : 1;
                if (combo >= 3) {
                    c.setCostForTurn(Math.max(0, c.costForTurn - magicNumber));
                }
            }
            p.hand.refreshHandLayout();
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
