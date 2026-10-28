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

/** 涓氱伀婧?*/
public class JjFireSplash extends AbstractJingjueCard {
    public static final String ID = makeID("JjFireSplash");
    public JjFireSplash() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 8; splashPct = 30; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); giveWeak(m, 1); }
    public void upp() { upgradeDamage(3); splashPct = 40; }
}
