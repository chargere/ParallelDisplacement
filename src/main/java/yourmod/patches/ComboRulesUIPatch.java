package yourmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import yourmod.util.ComboRulesUI;
import yourmod.util.DayNightSystem;
import yourmod.util.HongwenCharacterVfx;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

/** 将时间旅人连携图鉴按钮挂到战斗顶部面板的更新与绘制流程。 */
public class ComboRulesUIPatch {
    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class Update {
        public static void Postfix(TopPanel __instance) {
            DayNightSystem.update();
            ComboRulesUI.update();
            HongwenCharacterVfx.updatePose();
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class Render {
        public static void Postfix(TopPanel __instance, SpriteBatch sb) {
            DayNightSystem.render(sb);
            ComboRulesUI.render(sb);
        }
    }

    /** 地牢完整渲染结束后再绘制角色特效，避免被场景、角色动画或顶部面板覆盖。
     *  角色 VFX 只在此处绘制一次；TopPanel.render 的重复调用已移除，避免每帧画两遍。 */
    @SpirePatch(clz = AbstractDungeon.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class DungeonRender {
        public static void Postfix(AbstractDungeon __instance, SpriteBatch sb) {
            if (AbstractDungeon.player instanceof AbstractPlayer) {
                HongwenCharacterVfx.render((AbstractPlayer) AbstractDungeon.player, sb);
            }
        }
    }
}
