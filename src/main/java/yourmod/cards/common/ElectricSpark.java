package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.ScorchingAssaultPower;

import static yourmod.ModFile.makeID;

/**
 * 电能火花（2费）
 * 升级前：14 伤；每传导一个敌人获得 1 能量
 * 升级后：18 伤
 */
public class ElectricSpark extends AbstractEasyCard {
    public final static String ID = makeID("ElectricSpark");

    public ElectricSpark() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 14;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower sap = p.getPower(ScorchingAssaultPower.POWER_ID);
        if (sap instanceof ScorchingAssaultPower) {
            ((ScorchingAssaultPower) sap).grantEnergyOnConduct = true;
        }
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
