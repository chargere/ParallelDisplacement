package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.relics.PrismOpticSystem;
import yourmod.util.DamuhouFireMode;

import static yourmod.ModFile.makeID;

/** 过肩摔 */
public class DamuhouOverShoulder extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouOverShoulder");

    public DamuhouOverShoulder() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.NONE);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster t = DamuhouFireMode.sightedLiving();
        if (t != null) {
            calculateCardDamage(t);
            directHits(t, magicNumber, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        PrismOpticSystem.swapIfPresent();
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        printedCombo = magicNumber;
    }
}
