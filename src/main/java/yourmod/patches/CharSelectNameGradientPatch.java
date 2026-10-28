package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import yourmod.CharacterFile;
import yourmod.DamuhouCharacter;
import yourmod.DominicCharacter;
import yourmod.FvvEggCharacter;
import yourmod.ComingSoonTimeCharacter;
import yourmod.JingjueCharacter;
import yourmod.util.HongwenNameFx;

/**
 * 选人界面：替换 bannerNameFont 绘制。
 * 鸿文：黄底红灯带；多米尼克：蓝底炫彩；fvv蛋蛋：白底黄流光呼吸；战斗精英：粉底紫流光呼吸；精绝王者：紫黑底炫彩；时间旅人：赛博乱码，时人/查斯成对减速。
 */
@SpirePatch(clz = CharacterOption.class, method = "renderInfo")
public class CharSelectNameGradientPatch {

    public static boolean isHongwen(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == CharacterFile.Enums.THE_TODO;
    }

    public static boolean isDominic(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == DominicCharacter.Enums.THE_DOMINIC;
    }

    public static boolean isFvv(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == FvvEggCharacter.Enums.THE_FVV;
    }

    public static boolean isDamuhou(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == DamuhouCharacter.Enums.THE_DAMUHOU;
    }

    public static boolean isJingjue(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == JingjueCharacter.Enums.THE_JINGJUE;
    }

    public static boolean isTimeTraveler(CharacterOption opt) {
        return opt != null
                && opt.selected
                && !opt.locked
                && opt.c != null
                && opt.c.chosenClass == ComingSoonTimeCharacter.Enums.THE_TIME;
    }

    /** 桌面：renderSmartText(sb, font, text, x, y, w, h, color) */
    public static void drawDesktop(CharacterOption opt,
                                   com.badlogic.gdx.graphics.g2d.SpriteBatch sb,
                                   com.badlogic.gdx.graphics.g2d.BitmapFont font,
                                   String text, float x, float y,
                                   float lineWidth, float lineSpacing,
                                   com.badlogic.gdx.graphics.Color color) {
        if (font == FontHelper.bannerNameFont && isHongwen(opt)) {
            HongwenNameFx.renderBreathingGradientName(sb, font, text, x, y, lineWidth, lineSpacing);
        } else if (font == FontHelper.bannerNameFont && isDominic(opt)) {
            HongwenNameFx.renderDominicTitle(sb, font, text, x, y, lineWidth, lineSpacing);
        } else if (font == FontHelper.bannerNameFont && isFvv(opt)) {
            HongwenNameFx.renderFvvTitle(sb, font, text, x, y, lineWidth, lineSpacing);
        } else if (font == FontHelper.bannerNameFont && isDamuhou(opt)) {
            HongwenNameFx.renderDamuhouTitle(sb, font, text, x, y, lineWidth, lineSpacing);
        } else if (font == FontHelper.bannerNameFont && isJingjue(opt)) {
            HongwenNameFx.renderJingjueTitle(sb, font, text, x, y, lineWidth, lineSpacing);
        } else if (font == FontHelper.bannerNameFont && isTimeTraveler(opt)) {
            HongwenNameFx.renderTimeTravelerTitle(sb, font, text, x, y, lineWidth, lineSpacing);
        } else {
            FontHelper.renderSmartText(sb, font, text, x, y, lineWidth, lineSpacing, color);
        }
    }

    /** 手机：多一个 scale 参数 */
    public static void drawMobile(CharacterOption opt,
                                  com.badlogic.gdx.graphics.g2d.SpriteBatch sb,
                                  com.badlogic.gdx.graphics.g2d.BitmapFont font,
                                  String text, float x, float y,
                                  float lineWidth, float lineSpacing,
                                  com.badlogic.gdx.graphics.Color color, float scale) {
        if (font == FontHelper.bannerNameFont && isHongwen(opt)) {
            HongwenNameFx.renderBreathingGradientName(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else if (font == FontHelper.bannerNameFont && isDominic(opt)) {
            HongwenNameFx.renderDominicTitle(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else if (font == FontHelper.bannerNameFont && isFvv(opt)) {
            HongwenNameFx.renderFvvTitle(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else if (font == FontHelper.bannerNameFont && isDamuhou(opt)) {
            HongwenNameFx.renderDamuhouTitle(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else if (font == FontHelper.bannerNameFont && isJingjue(opt)) {
            HongwenNameFx.renderJingjueTitle(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else if (font == FontHelper.bannerNameFont && isTimeTraveler(opt)) {
            HongwenNameFx.renderTimeTravelerTitle(sb, font, text, x, y, lineWidth, lineSpacing, scale);
        } else {
            FontHelper.renderSmartText(sb, font, text, x, y, lineWidth, lineSpacing, color, scale);
        }
    }

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (!m.getClassName().equals(FontHelper.class.getName())
                        || !m.getMethodName().equals("renderSmartText")) {
                    return;
                }
                String sig = m.getSignature();
                if (sig.contains("FFFFLcom/badlogic/gdx/graphics/Color;)V")
                        && !sig.contains("FFFFLcom/badlogic/gdx/graphics/Color;F)V")) {
                    m.replace("{ "
                            + yourmod.patches.CharSelectNameGradientPatch.class.getName()
                            + ".drawDesktop(this, $1, $2, $3, $4, $5, $6, $7, $8); }");
                } else if (sig.contains("FFFFLcom/badlogic/gdx/graphics/Color;F)V")) {
                    m.replace("{ "
                            + yourmod.patches.CharSelectNameGradientPatch.class.getName()
                            + ".drawMobile(this, $1, $2, $3, $4, $5, $6, $7, $8, $9); }");
                }
            }
        };
    }
}
