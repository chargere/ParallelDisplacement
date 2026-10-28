package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.AutoAdd;

import static yourmod.ModFile.makeID;

/** 卡池占位。正式卡组写好后删。 */
@AutoAdd.Ignore
public class DamuhouPointBlank extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouPointBlank");

    public DamuhouPointBlank() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 12;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
