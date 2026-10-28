package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;
import yourmod.cards.derived.RainSound;

import static yourmod.ModFile.makeID;

/** 雨天的约定（稀有）：抽到状态/诅咒时转化为「雨声」。 */
public class RainyPromise extends AbstractEasyRelic {
    public static final String ID = makeID("RainyPromise");

    public RainyPromise() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, CharacterFile.Enums.TODO_COLOR);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (drawnCard == null) {
            return;
        }
        if (drawnCard.type != AbstractCard.CardType.STATUS && drawnCard.type != AbstractCard.CardType.CURSE) {
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new RainSound(), 1));
        AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(drawnCard, AbstractDungeon.player.hand));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RainyPromise();
    }
}
