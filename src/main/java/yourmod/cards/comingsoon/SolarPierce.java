package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.XingNianPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 昼夜双面重攻击：昼相集中，夜相扩散。 */
public class SolarPierce extends AbstractDayNightCard {
    public static final String ID = makeID("SolarPierce");

    public SolarPierce() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 16;
        refreshPhase();
    }

    @Override
    protected void updatePhaseText(boolean day) {
        baseDamage = (day ? 16 : 8) + (upgraded ? 4 : 0);
        setPhaseText("日轮贯", "造成 !D! 点伤害。获得 2 点星念。",
                "月蚀落", "对所有敌人造成 !D! 点伤害。");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isDayPhase()) {
            dmg(m, AbstractGameAction.AttackEffect.FIRE);
            Wiz.applyToSelf(new XingNianPower(p, 2));
        } else {
            addToBot(new DamageAllEnemiesAction(p,
                    DamageInfo.createDamageMatrix(damage, true),
                    DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        }
        ComboSystem.record(p, this);
    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}
