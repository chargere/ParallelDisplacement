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
public class JjDevour extends AbstractJingjueCard {
    public static final String ID = makeID("JjDevour");
    public JjDevour() { super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY); }
    public void use(AbstractPlayer p, AbstractMonster m) { if (m != null && m.hasPower(ImaginaryPower.POWER_ID)) { int n = m.getPower(ImaginaryPower.POWER_ID).amount; addToBot(new RemoveSpecificPowerAction(m, p, ImaginaryPower.POWER_ID)); giveEye(m, n); addToBot(new ExecuteAction(m)); } }
    public void upp() { upgradeBaseCost(1); }
}
