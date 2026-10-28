package yourmod.util;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.DamuhouCharacter;
import yourmod.relics.PrismOpticSystem;

import java.util.ArrayList;

/**
 * 战斗精英火控：右上角点按切换 half auto / full auto。
 */
public final class DamuhouFireMode {
    public static final String HALF_LABEL = "half auto";
    public static final String FULL_LABEL = "full auto";

    /** 避开图鉴 / 设置，贴在它们下方靠右。 */
    private static final float RIGHT_PAD = 28f;
    private static final float TOP_PAD = 136f;
    private static final float FONT_SCALE = 0.90f;
    private static final float FONT_SCALE_HOVER = 0.96f;

    public static boolean fullAuto = false;

    private static final Hitbox hb = new Hitbox(1f, 1f);
    private static final GlyphLayout gl = new GlyphLayout();

    private DamuhouFireMode() {
    }

    public static boolean isFullAuto() {
        return fullAuto;
    }

    public static boolean isHalfAuto() {
        return !fullAuto;
    }

    public static void resetToHalf() {
        fullAuto = false;
    }

    public static void toggle() {
        fullAuto = !fullAuto;
        CardCrawlGame.sound.play("UI_CLICK_1");
    }

    public static boolean active() {
        return AbstractDungeon.player instanceof DamuhouCharacter;
    }

    /** 红点 / 未选：最左。倍镜：最右。 */
    public static AbstractMonster sightedLiving() {
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.removeIf(m -> m == null || m.halfDead);
        if (living.isEmpty()) {
            return null;
        }
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        PrismOpticSystem relic = PrismOpticSystem.get();
        if (relic != null && relic.mode == PrismOpticSystem.Mode.SCOPE) {
            return living.get(living.size() - 1);
        }
        return living.get(0);
    }

    public static AbstractMonster leftmostLiving() {
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.removeIf(m -> m == null || m.halfDead);
        if (living.isEmpty()) {
            return null;
        }
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        return living.get(0);
    }

    public static AbstractMonster rightmostLiving() {
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.removeIf(m -> m == null || m.halfDead);
        if (living.isEmpty()) {
            return null;
        }
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        return living.get(living.size() - 1);
    }

    private static BitmapFont font() {
        return FontHelper.cardTitleFont;
    }

    private static float[] layout() {
        BitmapFont font = font();
        String label = fullAuto ? FULL_LABEL : HALF_LABEL;
        float prev = font.getData().scaleX;
        font.getData().setScale(hb.hovered ? FONT_SCALE_HOVER : FONT_SCALE);
        gl.setText(font, label);
        float w = gl.width;
        float h = gl.height;
        font.getData().setScale(prev);
        float right = Settings.WIDTH - RIGHT_PAD * Settings.scale;
        float y = Settings.HEIGHT - TOP_PAD * Settings.scale;
        return new float[]{right - w, y, w, h};
    }

    public static void update() {
        if (!active()) {
            return;
        }
        float[] box = layout();
        hb.resize(box[2] + 18f * Settings.scale, box[3] + 14f * Settings.scale);
        hb.move(box[0] + box[2] / 2f, box[1] - box[3] / 2f);
        hb.update();
        if (InputHelper.justClickedLeft && hb.hovered) {
            hb.clickStarted = true;
        }
        if (hb.clicked) {
            hb.clicked = false;
            InputHelper.justClickedLeft = false;
            toggle();
        }
    }

    public static void render(SpriteBatch sb) {
        if (!active()) {
            return;
        }
        String label = fullAuto ? FULL_LABEL : HALF_LABEL;
        float[] box = layout();
        HongwenNameFx.renderFireModeLabel(sb, font(), label, box[0], box[1],
                hb.hovered ? FONT_SCALE_HOVER : FONT_SCALE, fullAuto);
        hb.render(sb);
    }
}
