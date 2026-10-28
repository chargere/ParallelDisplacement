package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.FirstAttackEmberPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 燎原战纹（1费→升级0费）
 * 升级前：每回合第一张攻击对目标施加 2 烬火
 * 升级后：费用0
 */
public class ScorchingWarMark extends AbstractEasyCard {
    public final static String ID = makeID("ScorchingWarMark");

    public ScorchingWarMark() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FirstAttackEmberPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
