package yourmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import yourmod.powers.MemoryBurnoutPower;

/**
 * 星灰复制入手：在加入前写好虚无/消耗词条，避免再 copy 丢中文词条。
 */
public class MakeAshCardInHandAction extends AbstractGameAction {
    private final AbstractCard source;
    private boolean did;

    public MakeAshCardInHandAction(AbstractCard source) {
        this.source = source;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (!did) {
            did = true;
            AbstractCard c = source.makeStatEquivalentCopy();
            MemoryBurnoutPower.applyAshModifiers(c);
            UnlockTracker.markCardAsSeen(c.cardID);

            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c,
                        Settings.WIDTH / 2.0F - MathUtils.random(-200.0F, 200.0F) * Settings.scale,
                        Settings.HEIGHT / 2.0F));
            } else {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
            }
        }
        tickDuration();
    }
}
