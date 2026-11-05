package yourmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import yourmod.util.ComboRulesUI;
import yourmod.util.DayNightSystem;

/** 将时间旅人连携图鉴按钮挂到战斗顶部面板的更新与绘制流程。 */
public class ComboRulesUIPatch {
    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class Update {
        public static void Postfix(TopPanel __instance) {
            DayNightSystem.update();
            ComboRulesUI.update();
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class Render {
        public static void Postfix(TopPanel __instance, SpriteBatch sb) {
            DayNightSystem.render(sb);
            ComboRulesUI.render(sb);
        }
    }
}
