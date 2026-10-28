package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static yourmod.ModFile.makeID;

public class WorseFrost extends AbstractDominicCard {
    public static final String ID = makeID("WorseFrost");

    public WorseFrost() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseSecondDamage = 8;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m != null && m.hasPower(WeakPower.POWER_ID)) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
