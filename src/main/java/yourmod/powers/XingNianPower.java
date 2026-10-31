package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.util.ComboSystem;
import yourmod.util.FormUtil;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 星念：时间旅人的核心资源（累积、封顶、每回合 +1）。
 * 由起始遗物「风色x落陨」在战斗开始时赋予（初始 0）。
 * 同时承载三形态：描述实时显示当前形态与效果。
 *  - 溯（0~3）：回合开始获得 6 格挡（蓄势防守）。
 *  - 驻（4~7）：强化连携（攻攻技额外 +1 星念 / +1 能量）。
 *  - 落（8~12）：打出攻击牌获得 1 层临时力量（回合开始回收）。
 */
public class XingNianPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("XingNian");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    /** 星念上限（累积封顶） */
    public static final int CAP = 12;

    /** 落形态下本回合通过攻击牌获得的临时力量层数（回合末回收） */
    public int luoStrThisTurn = 0;

    public XingNianPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("focus");
    }

    /**
     * 回合开始：先回收上一回合落形态获得的临时力量，再处理本回合效果。
     * （本版本 AbstractPower 无 atEndOfTurn 钩子，故在回合开始回收，效果等价。）
     */
    @Override
    public void atStartOfTurn() {
        if (luoStrThisTurn > 0) {
            Wiz.applyToSelf(new StrengthPower(owner, -luoStrThisTurn));
            luoStrThisTurn = 0;
        }
        if (FormUtil.getForm(amount) == FormUtil.SU) {
            Wiz.atb(new GainBlockAction(owner, owner, 6));
        }
    }

    /** 星念首次施加（每场战斗开始）时清空全局连携队列，避免上场残留 */
    @Override
    public void onInitialApplication() {
        ComboSystem.reset();
    }

    /** 每回合开始（抽牌后）星念 +1，封顶 */
    @Override
    public void atStartOfTurnPostDraw() {
        if (amount < CAP) {
            amount++;
            updateDescription();
        }
    }

    /** 落形态：打出攻击牌获得 1 层临时力量（下回合开始由 atStartOfTurn 回收） */
    public void gainLuoTempStrength() {
        Wiz.applyToSelf(new StrengthPower(owner, 1));
        luoStrThisTurn++;
    }

    @Override
    public void updateDescription() {
        int form = FormUtil.getForm(amount);
        String hint;
        switch (form) {
            case FormUtil.SU:  hint = "回合开始获得6格挡"; break;
            case FormUtil.ZHU: hint = "连携强化（攻攻技+1星念/+1能量）"; break;
            default:           hint = "攻击牌获得临时力量"; break;
        }
        description = strings.DESCRIPTIONS[0] + amount + "/" + CAP
                + strings.DESCRIPTIONS[1] + " 形态：" + FormUtil.formName(form) + "（" + hint + "）";
    }
}
