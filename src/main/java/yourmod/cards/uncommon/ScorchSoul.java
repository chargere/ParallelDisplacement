package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyXCostAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;
import static yourmod.util.Wiz.getEnemies;
import static yourmod.util.Wiz.pwrAmt;

/**
 * 灼魂（X费）
 * 升级前：造成 X×5 伤；失去 max(1, X-全体烬火-全体静电) 生命
 * 升级后：造成 X×7 伤
 */
public class ScorchSoul extends AbstractEasyCard {
    public final static String ID = makeID("ScorchSoul");

    public ScorchSoul() {
        super(ID, -1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            int ember = 0;
            int stat = 0;
            for (AbstractMonster mo : getEnemies()) {
                ember += pwrAmt(mo, EmberFirePower.POWER_ID);
                stat += pwrAmt(mo, StaticMarkPower.POWER_ID);
            }
            int hpLoss = Math.max(1, effect - ember - stat);
            calculateCardDamage(m);
            for (int i = 0; i < effect; i++) {
                att(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
            att(new LoseHPAction(p, p, hpLoss));
            return true;
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}
