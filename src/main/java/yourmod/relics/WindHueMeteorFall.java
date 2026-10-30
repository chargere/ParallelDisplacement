package yourmod.relics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.ComingSoonTimeCharacter;
import yourmod.ModFile;
import yourmod.powers.FourthExistencePower;
import yourmod.powers.XingNianPower;
import yourmod.util.TexLoader;

import static yourmod.ModFile.makeID;
import static yourmod.ModFile.makeRelicPath;

/**
 * 风色x落陨：时间旅人初始遗物（原初遗物）。
 * 效果敬请期待；这里只做视觉：常态常亮、自带气场的 boss 级特效——
 *  ① 中央裂纹脉冲：光从中心周期向外扩散 + 呼吸脉动（循环1 粉 / 循环2 紫）；
 *  ② 外环光晕：6 颗珠光环绕、缓慢自转，常态可见（逼格来源）；
 *  ③ 中央竖直亮纹（裂纹本体），始终微亮。
 * 渲染通过覆写 renderOutline / render，在 super() 之后叠加自定义绘制实现。
 * 相位用 System.currentTimeMillis() 计算，不依赖 update() 是否被调用。
 * 发光贴图由 WindHueGlow.png 提供（程序化生成、已校验像素，避免版本差异）。
 */
public class WindHueMeteorFall extends AbstractEasyRelic {
    public static final String ID = makeID("WindHueMeteorFall");

    /** 整段循环时长（毫秒）：前半粉、后半紫 */
    private static final float CYCLE = 2400f;

    private static final Color PINK   = new Color(1.00f, 0.41f, 0.71f, 1f); // #FF69B4
    private static final Color PURPLE = new Color(0.59f, 0.31f, 0.86f, 1f); // #974FD9

    /** 预先生成的径向发光贴图（白心透明边），懒加载 */
    private static Texture glowTex = null;

    public WindHueMeteorFall() {
        // STARTER：归「初始遗物」组（它就是时间旅人的初始遗物）。
        // 「原初遗物」专属标识 = 描述里 #p【原初遗物】 紫色前缀（BaseMod 此版无自定义分组 API）。
        super(ID, AbstractRelic.RelicTier.STARTER, AbstractRelic.LandingSound.MAGICAL,
                ComingSoonTimeCharacter.Enums.TIME_COLOR);
    }

    /** 专属被动「第四种存在」：装备时先挂一次 */
    @Override
    public void onEquip() {
        applyFourthExistence();
    }

    /** 每场战斗开始时补挂（战斗结束 Power 会被清掉） */
    @Override
    public void atBattleStart() {
        applyFourthExistence();
    }

    private void applyFourthExistence() {
        try {
            AbstractPlayer p = AbstractDungeon.player;
            if (p == null) return;
            if (!p.hasPower(FourthExistencePower.POWER_ID)) {
                FourthExistencePower pow = new FourthExistencePower(p);
                if (AbstractDungeon.actionManager != null) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, pow));
                } else {
                    p.powers.add(pow);
                }
            }
            if (!p.hasPower(XingNianPower.POWER_ID)) {
                XingNianPower xk = new XingNianPower(p, 0);
                if (AbstractDungeon.actionManager != null) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, xk));
                } else {
                    p.powers.add(xk);
                }
            }
            swapHpIcon();
        } catch (Exception ignored) { }
    }

    /** 血量图标换成星云（只有时间旅人带这件遗物，全局替换是安全的） */
    private void swapHpIcon() {
        try {
            Texture n = TexLoader.getTexture(
                    ModFile.modID + "Resources/images/ui/ExistenceNebula.png");
            if (n != null) ImageMaster.TP_HP = n;
        } catch (Exception ignored) { }
    }

    private static Texture getGlow() {
        if (glowTex == null) {
            try {
                glowTex = TexLoader.getTexture(makeRelicPath("WindHueGlow.png"));
            } catch (Exception e) {
                glowTex = null;
            }
        }
        return glowTex;
    }

    /** 顶部状态栏 & 遗物列表：renderInTopPanel -> renderOutline(SpriteBatch, boolean) */
    @Override
    public void renderOutline(SpriteBatch sb, boolean isTip) {
        super.renderOutline(sb, isTip);
        drawEffects(sb, this.currentX, this.currentY, 48f * Settings.scale);
    }

    /** 图鉴/收藏界面的三参重载，覆盖以防不显示 */
    @Override
    public void renderOutline(Color color, SpriteBatch sb, boolean isTip) {
        super.renderOutline(color, sb, isTip);
        drawEffects(sb, this.currentX, this.currentY, 48f * Settings.scale);
    }

    /** 遗物界面（领取 / 收藏大图）：relic 以 currentX,currentY 为逻辑中心 */
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        drawEffects(sb, this.currentX, this.currentY, 96f * Settings.scale);
    }

    /**
     * 在 (cx,cy) 中心、以 size 为参考尺度绘制两道特效。
     * 任何异常就地吞掉，保证底图（super 已画）始终可见，不会崩游戏。
     */
    private void drawEffects(SpriteBatch sb, float cx, float cy, float size) {
        try {
            Texture glow = getGlow();
            if (glow == null) return;

            float t = (System.currentTimeMillis() % (long) CYCLE) / CYCLE; // 0..1
            boolean pinkPhase = t < 0.5f;
            Color base = pinkPhase ? PINK : PURPLE;
            float sub = pinkPhase ? (t / 0.5f) : ((t - 0.5f) / 0.5f);
            float half = size * 0.5f;

            // ① 中央裂纹脉冲：光从中心向外扩散 + 呼吸脉动（常态常亮）
            float pulse = 0.5f + 0.5f * (float) Math.sin(sub * (float) Math.PI * 2f);
            float cr = half * (0.30f + 0.45f * sub);
            float cA = 0.45f + 0.35f * pulse;
            sb.setColor(base.r, base.g, base.b, cA);
            sb.draw(glow, cx - cr, cy - cr, cr * 2f, cr * 2f);

            // ② 外环光晕（boss 气场）：6 颗珠光环绕、缓慢自转，常态可见
            final int ORBS = 6;
            for (int i = 0; i < ORBS; i++) {
                float ang = (float) Math.toRadians(i * (360f / ORBS)) + t * (float) Math.PI * 0.6f;
                float dist = half * 0.92f;
                float ox = cx + (float) Math.cos(ang) * dist;
                float oy = cy + (float) Math.sin(ang) * dist;
                float oR = half * 0.34f;
                float oA = 0.50f + 0.30f * ((float) Math.sin(t * (float) Math.PI * 2f + i) * 0.5f + 0.5f);
                sb.setColor(base.r, base.g, base.b, oA);
                sb.draw(glow, ox - oR, oy - oR, oR * 2f, oR * 2f);
            }

            // ③ 中央竖直亮纹（裂纹本体），始终微亮
            float lineA = 0.40f + 0.25f * pulse;
            float lw = size * 0.06f;
            sb.setColor(base.r, base.g, base.b, lineA);
            sb.draw(glow, cx - lw / 2f, cy - size * 0.46f, lw, size * 0.92f);

            sb.setColor(Color.WHITE);
        } catch (Exception ignored) { }
    }
}
