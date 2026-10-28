package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FinalSheatheMark extends AbstractDominicCard {
    public static final String ID = makeID("FinalSheatheMark");

    public FinalSheatheMark() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 26;
        baseSecondDamage = 14;
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
        setFrostPay(5, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        boolean first = !DominicCombatState.firstAttackPlayedThisTurn;
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        if (first) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            atb(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber));
        }
        DominicCombatState.firstAttackPlayedThisTurn = true;
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeSecondDamage(4);
    }
}
