package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.getEnemies;

public class ShatteredStarRain extends AbstractDominicCard {
    public static final String ID = makeID("ShatteredStarRain");

    public ShatteredStarRain() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 5;
        isMultiDamage = true;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        int marked = 0;
        for (AbstractMonster mo : getEnemies()) {
            if (mo.hasPower(StarMarkPower.POWER_ID)) {
                marked++;
            }
        }
        if (marked > 0) {
            atb(new DrawCardAction(p, marked));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
