package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 烬雷涡轮（0费）
 * 升级前：保留；获得 2 能量；本回合已打非攻击再+1
 * 升级后：获得 3 能量
 */
public class EmberThunderTurbo extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderTurbo");

    public EmberThunderTurbo() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energy = magicNumber;
        if (HongwenHooks.countNonAttacksThisTurn() > 0) {
            energy++;
        }
        atb(new GainEnergyAction(energy));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
