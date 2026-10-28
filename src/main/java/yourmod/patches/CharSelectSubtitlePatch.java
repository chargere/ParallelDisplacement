package yourmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import yourmod.CharacterFile;
import yourmod.ComingSoonTimeCharacter;
import yourmod.ComingSoonWindCharacter;
import yourmod.DominicCharacter;
import yourmod.FvvEggCharacter;
import yourmod.DamuhouCharacter;
import yourmod.JingjueCharacter;
import yourmod.util.HongwenNameFx;

/**
 * 选人界面副标题。
 */
@SpirePatch(clz = CharacterOption.class, method = "render")
public class CharSelectSubtitlePatch {

    private static final float NAME_OFFSET = 200.0f;
    private static final float SUBTITLE_ABOVE = 48.0f;
    private static final Color WIND_SUB = new Color(0.55f, 0.90f, 0.65f, 1f);

    public static void Postfix(CharacterOption __instance, SpriteBatch sb, float ___infoX, float ___infoY) {
        if (!__instance.selected || __instance.locked || __instance.c == null) {
            return;
        }
        String subtitle;
        Color plainColor = null;
        if (__instance.c.chosenClass == CharacterFile.Enums.THE_TODO) {
            subtitle = CharacterFile.getSelectSubtitle();
            HongwenNameFx.renderRedWhiteSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                    ___infoX - 35.0f * Settings.scale,
                    ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            return;
        }
        if (__instance.c.chosenClass == DominicCharacter.Enums.THE_DOMINIC) {
            subtitle = DominicCharacter.getSelectSubtitle();
            HongwenNameFx.renderDominicSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                    ___infoX - 35.0f * Settings.scale,
                    ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            return;
        }
        if (__instance.c.chosenClass == ComingSoonWindCharacter.Enums.THE_WIND) {
            subtitle = ComingSoonWindCharacter.getSelectSubtitle();
            plainColor = WIND_SUB;
        } else if (__instance.c.chosenClass == ComingSoonTimeCharacter.Enums.THE_TIME) {
            subtitle = ComingSoonTimeCharacter.getSelectSubtitle();
            if (subtitle != null && !subtitle.isEmpty()) {
                HongwenNameFx.renderTimeTravelerSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                        ___infoX - 35.0f * Settings.scale,
                        ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            }
            return;
        } else if (__instance.c.chosenClass == FvvEggCharacter.Enums.THE_FVV) {
            subtitle = FvvEggCharacter.getSelectSubtitle();
            if (subtitle != null && !subtitle.isEmpty()) {
                HongwenNameFx.renderFvvSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                        ___infoX - 35.0f * Settings.scale,
                        ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            }
            return;
        } else if (__instance.c.chosenClass == DamuhouCharacter.Enums.THE_DAMUHOU) {
            subtitle = DamuhouCharacter.getSelectSubtitle();
            if (subtitle != null && !subtitle.isEmpty()) {
                HongwenNameFx.renderDamuhouSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                        ___infoX - 35.0f * Settings.scale,
                        ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            }
            return;
        } else if (__instance.c.chosenClass == JingjueCharacter.Enums.THE_JINGJUE) {
            subtitle = JingjueCharacter.getSelectSubtitle();
            if (subtitle != null && !subtitle.isEmpty()) {
                HongwenNameFx.renderJingjueSubtitle(sb, FontHelper.tipHeaderFont, subtitle,
                        ___infoX - 35.0f * Settings.scale,
                        ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale);
            }
            return;
        } else {
            return;
        }
        if (subtitle == null || subtitle.isEmpty()) {
            return;
        }
        float x = ___infoX - 35.0f * Settings.scale;
        float y = ___infoY + (NAME_OFFSET + SUBTITLE_ABOVE) * Settings.scale;
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, subtitle, x, y, plainColor);
    }
}
