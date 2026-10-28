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
            s.cost = 0;
            s.costForTurn = 0;
            s.isCostModified = true;
            s.exhaust = true;
            s.isEthereal = true;
            makeInHand(s);
        }
        UselessPower.gain(p, p, 1);
    }
    public void upp() { upgradeMagicNumber(1); }
}