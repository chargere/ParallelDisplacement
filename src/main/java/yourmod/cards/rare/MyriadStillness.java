package yourmod.cards.rare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.forAllMonstersLiving;

/**
 * 万象·静止（2费→升级1费）
 * 升级前：保留；全体本回合眩晕；消耗
 * 升级后：费用1
 */
public class MyriadStillness extends AbstractEasyCard {
    public final static String ID = makeID("MyriadStillness");

    public MyriadStillness() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(mo, new StunMonsterPower(mo, 1)));
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}
