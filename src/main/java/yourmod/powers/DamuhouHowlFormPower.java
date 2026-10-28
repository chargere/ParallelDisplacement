package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 啸罗形态 */
public class DamuhouHowlFormPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouHowlForm");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouHowlFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("demonform");
    }

    @Override
    public void onAfterUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK || AbstractDungeon.player == null) {
            return;
        }
        flash();
        addToBot(new DrawCardAction(owner, amount));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (AbstractDungeon.player != null) {
                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (c.type == AbstractCard.CardType.ATTACK && c.costForTurn >= 0) {
                            c.setCostForTurn(Math.max(0, c.costForTurn - 1));
                        }
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
