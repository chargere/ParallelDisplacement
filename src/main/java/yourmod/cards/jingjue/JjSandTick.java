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

/** 鐮備箣鍒?*/
public class JjSandTick extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandTick");
    public JjSandTick() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.gainDilation(magicNumber); addToBot(new DrawCardAction(p, 1)); }
    public void upp() { upgradeMagicNumber(1); }
}
