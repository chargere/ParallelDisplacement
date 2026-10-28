package yourmod.cards.fvv;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.UselessPower;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

public class ReviewMeeting extends AbstractFvvCard {
    public static final String ID = makeID("ReviewMeeting");

    public ReviewMeeting() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.discardPile.isEmpty()) {
            return;
        }
        ArrayList<AbstractCard> opts = new ArrayList<>(p.discardPile.group);
        atb(new SelectCardsCenteredAction(opts, 1, "Select", cards -> {
            for (AbstractCard c : cards) {
                p.discardPile.removeCard(c);
                p.hand.addToHand(c);
                c.lighten(false);
                if (c.type == CardType.ATTACK) {
                    UselessPower.gain(p, p, 1);
                } else if (c.type == CardType.SKILL) {
                    atb(new GainBlockAction(p, p, block));
                }
            }
        }));
        if (upgraded) {
            atb(new DrawCardAction(1));
        }
    }

    @Override
    public void upp() {
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
