package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

public class DebtCleaver extends AbstractDominicCard {
    public static final String ID = makeID("DebtCleaver");

    public DebtCleaver() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 10;
        baseSecondDamage = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        int extras = Math.min(3, DominicCombatState.frostPayAcceptedThisTurn);
        for (int i = 0; i < extras; i++) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
