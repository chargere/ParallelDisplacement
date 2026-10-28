package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;

/**
 * 电流刺激（1费）
 * 升级前：6 伤；抽1；有静电再抽2
 * 升级后：8 伤
 */
public class CurrentStimulus extends AbstractEasyCard {
    public final static String ID = makeID("CurrentStimulus");

    public CurrentStimulus() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        addToBot(new DrawCardAction(1));
        if (m.hasPower(StaticMarkPower.POWER_ID)) {
            addToBot(new DrawCardAction(2));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
