package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/**
 * 雷纹传导（1费）
 * 升级前：6 格挡；复制目标最多 2 层静电给其他敌人
 * 升级后：9 格挡；最多复制 3 层
 */
public class ThunderSpread extends AbstractEasyCard {
    public final static String ID = makeID("ThunderSpread");

    public ThunderSpread() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        AbstractPower mark = m.getPower(StaticMarkPower.POWER_ID);
        if (mark == null || mark.amount <= 0) {
            return;
        }
        int spread = Math.min(magicNumber, mark.amount);
        for (AbstractMonster mo : getEnemies()) {
            if (mo != m) {
                addToBot(new ApplyPowerAction(mo, p, new StaticMarkPower(mo, spread), spread));
            }
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
