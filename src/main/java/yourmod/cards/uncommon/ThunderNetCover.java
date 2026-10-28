package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.FirstAttackStaticPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 雷网覆盖（1费→升级0费）
 * 升级前：每回合第一张攻击对目标施加 1 静电
 * 升级后：费用0
 */
public class ThunderNetCover extends AbstractEasyCard {
    public final static String ID = makeID("ThunderNetCover");

    public ThunderNetCover() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FirstAttackStaticPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
