package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;

public class IceShardScatter extends AbstractDominicCard {
    public static final String ID = makeID("IceShardScatter");

    public IceShardScatter() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 4;
        isMultiDamage = true;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        FrostColdPower.gainFrost(p, magicNumber);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
