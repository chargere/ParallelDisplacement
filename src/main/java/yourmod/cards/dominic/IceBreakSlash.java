package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

public class IceBreakSlash extends AbstractDominicCard {
    public static final String ID = makeID("IceBreakSlash");

    public IceBreakSlash() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseSecondDamage = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (HongwenHooks.tookDamageThisTurn) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
