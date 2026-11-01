package yourmod.util;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.powers.XingNianPower;

import java.util.*;

import static yourmod.util.Wiz.*;

/**
 * 全局连携系统（时间旅人核心机制之一）。
 *
 * 每打出一张牌，把它的「牌型」(攻 / 技 / 能) 推进一个【跨回合累计】的队列；
 * 若队列末尾凑齐某个预定义模式（如「攻攻技」），则触发该模式绑定的全部效果，
 * 并【把这截序列从队列消费掉】，剩余部分继续留着接后续出牌。
 *
 * 设计要点：
 *  - 队列在同一场战斗内跨回合保留（用于凑长连携）；新战斗开始时必须 reset() 清空。
 *  - 匹配只看队列【末尾】，命中即从尾部删除该模式长度，可连锁触发。
 *  - 模式用【数据驱动】的注册表（PATTERNS），新增法则只改数据，不改匹配逻辑。
 *  - 队列上限 MAX_QUEUE，避免跨多回合累计时无限增长（最长模式 3 张 + 余量）。
 */
public class ComboSystem {
    public enum CType { ATTACK, SKILL, POWER }
    private static final CType G = CType.ATTACK;   // 攻
    private static final CType J = CType.SKILL;    // 技
    private static final CType N = CType.POWER;    // 能

    /** 最长模式 3 张 + 1 余量，避免队列无界增长 */
    private static final int MAX_QUEUE = 4;

    /** 跨战斗累计的出牌序列（同一战斗内跨回合不清） */
    private static final LinkedList<CType> queue = new LinkedList<>();

    // ===== 效果接口与模式定义 =====
    private interface ComboEffect { void apply(AbstractPlayer p, int form); }

    private static class ComboPattern {
        final String name;
        final CType[] seq;
        final ComboEffect effect;
        ComboPattern(String name, CType[] seq, ComboEffect effect) {
            this.name = name; this.seq = seq; this.effect = effect;
        }
    }

    private static CType[] seq(CType... c) { return c; }

    /**
     * 全部连携法则（数据驱动）。数值源自《时间旅人·连携法则设计稿 v1》。
     * 每条按 基础 / 溯(SU) / 驻(ZHU) / 落(LUO) 四套效果展开。
     * 能系 4 条（相续/环抱/双星/收势）需能力卡进队列方可触发——当前起手套牌无能力卡，
     * 故第一局不可达，待后续加入「凝时」等能力卡即生效。
     */
    private static final List<ComboPattern> PATTERNS = new ArrayList<>();
    static {
        // ===== 攻 / 技 系 6 条 =====
        PATTERNS.add(new ComboPattern("回环", seq(G, G, J), (p, f) -> {
            if (f == FormUtil.SU)  { addXingNian(p, 1); gainBlock(p, 6); }
            else if (f == FormUtil.ZHU) { addXingNian(p, 3); gainEnergy(p, 2); }
            else if (f == FormUtil.LUO) { addXingNian(p, 2); gainEnergy(p, 1); damageRandom(p, 4); }
            else { addXingNian(p, 2); gainEnergy(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("连星", seq(G, G, G), (p, f) -> {
            if (f == FormUtil.SU)  { damageAll(p, 4); gainBlock(p, 4); }
            else if (f == FormUtil.ZHU) { damageAll(p, 9); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { damageAll(p, 6); tempStr(p, 1); addXingNian(p, 1); }
            else { damageAll(p, 6); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("错位", seq(J, G, J), (p, f) -> {
            if (f == FormUtil.SU)  { debuffFirst(p, 1, 1); gainBlock(p, 6); }
            else if (f == FormUtil.ZHU) { debuffFirst(p, 2, 2); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { debuffFirst(p, 1, 1); tempStr(p, 1); }
            else { debuffFirst(p, 1, 1); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("并流", seq(J, J, G), (p, f) -> {
            if (f == FormUtil.SU)  { addXingNian(p, 1); gainBlock(p, 6); draw(p, 1); }
            else if (f == FormUtil.ZHU) { addXingNian(p, 3); draw(p, 1); }
            else if (f == FormUtil.LUO) { addXingNian(p, 2); draw(p, 1); tempStr(p, 2); }
            else { addXingNian(p, 2); draw(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("溯流", seq(G, J, G), (p, f) -> {
            if (f == FormUtil.SU)  { gainBlock(p, 9); }
            else if (f == FormUtil.ZHU) { gainBlock(p, 5); damageFirst(p, 8); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { gainBlock(p, 5); damageFirst(p, 5); tempStr(p, 1); }
            else { gainBlock(p, 5); damageFirst(p, 5); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("凝滞", seq(J, J, J), (p, f) -> {
            if (f == FormUtil.SU)  { addXingNian(p, 2); gainBlock(p, 8); }
            else if (f == FormUtil.ZHU) { addXingNian(p, 4); gainEnergy(p, 1); }
            else if (f == FormUtil.LUO) { addXingNian(p, 3); gainEnergy(p, 1); tempStr(p, 1); }
            else { addXingNian(p, 3); gainEnergy(p, 1); }
        }));

        // ===== 能 系 4 条（需能力卡进队列） =====
        PATTERNS.add(new ComboPattern("相续", seq(G, N, G), (p, f) -> {
            if (f == FormUtil.SU)  { damageFirst(p, 6); gainBlock(p, 4); }
            else if (f == FormUtil.ZHU) { damageFirst(p, 11); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { damageFirst(p, 8); tempStr(p, 1); }
            else { damageFirst(p, 8); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("环抱", seq(J, N, J), (p, f) -> {
            if (f == FormUtil.SU)  { gainBlock(p, 8); addXingNian(p, 1); }
            else if (f == FormUtil.ZHU) { gainBlock(p, 9); draw(p, 1); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { gainBlock(p, 6); draw(p, 1); tempStr(p, 1); }
            else { gainBlock(p, 6); draw(p, 1); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("双星", seq(N, N, G), (p, f) -> {
            if (f == FormUtil.SU)  { gainEnergy(p, 1); damageAll(p, 4); gainBlock(p, 4); }
            else if (f == FormUtil.ZHU) { gainEnergy(p, 2); damageAll(p, 7); addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { gainEnergy(p, 1); damageAll(p, 5); tempStr(p, 1); }
            else { gainEnergy(p, 1); damageAll(p, 5); addXingNian(p, 1); }
        }));
        PATTERNS.add(new ComboPattern("收势", seq(G, G, N), (p, f) -> {
            // 收势原设计喂「时序」层数；时序 Power 尚未实现，暂以等价星念收益替代（见代码库待办）
            if (f == FormUtil.SU)  { addXingNian(p, 1); gainBlock(p, 6); }
            else if (f == FormUtil.ZHU) { addXingNian(p, 2); }
            else if (f == FormUtil.LUO) { addXingNian(p, 1); tempStr(p, 1); }
            else { addXingNian(p, 1); }
        }));
    }

    /** 新战斗开始时清空队列（避免上场残留） */
    public static void reset() {
        queue.clear();
    }

    /** 打出一张牌时调用：推进牌型 → 落形态临时力量 → 结算连携 */
    public static void record(AbstractPlayer p, AbstractCard card) {
        CType t = toType(card);
        if (t == null) return;
        queue.addLast(t);
        while (queue.size() > MAX_QUEUE) queue.pollFirst(); // 控制队列上限

        // 落形态：打出攻击牌获得 1 层临时力量（回合开始由 XingNianPower 回收）
        if (t == CType.ATTACK) {
            FormUtil.gainLuoTempStrength(p);
        }

        resolve(p);
    }

    private static CType toType(AbstractCard c) {
        switch (c.type) {
            case ATTACK: return CType.ATTACK;
            case SKILL:  return CType.SKILL;
            case POWER:  return CType.POWER;
            default:     return null;
        }
    }

    /** 末尾匹配：命中任一模式则触发并从尾部消费，循环连锁 */
    private static void resolve(AbstractPlayer p) {
        boolean again;
        do {
            again = false;
            for (ComboPattern pat : PATTERNS) {
                if (endsWith(pat.seq)) {
                    int form = FormUtil.getForm(FormUtil.getXingNian(p));
                    pat.effect.apply(p, form);
                    for (int i = 0; i < pat.seq.length; i++) queue.pollLast();
                    again = true; // 可能连锁触发下一个模式
                    break;
                }
            }
        } while (again);
    }

    private static boolean endsWith(CType[] pat) {
        if (queue.size() < pat.length) return false;
        int start = queue.size() - pat.length;
        for (int i = 0; i < pat.length; i++) {
            if (queue.get(start + i) != pat[i]) return false;
        }
        return true;
    }

    /** 只读预测：若再追加 攻/技/能 任一牌型，结尾会命中哪些法则。供未来连携可视化 UI 使用。 */
    public static Map<CType, List<String>> peekNext(AbstractPlayer p) {
        Map<CType, List<String>> r = new LinkedHashMap<>();
        for (CType t : new CType[]{G, J, N}) {
            queue.addLast(t);
            List<String> hit = new ArrayList<>();
            for (ComboPattern pat : PATTERNS) {
                if (endsWith(pat.seq)) hit.add(pat.name);
            }
            r.put(t, hit);
            queue.pollLast();
        }
        return r;
    }

    /** 供 UI / 调试：当前队列牌型快照 */
    public static List<CType> snapshot() {
        return new ArrayList<>(queue);
    }

    // ===== 效果辅助方法 =====

    /** 增加星念（封顶 CAP；无则新建 Power） */
    private static void addXingNian(AbstractPlayer p, int amt) {
        XingNianPower xn = (XingNianPower) p.getPower(XingNianPower.POWER_ID);
        if (xn != null) {
            if (xn.amount < XingNianPower.CAP) {
                xn.amount = Math.min(xn.amount + amt, XingNianPower.CAP);
                xn.updateDescription();
            }
        } else {
            applyToSelf(new XingNianPower(p, Math.min(amt, XingNianPower.CAP)));
        }
    }

    private static void gainEnergy(AbstractPlayer p, int n) {
        atb(new GainEnergyAction(n));
    }

    private static void gainBlock(AbstractPlayer p, int n) {
        atb(new GainBlockAction(p, p, n));
    }

    private static void draw(AbstractPlayer p, int n) {
        atb(new DrawCardAction(p, n));
    }

    /** 对全部存活敌人各造成 n 点伤害 */
    private static void damageAll(AbstractPlayer p, int n) {
        for (AbstractMonster m : alive()) {
            atb(new DamageAction(m, new DamageInfo(p, n, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    /** 对首个存活敌人造成 n 点伤害 */
    private static void damageFirst(AbstractPlayer p, int n) {
        AbstractMonster m = firstAlive();
        if (m != null) atb(new DamageAction(m, new DamageInfo(p, n, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    /** 对随机存活敌人造成 n 点伤害 */
    private static void damageRandom(AbstractPlayer p, int n) {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (m != null) atb(new DamageAction(m, new DamageInfo(p, n, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    /** 对首个存活敌人施加 易伤v / 虚弱w（0 表示不施加） */
    private static void debuffFirst(AbstractPlayer p, int v, int w) {
        AbstractMonster m = firstAlive();
        if (m == null) return;
        if (v > 0) applyToEnemy(m, new VulnerablePower(m, v, false));
        if (w > 0) applyToEnemy(m, new WeakPower(m, w, false));
    }

    /** 获得 n 层临时力量（由 XingNianPower 回合开始回收，与落形态攻击临时力量共用计数） */
    private static void tempStr(AbstractPlayer p, int n) {
        XingNianPower xn = (XingNianPower) p.getPower(XingNianPower.POWER_ID);
        if (xn != null) for (int i = 0; i < n; i++) xn.gainLuoTempStrength();
    }

    private static List<AbstractMonster> alive() {
        List<AbstractMonster> r = new ArrayList<>();
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) r.add(m);
            }
        }
        return r;
    }

    private static AbstractMonster firstAlive() {
        List<AbstractMonster> a = alive();
        return a.isEmpty() ? null : a.get(0);
    }
}
