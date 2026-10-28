package yourmod.cards.colorless;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.MagneticBombPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.atb;

/**
 * 磁吸炸弹（无色罕见 / 1费）
 * 指定一名敌人，3 回合后对其造成 20 伤害
 * 升级后：伤害 30
 * 彩蛋：打出时 50%「磁吸炸弹投出！」/ 50%「准备起爆」
 */
public class MagneticBomb extends AbstractEasyCard {
    public final static String ID = makeID("MagneticBomb");
    private static final int DELAY_TURNS = 3;

    public MagneticBomb() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseMagicNumber = magicNumber = 20;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String line = AbstractDungeon.cardRandomRng.randomBoolean()
                ? "磁吸炸弹投出！"
                : "准备起爆！";
        atb(new TalkAction(true, line, 0.0F, 2.0F));

        AbstractPower existing = m.getPower(MagneticBombPower.POWER_ID);
        if (existing instanceof MagneticBombPower) {
            ((MagneticBombPower) existing).stackBomb(DELAY_TURNS, magicNumber);
            existing.flash();
        } else {
            applyToEnemy(m, new MagneticBombPower(m, DELAY_TURNS, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(10);
    }
}
