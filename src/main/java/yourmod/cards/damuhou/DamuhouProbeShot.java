package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;
import yourmod.util.DamuhouFireMode;

import basemod.AutoAdd;

import static yourmod.ModFile.makeID;

/** 卡池占位。正式卡组写好后删。 */
@AutoAdd.Ignore
public class DamuhouProbeShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouProbeShot");

    public DamuhouProbeShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractMonster t = DamuhouFireMode.isFullAuto() ? DamuhouFireMode.sightedLiving() : m;
        if (t != null && !t.isDeadOrEscaped()) {
            addToBot(new ApplyPowerAction(t, p, new BreachPower(t, magicNumber), magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
