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

/** 閫嗚路鐤?*/
public class JjReverseDash extends AbstractJingjueCard {
    public static final String ID = makeID("JjReverseDash");
    public JjReverseDash() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 8; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); giveWeak(m, 1); if (p.hasPower(TimeDilationPower.POWER_ID)) addToBot(new GainEnergyAction(1)); }
    public void upp() { upgradeDamage(3); }
}
