package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 雷鸣虹锋（2费→升级1费）
 * 升级前：全体 8 伤；全体 1 静电印记
 * 升级后：费用1；全体 10 伤
 */
public class ThunderEdge extends AbstractEasyCard {
    public final static String ID = makeID("ThunderEdge");

    public ThunderEdge() {
        super(ID, 2, CardType.ATTACK, CardRarity.BASIC, CardTarget.ALL_ENEMY);
        baseDamage = 8; // 升级后 10
        baseMagicNumber = magicNumber = 1; // 静电印记层数
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.LIGHTNING);
        forAllMonstersLiving(mo -> applyToEnemy(mo, new StaticMarkPower(mo, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
        upgradeDamage(2);
    }
}
