package yourmod.powers.jingjue;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.actions.jingjue.ExhaustDrawTopAction;
import yourmod.powers.AbstractEasyPower;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 惧魇形态：每回合烧抽牌堆顶，消耗牌换沙漏与眼印。 */
public class DreadFormPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JjDreadForm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DreadFormPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
        useVanillaIcon("wraithform");
    }

    @Override
    public void onInitialApplication() {
        addToBot(new ExhaustDrawTopAction());
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        addToBot(new ExhaustDrawTopAction());
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null) {
            return;
        }
        flash();
        JingjueCombatState.gainHourglass(1);
        Wiz.forAllMonstersLiving(m -> addToBot(new ApplyPowerAction(m, owner, new EyeMarkPower(m, 1), 1)));
    }

    @Override
    public void stackPower(int stackAmount) {
    }

    @Override
    public void reducePower(int reduceAmount) {
    }

    @Override
    public void updateDescription() {
        locDesc(strings.DESCRIPTIONS[0]);
    }
}
