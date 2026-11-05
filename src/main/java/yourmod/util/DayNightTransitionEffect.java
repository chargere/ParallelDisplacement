package yourmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/** 昼夜翻转的全屏高光：昼切夜为黑，夜切昼为白。 */
public class DayNightTransitionEffect extends AbstractGameEffect {
    private final boolean toDay;
    private float age;

    public DayNightTransitionEffect(boolean wasDay, boolean nowDay) {
        this.toDay = nowDay;
        this.duration = 0.90F;
        this.age = 0.0F;
    }

    @Override
    public void update() {
        age += Gdx.graphics.getDeltaTime();
        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0.0F) isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        float pulse = (float) Math.sin(Math.min(1.0F, age / 0.90F) * Math.PI);
        float alpha = pulse * (toDay ? 0.18F : 0.24F);
        Color color = toDay ? new Color(1.0F, 0.98F, 0.88F, alpha)
                : new Color(0.01F, 0.015F, 0.045F, alpha);
        sb.setColor(color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
        renderTransitionParticles(sb, pulse);
        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, "形态变化",
                Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.62F,
                new Color(toDay ? 0.35F : 0.78F, toDay ? 0.28F : 0.68F,
                        toDay ? 0.18F : 1.0F, Math.min(0.85F, alpha + 0.28F)));
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, toDay ? "昼相" : "夜相",
                Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.57F,
                new Color(toDay ? 0.50F : 0.86F, toDay ? 0.42F : 0.76F,
                        toDay ? 0.24F : 1.0F, Math.min(0.8F, alpha + 0.18F)));
        sb.setColor(Color.WHITE);
    }

    private void renderTransitionParticles(SpriteBatch sb, float pulse) {
        float scale = Settings.scale;
        Color particleColor = toDay ? new Color(1.0F, 0.95F, 0.78F, pulse * 0.72F)
                : new Color(0.46F, 0.58F, 1.0F, pulse * 0.68F);
        sb.setColor(particleColor);
        float cx = Settings.WIDTH * 0.5F;
        float cy = Settings.HEIGHT * 0.62F;
        for (int i = 0; i < 26; i++) {
            float angle = i * 0.31415927F + (toDay ? pulse : -pulse) * 1.4F;
            float radius = (34.0F + i * 17.0F) * scale * (0.35F + pulse * 0.95F);
            float x = cx + (float) Math.cos(angle) * radius;
            float y = cy + (float) Math.sin(angle) * radius * 0.56F;
            float size = (2.0F + (i % 4) * 1.15F) * scale;
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, x, y, size, size);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, x - size * 1.8F, y + size * 0.45F,
                    size * 2.6F, size * 0.34F);
        }
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
    }
}
