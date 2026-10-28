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
public class JjNineTower extends AbstractJingjueCard {
    public static final String ID = makeID("JjNineTower");
    public JjNineTower() { super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseBlock = 11; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); JingjueCombatState.gainHourglass(magicNumber); }
    public void upp() { upgradeBlock(3); upgradeMagicNumber(1); }
}
