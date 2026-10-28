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
public class RageCert extends AbstractFvvCard {
    public static final String ID = makeID("RageCert");
    public RageCert() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> rares = new ArrayList<>();
        for (Map.Entry<String, AbstractCard> e : CardLibrary.cards.entrySet()) {
            AbstractCard c = e.getValue();
            if (c.color == FvvEggCharacter.Enums.FVV_COLOR && c.rarity == CardRarity.RARE) {
                rares.add(c.makeCopy());
            }
        }
        if (rares.isEmpty()) return;
        java.util.Collections.shuffle(rares, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
        ArrayList<AbstractCard> choices = new ArrayList<>();
        for (int i = 0; i < Math.min(3, rares.size()); i++) {
            AbstractCard opt = rares.get(i);
            choices.add(opt);
        }
        atb(new com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsCenteredAction(choices, 1, "Discover", cards -> {
            for (AbstractCard c : cards) {
                AbstractCard copy = c.makeStatEquivalentCopy();
                copy.setCostForTurn(0);
                copy.exhaust = true;
                makeInHand(copy);
            }
            UselessPower.gain(p, p, 2);
        }));
    }
    public void upp() {
        exhaust = false;
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}