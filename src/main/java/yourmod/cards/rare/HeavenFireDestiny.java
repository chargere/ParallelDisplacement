package yourmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.HeavenFireDestinyPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 天火·命定（3费→升级2费）
 * 升级前：回合结束全体 8 伤；每有烬火敌人+2；每有静电敌人+1
 * 升级后：费用2
 */
public class HeavenFireDestiny extends AbstractEasyCard {
    public final static String ID = makeID("HeavenFireDestiny");

    public HeavenFireDestiny() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new HeavenFireDestinyPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(2);
    }
}
