package yourmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cards.AbstractEasyCard;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

/**
 * 裁断（0费）
 * 升级前：弃光手牌，每张 3 格挡；抽3；若弃过攻击则+2能量再抽3；消耗
 * 升级后：每张 4 格挡
 */
public class Judgment extends AbstractEasyCard {
    public final static String ID = makeID("Judgment");

    public Judgment() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 3;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> handCopy = new ArrayList<>(p.hand.group);
        handCopy.remove(this);
        boolean hadAttack = false;
        int count = handCopy.size();
        for (AbstractCard c : handCopy) {
            if (c.type == CardType.ATTACK) hadAttack = true;
        }
        if (count > 0) {
            atb(new DiscardAction(p, p, count, false));
            atb(new GainBlockAction(p, p, block * count));
        }
        atb(new DrawCardAction(3));
        if (hadAttack) {
            atb(new GainEnergyAction(2));
            atb(new DrawCardAction(3));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(1);
    }
}
