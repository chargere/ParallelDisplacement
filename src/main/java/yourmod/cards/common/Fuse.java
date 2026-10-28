package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/**
 * 导火索（0费）
 * 升级前：4 格挡；场上有烬火则抽 3；消耗
 * 升级后：4 格挡；抽 4；不再消耗
 */
public class Fuse extends AbstractEasyCard {
    public final static String ID = makeID("Fuse");

    public Fuse() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 4;
        baseMagicNumber = magicNumber = 3;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        boolean anyEmber = false;
        for (AbstractMonster mo : getEnemies()) {
            if (mo.hasPower(EmberFirePower.POWER_ID)) {
                anyEmber = true;
                break;
            }
        }
        if (anyEmber) {
            addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        exhaust = false;
        uDesc();
    }
}
