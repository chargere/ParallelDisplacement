package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BreachPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 铺场 */
public class DamuhouLayBreach extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouLayBreach");

    public DamuhouLayBreach() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mo ->
                addToBot(new ApplyPowerAction(mo, p, new BreachPower(mo, magicNumber), magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
