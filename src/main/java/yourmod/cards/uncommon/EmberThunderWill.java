package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.WarIntentPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

/**
 * 焚雷战意（0费）
 * 升级前：获得焚雷战意；消耗
 * 升级后：无数值变化（仅名称+）
 */
public class EmberThunderWill extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderWill");

    public EmberThunderWill() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new WarIntentPower(p));
    }

    @Override
    public void upp() {
        // 升级保持 0 费
    }
}
