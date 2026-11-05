package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.XingNianPower;
import yourmod.util.ComboSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 初始昼夜技能：昼相蓄念，夜相抽牌。 */
public class MorningRest extends AbstractDayNightCard {
    public static final String ID = makeID("MorningRest");

    public MorningRest() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 6;
        refreshPhase();
    }

    @Override
    protected void updatePhaseText(boolean day) {
        baseBlock = (day ? 6 : 4) + (upgraded ? 2 : 0);
        setPhaseText("晨息", "获得 !B! 点格挡。获得 1 点星念。",
                "夜息", "获得 !B! 点格挡。抽 1 张牌。");
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
