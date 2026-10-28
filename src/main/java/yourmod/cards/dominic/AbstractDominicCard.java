package yourmod.cards.dominic;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.DominicCharacter;
import yourmod.actions.FrostPayGateAction;
import yourmod.cards.AbstractEasyCard;

import java.util.ArrayList;
import java.util.List;

import static yourmod.util.Wiz.atb;

/**
 * 多米尼克卡牌基类：默认 DOMINIC_COLOR，支持霜偿。
 */
public abstract class AbstractDominicCard extends AbstractEasyCard {
    /** 霜偿消耗的霜寒层数；0 表示无霜偿 */
    public int frostPayCost;
    public FrostPayType frostPayType;
    /** 冻裂/归墟的数值 X */
    public int frostPayValue;

    public AbstractDominicCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, rarity, target, DominicCharacter.Enums.DOMINIC_COLOR);
    }

    protected void setFrostPay(int cost, FrostPayType type, int value) {
        this.frostPayCost = cost;
        this.frostPayType = type;
        this.frostPayValue = value;
    }

    protected void setFrostPay(int cost, FrostPayType type) {
        setFrostPay(cost, type, 1);
    }

    /** 悬停时强制展示霜偿与对应代价说明（避免「霜偿2：迟滞」粘连导致关键词不亮） */
    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> tips = new ArrayList<>();
        if (frostPayCost > 0 && frostPayType != null) {
            tips.add(new TooltipInfo(
                    "霜偿",
                    "强力效果的账单。打出时选择：消耗 "
                            + frostPayCost
                            + " 层霜寒取消代价，或承受下方列出的代价。"
                            + " 也可按住 Shift 打牌，直接耗霜寒取消。"));
            tips.add(new TooltipInfo(
                    frostPayType.displayName,
                    frostPayType.detail(frostPayValue)));
        }
        return tips;
    }

    @Override
    public final void use(AbstractPlayer p, AbstractMonster m) {
        atb(new FrostPayGateAction(this, p, m, () -> useAfterFrostPay(p, m)));
    }

    /** 霜偿结算后的实际效果 */
    protected abstract void useAfterFrostPay(AbstractPlayer p, AbstractMonster m);
}
