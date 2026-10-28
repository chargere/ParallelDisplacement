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

/** 鐜夌煶鐪?*/
public class JjJadeEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjJadeEye");
    public JjJadeEye() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.gainHourglass(1); addToBot(new DrawCardAction(p, 1)); if (upgraded) JingjueCombatState.executeLineBonusThisTurn += 2; }
    public void upp() { }
}
