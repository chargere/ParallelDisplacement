package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/**
 * 电离偏转（1费）
 * 升级前：5 格挡；有静电则额外 5 格挡
 * 升级后：7 格挡；额外 7 格挡
 */
public class IonDeflect extends AbstractEasyCard {
    public final static String ID = makeID("IonDeflect");

    public IonDeflect() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 5;
        baseMagicNumber = magicNumber = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        for (AbstractMonster mo : getEnemies()) {
            if (mo.hasPower(StaticMarkPower.POWER_ID)) {
                addToBot(new GainBlockAction(p, magicNumber));
                break;
            }
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}
