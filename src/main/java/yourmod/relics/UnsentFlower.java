package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;
import yourmod.powers.FavorMarkPower;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/**
 * 未送出的花（罕见）：开战随机敌人眷顾；打眷顾目标额外 3 伤并回 1，每回合最多 3 次。
 */
public class UnsentFlower extends AbstractEasyRelic {
    public static final String ID = makeID("UnsentFlower");
    private static final int MAX_PROCS = 3;
    private int procsThisTurn;

    public UnsentFlower() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL, CharacterFile.Enums.TODO_COLOR);
    }

    public boolean canProcBonus() {
        return procsThisTurn < MAX_PROCS;
    }

    @Override
    public void atBattleStart() {
        procsThisTurn = 0;
        AbstractMonster target = Wiz.getRandomEnemy();
        if (target == null) {
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(target, this));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(target, AbstractDungeon.player, new FavorMarkPower(target), 1));
    }

    @Override
    public void atTurnStart() {
        procsThisTurn = 0;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount <= 0 || target == null || !target.hasPower(FavorMarkPower.POWER_ID)) {
            return;
        }
        // Avoid double-counting: FavorMark already added +3 in atDamageReceive;
        // heal once per successful bonus hit.
        if (procsThisTurn >= MAX_PROCS) {
            return;
        }
        // Only count hits that actually dealt damage after the bonus window
        if (!(info != null && info.type == DamageInfo.DamageType.NORMAL)) {
            return;
        }
        procsThisTurn++;
        flash();
        AbstractDungeon.player.heal(1, true);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UnsentFlower();
    }
}
