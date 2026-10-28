package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 烬雷涅槃（1费）
 * 升级前：移除全部减益；回复15；15 格挡；消耗
 * 升级后：回复20；20 格挡
 */
public class EmberThunderNirvana extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderNirvana");

    public EmberThunderNirvana() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 15;
        baseMagicNumber = magicNumber = 15;
        exhaust = true;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractPower> debuffs = new ArrayList<>();
        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                debuffs.add(pow);
            }
        }
        for (AbstractPower pow : debuffs) {
            atb(new RemoveSpecificPowerAction(p, p, pow.ID));
        }
        atb(new HealAction(p, p, magicNumber));
        blck();
    }

    @Override
    public void upp() {
        upgradeBlock(5);
        upgradeMagicNumber(5);
    }
}
