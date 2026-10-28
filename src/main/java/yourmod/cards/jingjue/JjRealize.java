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
public class JjRealize extends AbstractJingjueCard {
    public static final String ID = makeID("JjRealize");
    public JjRealize() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 6; }
    public void use(AbstractPlayer p, AbstractMonster m) { Wiz.forAllMonstersLiving(mo -> { if (mo.hasPower(ImaginaryPower.POWER_ID)) { int n = mo.getPower(ImaginaryPower.POWER_ID).amount; addToBot(new DamageAction(mo, new DamageInfo(p, n * magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE)); addToBot(new RemoveSpecificPowerAction(mo, p, ImaginaryPower.POWER_ID)); } }); }
    public void upp() { upgradeMagicNumber(1); }
}
