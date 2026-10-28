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
public class JjTimeLoan extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeLoan");
    public JjTimeLoan() { super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); if (!upgraded) JingjueCombatState.nextTurnDrawPenalty += 1; }
    public void upp() { }
}
