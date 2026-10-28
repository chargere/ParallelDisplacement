package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

/**
 * 万雷归宗（3费）
 * 升级前：全体 18 伤；本回合每传导一次 +7 伤
 * 升级后：全体 22 伤（缩放公式不变）
 */
public class MyriadThunder extends AbstractEasyCard {
    public final static String ID = makeID("MyriadThunder");

    public MyriadThunder() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 18;
        isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage = realBase + 7 * HongwenHooks.conductCountThisTurn;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage = realBase + 7 * HongwenHooks.conductCountThisTurn;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.LIGHTNING);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
