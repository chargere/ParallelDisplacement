package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;

public class AuroraCleave extends AbstractDominicCard {
    public static final String ID = makeID("AuroraCleave");

    public AuroraCleave() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 12;
        baseMagicNumber = magicNumber = 2;
        setFrostPay(3, FrostPayType.DELAY);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        FrostColdPower.gainFrost(p, magicNumber);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
