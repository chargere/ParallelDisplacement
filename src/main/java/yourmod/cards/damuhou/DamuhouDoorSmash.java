package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 砸门 */
public class DamuhouDoorSmash extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouDoorSmash");

    public DamuhouDoorSmash() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 19;
        printedCombo = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 1, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
