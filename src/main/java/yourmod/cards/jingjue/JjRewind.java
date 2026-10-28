package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
public class JjRewind extends AbstractJingjueCard {
    public static final String ID = makeID("JjRewind");
    public JjRewind() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!JingjueCombatState.consumeHourglass(magicNumber)) return;
        addToBot(new DrawCardAction(p, 2));
        if (!p.hand.isEmpty()) addToBot(new SelectCardsAction(p.hand.group, 1, "将一张牌放到抽牌堆顶。", false, c -> true, cards -> {
            for (AbstractCard c : cards) { p.hand.removeCard(c); p.drawPile.addToTop(c); }
        }));
    }
    public void upp() { upgradeMagicNumber(-1); }
}
