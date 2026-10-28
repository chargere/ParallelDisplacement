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
public class CopyPaste extends AbstractFvvCard {
    public static final String ID = makeID("CopyPaste");
    public CopyPaste() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SelectCardsInHandAction(1, "Copy", false, false,
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