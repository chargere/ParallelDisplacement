package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.XingNianPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 昼夜双面技能：昼相加速资源，夜相回收手牌节奏。 */
public class DawnBell extends AbstractDayNightCard {
    public static final String ID = makeID("DawnBell");

    public DawnBell() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 5;
        refreshPhase();
    }

    @Override
    protected void updatePhaseText(boolean day) {
        baseBlock = (day ? 5 : 3) + (upgraded ? 2 : 0);
        setPhaseText("晨钟", "获得 !B! 点格挡。获得 2 点星念。",
                "夜渡", "获得 !B! 点格挡。抽 1 张牌。");
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (isDayPhase()) Wiz.applyToSelf(new XingNianPower(p, 2));
        else addToBot(new DrawCardAction(1));
        ComboSystem.record(p, this);
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
