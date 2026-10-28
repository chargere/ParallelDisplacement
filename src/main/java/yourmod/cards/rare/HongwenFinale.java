package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

/**
 * 鸿文终焉（0费）
 * 升级前：4 伤；本场每失去 5 生命 +3 伤
 * 升级后：6 伤（缩放公式不变）
 */
public class HongwenFinale extends AbstractEasyCard {
    public final static String ID = makeID("HongwenFinale");

    public HongwenFinale() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 4;
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage = realBase + (HongwenHooks.lostHPThisCombat / 5) * 3;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage = realBase + (HongwenHooks.lostHPThisCombat / 5) * 3;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
