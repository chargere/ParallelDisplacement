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

/** 楝兼礊鍥炲搷 */
public class JjGhostEcho extends AbstractJingjueCard {
    public static final String ID = makeID("JjGhostEcho");
    public JjGhostEcho() { super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 16; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY); giveWeak(m, 1); }
    public void upp() { upgradeDamage(4); }
}
