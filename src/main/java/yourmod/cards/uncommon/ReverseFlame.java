package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;

/**
 * 逆火（0费）
 * 升级前：无能量则 15 伤+3 烬火，否则 3 伤；消耗
 * 升级后：无能量则 20 伤+4 烬火（轻击仍3）
 */
public class ReverseFlame extends AbstractEasyCard {
    public final static String ID = makeID("ReverseFlame");

    public ReverseFlame() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseSecondDamage = secondDamage = 15;
        baseMagicNumber = magicNumber = 3;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (EnergyPanel.totalCount == 0) {
            altDmg(m, AbstractGameAction.AttackEffect.FIRE);
            applyToEnemy(m, new EmberFirePower(m, magicNumber));
        } else {
            dmg(m, AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    public void upp() {
        upgradeSecondDamage(5);
        upgradeMagicNumber(1);
    }
}
