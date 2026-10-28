package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class FrostHarvest extends AbstractDominicCard {
    public static final String ID = makeID("FrostHarvest");

    public FrostHarvest() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseSecondDamage = 8;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m != null && m.currentHealth < m.maxHealth / 2) {
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeSecondDamage(4);
    }
}
