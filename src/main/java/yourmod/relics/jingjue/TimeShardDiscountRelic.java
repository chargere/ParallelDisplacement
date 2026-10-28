package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.relics.AbstractEasyRelic;

import static yourmod.ModFile.makeID;

public class TimeShardDiscountRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjTimeShardDiscount");
    private boolean used;

    public TimeShardDiscountRelic() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atTurnStart() {
        used = false;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (!used && c.costForTurn >= 2) {
                c.setCostForTurn(c.costForTurn - 1);
            }
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (!used && card.costForTurn >= 2) {
            card.setCostForTurn(card.costForTurn - 1);
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        if (c.cost >= 2) {
            used = true;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new TimeShardDiscountRelic();
    }
}
