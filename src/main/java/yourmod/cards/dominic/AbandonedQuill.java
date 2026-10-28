package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.DominicCombatState;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class AbandonedQuill extends AbstractDominicCard {
    public static final String ID = makeID("AbandonedQuill");

    public AbandonedQuill() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        exhaust = true;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        DominicCombatState.abandonedQuillActive = true;
        DominicCombatState.abandonedQuillFrostGained = 0;
        atb(new ScryAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
