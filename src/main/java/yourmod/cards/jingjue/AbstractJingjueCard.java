package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.JingjueCharacter;
import yourmod.actions.jingjue.JingjueDamageAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.powers.jingjue.EyeMarkPower;
import yourmod.powers.jingjue.ImaginaryPower;
import yourmod.powers.jingjue.ImmolatePower;
import yourmod.util.JingjueCombatState;
import yourmod.util.JingjueDesc;

import static yourmod.util.Wiz.atb;

/** 精绝王者卡牌基类。 */
public abstract class AbstractJingjueCard extends AbstractEasyCard {
    public int splashPct;

    public AbstractJingjueCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, rarity, target, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void initializeDescription() {
        rawDescription = JingjueDesc.isolate(rawDescription);
        super.initializeDescription();
    }

    protected void jjHit(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        jjHit(m, damage, fx, splashPct);
    }

    protected void jjHit(AbstractMonster m, int dmg, AbstractGameAction.AttackEffect fx, int splash) {
        if (m == null) {
            return;
        }
        JingjueCombatState.recordAttack(this, dmg);
        atb(new JingjueDamageAction(m, new DamageInfo(AbstractDungeon.player, dmg, damageTypeForTurn),
                fx, false, JingjueCombatState.splashPercent(splash)));
    }

    protected void jjHitAll(AbstractGameAction.AttackEffect fx) {
        atb(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx));
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != null && !m.isDeadOrEscaped() && m.currentHealth - damage <= JingjueCombatState.executeLine(m)) {
                    atb(new yourmod.actions.jingjue.ExecuteAction(m));
                }
            }
        }
    }

    protected void giveEye(AbstractMonster m, int n) {
        if (m == null || n <= 0) {
            return;
        }
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(yourmod.relics.jingjue.GhostAmuletRelic.ID)
                && !yourmod.relics.jingjue.GhostAmuletRelic.usedThisCombat) {
            n += 2;
            yourmod.relics.jingjue.GhostAmuletRelic.usedThisCombat = true;
            AbstractDungeon.player.getRelic(yourmod.relics.jingjue.GhostAmuletRelic.ID).flash();
        }
        atb(new ApplyPowerAction(m, AbstractDungeon.player, new EyeMarkPower(m, n), n));
        onGaveImaginaryOrEye();
    }

    protected void giveImaginary(AbstractMonster m, int n) {
        if (m == null || n <= 0) {
            return;
        }
        atb(new ApplyPowerAction(m, AbstractDungeon.player, new ImaginaryPower(m, n), n));
        onGaveImaginary();
    }

    protected void giveImmolate(AbstractMonster m, int n) {
        if (m == null || n <= 0) {
            return;
        }
        atb(new ApplyPowerAction(m, AbstractDungeon.player, new ImmolatePower(m, n), n));
    }

    protected void payHp(AbstractPlayer p, int n) {
        atb(new LoseHPAction(p, p, n));
    }

    protected void giveWeak(AbstractMonster m, int n) {
        if (m == null || n <= 0) {
            return;
        }
        atb(new ApplyPowerAction(m, AbstractDungeon.player, new WeakPower(m, n, false), n));
    }

    protected void giveVuln(AbstractMonster m, int n) {
        if (m == null || n <= 0) {
            return;
        }
        atb(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, n, false), n));
    }

    protected boolean hasImaginary(AbstractMonster m) {
        return m != null && m.hasPower(ImaginaryPower.POWER_ID);
    }

    protected boolean hasEye(AbstractMonster m) {
        return m != null && m.hasPower(EyeMarkPower.POWER_ID);
    }

    private void onGaveImaginary() {
        AbstractPower p = AbstractDungeon.player == null ? null
                : AbstractDungeon.player.getPower(yourmod.powers.jingjue.CorridorPower.POWER_ID);
        if (p != null && JingjueCombatState.corridorTriggers < p.amount) {
            JingjueCombatState.corridorTriggers++;
            JingjueCombatState.gainHourglass(1);
        }
    }

    private void onGaveImaginaryOrEye() {
    }
}
