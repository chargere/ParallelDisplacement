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
public class JjShadowCity extends AbstractJingjueCard {
    public static final String ID = makeID("JjShadowCity");
    public JjShadowCity() { super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF); }
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            public void update() {
                if (!p.discardPile.isEmpty()) {
                    AbstractCard c = p.discardPile.getRandomCard(AbstractCard.CardType.ATTACK, true);
                    if (c == null) {
                        c = p.discardPile.getRandomCard(true);
                    }
                    if (c != null) {
                        p.discardPile.removeCard(c);
                        addToTop(new EtherealizeAction(c, 2, false));
                    }
                }
                isDone = true;
            }
        });
    }
    public void upp() { upgradeBaseCost(1); }
}
