package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/** 绁搧鐑欏嵃 */
public class JjBrand extends AbstractJingjueCard {
    public static final String ID = makeID("JjBrand");
    public JjBrand() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 3; baseSecondMagic = secondMagic = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveEye(m, magicNumber); addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, secondMagic, false), secondMagic)); }
    public void upp() { upgradeMagicNumber(1); upgradeSecondMagic(1); }
}
