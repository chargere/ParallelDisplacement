package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EternalBurnPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 焚天·永燃（3费→升级2费）
 * 升级前：燎原计数 4/5/6 时无视烬火门槛，无条件加伤与推进，且不消耗烬火
 * 升级后：费用2
 */
public class EternalBurn extends AbstractEasyCard {
    public final static String ID = makeID("EternalBurn");

    public EternalBurn() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new EternalBurnPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
    }
}
