package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 不灭战纹（0费）
 * 升级前：保留；3 临时力量；本回合已打非攻击再+2
 * 升级后：4 临时力量；额外再+3
 */
public class UndyingWarMark extends AbstractEasyCard {
    public final static String ID = makeID("UndyingWarMark");

    public UndyingWarMark() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amt = magicNumber;
        if (HongwenHooks.countNonAttacksThisTurn() > 0) {
            amt += secondMagic;
        }
        atb(new ApplyPowerAction(p, p, new StrengthPower(p, amt), amt));
        atb(new ApplyPowerAction(p, p, new LoseStrengthPower(p, amt), amt));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}
