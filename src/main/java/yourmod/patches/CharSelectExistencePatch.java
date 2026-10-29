package yourmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import yourmod.ComingSoonTimeCharacter;
import yourmod.ModFile;
import yourmod.util.HongwenNameFx;
import yourmod.util.TexLoader;

/**
 * 选人界面：时间旅人的血量行改为「存在力：N」，血量图标换成星云，并写入背景故事。
 *
 * 原版两处都在 renderInfo 里（render() 内联调用它）：
 * - HP 行：renderSmartText(TEXT[4] + hp)，PC 端固定 infoY + 102 分支，基线坐标；
 * - flavor 行：renderSmartText(flavorText)，行宽写死 10000（=不换行），
 *   所以长文案会单行溢出屏幕 —— 这一行由本 patch 接管并自行折行。
 * 做法与副标题特效一致：Prefix 让原版画空串，Postfix 用自己的渲染接管，
 * 渲染完立刻复原字段，避免影响其他角色。
 */
@SpirePatch(clz = CharacterOption.class, method = "render")
public class CharSelectExistencePatch {

    private static final String EXISTENCE_LABEL = "存在力：";
    /** 时间旅人背景故事（选人界面 flavor）：固定两行，每句各占一行，字体自适应缩放保证不折行不溢出。 */
    private static final String FLAVOR_LINE1 =
            "行走在时间的夹层里，拾取被遗忘的片刻，也注视尚未发生的可能。";
    private static final String FLAVOR_LINE2 =
            "来尖塔不为征服，也不为逃离——只是想看看，生命一次次跌落又出发时，「存在」会不会显露出意义。";
    private static final String NEBULA_PATH =
            ModFile.modID + "Resources/images/ui/ExistenceNebula.png";

    /** 原版 renderSmartText 里 HP 行的偏移（PC 分支，基线坐标） */
    private static final float HP_TEXT_DX = 18.0f;
    private static final float HP_TEXT_DY = 102.0f;
    /** flavor 行：沿用原版偏移；宽度放宽到面板实际可用宽度（默认字号下尽量少折行） */
    private static final float FLAVOR_DX = 26.0f;
    /** 起始上移：默认字号下会占 3~4 行，起点抬高才不会压到下方的遗物名/描述区 */
    private static final float FLAVOR_DY = 64.0f;
    private static final float FLAVOR_WIDTH = 640.0f;
    private static final float FLAVOR_SPACING = 24.0f;
    /** flavor 文字颜色：淡青（贴合时间旅人主题，避免白字突兀） */
    private static final Color FLAVOR_COLOR = new Color(0.55f, 0.90f, 1.00f, 1.00f);
    /** 渲染 flavor 时给 renderSmartText 的行宽：对齐原版单行 flavor 的实际延伸宽度（~920px） */
    private static final float FLAVOR_WRAP_WIDTH = 1400.0f;

    private static String vanillaText4 = null;
    private static String vanillaFlavor = null;
    private static Texture vanillaHpIcon = null;
    private static Texture nebula = null;

    private static Texture getNebula() {
        if (nebula == null) {
            try {
                nebula = TexLoader.getTexture(NEBULA_PATH);
            } catch (Exception e) {
                nebula = null;
            }
        }
        return nebula;
    }

    private static boolean isTimeTraveler(CharacterOption opt) {
        return opt != null && opt.c != null
                && opt.c.chosenClass == ComingSoonTimeCharacter.Enums.THE_TIME;
    }

    public static void Prefix(CharacterOption __instance, SpriteBatch sb) {
        try {
            if (vanillaText4 == null) vanillaText4 = CharacterOption.TEXT[4];
            if (vanillaHpIcon == null) vanillaHpIcon = ImageMaster.TP_HP;
            if (!isTimeTraveler(__instance)) return;

            CharacterOption.TEXT[4] = "";              // 去掉原「HP:」前缀
            Texture n = getNebula();
            if (n != null) ImageMaster.TP_HP = n;      // 血量图标 -> 星云
            setString(__instance, "hp", "");           // HP 行画空，特效由 Postfix 接管
            if (vanillaFlavor == null) {
                vanillaFlavor = getString(__instance, "flavorText");
            }
            setString(__instance, "flavorText", "");   // flavor 行画空，由 Postfix 折行渲染
        } catch (Exception ignored) { }
    }

    public static void Postfix(CharacterOption __instance, SpriteBatch sb,
                               float ___infoX, float ___infoY) {
        try {
            if (vanillaText4 != null) CharacterOption.TEXT[4] = vanillaText4;
            if (vanillaHpIcon != null) ImageMaster.TP_HP = vanillaHpIcon;
            if (!isTimeTraveler(__instance)) return;
            if (!__instance.selected) return; // 收起/切换中不渲染，否则文本会从屏幕边缘探出来干扰其他角色

            // 血量行：粉紫渐变呼吸灯（与战斗精英副标题同款），上限跟随角色本身
            HongwenNameFx.renderDamuhouSubtitle(sb, FontHelper.tipHeaderFont,
                    EXISTENCE_LABEL + (int) __instance.c.maxHealth,
                    ___infoX + HP_TEXT_DX * Settings.scale,
                    ___infoY + HP_TEXT_DY * Settings.scale);
            setString(__instance, "hp", EXISTENCE_LABEL + (int) __instance.c.maxHealth);

            // 背景故事：默认字号（tipHeaderFont）+ 面板宽度自然折行，淡青染色
            FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont,
                    FLAVOR_LINE1 + " NL " + FLAVOR_LINE2,
                    ___infoX - FLAVOR_DX * Settings.scale,
                    ___infoY + FLAVOR_DY * Settings.scale,
                    FLAVOR_WRAP_WIDTH * Settings.scale,
                    FLAVOR_SPACING * Settings.scale,
                    FLAVOR_COLOR);
            if (vanillaFlavor != null) setString(__instance, "flavorText", vanillaFlavor);
        } catch (Exception ignored) { }
    }

    private static void setString(CharacterOption opt, String field, String value) {
        try {
            java.lang.reflect.Field f = CharacterOption.class.getDeclaredField(field);
            f.setAccessible(true);
            f.set(opt, value);
        } catch (Exception ignored) { }
    }

    private static String getString(CharacterOption opt, String field) {
        try {
            java.lang.reflect.Field f = CharacterOption.class.getDeclaredField(field);
            f.setAccessible(true);
            Object v = f.get(opt);
            return v == null ? "" : v.toString();
        } catch (Exception ignored) {
            return "";
        }
    }
}
