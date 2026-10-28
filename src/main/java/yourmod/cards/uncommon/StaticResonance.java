package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticResonancePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 静电共鸣（2费→升级1费）
 * 升级前：每当施加静电，额外施加 5 烬火
 * 升级后：费用1
 */
public class StaticResonance extends AbstractEasyCard {
    public final static String ID = makeID("StaticResonance");

    public StaticResonance() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new StaticResonancePower(p));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
