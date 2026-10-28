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
public class JjIceSeal extends AbstractJingjueCard {
    public static final String ID = makeID("JjIceSeal");
    public JjIceSeal() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; baseSecondMagic = secondMagic = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)); addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, secondMagic, false), secondMagic)); }
    public void upp() { upgradeSecondMagic(1); }
}
