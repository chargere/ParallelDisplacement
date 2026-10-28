package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.DamuhouCombatState;

import static yourmod.ModFile.makeID;

public class DamuhouPremonitionPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("DamuhouPremonition");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DamuhouPremonitionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
        useVanillaIcon("darkembrace");
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new ScryAction(amount));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!AbstractDungeon.player.drawPile.isEmpty()) {
                    AbstractCard top = AbstractDungeon.player.drawPile.getTopCard();
                    if (top.type == AbstractCard.CardType.ATTACK) {
                        DamuhouCombatState.thisTurnFirstAttackCombo += 1;
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
