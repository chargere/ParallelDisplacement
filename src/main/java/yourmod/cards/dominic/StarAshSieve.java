package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarAshSieve extends AbstractDominicCard {
    public static final String ID = makeID("StarAshSieve");

    public StarAshSieve() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        setFrostPay(2, FrostPayType.RETURN_TO_VOID, 1);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                atb(new ApplyPowerAction(mo, p, new StarMarkPower(mo, magicNumber), magicNumber));
            }
        }
        atb(new DiscardAction(p, p, 1, false));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
