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
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
"@

W "JjWalkingIllusion" @"
$h
public class JjWalkingIllusion extends AbstractJingjueCard {
    public static final String ID = makeID("JjWalkingIllusion");
    public JjWalkingIllusion() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new WalkingIllusionPower(p, 1)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjDeepEye" @"
$h
public class JjDeepEye extends AbstractJingjueCard {
    public static final String ID = makeID("JjDeepEye");
    public JjDeepEye() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 4; }
    public void use(AbstractPlayer p, AbstractMonster m) { giveEye(m, magicNumber); if (hasImaginary(m)) addToBot(new DrawCardAction(p, 1)); }
    public void upp() { upgradeMagicNumber(2); }
}
"@
W "JjTimeAnchor" @"
$h
public class JjTimeAnchor extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeAnchor");
    public JjTimeAnchor() { super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { int heal = JingjueCombatState.damageTakenThisTurn / 2; if (heal > 0) addToBot(new HealAction(p, p, heal)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjRewind" @"
$h
public class JjRewind extends AbstractJingjueCard {
    public static final String ID = makeID("JjRewind");
    public JjRewind() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!JingjueCombatState.consumeHourglass(magicNumber)) return;
        addToBot(new DrawCardAction(p, 2));
        if (!p.hand.isEmpty()) addToBot(new SelectCardsAction(p.hand.group, 1, "将一张牌放到抽牌堆顶。", false, c -> true, cards -> {
            for (AbstractCard c : cards) { p.hand.removeCard(c); p.drawPile.addToTop(c); }
        }));
    }
    public void upp() { upgradeMagicNumber(-1); }
}
"@
W "JjCorridor" @"
$h
public class JjCorridor extends AbstractJingjueCard {
    public static final String ID = makeID("JjCorridor");
    public JjCorridor() { super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new CorridorPower(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(2); }
}
"@
W "JjAltar" @"
$h
public class JjAltar extends AbstractJingjueCard {
    public static final String ID = makeID("JjAltar");
    public JjAltar() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new AltarHourglassPower(p, 1)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjExecuteCut" @"
$h
public class JjExecuteCut extends AbstractJingjueCard {
    public static final String ID = makeID("JjExecuteCut");
    public JjExecuteCut() { super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 14; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HEAVY); addToBot(new ExecuteAction(m)); }
    public void upp() { upgradeDamage(4); }
}
"@
W "JjNineTower" @"
$h
public class JjNineTower extends AbstractJingjueCard {
    public static final String ID = makeID("JjNineTower");
    public JjNineTower() { super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseBlock = 9; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); JingjueCombatState.gainHourglass(magicNumber); }
    public void upp() { upgradeBlock(3); upgradeMagicNumber(1); }
}
"@
W "JjKunlunWood" @"
$h
public class JjKunlunWood extends AbstractJingjueCard {
    public static final String ID = makeID("JjKunlunWood");
    public JjKunlunWood() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 7; }
    public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new HealAction(p, p, magicNumber)); Wiz.forAllMonstersLiving(mo -> giveEye(mo, 1)); }
    public void upp() { upgradeMagicNumber(3); }
}
"@
W "JjDapuBug" @"
$h
public class JjDapuBug extends AbstractJingjueCard {
    public static final String ID = makeID("JjDapuBug");
    public JjDapuBug() { super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { if (p.hasPower(DapuFormPower.POWER_ID)) ((DapuFormPower)p.getPower(DapuFormPower.POWER_ID)).toggle(); else applyToSelf(new DapuFormPower(p)); }
    public void upp() { upgradeBaseCost(0); }
}
"@
W "JjBeastGod" @"
$h
public class JjBeastGod extends AbstractJingjueCard {
    public static final String ID = makeID("JjBeastGod");
    public JjBeastGod() { super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 11; splashPct = 60; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.FIRE); }
    public void upp() { upgradeDamage(4); splashPct = 80; }
}
"@
W "JjStyxSpear" @"
$h
public class JjStyxSpear extends AbstractJingjueCard {
    public static final String ID = makeID("JjStyxSpear");
    public JjStyxSpear() { super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseDamage = 4; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { for (int i = 0; i < magicNumber; i++) { int d = 4 + i * 2; addToBot(new JingjueDamageAction(AbstractDungeon.getRandomMonster(), new DamageInfo(p, d, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, false, 0)); } }
    public void upp() { upgradeMagicNumber(1); }
}
"@
W "JjJingjianRev" @"
$h
public class JjJingjianRev extends AbstractJingjueCard {
    public static final String ID = makeID("JjJingjianRev");
    public JjJingjianRev() { super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 10; baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (hasImaginary(m)) giveEye(m, magicNumber); else jjHit(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL); }
    public void upp() { upgradeDamage(3); upgradeMagicNumber(1); }
}
"@
W "JjGhostCopper" @"
$h
public class JjGhostCopper extends AbstractJingjueCard {
    public static final String ID = makeID("JjGhostCopper");
    public JjGhostCopper() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { Wiz.forAllMonstersLiving(mo -> { addToBot(new ApplyPowerAction(mo, p, new PoisonPower(mo, p, magicNumber), magicNumber)); giveEye(mo, 1); }); }
    public void upp() { upgradeMagicNumber(2); }
}
"@
W "JjBloodGrudge" @"
$h
public class JjBloodGrudge extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodGrudge");
    public JjBloodGrudge() { super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 16; splashPct = 30; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 4); jjHit(m, AbstractGameAction.AttackEffect.SLASH_HEAVY); }
    public void upp() { upgradeDamage(4); splashPct = 40; }
}
"@
W "JjThunderMount" @"
$h
public class JjThunderMount extends AbstractJingjueCard {
    public static final String ID = makeID("JjThunderMount");
    public JjThunderMount() { super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseDamage = 13; isMultiDamage = true; baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHitAll(AbstractGameAction.AttackEffect.LIGHTNING); Wiz.forAllMonstersLiving(mo -> giveImaginary(mo, magicNumber)); }
    public void upp() { upgradeDamage(4); }
}
"@
W "JjTimeMissile" @"
$h
public class JjTimeMissile extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeMissile");
    public JjTimeMissile() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 2; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (!JingjueCombatState.consumeHourglass(3)) return; int d = JingjueCombatState.hourglass() * magicNumber + 3 * magicNumber; addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(d, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@
W "JjSkirmishStone" @"
$h
public class JjSkirmishStone extends AbstractJingjueCard {
    public static final String ID = makeID("JjSkirmishStone");
    public JjSkirmishStone() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new SkirmishStonePower(p, 3)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjWolfHead" @"
$h
public class JjWolfHead extends AbstractJingjueCard {
    public static final String ID = makeID("JjWolfHead");
    public JjWolfHead() { super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 7; }
    public void calculateCardDamage(AbstractMonster mo) { int s = baseDamage; if (hasEye(mo)) baseDamage *= 2; super.calculateCardDamage(mo); baseDamage = s; isDamageModified = damage != baseDamage; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HEAVY); }
    public void upp() { upgradeDamage(2); }
}
"@
W "JjIceSeal" @"
$h
public class JjIceSeal extends AbstractJingjueCard {
    public static final String ID = makeID("JjIceSeal");
    public JjIceSeal() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY); baseMagicNumber = magicNumber = 2; baseSecondMagic = secondMagic = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)); addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, secondMagic, false), secondMagic)); }
    public void upp() { upgradeSecondMagic(1); }
}
"@
W "JjBloodRite" @"
$h
public class JjBloodRite extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodRite");
    public JjBloodRite() { super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 2; baseSecondMagic = secondMagic = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { for (int i = 0; i < magicNumber; i++) addToBot(new MakeTempCardInHandAction(new JjBloodMark(), 1)); JingjueCombatState.gainHourglass(secondMagic); }
    public void upp() { upgradeMagicNumber(1); upgradeSecondMagic(1); }
}
"@
W "JjRavenSword" @"
$h
public class JjRavenSword extends AbstractJingjueCard {
    public static final String ID = makeID("JjRavenSword");
    public JjRavenSword() { super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 7; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); addToBot(new DrawCardAction(p, JingjueCombatState.executedThisTurn ? 2 : 1)); }
    public void upp() { upgradeDamage(2); }
}
"@
W "JjSoulPestle" @"
$h
public class JjSoulPestle extends AbstractJingjueCard {
    public static final String ID = makeID("JjSoulPestle");
    public JjSoulPestle() { super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 6; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT); giveEye(m, 1); jjHit(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY); giveEye(m, 1); }
    public void upp() { upgradeDamage(2); }
}
"@
W "JjCycleStop" @"
$h
public class JjCycleStop extends AbstractJingjueCard {
    public static final String ID = makeID("JjCycleStop");
    public JjCycleStop() { super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); }
    public void use(AbstractPlayer p, AbstractMonster m) { Wiz.forAllMonstersLiving(mo -> { giveImaginary(mo, 1); if (hasImaginary(mo)) giveImaginary(mo, 1); }); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjRegenCool" @"
$h
public class JjRegenCool extends AbstractJingjueCard {
    public static final String ID = makeID("JjRegenCool");
    public JjRegenCool() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseBlock = 8; baseMagicNumber = magicNumber = 30; }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); JingjueCombatState.splashThisTurnPct += magicNumber; }
    public void upp() { upgradeBlock(3); }
}
"@
W "JjTimeHunt" @"
$h
public class JjTimeHunt extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeHunt");
    public JjTimeHunt() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new HuntPower(p, 1)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjReverseProof" @"
$h
public class JjReverseProof extends AbstractJingjueCard {
    public static final String ID = makeID("JjReverseProof");
    public JjReverseProof() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new ReverseWalkerPower(p, 1)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjRealize" @"
$h
public class JjRealize extends AbstractJingjueCard {
    public static final String ID = makeID("JjRealize");
    public JjRealize() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { Wiz.forAllMonstersLiving(mo -> { if (mo.hasPower(ImaginaryPower.POWER_ID)) { int n = mo.getPower(ImaginaryPower.POWER_ID).amount; addToBot(new DamageAction(mo, new DamageInfo(p, n * magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE)); addToBot(new RemoveSpecificPowerAction(mo, p, ImaginaryPower.POWER_ID)); } }); }
    public void upp() { upgradeMagicNumber(1); }
}
"@
W "JjBloodMother" @"
$h
public class JjBloodMother extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodMother");
    public JjBloodMother() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY); baseDamage = 11; isMultiDamage = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { payHp(p, 5); jjHitAll(AbstractGameAction.AttackEffect.POISON); Wiz.forAllMonstersLiving(mo -> giveEye(mo, 2)); }
    public void upp() { upgradeDamage(3); }
}
"@
W "JjDantian" @"
$h
public class JjDantian extends AbstractJingjueCard {
    public static final String ID = makeID("JjDantian");
    public JjDantian() { super(ID, 3, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new DantianAltarPower(p, 3)); }
    public void upp() { upgradeBaseCost(2); }
}
"@
W "JjEyePool" @"
$h
public class JjEyePool extends AbstractJingjueCard {
    public static final String ID = makeID("JjEyePool");
    public JjEyePool() { super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { java.util.ArrayList<AbstractCard> hand = new java.util.ArrayList<>(p.hand.group); int n = hand.size(); for (AbstractCard c : hand) addToBot(new EtherealizeAction(c, 1, false)); addToBot(new DrawCardAction(p, n)); }
    public void upp() { upgradeBaseCost(1); }
}
"@
W "JjSerpentForce" @"
$h
public class JjSerpentForce extends AbstractJingjueCard {
    public static final String ID = makeID("JjSerpentForce");
    public JjSerpentForce() { super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new SerpentForcePower(p, 1)); }
    public void upp() { }
}
"@
W "JjJingjianBite" @"
$h
public class JjJingjianBite extends AbstractJingjueCard {
    public static final String ID = makeID("JjJingjianBite");
    public JjJingjianBite() { super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY); baseDamage = 11; }
    public void use(AbstractPlayer p, AbstractMonster m) { jjHit(m, AbstractGameAction.AttackEffect.POISON); if (hasImaginary(m) && m.hasPower(ImaginaryPower.POWER_ID)) { int stored = ((ImaginaryPower)m.getPower(ImaginaryPower.POWER_ID)).storedDamage; if (stored > 0) addToBot(new GainBlockAction(p, p, stored)); } }
    public void upp() { upgradeDamage(3); }
}
"@
W "JjTimeField" @"
$h
public class JjTimeField extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeField");
    public JjTimeField() { super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 1; }
    public void use(AbstractPlayer p, AbstractMonster m) { applyToSelf(new TimeFieldPower(p, magicNumber)); }
    public void upp() { upgradeMagicNumber(1); }
}
"@
W "JjTimeLoan" @"
$h
public class JjTimeLoan extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeLoan");
    public JjTimeLoan() { super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); if (!upgraded) JingjueCombatState.nextTurnDrawPenalty += 1; }
    public void upp() { }
}
"@
W "JjTimeAccel" @"
$h
public class JjTimeAccel extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeAccel");
    public JjTimeAccel() { super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 3; }
    public void use(AbstractPlayer p, AbstractMonster m) { if (!JingjueCombatState.consumeHourglass(magicNumber)) return; for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); }
    public void upp() { upgradeMagicNumber(-1); }
}
"@
W "JjCausalSketch" @"
$h
public class JjCausalSketch extends AbstractJingjueCard {
    public static final String ID = makeID("JjCausalSketch");
    public JjCausalSketch() { super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF); baseMagicNumber = magicNumber = 2; exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new DrawCardAction(p, magicNumber)); addToBot(new AbstractGameAction() { public void update() { for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); isDone = true; } }); }
    public void upp() { upgradeMagicNumber(1); }
}
"@
Write-Host "uncommons done"
