package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.CharacterFile;
import yourmod.powers.EmberFirePower;
import yourmod.powers.ScorchingAssaultPower;
import yourmod.util.TexLoader;

import static yourmod.ModFile.makeID;
import static yourmod.ModFile.makeRelicPath;
import static yourmod.util.Wiz.getEnemies;

/**
 * 无人怜悯（Boss 遗物，替换怜悯の火）：
 * 开战全体 6 层烬火 + 挂上燎原猛攻；
 * 击杀带烬火敌人：回复 3 生命并获得 1 点力量；
 * 若场上没有任何烬火，回合结束失去 3 点生命。
 */
public class NoMercy extends AbstractEasyRelic {
    public static final String ID = makeID("NoMercy");
    private static final int START_EMBER = 6;
    private static final int HEAL_ON_KILL = 3;
    private static final int STR_ON_KILL = 1;
    private static final int HP_LOSS_NO_EMBER = 3;

    /** 流光动画：预渲染帧 + 每帧时长(ms) */
    private static final int NM_FRAMES = 28;
    private static final long NM_FRAME_MS = 70L;
    private static Texture[] nmFrames = null;

    public NoMercy() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, CharacterFile.Enums.TODO_COLOR);
    }

    /** 懒加载 28 张流光帧（hongwenResources/images/relics/NoMercy_00..27.png） */
    private static void ensureNmFrames() {
        if (nmFrames == null) {
            nmFrames = new Texture[NM_FRAMES];
            for (int i = 0; i < NM_FRAMES; i++) {
                nmFrames[i] = TexLoader.getTexture(makeRelicPath("NoMercy_" + String.format("%02d", i) + ".png"));
            }
        }
    }

    /** 按当前时间把 this.img 切到流光帧；所有渲染点都从 img 取纹理，故一处切换全局生效 */
    private void applyNmFrame() {
        ensureNmFrames();
        int idx = (int) ((System.currentTimeMillis() / NM_FRAME_MS) % NM_FRAMES);
        this.img = nmFrames[idx];
    }

    // 覆盖全部渲染入口：顶部面板 / hover 提示 / Boss 大图 / 奖励列表 / 商店列表
    // 每处绘制前都把 this.img 切到当前流光帧，确保「任何时候、任何页面」都在流动
    @Override
    public void render(SpriteBatch sb) {
        applyNmFrame();
        super.render(sb);
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        applyNmFrame();
        super.renderInTopPanel(sb);
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        applyNmFrame();
        super.renderTip(sb);
    }

    @Override
    public void renderBossTip(SpriteBatch sb) {
        applyNmFrame();
        super.renderBossTip(sb);
    }

    @Override
    public void render(SpriteBatch sb, boolean isWithinAgent, Color tintColor) {
        applyNmFrame();
        super.render(sb, isWithinAgent, tintColor);
    }

    @Override
    public void renderWithoutAmount(SpriteBatch sb, Color tintColor) {
        applyNmFrame();
        super.renderWithoutAmount(sb, tintColor);
    }

    /** 仅在仍持有怜悯の火时进入 Boss 池（同原版黑血） */
    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null
                && AbstractDungeon.player.hasRelic(CompassionFire.ID);
    }

    /** 作为怜悯の火的强化版：获得时直接替换初始遗物 */
    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(CompassionFire.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(CompassionFire.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    return;
                }
            }
        }
        super.obtain();
    }

    /**
     * Boss 宝箱确认时走此路径（非原版四件套硬编码 ID）。
     * 显式走 {@link #obtain()} 完成替换，避免只标 isObtained 却没替换初始遗物。
     */
    @Override
    public void bossObtainLogic() {
        obtain();
        isObtained = true;
    }

    @Override
    public void atBattleStart() {
        yourmod.util.HongwenHooks.onBattleStart();
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new ScorchingAssaultPower(AbstractDungeon.player)));

        for (AbstractMonster m : getEnemies()) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(m, AbstractDungeon.player,
                            new EmberFirePower(m, START_EMBER), START_EMBER));
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m != null && m.hasPower(EmberFirePower.POWER_ID)) {
            flash();
            AbstractDungeon.player.heal(HEAL_ON_KILL, true);
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new StrengthPower(AbstractDungeon.player, STR_ON_KILL), STR_ON_KILL));
        }
    }

    @Override
    public void onPlayerEndTurn() {
        boolean anyEmber = false;
        for (AbstractMonster m : getEnemies()) {
            if (m.hasPower(EmberFirePower.POWER_ID)) {
                anyEmber = true;
                break;
            }
        }
        if (!anyEmber) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                    new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, HP_LOSS_NO_EMBER));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NoMercy();
    }
}
