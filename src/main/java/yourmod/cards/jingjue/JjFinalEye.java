package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
public class JjFinalEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjFinalEye");
    public JjFinalEye() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 30; baseMagicNumber = magicNumber = 5; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (m != null && m.type == AbstractMonster.EnemyType.NORMAL) addToBot(new ExecuteAction(m)); else { jjHit(m, AbstractGameAction.AttackEffect.FIRE); giveEye(m, magicNumber); } }
    public void upp() { upgradeDamage(10); upgradeMagicNumber(1); }
}
