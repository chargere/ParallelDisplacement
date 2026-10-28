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
public class WasteCrown extends AbstractFvvCard {
    public static final String ID = makeID("WasteCrown");
    public WasteCrown() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
    }
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new WasteCrownPower(p));
    }
    public void upp() { upgradeBaseCost(2); }
}