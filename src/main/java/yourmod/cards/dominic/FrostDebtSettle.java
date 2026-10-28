package yourmod.cards.dominic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.FrostColdPower;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class FrostDebtSettle extends AbstractDominicCard {
    public static final String ID = makeID("FrostDebtSettle");

    public FrostDebtSettle() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        exhaust = true;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("清算迟滞",
                "失去 " + magicNumber + " 层霜寒，清除手牌因迟滞增加的费用。", () -> {
            FrostColdPower.spendFrostActive(p, magicNumber);
            for (AbstractCard c : p.hand.group) {
                if (c.cost >= 0 && c.costForTurn > c.cost) {
                    c.setCostForTurn(c.cost);
                }
            }
        }));
        choices.add(new EasyModalChoiceCard("积蓄霜寒",
                "获得 " + secondMagic + " 层霜寒。", () -> {
            FrostColdPower.gainFrost(p, secondMagic);
        }));
        atb(new EasyModalChoiceAction(choices));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(-1);
        upgradeSecondMagic(1);
    }
}
