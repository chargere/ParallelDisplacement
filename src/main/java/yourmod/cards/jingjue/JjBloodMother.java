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
public class JjBloodMother extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodMother");
    public JjBloodMother() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseDamage = 14; isMultiDamage = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 5); jjHitAll(AbstractGameAction.AttackEffect.POISON); Wiz.forAllMonstersLiving(mo -> giveEye(mo, 2)); }
    public void upp() { upgradeDamage(3); }
}
