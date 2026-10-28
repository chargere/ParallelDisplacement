package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarTrackStomp extends AbstractDominicCard {
    public static final String ID = makeID("StarTrackStomp");

    public StarTrackStomp() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (m != null) {
            if (m.hasPower(StarMarkPower.POWER_ID)) {
                dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            } else {
                atb(new ApplyPowerAction(m, p, new StarMarkPower(m, 1), 1));
            }
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
