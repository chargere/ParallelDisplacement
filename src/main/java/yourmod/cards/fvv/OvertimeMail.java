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
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.powers.*;
import yourmod.util.FvvCombatState;

import java.util.ArrayList;
import java.util.Map;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;
@AutoAdd.Ignore
public class OvertimeMail extends AbstractEasyCard {
    public static final String ID = makeID("OvertimeMail");
    public OvertimeMail() {
        super(ID, -2, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, CardColor.COLORLESS);
        exhaust = true;
    }
    public void use(AbstractPlayer p, AbstractMonster m) {}
    public void triggerWhenDrawn() {
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, false));
        UselessPower.gainTop(AbstractDungeon.player, AbstractDungeon.player, 1);
    }
    public void upp() {}
}