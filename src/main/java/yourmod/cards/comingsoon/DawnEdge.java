package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 昼夜双面攻击：昼相推进星念，夜相削弱敌人。 */
public class DawnEdge extends AbstractDayNightCard {
    public static final String ID = makeID("DawnEdge");

    public DawnEdge() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        refreshPhase();
    }

    @Override
    protected void updatePhaseText(boolean day) {
        baseDamage = (day ? 8 : 6) + (upgraded ? 3 : 0);
        setPhaseText("曙锋", "造成 !D! 点伤害。获得 1 点星念。",
                "暮刃", "造成 !D! 点伤害。给予目标 1 层虚弱。");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (isDayPhase()) {
            Wiz.applyToSelf(new yourmod.powers.XingNianPower(p, 1));
        } else {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
        }
        ComboSystem.record(p, this);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
