package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 烬雷双生（3费）
 * 升级前：14 伤；不在手中时烬火加伤、静电降费；打出后重置
 * 升级后：18 伤
 */
public class EmberThunderTwin extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderTwin");
    public int bonusDamage = 0;
    public int costReduction = 0;
    private static final int BASE_COST = 3;

    public EmberThunderTwin() {
        super(ID, BASE_COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 14;
    }

    /** 不在手中时施加烬火：加伤 */
    public void onEmberWhileAway(int amount) {
        if (amount <= 0) return;
        bonusDamage += amount;
    }

    /** 不在手中时施加静电：降费 */
    public void onStaticWhileAway(int amount) {
        if (amount <= 0) return;
        costReduction += amount;
        this.cost = Math.max(0, BASE_COST - costReduction);
        this.costForTurn = this.cost;
        this.isCostModified = this.cost != BASE_COST;
    }

    @Override
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage += bonusDamage;
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage += bonusDamage;
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        bonusDamage = 0;
        costReduction = 0;
        this.cost = BASE_COST;
        this.costForTurn = BASE_COST;
        this.isCostModified = false;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        EmberThunderTwin c = (EmberThunderTwin) super.makeStatEquivalentCopy();
        c.bonusDamage = this.bonusDamage;
        c.costReduction = this.costReduction;
        c.cost = Math.max(0, BASE_COST - c.costReduction);
        c.costForTurn = c.cost;
        c.isCostModified = c.cost != BASE_COST;
        return c;
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
