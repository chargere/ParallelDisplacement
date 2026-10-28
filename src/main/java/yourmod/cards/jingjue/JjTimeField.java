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
public class JjTimeField extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeField");
    public JjTimeField() { super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new TimeFieldPower(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
