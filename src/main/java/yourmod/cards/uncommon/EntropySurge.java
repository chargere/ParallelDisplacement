package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 熵增（2费）
 * 升级前：10 伤；额外打击次数=目标静电层数（最多4）
 * 升级后：13 伤
 */
public class EntropySurge extends AbstractEasyCard {
    public final static String ID = makeID("EntropySurge");

    public EntropySurge() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = 1 + Math.min(pwrAmt(m, StaticMarkPower.POWER_ID), 4);
        for (int i = 0; i < hits; i++) {
            dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
