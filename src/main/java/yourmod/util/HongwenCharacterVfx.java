package yourmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import yourmod.CharacterFile;

/** 鸿文君主在战斗与事件场景中的轻量程序化炎雷特效。 */
public final class HongwenCharacterVfx {
    private static final TextureRegion PIXEL = new TextureRegion(ImageMaster.WHITE_SQUARE_IMG);
    private static float time;
    private static boolean effectQueued;

    private HongwenCharacterVfx() {
    }

    public static void ensureEffect() {
        if (!effectQueued && Wiz.isInCombat()
                && com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof CharacterFile) {
            com.megacrit.cardcrawl.dungeons.AbstractDungeon.topLevelEffects.add(new HongwenCharacterVfxEffect());
            effectQueued = true;
        }
    }

    static void finishEffect() {
        effectQueued = false;
    }

    /** 连续待机姿态：使用角色自身的动画偏移，避免 SCML 关键帧跳变。 */
    public static void updatePose() {
        if (!(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player instanceof CharacterFile)
                || !CardCrawlGame.isInARun()) return;
        AbstractPlayer player = (AbstractPlayer) com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        float t = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        time += t;
        player.animX = (float) Math.sin(time * 1.35F) * 0.8F * Settings.scale;
        player.animY = (float) Math.sin(time * 2.7F) * 1.2F * Settings.scale;
    }

    public static void render(AbstractPlayer player, SpriteBatch sb) {
        if (!(player instanceof CharacterFile) || !CardCrawlGame.isInARun()) return;

        time += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        float s = Settings.scale;
        float x = player.hb.cX;
        float y = player.hb.cY;
        float pulse = 0.5F + 0.5F * (float) Math.sin(time * 2.6F);

        // 匕首附近的短促火星。
        Color ember = new Color(1.0F, 0.48F, 0.16F, 0.82F + pulse * 0.16F);
        sb.setColor(ember);
        for (int i = 0; i < 5; i++) {
            float phase = time * (1.8F + i * 0.11F) + i * 1.4F;
            float px = x + (58.0F + (float) Math.cos(phase) * 12.0F) * s;
            float py = y + (25.0F + (float) Math.sin(phase * 1.4F) * 13.0F) * s;
            float size = (3.0F + (i % 2) * 1.2F) * s;
            sb.draw(PIXEL, px, py, size, size);
        }

        // 折叠刀的低频高光：短促、克制，不遮挡角色立绘。
        float glint = (float) Math.pow(Math.max(0.0F, Math.sin(time * 1.35F)), 10.0F);
        if (glint > 0.02F) {
            Color blade = new Color(1.0F, 0.88F, 0.52F, glint * 0.9F);
            sb.setColor(blade);
            float bx = x + 72.0F * s;
            float by = y + 28.0F * s;
            sb.draw(PIXEL, bx, by, 18.0F * s * glint, 1.6F * s);
        }

        // 角色周围的微弱呼吸光，模拟待机时的存在感。
        Color crest = new Color(1.0F, 0.72F, 0.28F, 0.08F + pulse * 0.07F);
        sb.setColor(crest);
        float r = (17.0F + pulse * 4.0F) * s;
        sb.draw(PIXEL, x - r, y + 62.0F * s, r * 2.0F, 2.0F * s);
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
}
