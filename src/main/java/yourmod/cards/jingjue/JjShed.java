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

/** 铔囪湑 */
public class JjShed extends AbstractJingjueCard {
    public static final String ID = makeID("JjShed");
    public JjShed() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseBlock = 6; exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); addToBot(new DrawCardAction(p, 1)); }
    public void upp() { upgradeBlock(2); }
}
