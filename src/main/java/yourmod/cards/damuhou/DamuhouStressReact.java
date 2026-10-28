package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 应激反应 */
public class DamuhouStressReact extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouStressReact");

    public DamuhouStressReact() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> drawn = new ArrayList<>();
                int n = Math.min(magicNumber, p.drawPile.size());
                for (int i = 0; i < n; i++) {
                    if (p.drawPile.isEmpty()) {
                        break;
                    }
                    AbstractCard c = p.drawPile.getTopCard();
                    p.drawPile.removeCard(c);
                    drawn.add(c);
                }
                for (AbstractCard c : drawn) {
                    if (c.type == AbstractCard.CardType.ATTACK) {
                        p.hand.addToHand(c);
                        c.setCostForTurn(Math.max(0, c.costForTurn - 1));
                    } else {
                        p.drawPile.addToTop(c);
                    }
                }
                p.hand.refreshHandLayout();
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
