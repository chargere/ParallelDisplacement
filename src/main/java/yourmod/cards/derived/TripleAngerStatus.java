package yourmod.cards.derived;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 【衍生卡】愤怒愤怒愤怒 —— 由「牢郑形态」加入手牌
 * （0费）获得 1 点能量；抽 !M! 张牌；消耗
 * 升级后（愤怒愤怒愤怒+）：抽 2 张
 */
@AutoAdd.Ignore
public class TripleAngerStatus extends AbstractEasyCard {
    public final static String ID = makeID("TripleAngerStatus");

    public TripleAngerStatus() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(1));
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
