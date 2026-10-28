package yourmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import yourmod.cards.uncommon.EmberThunderTwin;
import yourmod.powers.BurnHeavenPower;
import yourmod.powers.EmberFirePower;
import yourmod.powers.StaticMarkPower;
import yourmod.powers.StaticResonancePower;

import static yourmod.util.Wiz.getEnemies;

/** 鸿文机制全局钩子：施加烬火/静电时通知相关卡牌与能力。 */
public class HongwenHooks {
    /** 扩增中：防止焚天/共鸣递归 */
    public static boolean amplifying = false;
    /** 本场战斗是否受过攻击伤害（天命用） */
    public static boolean tookAttackDamageThisCombat = false;
    /** 本回合是否受过伤害（赦免用） */
    public static boolean tookDamageThisTurn = false;
    /** 本场战斗累计失去生命（鸿文终焉用） */
    public static int lostHPThisCombat = 0;
    /** 本场战斗传导次数 */
    public static int conductCountThisCombat = 0;
    /** 本回合传导次数（万雷归宗用） */
    public static int conductCountThisTurn = 0;
    /** 雷纹滤网：接下来若干次弃牌各对随机敌人施加 1 层静电 */
    public static int staticPerDiscardLeft = 0;
    /** 荒疫残烬：本场是否已触发过首次双倍 */
    public static boolean plagueEmberUsedThisCombat = false;

    public static void onBattleStart() {
        tookAttackDamageThisCombat = false;
        tookDamageThisTurn = false;
        lostHPThisCombat = 0;
        conductCountThisCombat = 0;
        conductCountThisTurn = 0;
        staticPerDiscardLeft = 0;
        plagueEmberUsedThisCombat = false;
    }

    /** 发生一次传导时调用（每次传导事件一次，与命中几个副目标无关） */
    public static void onConduct() {
        conductCountThisCombat++;
        conductCountThisTurn++;
        if (AbstractDungeon.player != null) {
            AbstractRelic echo = AbstractDungeon.player.getRelic(yourmod.relics.ConductEcho.ID);
            if (echo != null) {
                echo.flash();
                AbstractDungeon.player.heal(1, true);
            }
        }
    }

    /** 万军之敕等：传导伤害按百分比向上取整增伤 */
    public static int scaleConductDamage(int base) {
        if (AbstractDungeon.player == null || base <= 0) {
            return base;
        }
        AbstractPower amp = AbstractDungeon.player.getPower(yourmod.powers.ConductAmpPower.POWER_ID);
        if (amp == null || amp.amount <= 0) {
            return base;
        }
        return (int) Math.ceil(base * (100 + amp.amount) / 100.0);
    }

    /** 弃牌钩子：滤网静电 + 焚雷军令奖励 */
    public static void onCardDiscarded(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null || !isInCombat()) {
            return;
        }
        if (staticPerDiscardLeft > 0) {
            staticPerDiscardLeft--;
            AbstractMonster m = Wiz.getRandomEnemy();
            if (m != null) {
                AbstractDungeon.actionManager.addToBottom(
                        new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(
                                m, AbstractDungeon.player, new StaticMarkPower(m, 1), 1));
            }
        }
        AbstractPower orders = AbstractDungeon.player.getPower(yourmod.powers.EmberThunderOrdersPower.POWER_ID);
        if (orders != null) {
            ((yourmod.powers.EmberThunderOrdersPower) orders).onDiscardedCard(card);
        }
    }

    public static void onEmberApplied(AbstractMonster target, int amount) {
        if (amount <= 0 || AbstractDungeon.player == null || !isInCombat()) {
            return;
        }
        notifyTwinCardsEmber(amount);
        if (amplifying || target == null) {
            return;
        }
        tryPlagueDoubleEmber(target, amount);
        if (AbstractDungeon.player.getPower(BurnHeavenPower.POWER_ID) != null) {
            amplifyEmber(target, 3);
        }
    }

    public static void onStaticApplied(AbstractMonster target, int amount) {
        if (amount <= 0 || AbstractDungeon.player == null || !isInCombat()) {
            return;
        }
        notifyTwinCardsStatic(amount);
        if (amplifying || target == null) {
            return;
        }
        tryPlagueDoubleStatic(target, amount);
        if (AbstractDungeon.player.getPower(BurnHeavenPower.POWER_ID) != null) {
            amplifyEmber(target, 2);
        }
        if (AbstractDungeon.player.getPower(StaticResonancePower.POWER_ID) != null) {
            amplifyEmber(target, 5);
        }
    }

    private static void tryPlagueDoubleEmber(AbstractMonster target, int amount) {
        if (plagueEmberUsedThisCombat || AbstractDungeon.player.getRelic(yourmod.relics.PlagueEmberAsh.ID) == null) {
            return;
        }
        plagueEmberUsedThisCombat = true;
        AbstractDungeon.player.getRelic(yourmod.relics.PlagueEmberAsh.ID).flash();
        amplifyEmber(target, amount);
    }

    private static void tryPlagueDoubleStatic(AbstractMonster target, int amount) {
        if (plagueEmberUsedThisCombat || AbstractDungeon.player.getRelic(yourmod.relics.PlagueEmberAsh.ID) == null) {
            return;
        }
        plagueEmberUsedThisCombat = true;
        AbstractDungeon.player.getRelic(yourmod.relics.PlagueEmberAsh.ID).flash();
        amplifyStatic(target, amount);
    }

    private static void amplifyStatic(AbstractMonster target, int stacks) {
        amplifying = true;
        try {
            AbstractPower existing = target.getPower(StaticMarkPower.POWER_ID);
            if (existing != null) {
                existing.stackPower(stacks);
                existing.flash();
                existing.updateDescription();
            } else {
                target.addPower(new StaticMarkPower(target, stacks));
            }
        } finally {
            amplifying = false;
        }
    }

    /** 同步叠层烬火（不走 Action 队列，配合 amplifying 防递归） */
    private static void amplifyEmber(AbstractMonster target, int stacks) {
        amplifying = true;
        try {
            AbstractPower existing = target.getPower(EmberFirePower.POWER_ID);
            if (existing != null) {
                existing.stackPower(stacks);
                existing.flash();
                existing.updateDescription();
            } else {
                target.addPower(new EmberFirePower(target, stacks));
            }
        } finally {
            amplifying = false;
        }
    }

    private static void notifyTwinCardsEmber(int amount) {
        forAllTwinAway(c -> c.onEmberWhileAway(amount));
    }

    private static void notifyTwinCardsStatic(int amount) {
        forAllTwinAway(c -> c.onStaticWhileAway(amount));
    }

    private static void forAllTwinAway(java.util.function.Consumer<EmberThunderTwin> consumer) {
        if (AbstractDungeon.player == null) return;
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof EmberThunderTwin) consumer.accept((EmberThunderTwin) c);
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof EmberThunderTwin) consumer.accept((EmberThunderTwin) c);
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (c instanceof EmberThunderTwin) consumer.accept((EmberThunderTwin) c);
        }
    }

    public static boolean isInCombat() {
        return AbstractDungeon.player != null
                && AbstractDungeon.currMapNode != null
                && AbstractDungeon.getCurrRoom() != null
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static boolean anyEnemyHasStatic() {
        for (AbstractMonster m : getEnemies()) {
            if (m.hasPower(StaticMarkPower.POWER_ID)) return true;
        }
        return false;
    }

    public static int countAttacksThisTurn() {
        int n = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == AbstractCard.CardType.ATTACK) n++;
        }
        return n;
    }

    public static int countNonAttacksThisTurn() {
        int n = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type != AbstractCard.CardType.ATTACK) n++;
        }
        return n;
    }
}
