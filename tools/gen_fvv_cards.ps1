$ErrorActionPreference = "Stop"
$root = "c:\Users\38220\Desktop\ProTemplate-master"
$cardDir = Join-Path $root "src\main\java\yourmod\cards\fvv"
$utf8 = New-Object System.Text.UTF8Encoding $false
function W($name, $content) {
  [System.IO.File]::WriteAllText((Join-Path $cardDir $name), $content, $utf8)
}

$hdr = @'
package yourmod.cards.fvv;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
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
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.relics.UselessFetus;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;
import java.util.Collections;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

'@

# Helper to wrap class
function Card($file, $body) { W $file ($hdr + $body) }

Card "FvvStrike.java" @'
public class FvvStrike extends AbstractFvvCard {
    public static final String ID = makeID("FvvStrike");
    public FvvStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 6;
        tags.add(CardTags.STRIKE);
        tags.add(CardTags.STARTER_STRIKE);
    }
    public void use(AbstractPlayer p, AbstractMonster m) { dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT); }
    public void upp() { upgradeDamage(3); }
}
'@

Card "FvvDefend.java" @'
public class FvvDefend extends AbstractFvvCard {
    public static final String ID = makeID("FvvDefend");
    public FvvDefend() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 5;
        tags.add(CardTags.STARTER_DEFEND);
    }
    public void use(AbstractPlayer p, AbstractMonster m) { blck(); }
    public void upp() { upgradeBlock(2); }
}
'@

Card "AdmitUseless.java" @'
public class AdmitUseless extends AbstractFvvCard {
    public static final String ID = makeID("AdmitUseless");
    public AdmitUseless() {
        super(ID, 0, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        UselessPower.gain(p, p, magicNumber);
        atb(new DrawCardAction(1));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "UsefulPunch.java" @'
public class UsefulPunch extends AbstractFvvCard {
    public static final String ID = makeID("UsefulPunch");
    public UsefulPunch() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 1;
        baseMagicNumber = magicNumber = 1;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        UselessPower.gain(p, p, magicNumber);
    }
    public void upp() { upgradeDamage(1); upgradeMagicNumber(1); }
}
'@

Card "ReallyHurts.java" @'
public class ReallyHurts extends AbstractFvvCard {
    public static final String ID = makeID("ReallyHurts");
    public ReallyHurts() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        atb(new LoseHPAction(p, p, magicNumber));
    }
    public void upp() { upgradeDamage(3); upgradeMagicNumber(-1); }
}
'@

Card "ThinkLife.java" @'
public class ThinkLife extends AbstractFvvCard {
    public static final String ID = makeID("ThinkLife");
    public ThinkLife() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(magicNumber));
        atb(new DiscardAction(p, p, 1, false));
        atb(new AbstractGameAction() {
            public void update() {
                if (FvvCombatState.damageDealtThisTurn == 0) {
                    UselessPower.gain(p, p, secondMagic);
                }
                isDone = true;
            }
        });
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "UselessStrike.java" @'
public class UselessStrike extends AbstractFvvCard {
    public static final String ID = makeID("UselessStrike");
    public UselessStrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 3;
        tags.add(CardTags.STRIKE);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        UselessPower.gain(p, p, magicNumber);
    }
    public void upp() { upgradeDamage(2); upgradeMagicNumber(1); }
}
'@

Card "UselessDefend.java" @'
public class UselessDefend extends AbstractFvvCard {
    public static final String ID = makeID("UselessDefend");
    public UselessDefend() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 3;
        baseMagicNumber = magicNumber = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        UselessPower.gain(p, p, magicNumber);
    }
    public void upp() { upgradeBlock(2); upgradeMagicNumber(1); }
}
'@

Card "RageStroke.java" @'
public class RageStroke extends AbstractFvvCard {
    public static final String ID = makeID("RageStroke");
    public RageStroke() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
    }
    public void use(AbstractPlayer p, AbstractMonster m) { dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    public void applyPowers() {
        int bonus = UselessPower.getEffective(AbstractDungeon.player);
        int real = baseDamage;
        baseDamage += bonus;
        super.applyPowers();
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        int bonus = UselessPower.getEffective(AbstractDungeon.player);
        int real = baseDamage;
        baseDamage += bonus;
        super.calculateCardDamage(mo);
        baseDamage = real;
        isDamageModified = damage != baseDamage;
    }
    public void upp() { upgradeDamage(2); }
}
'@

Card "LieFlat.java" @'
public class LieFlat extends AbstractFvvCard {
    public static final String ID = makeID("LieFlat");
    public LieFlat() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseBlock = 0;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        UselessPower.gain(p, p, magicNumber);
        if (upgraded && baseBlock > 0) blck();
        atb(new PressEndTurnButtonAction());
    }
    public void upp() {
        upgradeMagicNumber(1);
        upgradeBlock(4);
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "PickTrash.java" @'
public class PickTrash extends AbstractFvvCard {
    public static final String ID = makeID("PickTrash");
    public PickTrash() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int max = Math.min(magicNumber, p.hand.size());
        if (max <= 0) return;
        atb(new SelectCardsInHandAction(max, "弃置", true, true, c -> true, cards -> {
            for (AbstractCard c : cards) {
                atb(new DiscardSpecificCardAction(c));
                UselessPower.gain(p, p, 1);
                atb(new DrawCardAction(1));
            }
        }));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "VerbalPromise.java" @'
public class VerbalPromise extends AbstractFvvCard {
    public static final String ID = makeID("VerbalPromise");
    public VerbalPromise() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new NextTurnEnergyLossPower(p, 1));
    }
    public void upp() { upgradeBlock(4); }
}
'@

Card "FakeDiligent.java" @'
public class FakeDiligent extends AbstractFvvCard {
    public static final String ID = makeID("FakeDiligent");
    public FakeDiligent() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 3;
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        UselessPower.gain(p, p, 1);
    }
    public void upp() {
        upgradeDamage(2);
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "SelfCritique.java" @'
public class SelfCritique extends AbstractFvvCard {
    public static final String ID = makeID("SelfCritique");
    public SelfCritique() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 4;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new WeakPower(p, magicNumber, false));
        UselessPower.gain(p, p, secondMagic);
    }
    public void upp() { upgradeMagicNumber(-1); upgradeSecondMagic(1); }
}
'@

Card "PassBuck.java" @'
public class PassBuck extends AbstractFvvCard {
    public static final String ID = makeID("PassBuck");
    public PassBuck() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new WeakPower(m, magicNumber, false));
        UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "NotBad.java" @'
public class NotBad extends AbstractFvvCard {
    public static final String ID = makeID("NotBad");
    public NotBad() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (UselessPower.getAmount(p) >= magicNumber) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }
    public void upp() { upgradeDamage(2); }
}
'@

Card "SlackDefend.java" @'
public class SlackDefend extends AbstractFvvCard {
    public static final String ID = makeID("SlackDefend");
    public SlackDefend() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        cardsToPreview = new SlackOff();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        shuffleIn(new SlackOff());
    }
    public void upp() { upgradeBlock(3); }
}
'@

Card "LateStrike.java" @'
public class LateStrike extends AbstractFvvCard {
    public static final String ID = makeID("LateStrike");
    public LateStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseSecondDamage = secondDamage = 11;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        // this card counts as about to be played; cardsPlayedThisTurn not yet incremented
        if (FvvCombatState.cardsPlayedThisTurn + 1 >= 3) {
            baseDamage = baseSecondDamage;
            calculateCardDamage(m);
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            baseDamage = upgraded ? 6 : 4;
        } else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }
    public void applyPowers() {
        if (FvvCombatState.cardsPlayedThisTurn + 1 >= 3) {
            int real = baseDamage;
            baseDamage = baseSecondDamage;
            super.applyPowers();
            baseDamage = real;
            isDamageModified = true;
        } else {
            super.applyPowers();
        }
    }
    public void calculateCardDamage(AbstractMonster mo) {
        if (FvvCombatState.cardsPlayedThisTurn + 1 >= 3) {
            int real = baseDamage;
            baseDamage = baseSecondDamage;
            super.calculateCardDamage(mo);
            baseDamage = real;
            isDamageModified = true;
        } else {
            super.calculateCardDamage(mo);
        }
    }
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(3);
    }
}
'@

Card "BuckHammer.java" @'
public class BuckHammer extends AbstractFvvCard {
    public static final String ID = makeID("BuckHammer");
    public BuckHammer() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (p.hand.size() >= 5) UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeDamage(3); }
}
'@

Card "Insignificant.java" @'
public class Insignificant extends AbstractFvvCard {
    public static final String ID = makeID("Insignificant");
    public Insignificant() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        selfRetain = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) { UselessPower.gain(p, p, magicNumber); }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "BatteryDead.java" @'
public class BatteryDead extends AbstractFvvCard {
    public static final String ID = makeID("BatteryDead");
    public BatteryDead() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        int n = Math.min(p.hand.size(), magicNumber);
        int gain = n;
        atb(new DiscardAction(p, p, p.hand.size(), false));
        UselessPower.gain(p, p, gain);
    }
    public void upp() { upgradeMagicNumber(2); }
}
'@

Card "SurfaceWork.java" @'
public class SurfaceWork extends AbstractFvvCard {
    public static final String ID = makeID("SurfaceWork");
    public SurfaceWork() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 15;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
        UselessPower.gain(p, p, 1);
    }
    public void applyPowers() {
        baseDamage = Math.min(magicNumber, FvvCombatState.blockGainedThisTurn);
        super.applyPowers();
        isDamageModified = true;
    }
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = Math.min(magicNumber, FvvCombatState.blockGainedThisTurn);
        super.calculateCardDamage(mo);
        isDamageModified = true;
    }
    public void upp() { upgradeMagicNumber(5); }
}
'@

Card "InnerDrain.java" @'
public class InnerDrain extends AbstractFvvCard {
    public static final String ID = makeID("InnerDrain");
    public InnerDrain() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.loseEnergy(1);
        atb(new DrawCardAction(magicNumber));
        UselessPower.gain(p, p, secondMagic);
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "AlmostOk.java" @'
public class AlmostOk extends AbstractFvvCard {
    public static final String ID = makeID("AlmostOk");
    public AlmostOk() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        if (UselessPower.getAmount(p) >= 2) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }
    public void upp() { upgradeDamage(2); }
}
'@

Card "ReadNoReply.java" @'
public class ReadNoReply extends AbstractFvvCard {
    public static final String ID = makeID("ReadNoReply");
    public ReadNoReply() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 7;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new NoSkillsPower(p));
        UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeBlock(3); }
}
'@

Card "ReplyRead.java" @'
public class ReplyRead extends AbstractFvvCard {
    public static final String ID = makeID("ReplyRead");
    public ReplyRead() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseSecondDamage = secondDamage = 12;
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (FvvCombatState.playedSkillThisTurn) {
            int real = baseDamage;
            baseDamage = baseSecondDamage;
            calculateCardDamage(m);
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            baseDamage = real;
        } else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            UselessPower.gain(p, p, magicNumber);
        }
    }
    public void applyPowers() {
        if (FvvCombatState.playedSkillThisTurn) {
            int real = baseDamage;
            baseDamage = baseSecondDamage;
            super.applyPowers();
            baseDamage = real;
            isDamageModified = true;
        } else super.applyPowers();
    }
    public void calculateCardDamage(AbstractMonster mo) {
        if (FvvCombatState.playedSkillThisTurn) {
            int real = baseDamage;
            baseDamage = baseSecondDamage;
            super.calculateCardDamage(mo);
            baseDamage = real;
            isDamageModified = true;
        } else super.calculateCardDamage(mo);
    }
    public void upp() {
        upgradeDamage(2);
        upgradeSecondDamage(3);
    }
}
'@

Card "KeyboardWarrior.java" @'
public class KeyboardWarrior extends AbstractFvvCard {
    public static final String ID = makeID("KeyboardWarrior");
    public KeyboardWarrior() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 3;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmgRandom(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        UselessPower.gain(p, p, 1);
        if (UselessPower.getAmount(p) >= 1) {
            ArrayList<AbstractCard> choices = new ArrayList<>();
            choices.add(new EasyModalChoiceCard("再喷一次", "花 1 #y没用，再对随机敌人造成伤害。", () -> {
                UselessPower.spend(p, 1);
                dmgRandom(AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            }));
            choices.add(new EasyModalChoiceCard("算了", "不做。", () -> {}));
            atb(new EasyModalChoiceAction(choices));
        }
    }
    public void upp() { upgradeDamage(1); }
}
'@

Card "SlackAddiction.java" @'
public class SlackAddiction extends AbstractFvvCard {
    public static final String ID = makeID("SlackAddiction");
    public SlackAddiction() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        cardsToPreview = new SlackOff();
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        shuffleIn(new SlackOff(), 2);
        atb(new DrawCardAction(magicNumber));
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "OvertimeStrike.java" @'
public class OvertimeStrike extends AbstractFvvCard {
    public static final String ID = makeID("OvertimeStrike");
    public OvertimeStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        tags.add(CardTags.STRIKE);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        // hand size after play typically excludes this card
        if (p.hand.size() <= 2) UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeDamage(3); }
}
'@

Card "BuckProduct.java" @'
public class BuckProduct extends AbstractFvvCard {
    public static final String ID = makeID("BuckProduct");
    public BuckProduct() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
        UselessPower.gain(p, p, secondMagic);
    }
    public void upp() { upgradeMagicNumber(1); }
}
'@

Card "CopyPaste.java" @'
public class CopyPaste extends AbstractFvvCard {
    public static final String ID = makeID("CopyPaste");
    public CopyPaste() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SelectCardsInHandAction(1, "复制", false, false,
            c -> c.rarity != CardRarity.RARE && c != this, cards -> {
            for (AbstractCard c : cards) {
                AbstractCard copy = c.makeStatEquivalentCopy();
                if (!upgraded) {
                    copy.modifyCostForCombat(1);
                }
                copy.exhaust = true;
                makeInHand(copy);
            }
        }));
        UselessPower.gain(p, p, 1);
    }
    public void upp() {
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
'@

Card "NonsenseTalk.java" @'
public class NonsenseTalk extends AbstractFvvCard {
    public static final String ID = makeID("NonsenseTalk");
    public NonsenseTalk() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(3));
        atb(new DiscardAction(p, p, magicNumber, false));
        UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeMagicNumber(-1); }
}
'@

Write-Host "Common cards done:" (Get-ChildItem $cardDir).Count
