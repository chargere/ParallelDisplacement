# Generate fvv egg card set - PowerShell UTF-8
$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$root = Split-Path (Split-Path $PSScriptRoot -Parent) -Parent
if (-not (Test-Path "$PSScriptRoot\..\src")) { $root = Resolve-Path "$PSScriptRoot\.." } else { $root = Resolve-Path "$PSScriptRoot\.." }
$cardDir = Join-Path $root "src\main\java\yourmod\cards\fvv"
$powerDir = Join-Path $root "src\main\java\yourmod\powers"
$patchDir = Join-Path $root "src\main\java\yourmod\patches"
New-Item -ItemType Directory -Force -Path $cardDir | Out-Null
New-Item -ItemType Directory -Force -Path $patchDir | Out-Null

function W($path, $content) {
  $dir = Split-Path $path -Parent
  if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
  [System.IO.File]::WriteAllText($path, $content, (New-Object System.Text.UTF8Encoding $false))
}

# ========== POWERS ==========
W "$powerDir\LateAwarenessPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.FvvCombatState;

import static yourmod.ModFile.makeID;

/** 迟到觉悟 */
public class LateAwarenessPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("LateAwareness");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LateAwarenessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && FvvCombatState.damageDealtThisTurn == 0) {
            flash();
            UselessPower.gain(owner, owner, amount);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\AdmitCommitteePower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;

/** 承认委员 */
public class AdmitCommitteePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("AdmitCommittee");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public AdmitCommitteePower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    public void onUselessGained(int am) {
        if (am <= 0 || timesThisTurn >= amount) {
            return;
        }
        timesThisTurn++;
        flash();
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\PassiveAggressionPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 被动攻击 */
public class PassiveAggressionPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("PassiveAggression");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public PassiveAggressionPower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    public void onDiscard() {
        if (timesThisTurn >= amount) {
            return;
        }
        if (AbstractDungeon.cardRandomRng.randomBoolean()) {
            timesThisTurn++;
            flash();
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\AfkPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.util.FvvCombatState;

import static yourmod.ModFile.makeID;

/** 挂机 */
public class AfkPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("Afk");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    /** 每回合打牌上限（含） */
    public int playLimit;

    public AfkPower(AbstractCreature owner, int playLimit) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, 1);
        this.playLimit = playLimit;
        this.isTwoAmount = true;
        this.amount2 = playLimit;
    }

    @Override
    public void stackPower(int stackAmount) {
        // keep highest playLimit when stacking
    }

    @Override
    public void atStartOfTurn() {
        if (FvvCombatState.lowPlayStreak >= 2) {
            flash();
            addToBot(new GainEnergyAction(2));
            UselessPower.gain(owner, owner, 3);
            FvvCombatState.lowPlayStreak = 0;
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + playLimit + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\ChatMutePower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 群聊屏蔽 */
public class ChatMutePower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("ChatMute");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public ChatMutePower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, amount);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            return Math.max(0, damageAmount - amount);
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\UselessEconomyPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 废物经济：amount==1 仅打击，amount==2 全部攻击 */
public class UselessEconomyPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("UselessEconomy");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public UselessEconomyPower(AbstractCreature owner, int mode) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, mode);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL || card == null) {
            return damage;
        }
        boolean ok = amount >= 2
                ? card.type == AbstractCard.CardType.ATTACK
                : card.hasTag(AbstractCard.CardTags.STRIKE);
        if (ok) {
            return damage + UselessPower.getEffective(owner);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        description = amount >= 2 ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\JustLikeThisPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 我就这样 */
public class JustLikeThisPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("JustLikeThis");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public boolean firstCard = true;

    public JustLikeThisPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        UselessPower.gain(owner, owner, amount);
        firstCard = true;
        discountHand();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (firstCard && card != null) {
            card.setCostForTurn(Math.max(0, card.costForTurn - 1));
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (firstCard) {
            firstCard = false;
            if (AbstractDungeon.player != null) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.cost >= 0) {
                        c.setCostForTurn(c.cost);
                    }
                }
            }
        }
    }

    private void discountHand() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn >= 0) {
                c.setCostForTurn(Math.max(0, c.costForTurn - 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\BornWithItPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import yourmod.relics.UselessFetus;

import static yourmod.ModFile.makeID;

/** 胎里带来 */
public class BornWithItPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("BornWithIt");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean convertedThisTurn = false;
    private boolean battleStartDone = false;

    public BornWithItPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    public boolean tryConvertToDraw() {
        if (AbstractDungeon.player == null || !AbstractDungeon.player.hasRelic(UselessFetus.ID)) {
            return false;
        }
        if (convertedThisTurn) {
            return false;
        }
        convertedThisTurn = true;
        flash();
        addToTop(new DrawCardAction(1));
        return true;
    }

    @Override
    public void atStartOfTurn() {
        convertedThisTurn = false;
    }

    @Override
    public void onInitialApplication() {
        tryBattleStartBonus();
    }

    @Override
    public void atStartOfCombat() {
        tryBattleStartBonus();
    }

    private void tryBattleStartBonus() {
        if (battleStartDone || AbstractDungeon.player == null) {
            return;
        }
        battleStartDone = true;
        if (!AbstractDungeon.player.hasRelic(UselessFetus.ID)) {
            flash();
            UselessPower.gain(owner, owner, 5);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\WasteCrownPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 废物王冠 */
public class WasteCrownPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("WasteCrown");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public WasteCrownPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && UselessPower.getAmount(owner) >= 5) {
            flash();
            addToBot(new DrawCardAction(1));
            addToBot(new GainBlockAction(owner, owner, 5));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\FamousEffectPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 名人效应 */
public class FamousEffectPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FamousEffect");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int timesThisTurn = 0;

    public FamousEffectPower(AbstractCreature owner, int maxPerTurn) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, maxPerTurn);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card == null || timesThisTurn >= amount) {
            return;
        }
        String n = card.name == null ? "" : card.name;
        if (n.contains("废") || n.contains("没用") || n.contains("毛")) {
            timesThisTurn++;
            flash();
            addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public void atStartOfTurn() {
        timesThisTurn = 0;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\FetalFailPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 胎教失败 */
public class FetalFailPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("FetalFail");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean gainedThisTurn = false;
    private boolean lostHpThisTurn = false;

    public FetalFailPower(AbstractCreature owner, int extra) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, extra);
    }

    public void onUselessGained() {
        if (gainedThisTurn) {
            return;
        }
        gainedThisTurn = true;
        flash();
        UselessPower.gainSilent(owner, amount);
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && !lostHpThisTurn) {
            lostHpThisTurn = true;
            flash();
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void atStartOfTurn() {
        gainedThisTurn = false;
        lostHpThisTurn = false;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\UselessAscendPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 毛用飞升：费用-1，回合结束清空没用并抽2 */
public class UselessAscendPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("UselessAscend");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public UselessAscendPower(AbstractCreature owner, int turns) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, turns);
    }

    @Override
    public void onInitialApplication() {
        discountAll();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card != null && card.costForTurn >= 0) {
            card.setCostForTurn(Math.max(0, card.costForTurn - 1));
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        discountAll();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            return;
        }
        flash();
        UselessPower.spendAll(owner);
        addToBot(new DrawCardAction(2));
        amount--;
        if (amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    private void discountAll() {
        if (AbstractDungeon.player == null) {
            return;
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.costForTurn >= 0) {
                c.setCostForTurn(Math.max(0, c.costForTurn - 1));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\NoSkillsPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 已读不回 */
public class NoSkillsPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NoSkills");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NoSkillsPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, true, owner, 1);
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (card != null && card.type == AbstractCard.CardType.SKILL) {
            card.cantUseMessage = strings.DESCRIPTIONS[1];
            return false;
        }
        return true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\NextTurnEnergyLossPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 下回合失去能量 */
public class NextTurnEnergyLossPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NextTurnEnergyLoss");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NextTurnEnergyLossPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.loseEnergy(amount);
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\EnergyFraudDebtPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 能量诈骗债务 */
public class EnergyFraudDebtPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("EnergyFraudDebt");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public EnergyFraudDebtPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.loseEnergy(amount);
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

W "$powerDir\NextCardBoostPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 今日停更：amount==1 下一张，amount==2 本回合 */
public class NextCardBoostPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NextCardBoost");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NextCardBoostPower(AbstractCreature owner, int mode) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, true, owner, mode);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.5f;
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 1.5f;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (amount == 1) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && amount >= 2) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        description = amount >= 2 ? strings.DESCRIPTIONS[1] : strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\BalanceHealPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 平衡调整：自损改回复 */
public class BalanceHealPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("BalanceHeal");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BalanceHealPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info != null && info.owner == owner) {
            flash();
            addToTop(new HealAction(owner, owner, damageAmount));
            return 0;
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\IAmEggPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 我是蛋蛋 */
public class IAmEggPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("IAmEgg");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private boolean triggered = false;

    public IAmEggPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, -1);
    }

    @Override
    public void atStartOfTurn() {
        triggered = false;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (triggered || card == null) {
            return;
        }
        triggered = true;
        int paid = card.costForTurn;
        if (card.freeToPlayOnce) {
            paid = 0;
        }
        if (paid >= 2) {
            flash();
            addToBot(new GainEnergyAction(1));
            UselessPower.gain(owner, owner, 1);
        }
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\SkipNextTurnPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 跳过下回合 */
public class SkipNextTurnPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SkipNextTurn");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SkipNextTurnPower(AbstractCreature owner) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, 1);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new PressEndTurnButtonAction());
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\SkipNextDrawPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 跳过下次抽牌（由 patch 消费） */
public class SkipNextDrawPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("SkipNextDraw");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SkipNextDrawPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0];
    }
}
'@

W "$powerDir\NextTurnDrawUselessPower.java" @'
package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static yourmod.ModFile.makeID;

/** 下回合：每层抽1并+1没用 */
public class NextTurnDrawUselessPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("NextTurnDrawUseless");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public NextTurnDrawUselessPower(AbstractCreature owner, int amount) {
        super(POWER_ID, strings.NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new DrawCardAction(amount));
        UselessPower.gain(owner, owner, amount);
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
'@

Write-Host "Powers written"
