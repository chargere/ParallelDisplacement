package yourmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import yourmod.util.DamuhouFireMode;

public class DamuhouFireModePatch {

    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class Update {
        public static void Postfix(TopPanel __instance) {
            DamuhouFireMode.update();
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class Render {
        public static void Postfix(TopPanel __instance, SpriteBatch sb) {
            DamuhouFireMode.render(sb);
        }
    }
}
