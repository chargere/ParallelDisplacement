package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ScorchingAssaultPower;

import static yourmod.ModFile.makeID;

/**
 * 时序残响（0费）
 * 升级前：保留；将本回合第一张被消耗的牌移回手牌；消耗
 * 升级后：不再消耗
 */
public class TemporalEcho extends AbstractEasyCard {
    public final static String ID = makeID("TemporalEcho");

    public TemporalEcho() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hasPower(ScorchingAssaultPower.POWER_ID)) {
            ScorchingAssaultPower pow = (ScorchingAssaultPower) p.getPower(ScorchingAssaultPower.POWER_ID);
            AbstractCard target = pow.firstExhaustedThisTurn;
            if (target != null && p.exhaustPile.contains(target)) {
                p.exhaustPile.moveToHand(target);
                target.unfadeOut();
            }
        }
    }

    @Override
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
