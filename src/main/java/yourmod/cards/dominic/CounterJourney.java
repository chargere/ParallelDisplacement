package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

public class CounterJourney extends AbstractDominicCard {
    public static final String ID = makeID("CounterJourney");

    public CounterJourney() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 23;
        baseSecondDamage = 30;
        setFrostPay(3, FrostPayType.FREEZE_CRACK, 3);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        if (DominicCombatState.lostDexBeforeFrostPay) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        } else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        if (DominicCombatState.lostDexThisTurn) {
            baseDamage = baseSecondDamage;
        }
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != (DominicCombatState.lostDexThisTurn ? baseSecondDamage : baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        if (DominicCombatState.lostDexThisTurn) {
            baseDamage = baseSecondDamage;
        }
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != (DominicCombatState.lostDexThisTurn ? baseSecondDamage : baseDamage);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeSecondDamage(4);
    }
}
