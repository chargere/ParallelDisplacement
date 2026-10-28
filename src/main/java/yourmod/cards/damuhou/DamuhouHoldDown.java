package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static yourmod.ModFile.makeID;

/** A9 压枪 */
public class DamuhouHoldDown extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouHoldDown");

    public DamuhouHoldDown() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 1, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(m);
                if (t != null && !t.isDeadOrEscaped()) {
                    addToTop(new ApplyPowerAction(t, p, new WeakPower(t, magicNumber, false), magicNumber));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
    }
}
