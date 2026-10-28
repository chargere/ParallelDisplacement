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
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction;
import yourmod.FvvEggCharacter;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

'@
function Card($file, $body) { W $file ($hdr + $body) }

Card "WasteRecycle.java" @'
public class WasteRecycle extends AbstractFvvCard {
    public static final String ID = makeID("WasteRecycle");
    public WasteRecycle() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 3;
        baseMagicNumber = magicNumber = 5;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spent = UselessPower.spendAll(p);
        int draws = Math.min(spent, magicNumber);
        for (int i = 0; i < spent; i++) {
            atb(new GainBlockAction(p, p, block));
        }
        if (draws > 0) atb(new DrawCardAction(draws));
    }
    public void upp() { upgradeBlock(1); upgradeMagicNumber(2); }
}
'@

Card "BreakDefense.java" @'
public class BreakDefense extends AbstractFvvCard {
    public static final String ID = makeID("BreakDefense");
    public BreakDefense() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 4;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spent = UselessPower.spendAll(p);
        baseDamage += spent * magicNumber;
        calculateCardDamage(m);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        baseDamage = upgraded ? 8 : 5;
    }
    public void applyPowers() {
        int bonus = UselessPower.getAmount(AbstractDungeon.player) * magicNumber;
        int real = baseDamage;
        baseDamage += bonus;
        super.applyPowers();
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int bonus = UselessPower.getAmount(AbstractDungeon.player) * magicNumber;
        int real = baseDamage;
        baseDamage += bonus;
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void upp() { upgradeDamage(3); upgradeMagicNumber(1); }
}
'@

Card "PlayDead.java" @'
public class PlayDead extends AbstractFvvCard {
    public static final String ID = makeID("PlayDead");
    public PlayDead() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new IntangiblePlayerPower(p, 1));
        UselessPower.gain(p, p, magicNumber);
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "LateAwareness.java" @'
public class LateAwareness extends AbstractFvvCard {
    public static final String ID = makeID("LateAwareness");
    public LateAwareness() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new LateAwarenessPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "PeerReview.java" @'
public class PeerReview extends AbstractFvvCard {
    public static final String ID = makeID("PeerReview");
    public PeerReview() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        selfRetain = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("承认", "获得 " + magicNumber + " #y没用。", () -> {
            UselessPower.gain(p, p, magicNumber);
        }));
        int drawAmt = upgraded ? 3 : 2;
        choices.add(new EasyModalChoiceCard("花钱", "花 " + secondMagic + " #y没用，抽 " + drawAmt + "。", () -> {
            if (UselessPower.getAmount(p) >= secondMagic) {
                UselessPower.spend(p, secondMagic);
                atb(new DrawCardAction(drawAmt));
            }
        }));
        atb(new EasyModalChoiceAction(choices));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "JustRun.java" @'
public class JustRun extends AbstractFvvCard {
    public static final String ID = makeID("JustRun");
    public JustRun() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 3;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int spent = UselessPower.spendAll(p);
        for (int i = 0; i < spent; i++) {
            dmgRandom(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
    }
    public void upp() { upgradeDamage(1); }
}
'@

Card "FakeHustle.java" @'
public class FakeHustle extends AbstractFvvCard {
    public static final String ID = makeID("FakeHustle");
    public FakeHustle() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 4;
        baseDamage = 4;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        UselessPower.gain(p, p, 1);
    }
    public void applyPowers() {
        baseDamage = magicNumber * (FvvCombatState.cardsPlayedThisTurn + 1);
        super.applyPowers();
        isDamageModified = true;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = magicNumber * (FvvCombatState.cardsPlayedThisTurn + 1);
        super.calculateCardDamage(mo);
        isDamageModified = true;
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "LeaveNote.java" @'
public class LeaveNote extends AbstractFvvCard {
    public static final String ID = makeID("LeaveNote");
    public LeaveNote() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 15;
        cardsToPreview = new SlackOff();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        shuffleIn(new SlackOff());
    }
    public void upp() { upgradeBlock(5); }
}
'@

Card "ArgueLine.java" @'
public class ArgueLine extends AbstractFvvCard {
    public static final String ID = makeID("ArgueLine");
    public ArgueLine() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseBlock = 2;
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m != null && m.getIntentBaseDmg() >= 0) {
            UselessPower.gain(p, p, magicNumber);
        } else {
            blck();
        }
    }
    public void upp() { upgradeDamage(3); }
}
'@

Card "AdmitCommittee.java" @'
public class AdmitCommittee extends AbstractFvvCard {
    public static final String ID = makeID("AdmitCommittee");
    public AdmitCommittee() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new AdmitCommitteePower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "SlackCombo.java" @'
public class SlackCombo extends AbstractFvvCard {
    public static final String ID = makeID("SlackCombo");
    public SlackCombo() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 6;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (FvvCombatState.lastCardType == CardType.SKILL) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            UselessPower.gain(p, p, 1);
        }
    }
    public void upp() { upgradeDamage(2); }
}
'@

Card "ReviewMeeting.java" @'
public class ReviewMeeting extends AbstractFvvCard {
    public static final String ID = makeID("ReviewMeeting");
    public ReviewMeeting() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 4;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.discardPile.isEmpty()) return;
        ArrayList<AbstractCard> opts = new ArrayList<>(p.discardPile.group);
        atb(new SelectCardsCenteredAction(opts, 1, "选择一张入手", cards -> {
            for (AbstractCard c : cards) {
                p.discardPile.removeCard(c);
                p.hand.addToHand(c);
                c.lighten(false);
                if (c.type == CardType.ATTACK) UselessPower.gain(p, p, 1);
                else if (c.type == CardType.SKILL) atb(new GainBlockAction(p, p, block));
            }
        }));
        if (upgraded) atb(new DrawCardAction(1));
    }
    public void upp() {
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "EnergyFraud.java" @'
public class EnergyFraud extends AbstractFvvCard {
    public static final String ID = makeID("EnergyFraud");
    public EnergyFraud() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(2));
        applyToSelf(new EnergyFraudDebtPower(p, 2));
        UselessPower.gain(p, p, 1);
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "ControlTest.java" @'
public class ControlTest extends AbstractFvvCard {
    public static final String ID = makeID("ControlTest");
    public ControlTest() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        int extra = UselessPower.getEffective(p) * magicNumber;
        if (extra > 0) {
            AbstractMonster lowest = null;
            for (AbstractMonster mo : getEnemies()) {
                if (lowest == null || mo.currentHealth < lowest.currentHealth) lowest = mo;
            }
            if (lowest != null) {
                atb(new DamageAction(lowest, new DamageInfo(p, extra, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
        }
    }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
'@

Card "PassiveAggression.java" @'
public class PassiveAggression extends AbstractFvvCard {
    public static final String ID = makeID("PassiveAggression");
    public PassiveAggression() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new PassiveAggressionPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "UnsubscribeLife.java" @'
public class UnsubscribeLife extends AbstractFvvCard {
    public static final String ID = makeID("UnsubscribeLife");
    public UnsubscribeLife() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.STATUS || c.type == CardType.CURSE) toRemove.add(c);
        }
        for (AbstractCard c : toRemove) {
            p.hand.moveToExhaustPile(c);
            UselessPower.gain(p, p, magicNumber);
            atb(new DrawCardAction(1));
        }
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "OpenBox.java" @'
public class OpenBox extends AbstractFvvCard {
    public static final String ID = makeID("OpenBox");
    public OpenBox() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int roll = AbstractDungeon.cardRandomRng.random(upgraded ? 2 : 3);
        switch (roll) {
            case 0: UselessPower.gain(p, p, 3); break;
            case 1: atb(new DrawCardAction(2)); break;
            case 2:
                atb(new LoseHPAction(p, p, 3));
                UselessPower.gain(p, p, 5);
                break;
            default:
                shuffleIn(new ManualBook());
                break;
        }
    }
    public void upp() {
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "AfkHang.java" @'
public class AfkHang extends AbstractFvvCard {
    public static final String ID = makeID("AfkHang");
    public AfkHang() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new AfkPower(p, magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "PaidPoop.java" @'
public class PaidPoop extends AbstractFvvCard {
    public static final String ID = makeID("PaidPoop");
    public PaidPoop() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyAction(1));
        UselessPower.gain(p, p, 2);
        atb(new DrawCardAction(1));
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "ChatMute.java" @'
public class ChatMute extends AbstractFvvCard {
    public static final String ID = makeID("ChatMute");
    public ChatMute() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ChatMutePower(p, magicNumber));
        UselessPower.gain(p, p, 2);
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "ExamStrike.java" @'
public class ExamStrike extends AbstractFvvCard {
    public static final String ID = makeID("ExamStrike");
    public ExamStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 30;
        tags.add(CardTags.STRIKE);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
    }
    public void applyPowers() {
        baseDamage = Math.min(magicNumber, FvvCombatState.uselessGainedThisCombat);
        super.applyPowers();
        isDamageModified = true;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = Math.min(magicNumber, FvvCombatState.uselessGainedThisCombat);
        super.calculateCardDamage(mo);
        isDamageModified = true;
    }
    public void upp() { upgradeMagicNumber(10); }
}
'@

Card "NumberScapegoat.java" @'
public class NumberScapegoat extends AbstractFvvCard {
    public static final String ID = makeID("NumberScapegoat");
    public NumberScapegoat() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new StrengthPower(m, -magicNumber));
        UselessPower.gain(p, p, 2);
    }
    public void upp() {
        upgradeMagicNumber(1);
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Write-Host "Uncommon done count:" (Get-ChildItem $cardDir).Count
