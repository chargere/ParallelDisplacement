package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 熔炉之息（罕见）：每打出 4 张攻击，全体 2 烬火 + 1 静电（跨回合累计）。 */
public class FurnaceBreath extends AbstractEasyRelic {
    public static final String ID = makeID("FurnaceBreath");
    private static final int THRESHOLD = 4;

    public FurnaceBreath() {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY, CharacterFile.Enums.TODO_COLOR);
        counter = 0;
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        if (c == null || c.type != AbstractCard.CardType.ATTACK) {
            return;
        }
        counter++;
        if (counter >= THRESHOLD) {
            counter -= THRESHOLD;
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new RelicAboveCreatureAction(AbstractDungeon.player, this));
            for (AbstractMonster mo : Wiz.getEnemies()) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(mo, AbstractDungeon.player, new EmberFirePower(mo, 2), 2));
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(mo, AbstractDungeon.player, new StaticMarkPower(mo, 1), 1));
            }
        }
    }

    @Override
    public void onVictory() {
        counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FurnaceBreath();
    }
}
