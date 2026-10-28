package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;

/**
 * 余烬回收（1费）
 * 升级前：8 格挡；消耗目标全部烬火，每层额外 +4 格挡
 * 升级后：11 格挡；每层额外 +5 格挡
 */
public class EmberRecycle extends AbstractEasyCard {
    public final static String ID = makeID("EmberRecycle");

    public EmberRecycle() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        AbstractPower ember = m.getPower(EmberFirePower.POWER_ID);
        if (ember != null && ember.amount > 0) {
            int stacks = ember.amount;
            addToBot(new RemoveSpecificPowerAction(m, p, EmberFirePower.POWER_ID));
            addToBot(new GainBlockAction(p, stacks * magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
