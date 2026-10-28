package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.HongwenHooks;

import static yourmod.ModFile.makeID;

/**
 * 烬雷坍缩（0费起）
 * 升级前：13 伤；费用=本回合已打出非攻击牌数
 * 升级后：17 伤
 */
public class EmberThunderCollapse extends AbstractEasyCard {
    public final static String ID = makeID("EmberThunderCollapse");

    public EmberThunderCollapse() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 13;
    }

    private void updateCostFromNonAttacks() {
        int n = HongwenHooks.countNonAttacksThisTurn();
        setCostForTurn(n);
    }

    @Override
    public void applyPowers() {
        updateCostFromNonAttacks();
        super.applyPowers();
    }

    @Override
    public void triggerWhenDrawn() {
        updateCostFromNonAttacks();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        updateCostFromNonAttacks();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
