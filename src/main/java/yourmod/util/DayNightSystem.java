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

        float x = Settings.WIDTH - 194.0F * scale;
        float y = Settings.HEIGHT - 112.0F * scale;
        float w = 166.0F * scale;
        float h = 58.0F * scale;

        sb.setColor(new Color(0.015F, 0.035F, 0.09F, 0.86F));
        sb.draw(PIXEL, x, y, w, h);
        sb.setColor(day ? new Color(0.92F, 0.95F, 1.0F, 0.9F)
                : new Color(0.53F, 0.42F, 0.80F, 0.9F));
        sb.draw(PIXEL, x, y, 3.0F * scale, h);

        // 小钟：外框、十二点标记和随时间转动的指针。
        float cx = x + 29.0F * scale;
        float cy = y + 29.0F * scale;
        float r = 17.0F * scale;
        sb.setColor(day ? new Color(0.78F, 0.90F, 1.0F, 0.88F)
                : new Color(0.66F, 0.57F, 0.92F, 0.88F));
        sb.draw(PIXEL, cx - r, cy + r, 2.0F * r, 2.0F * scale);
        sb.draw(PIXEL, cx - r, cy - r, 2.0F * r, 2.0F * scale);
        sb.draw(PIXEL, cx - r, cy - r, 2.0F * scale, 2.0F * r);
        sb.draw(PIXEL, cx + r - 2.0F * scale, cy - r, 2.0F * scale, 2.0F * r);
        float angle = (elapsed / CYCLE_SECONDS) * 6.2831855F - 1.5707963F;
        float handX = cx + (float) Math.cos(angle) * 12.0F * scale;
        float handY = cy + (float) Math.sin(angle) * 12.0F * scale;
        drawLine(sb, cx, cy, handX, handY, 2.0F * scale);

        Color textColor = day ? new Color(0.90F, 0.97F, 1.0F, 1.0F)
                : new Color(0.82F, 0.74F, 1.0F, 1.0F);
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont, day ? "昼相" : "夜相",
                x + 56.0F * scale, y + 36.0F * scale, textColor);
        FontHelper.renderFontLeft(sb, FontHelper.tipBodyFont,
                String.format("%02d 秒后切换", (int) Math.ceil(secondsRemaining())),
                x + 56.0F * scale, y + 14.0F * scale,
                new Color(0.66F, 0.78F, 0.92F, 1.0F));
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
