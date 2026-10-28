$dir = "c:\Users\38220\Desktop\ProTemplate-master\src\main\java\yourmod\cards\jingjue"

function Write-Card($name, $body) {
  $path = Join-Path $dir "$name.java"
  Set-Content -Path $path -Value $body -Encoding UTF8
}

$hdr = @"
package yourmod.cards.jingjue;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

"@

# Commons
Write-Card "JjGhostTalisman" @"
$hdr
/** 鬼眼符 */
public class JjGhostTalisman extends AbstractJingjueCard {
    public static final String ID = makeID("JjGhostTalisman");
    public JjGhostTalisman() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 6; baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL); giveEye(m, magicNumber); }
    public void upp() { upgradeDamage(3); upgradeMagicNumber(1); }
}
"@

Write-Card "JjJingjian" @"
$hdr
/** 净见阿含 */
public class JjJingjian extends AbstractJingjueCard {
    public static final String ID = makeID("JjJingjian");
    public JjJingjian() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 5; baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.POISON); giveImaginary(m, magicNumber); }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
"@

Write-Card "JjSandGain" @"
$hdr
/** 沙漏·积 */
public class JjSandGain extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandGain");
    public JjSandGain() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.gainHourglass(magicNumber); }
    public void upp() { upgradeMagicNumber(1); }
}
"@

Write-Card "JjSlowWalk" @"
$hdr
/** 行境·缓 */
public class JjSlowWalk extends AbstractJingjueCard {
    public static final String ID = makeID("JjSlowWalk");
    public JjSlowWalk() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveImaginary(m, magicNumber); }
    public void upp() { upgradeMagicNumber(1); }
}
"@

Write-Card "JjFireSplash" @"
$hdr
/** 业火溅 */
public class JjFireSplash extends AbstractJingjueCard {
    public static final String ID = makeID("JjFireSplash");
    public JjFireSplash() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 7; splashPct = 30; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); }
    public void upp() { upgradeDamage(3); splashPct = 40; }
}
"@

Write-Card "JjIceGuard" @"
$hdr
/** 乃穷冰 */
public class JjIceGuard extends AbstractJingjueCard {
    public static final String ID = makeID("JjIceGuard");
    public JjIceGuard() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseBlock = 6; baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)); }
    public void upp() { upgradeBlock(2); upgradeMagicNumber(1); }
}
"@

Write-Card "JjBloodBait" @"
$hdr
/** 血饵花 */
public class JjBloodBait extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodBait");
    public JjBloodBait() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 3); addToBot(new DrawCardAction(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@

Write-Card "JjGhostEcho" @"
$hdr
/** 鬼洞回响 */
public class JjGhostEcho extends AbstractJingjueCard {
    public static final String ID = makeID("JjGhostEcho");
    public JjGhostEcho() { super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 14; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY); }
    public void upp() { upgradeDamage(4); }
}
"@

Write-Card "JjSandWall" @"
$hdr
/** 幻沙壁 */
public class JjSandWall extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandWall");
    public JjSandWall() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseBlock = 6; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); JingjueCombatState.executeLineBonusThisTurn += magicNumber; }
    public void upp() { upgradeBlock(3); }
}
"@

Write-Card "JjBrand" @"
$hdr
/** 祭品烙印 */
public class JjBrand extends AbstractJingjueCard {
    public static final String ID = makeID("JjBrand");
    public JjBrand() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; baseSecondMagic = secondMagic = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveEye(m, magicNumber); addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, secondMagic, false), secondMagic)); }
    public void upp() { upgradeMagicNumber(1); upgradeSecondMagic(1); }
}
"@

Write-Card "JjShed" @"
$hdr
/** 蛇蜕 */
public class JjShed extends AbstractJingjueCard {
    public static final String ID = makeID("JjShed");
    public JjShed() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseBlock = 4; exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); addToBot(new DrawCardAction(p, 1)); }
    public void upp() { upgradeBlock(2); }
}
"@

Write-Card "JjDarkRiver" @"
$hdr
/** 兹独暗河 */
public class JjDarkRiver extends AbstractJingjueCard {
    public static final String ID = makeID("JjDarkRiver");
    public JjDarkRiver() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.isEmpty()) return;
        addToBot(new com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction(p.hand.group, 1, "选择一张牌虚化 2 回合。", false, c -> true, cards -> {
            for (com.megacrit.cardcrawl.cards.AbstractCard c : cards) addToBot(new EtherealizeAction(c, 2, upgraded));
        }));
    }
    public void upp() { }
}
"@

Write-Card "JjFireBug" @"
$hdr
/** 火瓢虫 */
public class JjFireBug extends AbstractJingjueCard {
    public static final String ID = makeID("JjFireBug");
    public JjFireBug() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 4; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); giveImmolate(m, magicNumber); }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
"@

Write-Card "JjReverseStep" @"
$hdr
/** 逆行步 */
public class JjReverseStep extends AbstractJingjueCard {
    public static final String ID = makeID("JjReverseStep");
    public JjReverseStep() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 6; baseMagicNumber = magicNumber = 4; }
    public void applyPowers() { int s = baseDamage; if (JingjueCombatState.gainedHourglassThisTurn) baseDamage += magicNumber; super.applyPowers(); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; if (JingjueCombatState.gainedHourglassThisTurn) baseDamage += magicNumber; super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(2); }
}
"@

Write-Card "JjBloodBlade" @"
$hdr
/** 血祭刃 */
public class JjBloodBlade extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodBlade");
    public JjBloodBlade() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 10; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 2); jjHit(m, AbstractGameAction.AttackEffect.SLASH_HEAVY); }
    public void upp() { upgradeDamage(3); }
}
"@

Write-Card "JjCorpseScent" @"
$hdr
/** 尸香幻 */
public class JjCorpseScent extends AbstractJingjueCard {
    public static final String ID = makeID("JjCorpseScent");
    public JjCorpseScent() { super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveImaginary(m, magicNumber); addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@

Write-Card "JjSandstorm" @"
$hdr
/** 沙暴 */
public class JjSandstorm extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandstorm");
    public JjSandstorm() { super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY); baseDamage = 9; isMultiDamage = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHitAll(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    public void upp() { upgradeDamage(4); }
}
"@

Write-Card "JjJadeEye" @"
$hdr
/** 玉石眼 */
public class JjJadeEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjJadeEye");
    public JjJadeEye() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.gainHourglass(1); addToBot(new DrawCardAction(p, 1)); if (upgraded) JingjueCombatState.executeLineBonusThisTurn += 2; }
    public void upp() { }
}
"@

Write-Card "JjSandTick" @"
$hdr
/** 砂之刻 */
public class JjSandTick extends AbstractJingjueCard {
    public static final String ID = makeID("JjSandTick");
    public JjSandTick() { super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.gainDilation(magicNumber); addToBot(new DrawCardAction(p, 1)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@

Write-Card "JjReverseDash" @"
$hdr
/** 逆行·疾 */
public class JjReverseDash extends AbstractJingjueCard {
    public static final String ID = makeID("JjReverseDash");
    public JjReverseDash() { super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY); baseDamage = 6; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); if (p.hasPower(TimeDilationPower.POWER_ID)) addToBot(new GainEnergyAction(1)); }
    public void upp() { upgradeDamage(3); }
}
"@

Write-Host "commons done"
