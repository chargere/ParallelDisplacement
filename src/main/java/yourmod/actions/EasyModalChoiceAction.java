package yourmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/** 弹出选项卡让玩家选一张（模态选择） */
public class EasyModalChoiceAction extends SelectCardsCenteredAction {

    public EasyModalChoiceAction(ArrayList<AbstractCard> list, int amount, String textforSelect) {
        // 抉择里 addToBot 的伤害会排到关闭破绽窗口后面；把这次新加的行动挪到队列头。
        super(list, amount, textforSelect, cards -> {
            ArrayList<AbstractGameAction> q = AbstractDungeon.actionManager.actions;
            int before = q.size();
            for (AbstractCard c : cards) {
                c.onChoseThisOption();
            }
            if (q.size() > before) {
                ArrayList<AbstractGameAction> tail = new ArrayList<>(q.subList(before, q.size()));
                q.subList(before, q.size()).clear();
                q.addAll(0, tail);
            }
        });
    }

    public EasyModalChoiceAction(ArrayList<AbstractCard> list, int amount) {
        this(list, amount, "Choose.");
    }

    public EasyModalChoiceAction(ArrayList<AbstractCard> list) {
        this(list, 1, "Choose.");
    }
}
