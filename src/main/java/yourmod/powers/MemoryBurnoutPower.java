package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.actions.MakeAshCardInHandAction;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 记忆燃尽：归墟消耗的牌进入星灰，下回合以0费虚无消耗复制入手 */
public class MemoryBurnoutPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("MemoryBurnout");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private final ArrayList<AbstractCard> ashList = new ArrayList<>();

    public MemoryBurnoutPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1);
    }

    public void addAshCard(AbstractCard card) {
        if (card == null) {
            return;
        }
        flash();
        ashList.add(card.makeStatEquivalentCopy());
    }

    @Override
    public void atStartOfTurn() {
        if (ashList.isEmpty()) {
            return;
        }
        flash();
        for (AbstractCard c : ashList) {
            addToBot(new MakeAshCardInHandAction(c));
        }
        ashList.clear();
        addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(owner, owner, this));
    }

    /**
     * 0 费 + 虚无 + 消耗。
     * 中文客户端走 initializeDescriptionCN，不会仅凭 isEthereal/exhaust 刷出词条，
     * 必须写进 rawDescription。
     */
    public static void applyAshModifiers(AbstractCard copy) {
        if (copy == null) {
            return;
        }
        copy.cost = 0;
        copy.costForTurn = 0;
        copy.isCostModified = true;
        copy.isEthereal = true;
        copy.exhaust = true;
        String desc = copy.rawDescription == null ? "" : copy.rawDescription;
        StringBuilder prefix = new StringBuilder();
        if (!desc.contains("虚无")) {
            prefix.append("虚无。 NL ");
        }
        if (!desc.contains("消耗")) {
            prefix.append("消耗。 NL ");
        }
        if (prefix.length() > 0) {
            copy.rawDescription = prefix + desc;
        }
        copy.initializeDescription();
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
