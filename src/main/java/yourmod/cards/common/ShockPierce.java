package yourmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;

/**
 * 电击穿刺（1费）
 * 升级前：8 伤；无静电则施加 2 静电印记
 * 升级后：11 伤
 */
public class ShockPierce extends AbstractEasyCard {
    public final static String ID = makeID("ShockPierce");

    public ShockPierce() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        if (!m.hasPower(StaticMarkPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(m, p, new StaticMarkPower(m, magicNumber), magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
