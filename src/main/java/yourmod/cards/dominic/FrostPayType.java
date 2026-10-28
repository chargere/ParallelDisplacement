package yourmod.cards.dominic;

/**
 * 霜偿账单类型。
 */
public enum FrostPayType {
    DELAY("迟滞", "打出后，本回合手牌中与其相邻的左右两张牌费用各 +%d。"),
    FREEZE_CRACK("冻裂", "打出后，本回合失去 %d 点敏捷（下回合开始时恢复）。"),
    STAR_EXTINGUISH("星灭", "打出后，本回合不能再打出攻击牌。"),
    RETURN_TO_VOID("归墟", "打出后，本回合接下来 %d 张打出的牌将被消耗。");

    public final String displayName;
    private final String detailFmt;

    FrostPayType(String displayName, String detailFmt) {
        this.displayName = displayName;
        this.detailFmt = detailFmt;
    }

    /** 悬停/弹窗用的完整说明（含数值） */
    public String detail(int value) {
        int v = Math.max(1, value);
        if (this == STAR_EXTINGUISH) {
            return detailFmt;
        }
        return String.format(detailFmt, v);
    }

    /** 一行短标签，如「迟滞」或「归墟2」 */
    public String shortLabel(int value) {
        if (this == STAR_EXTINGUISH || this == DELAY && value <= 1) {
            return displayName;
        }
        if (this == DELAY) {
            return displayName + value;
        }
        return displayName + value;
    }
}
