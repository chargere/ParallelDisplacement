package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.cards.common.ElectricSpark;
import yourmod.cards.rare.HeavenlySilence;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.getEnemies;

/**
 * 燎原猛攻：挂在玩家身上。
 * amount = 下次触发要消耗的烬火层数（1~6 循环）。
 * 同时负责静电印记的传导。
 */
public class ScorchingAssaultPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ScorchingAssault");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int MAX_COUNTER = 6;

    /** 当前这张牌是否为单体攻击（传导只对单体生效） */
    private boolean singleTargetAttackActive = false;
    /** 防止传导伤害再次触发传导 */
    private boolean conducting = false;
    /** 本张牌是否成功触发了烬火 */
    private boolean emberTriggered = false;
    private AbstractCreature emberTarget = null;
    private int emberConsume = 0;
    /** 电能火花：本次传导每命中一个副目标回复 1 能量 */
    public boolean grantEnergyOnConduct = false;
    /** 天雷寂灭：传导伤害可再次传导（用 NORMAL 打出副目标） */
    public boolean allowMultiConduct = false;
    /** 本回合第一张被消耗的牌（时序残响用） */
    public AbstractCard firstExhaustedThisTurn = null;
    private final java.util.HashSet<AbstractCreature> conductVisited = new java.util.HashSet<>();

    public ScorchingAssaultPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1); // 计数从 1 开始
    }

    @Override
    public void atStartOfTurn() {
        firstExhaustedThisTurn = null;
        if (AbstractDungeon.player != null) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof yourmod.cards.uncommon.TimeLag) {
                    ((yourmod.cards.uncommon.TimeLag) c).heldAnotherTurn();
                }
            }
        }
        // 传导相关标记按回合重置
        singleTargetAttackActive = false;
        grantEnergyOnConduct = false;
        allowMultiConduct = false;
        emberTriggered = false;
        emberTarget = null;
        emberConsume = 0;
        conductVisited.clear();
        yourmod.util.HongwenHooks.tookDamageThisTurn = false;
        yourmod.util.HongwenHooks.conductCountThisTurn = 0;
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (firstExhaustedThisTurn == null) {
            firstExhaustedThisTurn = card;
        }
    }

    /** 是否算「单体攻击牌」 */
    public static boolean isSingleTargetAttack(AbstractCard card) {
        return card != null
                && card.type == AbstractCard.CardType.ATTACK
                && (card.target == AbstractCard.CardTarget.ENEMY
                || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY);
    }

    /** 永燃：计数 4/5/6 时，单体攻击无条件 +4×X（不依赖敌人烬火） */
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        if (!isSingleTargetAttack(card)) {
            return damage;
        }
        if (EternalBurnPower.isFreeProc(this.amount)) {
            return damage + 4 * this.amount;
        }
        return damage;
    }

    /** 出牌时：层数够就记下「待消耗」；永燃 4/5/6 则无视烬火门槛 */
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 新牌开始时清掉上一张牌残留的传导标记
        // 注意：此时伤害 Action 往往已结算完；传导判定见 onAttack 里的 cardInUse
        singleTargetAttackActive = isSingleTargetAttack(card);
        grantEnergyOnConduct = ElectricSpark.ID.equals(card.cardID);
        allowMultiConduct = HeavenlySilence.ID.equals(card.cardID);
        emberTriggered = false;
        emberTarget = null;
        emberConsume = 0;
        conductVisited.clear();

        if (!singleTargetAttackActive || action.target == null) {
            return;
        }

        boolean freeProc = EternalBurnPower.isFreeProc(this.amount);
        AbstractPower ember = action.target.getPower(EmberFirePower.POWER_ID);
        boolean enoughEmber = ember != null && ember.amount >= this.amount;

        if (freeProc || enoughEmber) {
            flash();
            if (freeProc) {
                AbstractPower eternal = AbstractDungeon.player.getPower(EternalBurnPower.POWER_ID);
                if (eternal != null) {
                    eternal.flash();
                }
            }
            emberTriggered = true;
            emberTarget = action.target;
            emberConsume = this.amount;
        }
        // 层数不够且非永燃自由触发：不触发、计数不涨
    }

    /**
     * UseCardAction 首帧回调（此时 use() 排入的伤害 Action 通常已结算完）。
     * 注意：匿名 AbstractGameAction 里不能写裸名 target——会被父类字段阴影，导致永远不扣层。
     */
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (emberTriggered && emberConsume > 0 && emberTarget != null) {
            // 永燃自由触发：不扣烬火；否则直接排队扣层
            if (!EternalBurnPower.isFreeProc(emberConsume)
                    && emberTarget.hasPower(EmberFirePower.POWER_ID)) {
                addToBot(new ReducePowerAction(emberTarget, AbstractDungeon.player,
                        EmberFirePower.POWER_ID, emberConsume));
            }
            this.amount++;
            if (this.amount > MAX_COUNTER) {
                this.amount = 1;
            }
            updateDescription();
        }

        emberTriggered = false;
        emberTarget = null;
        emberConsume = 0;
    }

    /**
     * 打中带静电印记的敌人时：传导给其他印记敌人；有雷网效应则改为再打同一目标一次。
     * <p>
     * 注意：{@code use()} 里 {@code addToBot(Damage)} 早于 {@code UseCardAction.onUseCard}，
     * 伤害结算时 {@link #singleTargetAttackActive} 往往还是旧值。必须以 {@code cardInUse} 为准。
     */
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (AbstractDungeon.player == null || AbstractDungeon.getCurrRoom() == null) {
            return;
        }
        AbstractCard inUse = AbstractDungeon.player.cardInUse;
        boolean single = singleTargetAttackActive || isSingleTargetAttack(inUse);
        if (!single) {
            return;
        }
        boolean multi = allowMultiConduct
                || (inUse != null && HeavenlySilence.ID.equals(inUse.cardID));
        boolean grantEnergy = grantEnergyOnConduct
                || (inUse != null && ElectricSpark.ID.equals(inUse.cardID));

        if (conducting && !multi) {
            return;
        }
        if (info.type != DamageInfo.DamageType.NORMAL || info.output <= 0) {
            return;
        }
        if (target == null || !target.hasPower(StaticMarkPower.POWER_ID)) {
            return;
        }

        // 雷网效应：传导打回同一目标（自己电自己 ≈ 双倍）
        // 作为源目标不耗印记；作为被传导的「自己」仍消耗 1 层
        if (owner.hasPower(yourmod.powers.ThunderNetEffectPower.POWER_ID)) {
            if (conducting) {
                return;
            }
            conducting = true;
            try {
                yourmod.util.HongwenHooks.onConduct();
                int conductDmg = yourmod.util.HongwenHooks.scaleConductDamage(info.output);
                target.damage(new DamageInfo(AbstractDungeon.player, conductDmg, DamageInfo.DamageType.THORNS));
                if (grantEnergy) {
                    AbstractDungeon.player.gainEnergy(1);
                }
                if (target.hasPower(StaticMarkPower.POWER_ID)) {
                    addToTop(new ReducePowerAction(target, owner, StaticMarkPower.POWER_ID, 1));
                }
            } finally {
                conducting = false;
            }
            return;
        }

        if (multi && conductVisited.contains(target)) {
            return;
        }

        boolean outer = !conducting;
        conducting = true;
        if (outer) {
            conductVisited.clear();
            conductVisited.add(target);
        }
        try {
            for (AbstractMonster m : getEnemies()) {
                if (m == null || m == target || !m.hasPower(StaticMarkPower.POWER_ID)) {
                    continue;
                }
                if (multi && conductVisited.contains(m)) {
                    continue;
                }
                if (multi) {
                    conductVisited.add(m);
                }
                yourmod.util.HongwenHooks.onConduct();
                // 多段传导仍用 NORMAL 以便再次传导，但绝不能把 conducting 置 false：
                // 否则嵌套 onAttack 会当成 outer、清空 visit 集合，高进阶多目标时栈溢出闪退。
                DamageInfo.DamageType dtype = multi
                        ? DamageInfo.DamageType.NORMAL
                        : DamageInfo.DamageType.THORNS;
                int conductDmg = yourmod.util.HongwenHooks.scaleConductDamage(info.output);
                if (AbstractDungeon.player != null) {
                    m.damage(new DamageInfo(AbstractDungeon.player, conductDmg, dtype));
                    if (grantEnergy) {
                        AbstractDungeon.player.gainEnergy(1);
                    }
                }
                if (m.hasPower(StaticMarkPower.POWER_ID)) {
                    addToTop(new ReducePowerAction(m, owner, StaticMarkPower.POWER_ID, 1));
                }
            }
        } finally {
            if (outer) {
                conducting = false;
                allowMultiConduct = false;
                grantEnergyOnConduct = false;
                conductVisited.clear();
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1] + (4 * amount) + strings.DESCRIPTIONS[2];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            yourmod.util.HongwenHooks.tookDamageThisTurn = true;
            if (info.type == DamageInfo.DamageType.NORMAL) {
                yourmod.util.HongwenHooks.tookAttackDamageThisCombat = true;
            }
        }
        return damageAmount;
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            yourmod.util.HongwenHooks.lostHPThisCombat += damageAmount;
            yourmod.util.HongwenHooks.tookDamageThisTurn = true;
        }
    }
}
