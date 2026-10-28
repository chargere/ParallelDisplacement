package yourmod.cards.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 余烬护盾（1费）
 * 升级前：7 格挡；全体 2 烬火
 * 升级后：10 格挡；全体 3 烬火
 */
public class EmberShield extends AbstractEasyCard {
    public final static String ID = makeID("EmberShield");

    public EmberShield() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 7;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        forAllMonstersLiving(mo -> applyToEnemy(mo, new EmberFirePower(mo, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(1);
    }
}
