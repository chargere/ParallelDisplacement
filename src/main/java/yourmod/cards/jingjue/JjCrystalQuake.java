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
public class JjCrystalQuake extends AbstractJingjueCard {
    public static final String ID = makeID("JjCrystalQuake");
    public JjCrystalQuake() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseDamage = 22; isMultiDamage = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHitAll(AbstractGameAction.AttackEffect.LIGHTNING); Wiz.forAllMonstersLiving(mo -> { giveImaginary(mo, 1); giveWeak(mo, 1); }); }
    public void upp() { upgradeDamage(6); }
}
