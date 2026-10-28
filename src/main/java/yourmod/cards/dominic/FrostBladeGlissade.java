package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;

public class FrostBladeGlissade extends AbstractDominicCard {
    public static final String ID = makeID("FrostBladeGlissade");

    public FrostBladeGlissade() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseSecondDamage = 9;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (DominicCombatState.frostPayAcceptedThisTurn > 0) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            FrostColdPower.gainFrost(p, 2);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
