package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

/**
 * 藏器于身（1费）
 * 升级前：10 伤；本回合每打过一张非攻击 +5 伤
 * 升级后：13 伤（缩放公式不变）
 */
public class HiddenEdge extends AbstractEasyCard {
    public final static String ID = makeID("HiddenEdge");

    public HiddenEdge() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 10;
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage = realBase + 5 * HongwenHooks.countNonAttacksThisTurn();
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage = realBase + 5 * HongwenHooks.countNonAttacksThisTurn();
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
