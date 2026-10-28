package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.AutoAdd;

import static yourmod.ModFile.makeID;

/** 卡池占位。正式卡组写好后删。 */
@AutoAdd.Ignore
public class DamuhouCoverFire extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCoverFire");

    public DamuhouCoverFire() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 6;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.FIRE);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
