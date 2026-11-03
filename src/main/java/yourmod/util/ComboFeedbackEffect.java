package yourmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.List;

/** 连携触发时的轻量屏幕反馈：三道时间流光 + 中央标题。 */
public class ComboFeedbackEffect extends AbstractGameEffect {
    private static final TextureRegion PIXEL = new TextureRegion(ImageMaster.WHITE_SQUARE_IMG);
    private final String comboName;
    private final String summary;
    private final int form;
    private final String[] queueLabels;
    private final float centerX;
    private final float centerY;
    private float age;

    public ComboFeedbackEffect(String comboName, int form, List<ComboSystem.CType> playedTypes) {
        this.comboName = comboName;
        this.form = form;
        this.summary = summaryFor(comboName, form);
        this.queueLabels = labelsFor(playedTypes);
        this.centerX = Settings.WIDTH * 0.5F;
        this.centerY = Settings.HEIGHT * 0.78F;
        this.duration = 1.45F;
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
        float t = Math.min(1.0F, age / 0.18F);
        float fade = Math.min(1.0F, Math.max(0.0F, duration / 0.28F));
        float alpha = Math.min(t, fade);
        float scale = Settings.scale;

        // 七道不同速度和角度的流光，像时间裂隙在标题背后展开。
        for (int i = 0; i < 7; i++) {
            float speed = 260.0F + i * 58.0F;
            float span = 760.0F * scale;
            float phase = (age * speed + i * 137.0F * scale) % span;
            float x = centerX - 380.0F * scale + phase;
            float y = centerY + (i - 3) * 25.0F * scale
                    + (float) Math.sin(age * 3.0F + i * 0.9F) * 7.0F * scale;
            float width = (230.0F - (i % 3) * 38.0F) * scale;
            float height = (2.5F + (i % 2)) * scale;
            float rotation = (i % 2 == 0 ? 5.0F : -4.0F);
            float lineAlpha = alpha * (0.24F + (i % 3) * 0.08F);
            sb.setColor(new Color(0.55F, 0.82F, 1.0F, lineAlpha));
            sb.draw(PIXEL, x, y, width * 0.5F, height * 0.5F,
                    width, height, 1.0F, 1.0F, rotation);
        }

        renderQueue(sb, alpha, scale);

        // 标题两侧的短光弧，给中央文字一个“被时间锁定”的轮廓。
        float pulse = 1.0F + 0.08F * (float) Math.sin(age * 8.0F);
        sb.setColor(new Color(0.75F, 0.92F, 1.0F, alpha * 0.42F));
        sb.draw(PIXEL, centerX - 245.0F * scale * pulse, centerY + 2.0F * scale,
                90.0F * scale, 2.0F * scale);
        sb.draw(PIXEL, centerX + 155.0F * scale * pulse, centerY + 2.0F * scale,
                90.0F * scale, 2.0F * scale);

        // 中央标题与摘要。标题使用青白，摘要使用淡紫，和星念视觉保持一致。
        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont,
                "COMBO · " + comboName, centerX, centerY + 18.0F * scale,
                new Color(0.82F, 0.96F, 1.0F, alpha));
        FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont,
                summary, centerX, centerY - 18.0F * scale,
                new Color(0.83F, 0.72F, 1.0F, alpha * 0.95F));
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        // 使用游戏内置纹理，无需释放资源。
    }

    private static String summaryFor(String name, int form) {
        String base;
        if ("连星".equals(name)) base = "全体伤害 5　星念 +1";
        else if ("回环".equals(name)) base = "能量 +1　星念 +2";
        else if ("溯流".equals(name)) base = "格挡 +6　目标伤害 5";
        else if ("错位".equals(name)) base = "易伤 1　虚弱 1　星念 +1";
        else if ("凝滞".equals(name)) base = "抽 1 张牌　星念 +3";
        else if ("并流".equals(name)) base = "格挡 +5　星念 +2";
        else if ("相续".equals(name)) base = "目标伤害 8　星念 +1";
        else base = "时间线已接续";
        if (form == FormUtil.SU) base += "　溯：格挡 +5";
        else if (form == FormUtil.ZHU) base += "　驻：星念 +1";
        else if (form == FormUtil.LUO) base += "　落：伤害 +3";
        return base;
    }

    private void renderQueue(SpriteBatch sb, float alpha, float scale) {
        if (queueLabels.length == 0) return;
        float y = Settings.HEIGHT * 0.61F;
        float gap = 58.0F * scale;
        float start = centerX - (queueLabels.length - 1) * gap * 0.5F;
        for (int i = 0; i < queueLabels.length; i++) {
            float x = start + i * gap;
            sb.setColor(new Color(0.10F, 0.16F, 0.28F, alpha * 0.86F));
            sb.draw(PIXEL, x - 19.0F * scale, y - 17.0F * scale,
                    38.0F * scale, 34.0F * scale);
            sb.setColor(new Color(0.48F, 0.78F, 1.0F, alpha * 0.9F));
            FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, queueLabels[i], x, y,
                    new Color(0.88F, 0.96F, 1.0F, alpha));
            if (i + 1 < queueLabels.length) {
                FontHelper.renderFontCentered(sb, FontHelper.tipBodyFont, "→", x + gap * 0.5F, y,
                        new Color(0.65F, 0.78F, 0.95F, alpha * 0.8F));
            }
        }
        sb.setColor(Color.WHITE);
    }

    private static String[] labelsFor(List<ComboSystem.CType> types) {
        ArrayList<String> labels = new ArrayList<>();
        if (types != null) {
            for (ComboSystem.CType type : types) {
                if (type == ComboSystem.CType.ATTACK) labels.add("攻");
                else if (type == ComboSystem.CType.SKILL) labels.add("技");
                else if (type == ComboSystem.CType.POWER) labels.add("能");
            }
        }
        return labels.toArray(new String[0]);
    }
}


