package yourmod.cards.fvv;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.UselessPower;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

public class PeerReview extends AbstractFvvCard {
    public static final String ID = makeID("PeerReview");

    public PeerReview() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("Admit", "Gain " + magicNumber + " Useless.", () -> {
            UselessPower.gain(p, p, magicNumber);
        }));
        int drawAmt = upgraded ? 3 : 2;
        choices.add(new EasyModalChoiceCard("Spend", "Spend " + secondMagic + " Useless, draw " + drawAmt + ".", () -> {
            if (UselessPower.getAmount(p) >= secondMagic) {
                UselessPower.spend(p, secondMagic);
                atb(new DrawCardAction(drawAmt));
            }
        }));
        atb(new EasyModalChoiceAction(choices));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
