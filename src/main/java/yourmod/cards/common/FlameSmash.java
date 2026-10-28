package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;

/**
 * 烈焰猛击（2费）
 * 升级前：12 伤；若易伤则耗1易伤再造成 8 伤
 * 升级后：15 伤；第二段 10 伤
 */
public class FlameSmash extends AbstractEasyCard {
    public final static String ID = makeID("FlameSmash");

    public FlameSmash() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 12;
        baseSecondDamage = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            addToBot(new ReducePowerAction(m, p, VulnerablePower.POWER_ID, 1));
            altDmg(m, AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeSecondDamage(2);
    }
}
