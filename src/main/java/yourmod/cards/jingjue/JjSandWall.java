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

/** 骞绘矙澹?*/
public class JjSandWall extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandWall");
    public JjSandWall() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseBlock = 8; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); JingjueCombatState.executeLineBonusThisTurn += magicNumber; Wiz.forAllMonstersLiving(mo -> giveWeak(mo, 1)); }
    public void upp() { upgradeBlock(3); }
}
