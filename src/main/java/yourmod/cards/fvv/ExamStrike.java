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