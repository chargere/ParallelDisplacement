package yourmod.cards.jingjue;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.jingjue.EtherealizeAction;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

/** 行境·虚 */
public class JjRealmFade extends AbstractJingjueCard {
    public static final String ID = makeID("JjRealmFade");

    public JjRealmFade() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        JingjueCombatState.gainHourglass(1);
        if (!p.hand.isEmpty()) {
            addToBot(new SelectCardsAction(p.hand.group, 1, "选择一张牌虚化 1 回合。", false, c -> true, cards -> {
                for (AbstractCard c : cards) {
                    addToTop(new EtherealizeAction(c, 1, upgraded));
                }
            }));
        }
        addToBot(new DrawCardAction(p, 1));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
