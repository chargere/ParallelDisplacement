package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.UndyingWallPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 不灭之壁（1费→升级0费）
 * 升级前：每当打出攻击牌，获得 3 格挡
 * 升级后：费用0
 */
public class UndyingWall extends AbstractEasyCard {
    public final static String ID = makeID("UndyingWall");

    public UndyingWall() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new UndyingWallPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
