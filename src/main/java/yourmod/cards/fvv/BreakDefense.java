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