package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.UnyieldingEmberPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 不屈余烬（1费→升级0费）
 * 升级前：每当失去生命，对随机敌人施加 5 烬火
 * 升级后：费用0
 */
public class UnyieldingEmber extends AbstractEasyCard {
    public final static String ID = makeID("UnyieldingEmber");

    public UnyieldingEmber() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new UnyieldingEmberPower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
