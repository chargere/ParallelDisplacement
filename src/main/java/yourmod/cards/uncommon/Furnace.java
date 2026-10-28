package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 熔炉（1费）
 * 升级前：消耗手中所有非攻击；每张获得 6 格挡并抽1
 * 升级后：每张获得 8 格挡并抽1
 */
public class Furnace extends AbstractEasyCard {
    public final static String ID = makeID("Furnace");

    public Furnace() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> toExhaust = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c != this && c.type != CardType.ATTACK) {
                toExhaust.add(c);
            }
        }
        for (AbstractCard c : toExhaust) {
            atb(new ExhaustSpecificCardAction(c, p.hand));
            atb(new GainBlockAction(p, p, block));
            atb(new DrawCardAction(1));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
