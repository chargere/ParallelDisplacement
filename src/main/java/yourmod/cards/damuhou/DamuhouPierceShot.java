package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 子弹穿透 */
public class DamuhouPierceShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouPierceShot");

    public DamuhouPierceShot() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseSecondDamage = secondDamage = 4;
        printedCombo = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 1, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        AbstractMonster behind = neighborBehind(m);
        if (behind != null) {
            int saved = damage;
            damage = secondDamage;
            calculateCardDamage(behind);
            super.dmg(behind, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            damage = saved;
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(2);
    }
}
