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

/** 鍑€瑙侀樋鍚?*/
public class JjJingjian extends AbstractJingjueCard {
    public static final String ID = makeID("JjJingjian");
    public JjJingjian() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 7; baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.POISON); giveImaginary(m, magicNumber); giveVuln(m, 1); }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
