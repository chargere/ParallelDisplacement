package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.cards.derived.TripleAngerStatus;

import static yourmod.ModFile.makeID;

/**
 * 牢郑形态：回合开始将「愤怒愤怒愤怒」×层数加入手牌；
 * 层数每回合 +1，上限 5。
 * 若来自升级版牢郑，则加入升级版「愤怒愤怒愤怒+」（抽 2）。
 */
public class RageFormPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("RageForm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /** 是否生成升级版愤怒愤怒愤怒 */
    public final boolean upgradedAnger;

    public RageFormPower(AbstractCreature owner, int amount, boolean upgradedAnger) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        this.upgradedAnger = upgradedAnger;
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        flash();
        TripleAngerStatus card = new TripleAngerStatus();
        if (upgradedAnger) {
            card.upgrade();
        }
        addToBot(new MakeTempCardInHandAction(card, amount));
        if (amount < 5) {
            amount++;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        if (upgradedAnger) {
            description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[2];
        } else {
            description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
        }
    }
}
