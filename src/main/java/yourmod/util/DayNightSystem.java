package yourmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import yourmod.cards.comingsoon.AbstractDayNightCard;
import yourmod.ComingSoonTimeCharacter;

/** 时间旅人的战斗内昼夜时钟：每 30 秒自动翻转一次。 */
public final class DayNightSystem {
    public static final float CYCLE_SECONDS = 30.0F;
    private static final TextureRegion PIXEL = new TextureRegion(ImageMaster.WHITE_SQUARE_IMG);
    private static boolean day = true;
    private static float elapsed;

    private DayNightSystem() {
    }

    public static void reset() {
        day = true;
        elapsed = 0.0F;
        refreshPhaseCards();
    }

    public static void update() {
        if (!active()) return;
        elapsed += Gdx.graphics.getDeltaTime();
        if (elapsed >= CYCLE_SECONDS) {
            elapsed -= CYCLE_SECONDS;
            boolean wasDay = day;
            day = !day;
            refreshPhaseCards();
            AbstractDungeon.effectList.add(new DayNightTransitionEffect(wasDay, day));
        }
    }

    public static boolean isDay() {
        return day;
    }

    public static boolean isNight() {
        return !day;
    }

    public static float secondsRemaining() {
        return Math.max(0.0F, CYCLE_SECONDS - elapsed);
    }

    public static void render(SpriteBatch sb) {
        if (!active()) return;
        float scale = Settings.scale;

        // 持续的环境滤镜：昼相偏暖，夜相偏冷，透明度保持很低以免遮住战斗信息。
        sb.setColor(day ? new Color(1.0F, 0.88F, 0.62F, 0.045F)
                : new Color(0.10F, 0.16F, 0.42F, 0.085F));
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        renderAmbientParticles(sb, scale);
        renderEnergyAura(sb);

        // 向左留出完整的文字与流光边距，避免观测仪贴住屏幕右缘。
        float x = Settings.WIDTH - 382.0F * scale;
        float y = Settings.HEIGHT - 190.0F * scale;
        float w = 250.0F * scale;
        float h = 132.0F * scale;
        float cx = x + 62.0F * scale;
        float cy = y + 66.0F * scale;
        float r = 45.0F * scale;

        // 观测仪主体：比普通时钟更大，像悬在战场上的时间法则核心。
        sb.setColor(new Color(0.008F, 0.018F, 0.055F, 0.92F));
        sb.draw(PIXEL, x, y, w, h);
        Color phase = day ? new Color(0.52F, 0.94F, 1.0F, 0.95F)
                : new Color(0.72F, 0.48F, 1.0F, 0.95F);
        sb.setColor(phase);
        sb.draw(PIXEL, x, y, 4.0F * scale, h);
        drawRing(sb, cx, cy, r, phase, 32, 2.0F * scale);
        drawRing(sb, cx, cy, r - 8.0F * scale,
                day ? new Color(1.0F, 0.80F, 0.42F, 0.68F) : new Color(0.30F, 0.40F, 0.90F, 0.72F),
                24, 1.0F * scale);
        renderObserverFlow(sb, cx, cy, r, scale);
        float angle = (elapsed / CYCLE_SECONDS) * 6.2831855F - 1.5707963F;
        float handX = cx + (float) Math.cos(angle) * 35.0F * scale;
        float handY = cy + (float) Math.sin(angle) * 35.0F * scale;
        drawLine(sb, cx, cy, handX, handY, 3.0F * scale);
        sb.setColor(phase);
        sb.draw(PIXEL, cx - 4.0F * scale, cy - 4.0F * scale, 8.0F * scale, 8.0F * scale);

        Color textColor = day ? new Color(0.92F, 0.98F, 1.0F, 1.0F)
                : new Color(0.88F, 0.78F, 1.0F, 1.0F);
        FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, day ? "观测相 · 昼" : "观测相 · 夜",
                x + 122.0F * scale, y + 91.0F * scale, textColor);
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, "时间观测仪",
                x + 122.0F * scale, y + 68.0F * scale,
                new Color(0.56F, 0.72F, 0.92F, 1.0F));
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont,
                String.format("%02d 秒后相变", (int) Math.ceil(secondsRemaining())),
                x + 122.0F * scale, y + 39.0F * scale,
                new Color(0.76F, 0.86F, 0.98F, 1.0F));
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, "观测者权限 · ACTIVE",
                x + 122.0F * scale, y + 17.0F * scale,
                new Color(0.42F, 0.56F, 0.76F, 1.0F));
        sb.setColor(Color.WHITE);
    }

    private static void drawRing(SpriteBatch sb, float cx, float cy, float radius, Color color,
                                 int segments, float width) {
        sb.setColor(color);
        for (int i = 0; i < segments; i++) {
            float a1 = (i / (float) segments) * 6.2831855F;
            float a2 = ((i + 1) / (float) segments) * 6.2831855F;
            drawLine(sb, cx + (float) Math.cos(a1) * radius, cy + (float) Math.sin(a1) * radius,
                    cx + (float) Math.cos(a2) * radius, cy + (float) Math.sin(a2) * radius, width);
        }
    }

    private static void renderObserverFlow(SpriteBatch sb, float cx, float cy, float radius, float scale) {
        float t = elapsed * (day ? 1.15F : -0.92F);
        Color flow = day ? new Color(0.48F, 0.96F, 1.0F, 0.82F)
                : new Color(0.86F, 0.42F, 1.0F, 0.88F);
        // 三段不同速度的流光，绕着观测盘持续追逐。
        for (int i = 0; i < 3; i++) {
            float a = t + i * 2.0943952F;
            float a2 = a - (day ? 0.42F : 0.34F);
            float outer = radius + (7.0F + i * 2.0F) * scale;
            sb.setColor(new Color(flow.r, flow.g, flow.b, 0.16F));
            drawLine(sb, cx + (float) Math.cos(a2) * outer, cy + (float) Math.sin(a2) * outer,
                    cx + (float) Math.cos(a) * outer, cy + (float) Math.sin(a) * outer, 6.0F * scale);
            sb.setColor(new Color(flow.r, flow.g, flow.b, 0.78F));
            drawLine(sb, cx + (float) Math.cos(a2 + 0.08F) * outer, cy + (float) Math.sin(a2 + 0.08F) * outer,
                    cx + (float) Math.cos(a) * outer, cy + (float) Math.sin(a) * outer, 2.0F * scale);
        }
        // 刻度脉冲：每隔几格亮起，制造观测仪正在读取时间的感觉。
        float pulse = 0.5F + 0.5F * (float) Math.sin(elapsed * 4.0F);
        for (int i = 0; i < 12; i++) {
            float a = i * 0.5235988F - 1.5707963F;
            float inner = radius - 5.0F * scale;
            float outer = radius + (2.0F + pulse * 3.0F) * scale;
            sb.setColor(new Color(flow.r, flow.g, flow.b, (i % 3 == 0 ? 0.65F : 0.22F) + pulse * 0.18F));
            drawLine(sb, cx + (float) Math.cos(a) * inner, cy + (float) Math.sin(a) * inner,
                    cx + (float) Math.cos(a) * outer, cy + (float) Math.sin(a) * outer,
                    (i % 3 == 0 ? 2.2F : 1.0F) * scale);
        }
        sb.setColor(Color.WHITE);
    }

    private static void renderAmbientParticles(SpriteBatch sb, float scale) {
        float drift = elapsed;
        Color particleColor = day ? new Color(1.0F, 0.90F, 0.62F, 0.24F)
                : new Color(0.46F, 0.58F, 1.0F, 0.28F);
        sb.setColor(particleColor);
        for (int i = 0; i < 9; i++) {
            float x = ((i * 173.0F + drift * (day ? 9.0F : -7.0F)) % Settings.WIDTH + Settings.WIDTH) % Settings.WIDTH;
            float yBase = 150.0F * scale + (i * 97.0F * scale) % (Settings.HEIGHT - 250.0F * scale);
            float y = yBase + (float) Math.sin(drift * 1.5F + i * 1.7F) * 12.0F * scale;
            float size = (2.5F + (i % 3) * 1.25F) * scale;
            sb.draw(PIXEL, x, y, size, size);
            sb.draw(PIXEL, x - size * 1.7F, y + size * 0.45F, size * 2.4F, size * 0.35F);
        }
        sb.setColor(Color.WHITE);
    }

    private static void renderEnergyAura(SpriteBatch sb) {
        float cx = 198.0F * Settings.xScale;
        float cy = 190.0F * Settings.yScale;
        float r = 58.0F * Settings.scale;
        Color primary = day ? new Color(0.42F, 0.96F, 0.94F, 0.28F)
                : new Color(0.72F, 0.42F, 1.0F, 0.30F);
        Color accent = day ? new Color(1.0F, 0.46F, 0.78F, 0.22F)
                : new Color(1.0F, 0.42F, 0.80F, 0.24F);
        sb.setColor(primary);
        sb.draw(PIXEL, cx - r, cy + r, r * 2.0F, 3.0F * Settings.scale);
        sb.draw(PIXEL, cx - r, cy - r, r * 2.0F, 3.0F * Settings.scale);
        sb.setColor(accent);
        sb.draw(PIXEL, cx - r, cy - r, 3.0F * Settings.scale, r * 2.0F);
        sb.draw(PIXEL, cx + r - 3.0F * Settings.scale, cy - r,
                3.0F * Settings.scale, r * 2.0F);
        sb.setColor(Color.WHITE);
    }

    private static void drawLine(SpriteBatch sb, float x1, float y1, float x2, float y2, float width) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));
        sb.draw(PIXEL, x1, y1 - width * 0.5F, 0.0F, width * 0.5F,
                length, width, 1.0F, 1.0F, angle);
    }

    private static boolean active() {
        return AbstractDungeon.player instanceof ComingSoonTimeCharacter
                && Wiz.isInCombat();
    }

    private static void refreshPhaseCards() {
        if (AbstractDungeon.player == null) return;
        refreshGroup(AbstractDungeon.player.hand.group);
        refreshGroup(AbstractDungeon.player.drawPile.group);
        refreshGroup(AbstractDungeon.player.discardPile.group);
        refreshGroup(AbstractDungeon.player.exhaustPile.group);
        refreshGroup(AbstractDungeon.player.limbo.group);
    }

    private static void refreshGroup(java.util.ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            if (card instanceof AbstractDayNightCard) {
                ((AbstractDayNightCard) card).refreshPhase();
                card.flash(new Color(0.86F, 0.96F, 1.0F, 1.0F));
            }
        }
    }
}
