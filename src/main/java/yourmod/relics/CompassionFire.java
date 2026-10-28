package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.CharacterFile;
import yourmod.powers.EmberFirePower;
import yourmod.powers.ScorchingAssaultPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/**
 * 怜悯の火（初始遗物）：
 * 开战给血最高敌人 4 层烬火，并给玩家挂上燎原猛攻；
 * 击杀带烬火的敌人回复 3 生命。
 */
public class CompassionFire extends AbstractEasyRelic {
    public static final String ID = makeID("CompassionFire");
    private static final int START_EMBER = 4;
    private static final int HEAL_ON_KILL = 3;

    public CompassionFire() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, CharacterFile.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        yourmod.util.HongwenHooks.onBattleStart();
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        // 玩家获得燎原猛攻（计数从 1 开始）
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new ScorchingAssaultPower(AbstractDungeon.player)));

        // 找当前生命最高的敌人，挂 4 层烬火
        AbstractMonster target = null;
        for (AbstractMonster m : getEnemies()) {
            if (target == null || m.currentHealth > target.currentHealth) {
                target = m;
            }
        }
        if (target != null) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(target, AbstractDungeon.player,
                            new EmberFirePower(target, START_EMBER), START_EMBER));
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m != null && m.hasPower(EmberFirePower.POWER_ID)) {
            flash();
            AbstractDungeon.player.heal(HEAL_ON_KILL, true);
        }
    }
}
