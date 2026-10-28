package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.DualGuardPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 平衡（1费→升级0费）
 * 升级前：获得双御；消耗
 * 升级后：费用0
 */
public class BalanceCard extends AbstractEasyCard {
    public final static String ID = makeID("BalanceCard");

    public BalanceCard() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DualGuardPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
