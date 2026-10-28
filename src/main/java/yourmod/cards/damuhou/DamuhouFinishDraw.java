package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 极光连击 */
public class DamuhouFinishDraw extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFinishDraw");

    public DamuhouFinishDraw() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 2;
        printedCombo = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, magicNumber, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(m);
                if (t == null || t.isDeadOrEscaped()) {
                    addToTop(new DrawCardAction(p, secondMagic));
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
