package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.XingNianPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 昼夜双面技能：昼相积累，夜相整理手牌。 */
public class SunGuard extends AbstractDayNightCard {
    public static final String ID = makeID("SunGuard");

    public SunGuard() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
        refreshPhase();
    }

    @Override
    protected void updatePhaseText(boolean day) {
        baseBlock = (day ? 8 : 4) + (upgraded ? 2 : 0);
        setPhaseText("日守", "获得 !B! 点格挡。获得 1 点星念。",
                "夜幕", "获得 !B! 点格挡。抽 1 张牌。");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (isDayPhase()) Wiz.applyToSelf(new XingNianPower(p, 1));
        else addToBot(new DrawCardAction(1));
        ComboSystem.record(p, this);
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
