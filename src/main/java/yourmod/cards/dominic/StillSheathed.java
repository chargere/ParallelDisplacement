package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

public class StillSheathed extends AbstractDominicCard {
    public static final String ID = makeID("StillSheathed");

    public StillSheathed() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseSecondDamage = 9;
        setFrostPay(2, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        boolean first = !DominicCombatState.firstAttackPlayedThisTurn;
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (first) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        DominicCombatState.firstAttackPlayedThisTurn = true;
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
