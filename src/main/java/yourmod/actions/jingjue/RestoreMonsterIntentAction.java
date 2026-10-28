package yourmod.actions.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/** 意图闪完后把图标拉回来，或放出积存后重 roll 下一动。 */
public class RestoreMonsterIntentAction extends AbstractGameAction {
    private final AbstractMonster monster;
    private final boolean rollNext;

    public RestoreMonsterIntentAction(AbstractMonster monster, boolean rollNext) {
        this.monster = monster;
        this.rollNext = rollNext;
        this.actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (monster != null && !monster.isDeadOrEscaped()) {
            if (rollNext) {
                monster.rollMove();
            }
            monster.createIntent();
        }
        isDone = true;
    }
}
