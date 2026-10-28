package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Unsharpened extends AbstractDominicCard {
    public static final String ID = makeID("Unsharpened");

    public Unsharpened() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 25;
        baseMagicNumber = magicNumber = 3;
        setFrostPay(4, FrostPayType.STAR_EXTINGUISH);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        atb(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}
