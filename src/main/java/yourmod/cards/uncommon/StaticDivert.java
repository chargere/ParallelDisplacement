package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 静电导流（0费）
 * 升级前：2能量+抽1；有静电敌人则再1能量+抽2；消耗
 * 升级后：不再消耗
 */
public class StaticDivert extends AbstractEasyCard {
    public final static String ID = makeID("StaticDivert");

    public StaticDivert() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(2));
        atb(new DrawCardAction(1));
        if (HongwenHooks.anyEnemyHasStatic()) {
            atb(new GainEnergyAction(1));
            atb(new DrawCardAction(2));
        }
    }

    @Override
    public void upp() {
        this.exhaust = false;
    }
}
