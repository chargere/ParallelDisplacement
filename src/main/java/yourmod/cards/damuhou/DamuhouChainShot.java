package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 连环射击 */
public class DamuhouChainShot extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouChainShot");

    public DamuhouChainShot() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        final int inherit = magicNumber;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(m);
                if (t == null || t.isDeadOrEscaped()) {
                    DamuhouCombatState.addThisTurnNextCombo(inherit);
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        printedCombo = magicNumber;
    }
}
