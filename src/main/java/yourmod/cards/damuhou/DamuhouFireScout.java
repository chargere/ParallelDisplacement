package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 火力侦察 */
public class DamuhouFireScout extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouFireScout");

    public DamuhouFireScout() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 3;
        printedCombo = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        comboHits(m, 2, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> top = new ArrayList<>();
                int n = Math.min(magicNumber, p.drawPile.size());
                for (int i = 0; i < n; i++) {
                    top.add(p.drawPile.group.get(p.drawPile.size() - 1 - i));
                }
                ArrayList<AbstractCard> attacks = new ArrayList<>();
                for (AbstractCard c : top) {
                    if (c.type == AbstractCard.CardType.ATTACK) {
                        attacks.add(c);
                    }
                }
                if (!attacks.isEmpty()) {
                    AbstractDungeon.actionManager.addToTop(new SelectCardsAction(attacks, attacks.size(),
                            "你可以弃置其中的攻击牌。", true, card -> true, cards -> {
                        for (AbstractCard c : cards) {
                            AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(c, p.drawPile));
                        }
                    }));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}
