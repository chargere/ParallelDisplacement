package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.NextTurnEnergyPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

/**
 * 雷纹回馈（0费）
 * 升级前：获得 2 能量；下回合再获得 2 能量
 * 升级后：本回合与下回合各 3 能量
 */
public class ThunderFeedback extends AbstractEasyCard {
    public final static String ID = makeID("ThunderFeedback");

    public ThunderFeedback() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(magicNumber));
        applyToSelf(new NextTurnEnergyPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
