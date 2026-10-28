package yourmod.cards.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.TigerCrouchCannonPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;

/**
 * 虎蹲炮（无色罕见 / 2费）
 * 指定一名敌人，3 回合后击晕 2 回合
 * 升级后：费用 1
 */
public class TigerCrouchCannon extends AbstractEasyCard {
    public final static String ID = makeID("TigerCrouchCannon");
    private static final int DELAY_TURNS = 3;

    public TigerCrouchCannon() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower existing = m.getPower(TigerCrouchCannonPower.POWER_ID);
        if (existing instanceof TigerCrouchCannonPower) {
            ((TigerCrouchCannonPower) existing).stackCannon(DELAY_TURNS, magicNumber);
            existing.flash();
        } else {
            applyToEnemy(m, new TigerCrouchCannonPower(m, DELAY_TURNS, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
