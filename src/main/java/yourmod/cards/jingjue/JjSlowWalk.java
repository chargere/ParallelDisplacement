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

/** 琛屽路缂?*/
public class JjSlowWalk extends AbstractJingjueCard {
    public static final String ID = makeID("JjSlowWalk");
    public JjSlowWalk() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveImaginary(m, magicNumber); giveWeak(m, 1); }
    public void upp() { upgradeMagicNumber(1); }
}
