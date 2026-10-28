package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberThunderOrdersPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 焚雷军令（1费→升级0费）
 * 升级前：回合开始预见3；弃攻击获1临时力量；弃技能获3格挡
 * 升级后：费用0
 */
public class EmberThunderOrders extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderOrders");

    public EmberThunderOrders() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new EmberThunderOrdersPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
