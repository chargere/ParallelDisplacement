package yourmod.cards.dominic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FrostColdPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class CloseStarChart extends AbstractDominicCard {
    public static final String ID = makeID("CloseStarChart");

    public CloseStarChart() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        exhaust = true;
        baseMagicNumber = magicNumber = 4;
        baseBlock = 2;
    }

    @Override
    protected void useAfterFrostPay(AbstractPlayer p, AbstractMonster m) {
        int layers = FrostColdPower.getFrost(p);
        if (layers <= 0) {
            return;
        }
        FrostColdPower.spendFrostActive(p, layers);
        for (int i = 0; i < layers; i++) {
            atb(new DamageAction(m, new DamageInfo(p, magicNumber, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            atb(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
