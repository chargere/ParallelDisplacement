package yourmod.cards.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 静电屏障（1费）
 * 升级前：6 格挡；全体 1 静电印记
 * 升级后：9 格挡
 */
public class StaticBarrier extends AbstractEasyCard {
    public final static String ID = makeID("StaticBarrier");

    public StaticBarrier() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        forAllMonstersLiving(mo -> applyToEnemy(mo, new StaticMarkPower(mo, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}
