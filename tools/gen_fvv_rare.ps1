$ErrorActionPreference = "Stop"
$root = "c:\Users\38220\Desktop\ProTemplate-master"
$cardDir = Join-Path $root "src\main\java\yourmod\cards\fvv"
$utf8 = New-Object System.Text.UTF8Encoding $false
function W($name, $content) {
  [System.IO.File]::WriteAllText((Join-Path $cardDir $name), $content, $utf8)
}
$hdr = @'
package yourmod.cards.fvv;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import yourmod.FvvEggCharacter;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;
import java.util.Map;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

'@
function Card($file, $body) { W $file ($hdr + $body) }

Card "WasteEconomy.java" @'
public class WasteEconomy extends AbstractFvvCard {
    public static final String ID = makeID("WasteEconomy");
    public WasteEconomy() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new UselessEconomyPower(p, magicNumber));
    }
    public void upp() {
        upgradeMagicNumber(1);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "UltimateUseless.java" @'
public class UltimateUseless extends AbstractFvvCard {
    public static final String ID = makeID("UltimateUseless");
    public UltimateUseless() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 0;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int u = UselessPower.spendAll(p);
        baseDamage = Math.min(99, u * u);
        calculateCardDamage(m);
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
        baseDamage = 0;
    }
    public void applyPowers() {
        int u = UselessPower.getAmount(AbstractDungeon.player);
        baseDamage = Math.min(99, u * u);
        super.applyPowers();
        isDamageModified = true;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int u = UselessPower.getAmount(AbstractDungeon.player);
        baseDamage = Math.min(99, u * u);
        super.calculateCardDamage(mo);
        isDamageModified = true;
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "JustLikeThis.java" @'
public class JustLikeThis extends AbstractFvvCard {
    public static final String ID = makeID("JustLikeThis");
    public JustLikeThis() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new JustLikeThisPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "SocialDeath.java" @'
public class SocialDeath extends AbstractFvvCard {
    public static final String ID = makeID("SocialDeath");
    public SocialDeath() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 8;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
        applyToEnemy(m, new WeakPower(m, magicNumber, false));
        UselessPower.gain(p, p, secondMagic);
    }
    public void upp() {
        upgradeMagicNumber(-1);
        upgradeSecondMagic(2);
    }
}
'@

Card "TheoryCorrect.java" @'
public class TheoryCorrect extends AbstractFvvCard {
    public static final String ID = makeID("TheoryCorrect");
    public TheoryCorrect() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 10;
        isInnate = true;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasAtk = false;
        for (AbstractCard c : p.drawPile.group) if (c.type == CardType.ATTACK) { hasAtk = true; break; }
        if (!hasAtk) for (AbstractCard c : p.discardPile.group) if (c.type == CardType.ATTACK) { hasAtk = true; break; }
        if (!hasAtk) UselessPower.gain(p, p, magicNumber);
        else {
            UselessPower.gain(p, p, 2);
            atb(new DrawCardAction(1));
        }
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "FillerStrike.java" @'
public class FillerStrike extends AbstractFvvCard {
    public static final String ID = makeID("FillerStrike");
    public FillerStrike() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
        cardsToPreview = new FvvStrike();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            FvvStrike s = new FvvStrike();
            s.freeToPlayOnce = true;
            s.setCostForTurn(0);
            s.exhaust = true;
            s.exhaustOnUseOnce = true;
            makeInHand(s);
        }
        UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "BornWithIt.java" @'
public class BornWithIt extends AbstractFvvCard {
    public static final String ID = makeID("BornWithIt");
    public BornWithIt() {
        super(ID, 0, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        isEthereal = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BornWithItPower(p));
    }
    public void upp() {
        isEthereal = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "RageFinale.java" @'
public class RageFinale extends AbstractFvvCard {
    public static final String ID = makeID("RageFinale");
    public RageFinale() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 12;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
    public void applyPowers() {
        int bonus = FvvCombatState.uselessGainedThisCombat;
        int real = baseDamage;
        baseDamage += bonus;
        super.applyPowers();
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int bonus = FvvCombatState.uselessGainedThisCombat;
        int real = baseDamage;
        baseDamage += bonus;
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void upp() {
        upgradeDamage(3);
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "GroupSlack.java" @'
public class GroupSlack extends AbstractFvvCard {
    public static final String ID = makeID("GroupSlack");
    public GroupSlack() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 3;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new WeakPower(mo, magicNumber, false));
            applyToEnemy(mo, new VulnerablePower(mo, magicNumber, false));
        });
        UselessPower.gain(p, p, secondMagic);
    }
    public void upp() {
        upgradeMagicNumber(1);
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "WasteCrown.java" @'
public class WasteCrown extends AbstractFvvCard {
    public static final String ID = makeID("WasteCrown");
    public WasteCrown() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new WasteCrownPower(p));
    }
    public void upp() { upgradeBaseCost(2); }
}
'@

Card "OneClickBreak.java" @'
public class OneClickBreak extends AbstractFvvCard {
    public static final String ID = makeID("OneClickBreak");
    public OneClickBreak() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 3;
        isMultiDamage = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spent = UselessPower.spendAll(p);
        if (spent < 1) return;
        for (int i = 0; i < spent; i++) {
            allDmg(AbstractGameAction.AttackEffect.FIRE);
        }
    }
    public void applyPowers() {
        int n = Math.max(1, UselessPower.getAmount(AbstractDungeon.player));
        // preview one wave
        super.applyPowers();
    }
    public void upp() { upgradeDamage(1); }
}
'@

Card "ExistCrisis.java" @'
public class ExistCrisis extends AbstractFvvCard {
    public static final String ID = makeID("ExistCrisis");
    public ExistCrisis() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int n = p.hand.size();
        atb(new DiscardAction(p, p, n, false));
        atb(new DrawCardAction(n));
        UselessPower.gain(p, p, n);
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "FamousEffect.java" @'
public class FamousEffect extends AbstractFvvCard {
    public static final String ID = makeID("FamousEffect");
    public FamousEffect() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FamousEffectPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "SelfUltimatum.java" @'
public class SelfUltimatum extends AbstractFvvCard {
    public static final String ID = makeID("SelfUltimatum");
    public SelfUltimatum() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 25;
        baseMagicNumber = magicNumber = 3;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (UselessPower.getAmount(p) >= 8) {
            UselessPower.spendAll(p);
            dmg(m, AbstractGameAction.AttackEffect.SMASH);
        } else {
            UselessPower.gain(p, p, magicNumber);
        }
    }
    public void upp() {
        upgradeDamage(5);
        upgradeMagicNumber(1);
    }
}
'@

Card "NoWords.java" @'
public class NoWords extends AbstractFvvCard {
    public static final String ID = makeID("NoWords");
    public NoWords() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        UselessPower.gain(p, p, magicNumber);
        atb(new DrawCardAction(1));
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "CasualHit.java" @'
public class CasualHit extends AbstractFvvCard {
    public static final String ID = makeID("CasualHit");
    public CasualHit() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 12;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (FvvCombatState.playedZeroCostThisTurn) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            UselessPower.gain(p, p, 2);
        }
    }
    public void upp() { upgradeDamage(3); }
}
'@

Card "FetalFail.java" @'
public class FetalFail extends AbstractFvvCard {
    public static final String ID = makeID("FetalFail");
    public FetalFail() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FetalFailPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "UselessFly.java" @'
public class UselessFly extends AbstractFvvCard {
    public static final String ID = makeID("UselessFly");
    public UselessFly() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 15;
        baseSecondMagic = secondMagic = 20;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int u = UselessPower.getAmount(p);
        if (u >= magicNumber) {
            applyToSelf(new UselessAscendPower(p, 2));
        } else {
            int doubled = Math.min(secondMagic, u * 2);
            int need = doubled - u;
            if (need > 0) UselessPower.gainSilent(p, need);
            else if (doubled < u) UselessPower.spend(p, u - doubled);
        }
    }
    public void upp() {
        upgradeMagicNumber(-3);
        upgradeSecondMagic(5);
    }
}
'@

Card "ManualSet.java" @'
public class ManualSet extends AbstractFvvCard {
    public static final String ID = makeID("ManualSet");
    public ManualSet() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        cardsToPreview = new ManualBook();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        shuffleIn(new ManualBook(), magicNumber);
        applyToSelf(new NextTurnDrawUselessPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(-1); }
}
'@

Card "PlacentaAscend.java" @'
public class PlacentaAscend extends AbstractFvvCard {
    public static final String ID = makeID("PlacentaAscend");
    public PlacentaAscend() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 4;
        isMultiDamage = true;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int layers = UselessPower.spendAll(p);
        layers += exhaustSlack(p.hand);
        layers += exhaustSlack(p.drawPile);
        layers += exhaustSlack(p.discardPile);
        for (int i = 0; i < layers; i++) {
            allDmg(AbstractGameAction.AttackEffect.FIRE);
        }
        if (!upgraded) applyToSelf(new SkipNextTurnPower(p));
    }
    private int exhaustSlack(com.megacrit.cardcrawl.cards.CardGroup g) {
        ArrayList<AbstractCard> rem = new ArrayList<>();
        for (AbstractCard c : g.group) {
            if (c instanceof SlackOff || c instanceof SlackOffHard) rem.add(c);
        }
        for (AbstractCard c : rem) {
            g.moveToExhaustPile(c);
        }
        return rem.size();
    }
    public void upp() {
        upgradeDamage(1);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

# Event cards
Card "BattleUseless.java" @'
public class BattleUseless extends AbstractFvvCard {
    public static final String ID = makeID("BattleUseless");
    public BattleUseless() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        selfRetain = true;
        exhaust = true;
        isInnate = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        UselessPower.gain(p, p, magicNumber);
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "TodayOffline.java" @'
public class TodayOffline extends AbstractFvvCard {
    public static final String ID = makeID("TodayOffline");
    public TodayOffline() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new NextCardBoostPower(p, magicNumber));
    }
    public void upp() {
        upgradeMagicNumber(1);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "BalancePatch.java" @'
public class BalancePatch extends AbstractFvvCard {
    public static final String ID = makeID("BalancePatch");
    public BalancePatch() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BalanceHealPower(p));
    }
    public void upp() { upgradeBaseCost(1); }
}
'@

Card "LeaveReview.java" @'
public class LeaveReview extends AbstractFvvCard {
    public static final String ID = makeID("LeaveReview");
    public LeaveReview() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 5;
        cardsToPreview = new ReadReceipt();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        shuffleIn(new ReadReceipt());
    }
    public void upp() { upgradeBlock(3); }
}
'@

Card "ReportReward.java" @'
public class ReportReward extends AbstractFvvCard {
    public static final String ID = makeID("ReportReward");
    public ReportReward() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m != null && (m.hasPower(WeakPower.POWER_ID) || m.hasPower(VulnerablePower.POWER_ID))) {
            UselessPower.gain(p, p, 3);
        }
    }
    public void upp() { upgradeDamage(3); }
}
'@

Card "ServerDown.java" @'
public class ServerDown extends AbstractFvvCard {
    public static final String ID = makeID("ServerDown");
    public ServerDown() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new StrengthPower(mo, -magicNumber));
            applyToEnemy(mo, new GainStrengthPower(mo, magicNumber));
        });
        applyToSelf(new SkipNextDrawPower(p, 1));
        UselessPower.gain(p, p, 4);
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "IAmEgg.java" @'
public class IAmEgg extends AbstractFvvCard {
    public static final String ID = makeID("IAmEgg");
    public IAmEgg() {
        super(ID, 0, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        isEthereal = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new IAmEggPower(p));
    }
    public void upp() {
        isEthereal = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "RageCert.java" @'
public class RageCert extends AbstractFvvCard {
    public static final String ID = makeID("RageCert");
    public RageCert() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> rares = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> e : CardLibrary.cards.entrySet()) {
            AbstractCard c = e.getValue();
            if (c.color == FvvEggCharacter.Enums.FVV_COLOR && c.rarity == CardRarity.RARE) {
                rares.add(c.makeCopy());
            }
        }
        if (rares.isEmpty()) return;
        java.util.Collections.shuffle(rares, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        ArrayList<AbstractCard> choices = new ArrayList<>();
        for (int i = 0; i < Math.min(3, rares.size()); i++) {
            AbstractCard opt = rares.get(i);
            choices.add(opt);
        }
        atb(new com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction(choices, 1, "发现", cards -> {
            for (AbstractCard c : cards) {
                AbstractCard copy = c.makeStatEquivalentCopy();
                copy.setCostForTurn(0);
                copy.exhaust = true;
                makeInHand(copy);
            }
            UselessPower.gain(p, p, 2);
        }));
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

# Status / derived
Card "SlackOff.java" @'
@AutoAdd.Ignore
public class SlackOff extends AbstractEasyCard {
    public static final String ID = makeID("SlackOff");
    public SlackOff() {
        super(ID, -2, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, CardColor.COLORLESS);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {}
    public void triggerWhenDrawn() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        UselessPower.gainTop(AbstractDungeon.player, AbstractDungeon.player, 1);
    }
    public void upp() {}
}
'@

Card "SlackOffHard.java" @'
@AutoAdd.Ignore
public class SlackOffHard extends AbstractEasyCard {
    public static final String ID = makeID("SlackOffHard");
    public SlackOffHard() {
        super(ID, -2, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, CardColor.COLORLESS);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {}
    public void triggerWhenDrawn() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        UselessPower.gainTop(AbstractDungeon.player, AbstractDungeon.player, 2);
    }
    public void upp() {}
}
'@

Card "ManualBook.java" @'
@AutoAdd.Ignore
public class ManualBook extends AbstractEasyCard {
    public static final String ID = makeID("ManualBook");
    public ManualBook() {
        super(ID, -2, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE, CardColor.CURSE);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {}
    public void triggerWhenDrawn() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        addToTop(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
        UselessPower.gainTop(AbstractDungeon.player, AbstractDungeon.player, 1);
    }
    public void upp() {}
}
'@

Card "OvertimeMail.java" @'
@AutoAdd.Ignore
public class OvertimeMail extends AbstractEasyCard {
    public static final String ID = makeID("OvertimeMail");
    public OvertimeMail() {
        super(ID, -2, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, CardColor.COLORLESS);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {}
    public void triggerWhenDrawn() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
        UselessPower.gainTop(AbstractDungeon.player, AbstractDungeon.player, 1);
    }
    public void upp() {}
}
'@

Card "ReadReceipt.java" @'
@AutoAdd.Ignore
public class ReadReceipt extends AbstractEasyCard {
    public static final String ID = makeID("ReadReceipt");
    public ReadReceipt() {
        super(ID, 0, CardType.STATUS, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        selfRetain = true;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(1));
        UselessPower.gain(p, p, 1);
    }
    public void upp() {}
}
'@

Card "PlacentaShard.java" @'
@AutoAdd.Ignore
public class PlacentaShard extends AbstractEasyCard {
    public static final String ID = makeID("PlacentaShard");
    public PlacentaShard() {
        super(ID, 0, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = 1;
        exhaust = true;
        isEthereal = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        UselessPower.gain(p, p, 1);
    }
    public void upp() {}
}
'@

Write-Host "Total cards:" (Get-ChildItem $cardDir).Count
