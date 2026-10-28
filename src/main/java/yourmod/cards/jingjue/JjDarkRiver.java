package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 兹独暗河 */
public class JjDarkRiver extends AbstractJingjueCard {
    public static final String ID = makeID("JjDarkRiver");
    public JjDarkRiver() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.isEmpty()) return;
        addToBot(new com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction(p.hand.group, 1, "选择一张牌虚化 2 回合。", false, c -> true, cards -> {
            for (com.megacrit.cardcrawl.cards.AbstractCard c : cards) addToBot(new EtherealizeAction(c, 2, upgraded));
        }));
    }
    public void upp() { }
}
