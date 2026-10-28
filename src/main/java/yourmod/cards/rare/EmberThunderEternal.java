package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberThunderEternalPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 烬雷·永恒（2费→升级1费）
 * 升级前：每回合第一张攻击加入0费虚无消耗复制
 * 升级后：费用1
 */
public class EmberThunderEternal extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderEternal");

    public EmberThunderEternal() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new EmberThunderEternalPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
