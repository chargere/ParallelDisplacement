package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/**
 * 星念：时间旅人的核心资源（累积、封顶、每回合 +1）。
 * 由起始遗物「风色x落陨」在战斗开始时赋予（初始 0）。
 * 卡牌可直接读取 / 累加本 Power 的 amount；形态切换也以它为依据。
 */
public class XingNianPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("XingNian");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /** 星念上限（累积封顶） */
    public static final int CAP = 12;

    public XingNianPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("focus");
    }

    /** 每回合开始（抽牌后）星念 +1，封顶 */
    @Override
    public void atStartOfTurnPostDraw() {
        if (amount < CAP) {
            amount++;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + "/" + CAP + strings.DESCRIPTIONS[1];
    }
}
