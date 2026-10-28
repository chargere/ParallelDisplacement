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