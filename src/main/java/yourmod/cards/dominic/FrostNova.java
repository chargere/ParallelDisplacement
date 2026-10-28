package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class FrostNova extends AbstractDominicCard {
    public static final String ID = makeID("FrostNova");

    public FrostNova() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 8;
        baseBlock = 10;
        isMultiDamage = true;
        setFrostPay(2, FrostPayType.FREEZE_CRACK, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        blck();
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeBlock(4);
    }
}
