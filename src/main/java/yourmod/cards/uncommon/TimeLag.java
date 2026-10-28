package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 时差（1费）
 * 升级前：7 伤；每多留手一回合 +3 伤；打出后重置
 * 升级后：10 伤（缩放公式不变）
 */
public class TimeLag extends AbstractEasyCard {
    public final static String ID = makeID("TimeLag");
    public int holdTurns = 0;

    public TimeLag() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 7;
    }

    @Override
    public void triggerWhenDrawn() {
        holdTurns++;
        applyPowers();
    }

    /** 回合开始仍在手中时由 ScorchingAssaultPower 调用 */
    public void heldAnotherTurn() {
        holdTurns++;
        applyPowers();
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage = realBase + 3 * holdTurns;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage = realBase + 3 * holdTurns;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        holdTurns = 0;
    }

    @Override
    public AbstractEasyCard makeStatEquivalentCopy() {
        TimeLag c = (TimeLag) super.makeStatEquivalentCopy();
        c.holdTurns = this.holdTurns;
        return c;
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
