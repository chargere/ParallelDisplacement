package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StarMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class StarChartFragment extends AbstractDominicCard {
    public static final String ID = makeID("StarChartFragment");

    public StarChartFragment() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        selfRetain = true;
        setFrostPay(3, FrostPayType.RETURN_TO_VOID, 2);
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        atb(new ScryAction(magicNumber));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                atb(new ApplyPowerAction(mo, p, new StarMarkPower(mo, 1), 1));
            }
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
