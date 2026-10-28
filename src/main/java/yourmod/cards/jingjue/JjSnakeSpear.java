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
public class JjSnakeSpear extends AbstractJingjueCard {
    public static final String ID = makeID("JjSnakeSpear");
    public JjSnakeSpear() { super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 10; splashPct = 50; exhaust = true; }
    public void applyPowers() { int s = baseDamage; baseDamage += 2 * JingjueCombatState.hourglass(); super.applyPowers(); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; baseDamage += 2 * JingjueCombatState.hourglass(); super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    public void upp() { upgradeDamage(4); splashPct = 70; }
}
