package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 快速拔枪 */
public class DamuhouQuickDraw extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouQuickDraw");

    public DamuhouQuickDraw() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int gained = 0;
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (c.type == AbstractCard.CardType.ATTACK) {
                        gained += secondMagic;
                    }
                }
                if (gained > 0) {
                    addToTop(new GainEnergyAction(gained));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}
