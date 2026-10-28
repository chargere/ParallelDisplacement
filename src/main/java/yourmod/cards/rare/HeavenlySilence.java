package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ScorchingAssaultPower;

import static yourmod.ModFile.makeID;

/**
 * 天雷寂灭（1费）
 * 升级前：8 伤；本次攻击的静电传导可再次传导
 * 升级后：11 伤
 */
public class HeavenlySilence extends AbstractEasyCard {
    public final static String ID = makeID("HeavenlySilence");

    public HeavenlySilence() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower pow = p.getPower(ScorchingAssaultPower.POWER_ID);
        if (pow instanceof ScorchingAssaultPower) {
            ((ScorchingAssaultPower) pow).allowMultiConduct = true;
        }
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
