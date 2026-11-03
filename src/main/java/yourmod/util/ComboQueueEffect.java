package yourmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

/** 持续显示本场战斗中尚未被连携消费的牌型队列。 */
public class ComboQueueEffect extends AbstractGameEffect {
    private static final TextureRegion PIXEL = new TextureRegion(ImageMaster.WHITE_SQUARE_IMG);
    private final ArrayList<ComboSystem.CType> queue = new ArrayList<>();
    private float pulse;

    public ComboQueueEffect() {
        this.duration = -1.0F;
        this.pulse = 0.0F;
    }

    public void setQueue(List<ComboSystem.CType> types) {
        queue.clear();
        if (types != null) queue.addAll(types);
    }

    public void clear() {
        queue.clear();
        isDone = true;
    }

    @Override
    public void update() {
        pulse += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch sb) {
        if (queue.isEmpty()) return;
        float scale = Settings.scale;
        float alpha = 0.82F + 0.10F * (float) Math.sin(pulse * 4.0F);
        float y = Settings.HEIGHT * 0.61F;
        float gap = 58.0F * scale;
        float start = Settings.WIDTH * 0.5F - (queue.size() - 1) * gap * 0.5F;

        for (int i = 0; i < queue.size(); i++) {
            float x = start + i * gap;
            Color fill = colorFor(queue.get(i), alpha * 0.82F);
            sb.setColor(fill);
            sb.draw(PIXEL, x - 20.0F * scale, y - 17.0F * scale,
                    40.0F * scale, 34.0F * scale);
            FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont,
                    labelFor(queue.get(i)), x, y,
                    new Color(0.90F, 0.97F, 1.0F, alpha));
            if (i + 1 < queue.size()) {
                FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, "→",
                        x + gap * 0.5F, y,
                        new Color(0.62F, 0.80F, 0.98F, alpha * 0.86F));
            }
        }
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        // 使用游戏内置纹理，无需释放资源。
    }

    private static String labelFor(ComboSystem.CType type) {
        if (type == ComboSystem.CType.ATTACK) return "攻";
        if (type == ComboSystem.CType.SKILL) return "技";
        return "能";
    }

    private static Color colorFor(ComboSystem.CType type, float alpha) {
        if (type == ComboSystem.CType.ATTACK) return new Color(0.56F, 0.22F, 0.38F, alpha);
        if (type == ComboSystem.CType.SKILL) return new Color(0.16F, 0.36F, 0.56F, alpha);
        return new Color(0.38F, 0.25F, 0.58F, alpha);
    }
}
