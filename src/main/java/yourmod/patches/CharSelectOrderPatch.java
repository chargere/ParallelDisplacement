package yourmod.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import yourmod.CharacterFile;
import yourmod.ComingSoonTimeCharacter;
import yourmod.ComingSoonWindCharacter;
import yourmod.DominicCharacter;
import yourmod.FvvEggCharacter;
import yourmod.DamuhouCharacter;
import yourmod.JingjueCharacter;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * BaseMod 按角色名排序；本模组强制：凯文 → 多米尼克 → 风光 → 时间旅人 → fvv蛋蛋 → 战斗精英 → 精绝王者。
 */
public class CharSelectOrderPatch {

    public static final Comparator<CharacterOption> COMPARATOR = (a, b) -> {
        int oa = orderKey(a);
        int ob = orderKey(b);
        if (oa != ob) {
            return Integer.compare(oa, ob);
        }
        String na = (a != null && a.name != null) ? a.name : "";
        String nb = (b != null && b.name != null) ? b.name : "";
        return na.compareTo(nb);
    };

    private static int orderKey(CharacterOption opt) {
        if (opt == null || opt.c == null || opt.c.chosenClass == null) {
            return 100;
        }
        if (opt.c.chosenClass == CharacterFile.Enums.THE_TODO) {
            return 0;
        }
        if (opt.c.chosenClass == DominicCharacter.Enums.THE_DOMINIC) {
            return 1;
        }
        if (opt.c.chosenClass == ComingSoonWindCharacter.Enums.THE_WIND) {
            return 2;
        }
        if (opt.c.chosenClass == ComingSoonTimeCharacter.Enums.THE_TIME) {
            return 3;
        }
        if (opt.c.chosenClass == FvvEggCharacter.Enums.THE_FVV) {
            return 4;
        }
        if (opt.c.chosenClass == DamuhouCharacter.Enums.THE_DAMUHOU) {
            return 5;
        }
        if (opt.c.chosenClass == JingjueCharacter.Enums.THE_JINGJUE) {
            return 6;
        }
        return 50;
    }

    public static void reorderModCharacters(ArrayList<CharacterOption> options) {
        if (options == null || options.size() < 2) {
            return;
        }
        options.sort(COMPARATOR);
    }

    @SpirePatch(clz = BaseMod.class, method = "generateCharacterOptions")
    public static class ReplaceBaseModSort {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ("sort".equals(m.getMethodName())) {
                        m.replace("$0.sort(" + CharSelectOrderPatch.class.getName() + ".COMPARATOR);");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "initialize")
    public static class AfterCharSelectInit {
        public static void Postfix(CharacterSelectScreen __instance) {
            reorderModCharacters(__instance.options);
        }
    }
}
