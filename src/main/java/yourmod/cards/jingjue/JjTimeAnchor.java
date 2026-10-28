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
public class JjTimeAnchor extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeAnchor");
    public JjTimeAnchor() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { int heal = JingjueCombatState.damageTakenThisTurn / 2; if (heal > 0) addToBot(new HealAction(p, p, heal)); }
    public void upp() { upgradeBaseCost(0); }
}
