package yourmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 烬灭裁决（2费）
 * 升级前：消耗目标全部烬火；14 伤，每层额外 +5 伤
 * 升级后：18 伤（每层仍+5）
 */
public class EmberAnnihilation extends AbstractEasyCard {
    public final static String ID = makeID("EmberAnnihilation");

    public EmberAnnihilation() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 14;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int stacks = pwrAmt(m, EmberFirePower.POWER_ID);
        AbstractPower ember = m.getPower(EmberFirePower.POWER_ID);
        if (ember != null && stacks > 0) {
            atb(new RemoveSpecificPowerAction(m, p, EmberFirePower.POWER_ID));
        }
        int realBase = baseDamage;
        baseDamage = realBase + stacks * 5;
        calculateCardDamage(m);
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        baseDamage = realBase;
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
