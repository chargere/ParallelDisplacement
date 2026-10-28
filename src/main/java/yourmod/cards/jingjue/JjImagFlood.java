package yourmod.cards.jingjue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.powers.jingjue.*;
import yourmod.util.JingjueCombatState;
import yourmod.util.Wiz;
import yourmod.actions.jingjue.*;
import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
public class JjImagFlood extends AbstractJingjueCard {
    public static final String ID = makeID("JjImagFlood");
    public JjImagFlood() { super(ID, -1, CardType.POWER, CardRarity.RARE, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) { int x = energyOnUse; if (x < 0) x = EnergyPanel.totalCount; if (!freeToPlayOnce) p.energy.use(x); JingjueCombatState.gainHourglass(x * (upgraded ? 4 : 3)); java.util.ArrayList<AbstractCard> hand = new java.util.ArrayList<>(p.hand.group); int n = Math.min(x, hand.size()); for (int i = 0; i < n; i++) addToBot(new EtherealizeAction(hand.get(i), 1, false)); }
    public void upp() { }
}
