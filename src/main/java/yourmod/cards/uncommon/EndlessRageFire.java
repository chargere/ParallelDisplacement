package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.shuffleIn;

/**
 * 不息怒火（1费）
 * 升级前：6 伤；本回合每打过一张攻击 +3 伤；消耗并洗入复制
 * 升级后：9 伤（缩放公式不变）
 */
public class EndlessRageFire extends AbstractEasyCard {
    public final static String ID = makeID("EndlessRageFire");

    public EndlessRageFire() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
        exhaust = true;
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage += 3 * HongwenHooks.countAttacksThisTurn();
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage += 3 * HongwenHooks.countAttacksThisTurn();
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        shuffleIn(makeStatEquivalentCopy());
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
