package yourmod.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.JingjueCharacter;
import yourmod.util.JingjueCombatState;

/** 精绝：把处决线以下的血条涂黑。 */
public class JingjueExecuteBarPatch {

    private static final Color EXEC_BLACK = new Color(0.07f, 0.07f, 0.08f, 1f);

    @SpirePatch(clz = AbstractCreature.class, method = "renderRedHealthBar",
            paramtypez = {SpriteBatch.class, float.class, float.class})
    public static class PaintExecuteZone {
        public static void Postfix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (!(AbstractDungeon.player instanceof JingjueCharacter)) {
                return;
            }
            if (!(__instance instanceof AbstractMonster)) {
                return;
            }
            AbstractMonster m = (AbstractMonster) __instance;
            if (m.isDeadOrEscaped() || m.currentHealth <= 0 || m.maxHealth <= 0) {
                return;
            }
            int line = JingjueCombatState.executeLine(m);
            if (line <= 0) {
                return;
            }
            Float barW = ReflectionHacks.getPrivate(__instance, AbstractCreature.class, "targetHealthBarWidth");
            if (barW == null || barW <= 0f) {
                return;
            }
            Float h = ReflectionHacks.getPrivateStatic(AbstractCreature.class, "HEALTH_BAR_HEIGHT");
            Float oy = ReflectionHacks.getPrivateStatic(AbstractCreature.class, "HEALTH_BAR_OFFSET_Y");
            if (h == null || oy == null) {
                return;
            }
            float execW = m.hb.width * (line / (float) m.maxHealth);
            float drawW = Math.min(barW, execW);
            if (drawW <= 1f) {
                return;
            }
            EXEC_BLACK.a = __instance.hbAlpha;
            sb.setColor(EXEC_BLACK);
            float by = y + oy;
            if (m.currentHealth > 0) {
                sb.draw(ImageMaster.HEALTH_BAR_L, x - h, by, h, h);
            }
            sb.draw(ImageMaster.HEALTH_BAR_B, x, by, drawW, h);
            sb.draw(ImageMaster.HEALTH_BAR_R, x + drawW, by, h, h);
        }
    }
}
