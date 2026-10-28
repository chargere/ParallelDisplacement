package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.DamuhouCharacter;
import yourmod.actions.DamuhouLockDamageAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.BreachPower;
import yourmod.util.DamuhouCombatState;
import yourmod.util.DamuhouFireMode;
import yourmod.util.Wiz;

import java.util.ArrayList;

/**
 * 达穆候卡牌基类。
 * 半自动 = 原版点怪；全自动 = 自动锁定瞄具端。攻击写 {@code dmg()} 即可，两档都会吃。
 */
public abstract class AbstractDamuhouCard extends AbstractEasyCard {
    private final CardTarget intendedTarget;
    /** 牌面上的连击数，给弹药循环 / 枪斗术 / 快速装弹看。伤害 ≥8 的单发牌视为 0，不吃连击加成。 */
    public int printedCombo = 1;

    public AbstractDamuhouCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, rarity, target, DamuhouCharacter.Enums.DAMUHOU_COLOR);
        this.intendedTarget = target;
    }

    @Override
    public void update() {
        super.update();
        if (type == CardType.ATTACK && intendedTarget == CardTarget.ENEMY) {
            target = DamuhouFireMode.isFullAuto() ? CardTarget.NONE : CardTarget.ENEMY;
        }
    }

    @Override
    public void applyPowers() {
        if (type == CardType.ATTACK && intendedTarget == CardTarget.ENEMY && DamuhouFireMode.isFullAuto()) {
            AbstractMonster t = DamuhouFireMode.sightedLiving();
            if (t != null) {
                calculateCardDamage(t);
                return;
            }
        }
        super.applyPowers();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) {
            return false;
        }
        return type != CardType.ATTACK || intendedTarget != CardTarget.ENEMY
                || !DamuhouFireMode.isFullAuto()
                || DamuhouFireMode.sightedLiving() != null;
    }

    @Override
    protected void dmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            addToBot(new DamuhouLockDamageAction(this, fx));
        } else {
            super.dmg(m, fx);
        }
    }

    @Override
    protected void dmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            addToTop(new DamuhouLockDamageAction(this, fx));
        } else {
            super.dmgTop(m, fx);
        }
    }

    @Override
    protected void altDmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            addToBot(new DamuhouLockDamageAction(this, fx, true));
        } else {
            super.altDmg(m, fx);
        }
    }

    @Override
    protected void altDmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            addToTop(new DamuhouLockDamageAction(this, fx, true));
        } else {
            super.altDmgTop(m, fx);
        }
    }

    @Override
    protected void dmgRandom(AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            dmg(null, fx);
        } else {
            super.dmgRandom(fx);
        }
    }

    @Override
    protected void dmgRandomTop(AbstractGameAction.AttackEffect fx) {
        if (DamuhouFireMode.isFullAuto()) {
            dmgTop(null, fx);
        } else {
            super.dmgRandomTop(fx);
        }
    }

    protected AbstractMonster comboTarget(AbstractMonster clicked) {
        return DamuhouFireMode.isFullAuto() ? DamuhouFireMode.sightedLiving() : clicked;
    }

    /** 伤害 ≥8 的攻击按连击 0 处理，避免被多打几下炸穿。 */
    public int effectivePrintedCombo() {
        if (type == CardType.ATTACK && baseDamage >= 8) {
            return 0;
        }
        return printedCombo;
    }

    protected boolean acceptsComboBonus() {
        return effectivePrintedCombo() > 0;
    }

    protected int resolveHits(AbstractMonster clicked, int hits) {
        if (!acceptsComboBonus()) {
            return Math.max(1, hits);
        }
        int n = hits + DamuhouCombatState.consumeComboBonus();
        if (DamuhouCombatState.attacksPlayedThisTurn == 0) {
            n += DamuhouCombatState.firstAttackCombo + DamuhouCombatState.thisTurnFirstAttackCombo;
        }
        if (DamuhouCombatState.nthCardIndex > 0
                && DamuhouCombatState.cardsPlayedThisTurn + 1 == DamuhouCombatState.nthCardIndex) {
            n += 1;
        }
        n += DamuhouCombatState.focusExtra(comboTarget(clicked));
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(yourmod.powers.DamuhouSpareMagPower.POWER_ID)) {
            n += AbstractDungeon.player.getPower(yourmod.powers.DamuhouSpareMagPower.POWER_ID).amount;
        }
        return Math.max(1, n);
    }

    protected void comboHits(AbstractMonster clicked, int hits, AbstractGameAction.AttackEffect fx) {
        int extra = DamuhouCombatState.consumeHalfAutoDamage();
        int saved = damage;
        damage += extra;
        int n = resolveHits(clicked, hits);
        int missBlock = leftoverMissBlock();
        for (int i = 0; i < n; i++) {
            if (missBlock > 0) {
                addToBot(new yourmod.actions.DamuhouHalfHitOrBlockAction(this, clicked, fx, missBlock));
            } else {
                dmg(clicked, fx);
            }
        }
        damage = saved;
        queueKillFollowup(clicked);
    }

    protected void directHits(AbstractMonster t, int hits, AbstractGameAction.AttackEffect fx) {
        int extra = DamuhouCombatState.consumeHalfAutoDamage();
        int saved = damage;
        damage += extra;
        int n = resolveHits(t, hits);
        for (int i = 0; i < n; i++) {
            if (t == null || t.isDeadOrEscaped()) {
                break;
            }
            calculateCardDamage(t);
            super.dmg(t, fx);
        }
        damage = saved;
        queueKillFollowup(t);
    }

    private void queueKillFollowup(AbstractMonster clicked) {
        int heal = DamuhouCombatState.nextAttackHealOnKill;
        DamuhouCombatState.nextAttackHealOnKill = 0;
        boolean first = DamuhouCombatState.attacksPlayedThisTurn == 0;
        int refund = first ? DamuhouCombatState.firstAttackKillEnergy : 0;
        if (heal <= 0 && refund <= 0) {
            return;
        }
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster t = comboTarget(clicked);
                if (t == null || t.isDeadOrEscaped()) {
                    if (heal > 0) {
                        addToTop(new com.megacrit.cardcrawl.actions.common.HealAction(
                                com.megacrit.cardcrawl.dungeons.AbstractDungeon.player,
                                com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, heal));
                    }
                    if (refund > 0) {
                        com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.gainEnergy(refund);
                    }
                }
                isDone = true;
            }
        });
    }

    protected void comboHitsAll(int hits, AbstractGameAction.AttackEffect fx) {
        int n = Math.max(1, hits);
        if (acceptsComboBonus()) {
            n += DamuhouCombatState.consumeComboBonus();
            if (AbstractDungeon.player != null
                    && AbstractDungeon.player.hasPower(yourmod.powers.DamuhouSpareMagPower.POWER_ID)) {
                n += AbstractDungeon.player.getPower(yourmod.powers.DamuhouSpareMagPower.POWER_ID).amount;
            }
        }
        for (int i = 0; i < n; i++) {
            allDmg(fx);
        }
    }

    protected boolean targetHpOverHalf(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        return t != null && !t.isDeadOrEscaped() && t.currentHealth * 2 > t.maxHealth;
    }

    protected int leftoverMissBlock() {
        if (!DamuhouFireMode.isHalfAuto() || AbstractDungeon.player == null) {
            return 0;
        }
        if (!AbstractDungeon.player.hasPower(yourmod.powers.DamuhouLeftoverPower.POWER_ID)) {
            return 0;
        }
        return AbstractDungeon.player.getPower(yourmod.powers.DamuhouLeftoverPower.POWER_ID).amount;
    }

    protected boolean targetHasAmp(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        return t != null && t.hasPower(yourmod.powers.PrismAmpPower.POWER_ID);
    }

    protected int targetBreachStacks(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        if (t == null || !t.hasPower(BreachPower.POWER_ID)) {
            return 0;
        }
        return t.getPower(BreachPower.POWER_ID).amount;
    }

    protected boolean targetHasBreach(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        return t != null && t.hasPower(BreachPower.POWER_ID);
    }

    protected boolean targetIsMiddle(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        if (t == null) {
            return false;
        }
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        if (living.size() < 3) {
            return false;
        }
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        return t != living.get(0) && t != living.get(living.size() - 1);
    }

    protected boolean targetHpUnderPct(AbstractMonster clicked, int pct) {
        AbstractMonster t = comboTarget(clicked);
        return t != null && !t.isDeadOrEscaped() && t.currentHealth * 100 <= t.maxHealth * pct;
    }

    protected AbstractMonster neighborBehind(AbstractMonster clicked) {
        AbstractMonster t = comboTarget(clicked);
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        int i = living.indexOf(t);
        if (i >= 0 && i + 1 < living.size()) {
            return living.get(i + 1);
        }
        return null;
    }

    protected ArrayList<AbstractMonster> neighborsOf(AbstractMonster clicked) {
        return neighborsAround(comboTarget(clicked));
    }

    protected ArrayList<AbstractMonster> neighborsAround(AbstractMonster t) {
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        ArrayList<AbstractMonster> out = new ArrayList<>();
        int i = living.indexOf(t);
        if (i < 0) {
            return out;
        }
        if (i > 0) {
            out.add(living.get(i - 1));
        }
        if (i + 1 < living.size()) {
            out.add(living.get(i + 1));
        }
        return out;
    }
}
