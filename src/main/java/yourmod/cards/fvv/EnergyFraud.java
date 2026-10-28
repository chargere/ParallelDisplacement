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