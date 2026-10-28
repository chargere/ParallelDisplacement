package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 弹射 */
public class DamuhouRicochet extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouRicochet");

    public DamuhouRicochet() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 2, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(m);
                if (t == null || t.isDeadOrEscaped()) {
                    for (AbstractMonster n : neighborsOf(m)) {
                        if (n != null && !n.isDeadOrEscaped()) {
                            int saved = damage;
                            damage = magicNumber;
                            calculateCardDamage(n);
                            addToTop(new com.megacrit.cardcrawl.actions.common.DamageAction(n,
                                    new com.megacrit.cardcrawl.cards.DamageInfo(p, damage, damageTypeForTurn),
                                    AttackEffect.BLUNT_LIGHT));
                            damage = saved;
                        }
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
