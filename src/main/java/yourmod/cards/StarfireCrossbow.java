package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 流星火弩（1费）
 * 升级前：全体 8 伤；全体 3 烬火
 * 升级后：全体 5 烬火（伤害不变）
 */
public class StarfireCrossbow extends AbstractEasyCard {
    public final static String ID = makeID("StarfireCrossbow");

    public StarfireCrossbow() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ALL_ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 3; // 烬火层数，升级 +2
        isMultiDamage = true; // 全体攻击需要
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.FIRE);
        forAllMonstersLiving(mo -> applyToEnemy(mo, new EmberFirePower(mo, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
