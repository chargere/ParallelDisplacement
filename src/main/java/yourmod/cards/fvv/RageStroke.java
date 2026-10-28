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