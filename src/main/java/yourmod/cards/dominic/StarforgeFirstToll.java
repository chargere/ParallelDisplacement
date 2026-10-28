package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarforgeFirstToll extends AbstractDominicCard {
    public static final String ID = makeID("StarforgeFirstToll");

    public StarforgeFirstToll() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 28;
        baseSecondDamage = 12;
        setFrostPay(5, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        if (DominicCombatState.lastFrostPayCancelled) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            atb(new ApplyPowerAction(m, p, new StarMarkPower(m, 2), 2));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeSecondDamage(4);
    }
}
