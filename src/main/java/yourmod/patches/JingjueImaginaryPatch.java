package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import yourmod.actions.jingjue.RestoreMonsterIntentAction;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.powers.jingjue.TimeTurbulencePower;
import yourmod.util.JingjueCombatState;

/**
 * 虚数：拦截怪物出招。不直接 Prefix 抽象方法 takeTurn，
 * 桌面版无 LocalVariableTable 时 ParamInfo.findName 会 NPE。
 */
public class JingjueImaginaryPatch {

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class SkipOrRelease {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (AbstractMonster.class.getName().equals(m.getClassName())
                            && "takeTurn".equals(m.getMethodName())) {
                        m.replace("{ if (!" + JingjueImaginaryPatch.class.getName()
                                + ".intercept($0)) { $proceed(); } }");
                    }
                }
            };
        }
    }

    /** @return true 表示已处理，跳过原 takeTurn */
    public static boolean intercept(AbstractMonster monster) {
        if (monster == null) {
            return false;
        }
        if (JingjueCombatState.blocksTimeTurbulence(monster)) {
            TimeTurbulencePower.clear(monster);
            return false;
        }
        JingjueCombatState.replayStoredMove(monster);
        TimeTurbulencePower flux = TimeTurbulencePower.get(monster);
        if (flux != null && flux.shouldRelease()) {
            flux.releaseStored();
        }
        if (!monster.hasPower(ImaginaryPower.POWER_ID)) {
            return false;
        }
        ImaginaryPower p = (ImaginaryPower) monster.getPower(ImaginaryPower.POWER_ID);
        if (p.cancelled || p.amount <= 0) {
            return false;
        }
        JingjueCombatState.capturedIntents.put(monster, JingjueCombatState.readIntentTotal(monster));
        JingjueCombatState.storeSkippedMove(monster);
        AbstractDungeon.actionManager.addToBottom(new RestoreMonsterIntentAction(monster, true));
        return true;
    }
}
