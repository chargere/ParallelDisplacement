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

/** 琛€楗佃姳 */
public class JjBloodBait extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodBait");
    public JjBloodBait() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 3); addToBot(new DrawCardAction(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
