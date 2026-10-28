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
public class JjPhoenixStrike extends AbstractJingjueCard {
    public static final String ID = makeID("JjPhoenixStrike");
    public JjPhoenixStrike() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 16; splashPct = 60; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); addToBot(new AbstractGameAction() { public void update() { if (m != null && m.isDeadOrEscaped()) JingjueCombatState.executeLineBonusPct += 3; isDone = true; } }); }
    public void upp() { upgradeDamage(4); splashPct = 80; }
}
