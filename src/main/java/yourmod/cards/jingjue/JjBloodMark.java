package yourmod.cards.jingjue;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

/** 血印 */
@AutoAdd.Ignore
public class JjBloodMark extends AbstractJingjueCard {
    public static final String ID = makeID("JjBloodMark");

    public JjBloodMark() {
        super(ID, -2, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE);
        color = CardColor.COLORLESS;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (AbstractDungeon.player != null) {
            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 2));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void upp() {
    }
}
