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
public class JjImmolateAll extends AbstractJingjueCard {
    public static final String ID = makeID("JjImmolateAll");
    public JjImmolateAll() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { int d = JingjueCombatState.hourglass() * magicNumber; addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(Math.max(1, d), true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)); addToBot(new RemoveSpecificPowerAction(p, p, HourglassPower.POWER_ID)); }
    public void upp() { upgradeMagicNumber(1); }
}
