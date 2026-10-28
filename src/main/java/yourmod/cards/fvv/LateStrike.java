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
public class LateStrike extends AbstractFvvCard {
    public static final String ID = makeID("LateStrike");
    public LateStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseSecondDamage = secondDamage = 11;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        // onCardPlayed already counted this card
        if (FvvCombatState.cardsPlayedThisTurn >= 3) {
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