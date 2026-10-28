package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DamuhouSpiteComboPower;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

/** 自信颗秒 */
public class DamuhouCockyKill extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouCockyKill");

    public DamuhouCockyKill() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 2;
        printedCombo = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 1, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(m);
                if (t != null && !t.isDeadOrEscaped()) {
                    DamuhouCombatState.addThisTurnNextCombo(magicNumber);
                    addToTop(new ApplyPowerAction(p, p, new DamuhouSpiteComboPower(p, magicNumber), magicNumber));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
    }
}
