package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

/**
 * 天命（1费）
 * 升级前：保留；8 格挡；本场未受攻击伤害则再 10 格挡
 * 升级后：11 格挡；额外 12 格挡
 */
public class MandateOfHeaven extends AbstractEasyCard {
    public final static String ID = makeID("MandateOfHeaven");

    public MandateOfHeaven() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 10;
        selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (!HongwenHooks.tookAttackDamageThisCombat) {
            addToBot(new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(2);
    }
}
