$dir = "c:\Users\38220\Desktop\ProTemplate-master\src\main\java\yourmod\cards\jingjue"
function W($name, $body) { Set-Content -Path (Join-Path $dir "$name.java") -Value $body -Encoding UTF8 }
$h = @"
package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
"@

W "JjShadowCity" @"
$h
public class JjShadowCity extends AbstractJingjueCard {
    public static final String ID = makeID("JjShadowCity");
    public JjShadowCity() { super(ID, 3, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new AltarHourglassPower(p, 0)); addToBot(new AbstractGameAction() { public void update() { if (!p.discardPile.isEmpty()) { AbstractCard c = p.discardPile.getRandomCard(true); if (c != null && c.type == AbstractCard.CardType.ATTACK) addToTop(new EtherealizeAction(c, 2, false)); } isDone = true; } }); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjBoundlessEye" @"
$h
public class JjBoundlessEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjBoundlessEye");
    public JjBoundlessEye() { super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { Wiz.forAllMonstersLiving(mo -> giveImaginary(mo, 2)); applyToSelf(new BoundlessEyePower(p, 1)); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjWorldlyPearl" @"
$h
public class JjWorldlyPearl extends AbstractJingjueCard {
    public static final String ID = makeID("JjWorldlyPearl");
    public JjWorldlyPearl() { super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new WorldlyPearlPower(p, 10, 4, 20)); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjPhoenixStrike" @"
$h
public class JjPhoenixStrike extends AbstractJingjueCard {
    public static final String ID = makeID("JjPhoenixStrike");
    public JjPhoenixStrike() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 16; splashPct = 60; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); addToBot(new AbstractGameAction() { public void update() { if (m != null && m.isDeadOrEscaped()) JingjueCombatState.executeLineBonusPct += 3; isDone = true; } }); }
    public void upp() { upgradeDamage(4); splashPct = 80; }
}
"@
W "JjCrystalQuake" @"
$h
public class JjCrystalQuake extends AbstractJingjueCard {
    public static final String ID = makeID("JjCrystalQuake");
    public JjCrystalQuake() { super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseDamage = 20; isMultiDamage = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHitAll(AbstractGameAction.AttackEffect.LIGHTNING); Wiz.forAllMonstersLiving(mo -> giveImaginary(mo, 1)); }
    public void upp() { upgradeDamage(6); }
}
"@
W "JjSnakeBone" @"
$h
public class JjSnakeBone extends AbstractJingjueCard {
    public static final String ID = makeID("JjSnakeBone");
    public JjSnakeBone() { super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF); baseMagicNumber = magicNumber = 10; }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new SnakeBonePower(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(5); }
}
"@
W "JjDevour" @"
$h
public class JjDevour extends AbstractJingjueCard {
    public static final String ID = makeID("JjDevour");
    public JjDevour() { super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY); }
    public void use(AbstractPlayer p, AbstractMonster m) { if (m != null && m.hasPower(ImaginaryPower.POWER_ID)) { int n = m.getPower(ImaginaryPower.POWER_ID).amount; addToBot(new RemoveSpecificPowerAction(m, p, ImaginaryPower.POWER_ID)); giveEye(m, n); addToBot(new ExecuteAction(m)); } }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjBlackFace" @"
$h
public class JjBlackFace extends AbstractJingjueCard {
    public static final String ID = makeID("JjBlackFace");
    public JjBlackFace() { super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new BlackFacePower(p, 2)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjTimeRewind" @"
$h
public class JjTimeRewind extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeRewind");
    public JjTimeRewind() { super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.nextTurnHeal += Math.max(JingjueCombatState.damageTakenThisTurn, 1); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjFinalEye" @"
$h
public class JjFinalEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjFinalEye");
    public JjFinalEye() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 30; baseMagicNumber = magicNumber = 5; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (m != null && m.type == AbstractMonster.EnemyType.NORMAL) addToBot(new ExecuteAction(m)); else { jjHit(m, AbstractGameAction.AttackEffect.FIRE); giveEye(m, magicNumber); } }
    public void upp() { upgradeDamage(10); upgradeMagicNumber(1); }
}
"@
W "JjStoneLag" @"
$h
public class JjStoneLag extends AbstractJingjueCard {
    public static final String ID = makeID("JjStoneLag");
    public JjStoneLag() { super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new WalkingIllusionPower(p, 1)); applyToSelf(new SkirmishStonePower(p, 5)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjImmolateAll" @"
$h
public class JjImmolateAll extends AbstractJingjueCard {
    public static final String ID = makeID("JjImmolateAll");
    public JjImmolateAll() { super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { int d = JingjueCombatState.hourglass() * magicNumber; addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(Math.max(1, d), true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)); addToBot(new RemoveSpecificPowerAction(p, p, HourglassPower.POWER_ID)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@
W "JjIceFinale" @"
$h
public class JjIceFinale extends AbstractJingjueCard {
    public static final String ID = makeID("JjIceFinale");
    public JjIceFinale() { super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY); baseBlock = 20; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); Wiz.forAllMonstersLiving(mo -> { addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, 2, false), 2)); addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, upgraded ? 2 : 1, false), upgraded ? 2 : 1)); }); }
    public void upp() { upgradeBlock(6); }
}
"@
W "JjBloodCycle" @"
$h
public class JjBloodCycle extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodCycle");
    public JjBloodCycle() { super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF); baseMagicNumber = magicNumber = 5; }
    public void use(AbstractPlayer p, AbstractMonster m) { p.decreaseMaxHealth(upgraded ? 3 : 5); applyToSelf(new StrengthPower(p, magicNumber)); applyToSelf(new DexterityPower(p, magicNumber)); }
    public void upp() { }
}
"@
W "JjJingjianLord" @"
$h
public class JjJingjianLord extends AbstractJingjueCard {
    public static final String ID = makeID("JjJingjianLord");
    public JjJingjianLord() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 12; baseMagicNumber = magicNumber = 3; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; if (hasImaginary(mo)) baseDamage += magicNumber * mo.getPower(ImaginaryPower.POWER_ID).amount; super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.POISON); }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
"@
W "JjZagramma" @"
$h
public class JjZagramma extends AbstractJingjueCard {
    public static final String ID = makeID("JjZagramma");
    public JjZagramma() { super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new ZagrammaEyePower(p)); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjTimeEnd" @"
$h
public class JjTimeEnd extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeEnd");
    public JjTimeEnd() { super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseDamage = 9; isMultiDamage = true; exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { int x = energyOnUse; if (x < 0) x = EnergyPanel.totalCount; if (!freeToPlayOnce) p.energy.use(x); int d = x * (upgraded ? 12 : 9); splashPct = x * 25; addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(d, true), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SMASH)); }
    public void upp() { }
}
"@
W "JjSnakeSword" @"
$h
public class JjSnakeSword extends AbstractJingjueCard {
    public static final String ID = makeID("JjSnakeSword");
    public JjSnakeSword() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 16; splashPct = 80; baseMagicNumber = magicNumber = 3; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; if (hasImaginary(mo)) baseDamage *= 2; super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HEAVY); giveEye(m, magicNumber); }
    public void upp() { upgradeDamage(4); splashPct = 100; upgradeMagicNumber(1); }
}
"@
W "JjSnakeSpear" @"
$h
public class JjSnakeSpear extends AbstractJingjueCard {
    public static final String ID = makeID("JjSnakeSpear");
    public JjSnakeSpear() { super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 10; splashPct = 50; exhaust = true; }
    public void applyPowers() { int s = baseDamage; baseDamage += 2 * JingjueCombatState.hourglass(); super.applyPowers(); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; baseDamage += 2 * JingjueCombatState.hourglass(); super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    public void upp() { upgradeDamage(4); splashPct = 70; }
}
"@
W "JjThunderBeast" @"
$h
public class JjThunderBeast extends AbstractJingjueCard {
    public static final String ID = makeID("JjThunderBeast");
    public JjThunderBeast() { super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY); baseDamage = 30; isMultiDamage = true; splashPct = 100; exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHitAll(AbstractGameAction.AttackEffect.LIGHTNING); Wiz.forAllMonstersLiving(mo -> giveImaginary(mo, 3)); }
    public void upp() { upgradeDamage(10); }
}
"@
W "JjTimeDevour" @"
$h
public class JjTimeDevour extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeDevour");
    public JjTimeDevour() { super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new ReverseWalkerPower(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(2); }
}
"@
W "JjCausalBreak" @"
$h
public class JjCausalBreak extends AbstractJingjueCard {
    public static final String ID = makeID("JjCausalBreak");
    public JjCausalBreak() { super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (m == null) return; if (JingjueCombatState.isEliteOrBoss(m)) { int loss = Math.max(1, m.maxHealth / 4); addToBot(new LoseHPAction(m, p, loss)); if (m.hasPower(ImaginaryPower.POWER_ID)) ((ImaginaryPower)m.getPower(ImaginaryPower.POWER_ID)).clearStored(); } else addToBot(new ExecuteAction(m)); }
    public void upp() { upgradeBaseCost(0); }
}
"@
W "JjImagFlood" @"
$h
public class JjImagFlood extends AbstractJingjueCard {
    public static final String ID = makeID("JjImagFlood");
    public JjImagFlood() { super(ID, -1, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { int x = energyOnUse; if (x < 0) x = EnergyPanel.totalCount; if (!freeToPlayOnce) p.energy.use(x); JingjueCombatState.gainHourglass(x * (upgraded ? 4 : 3)); java.util.ArrayList<AbstractCard> hand = new java.util.ArrayList<>(p.hand.group); int n = Math.min(x, hand.size()); for (int i = 0; i < n; i++) addToBot(new EtherealizeAction(hand.get(i), 1, false)); }
    public void upp() { }
}
"@
W "JjThunderVolley" @"
$h
public class JjThunderVolley extends AbstractJingjueCard {
    public static final String ID = makeID("JjThunderVolley");
    public JjThunderVolley() { super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY); baseDamage = 12; splashPct = 60; }
    public void use(AbstractPlayer p, AbstractMonster m) { for (int i = 0; i < 3; i++) jjHit(m, AbstractGameAction.AttackEffect.LIGHTNING); }
    public void upp() { upgradeDamage(3); }
}
"@
W "JjTimeExpand" @"
$h
public class JjTimeExpand extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeExpand");
    public JjTimeExpand() { super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new TimeFieldPower(p, 1)); }
    public void upp() { }
}
"@
W "JjTimeSand" @"
$h
public class JjTimeSand extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeSand");
    public JjTimeSand() { super(ID, -1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { int x = energyOnUse; if (x < 0) x = EnergyPanel.totalCount; if (!freeToPlayOnce) p.energy.use(x); JingjueCombatState.nextCardsDiscount += x; for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); }
    public void upp() { exhaust = false; }
}
"@
W "JjTimePause" @"
$h
public class JjTimePause extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimePause");
    public JjTimePause() { super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { JingjueCombatState.keepEnergy = EnergyPanel.getCurrentEnergy(); JingjueCombatState.keepBlock = p.currentBlock; addToBot(new DrawCardAction(p, 2)); }
    public void upp() { upgradeBaseCost(2); }
}
"@
Write-Host "rares done"
