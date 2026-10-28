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
public class JjTimeSand extends AbstractJingjueCard {
    public static final String ID = makeID("JjTimeSand");
    public JjTimeSand() { super(ID, -1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF); exhaust = true; }
    public void use(AbstractPlayer p, AbstractMonster m) { int x = energyOnUse; if (x < 0) x = EnergyPanel.totalCount; if (!freeToPlayOnce) p.energy.use(x); JingjueCombatState.nextCardsDiscount += x; for (AbstractCard c : p.hand.group) if (c.costForTurn >= 1) c.setCostForTurn(c.costForTurn - 1); }
    public void upp() { exhaust = false; }
}
