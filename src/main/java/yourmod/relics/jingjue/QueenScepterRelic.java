package yourmod.relics.jingjue;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.JingjueCharacter;
import yourmod.cards.jingjue.AbstractJingjueCard;
import yourmod.relics.AbstractEasyRelic;
import yourmod.util.JingjueCombatState;

import static yourmod.ModFile.makeID;

public class QueenScepterRelic extends AbstractEasyRelic {
    public static final String ID = makeID("JjQueenScepter");
    private boolean firstAttack;

    public QueenScepterRelic() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, JingjueCharacter.Enums.JINGJUE_COLOR);
    }

    @Override
    public void atBattleStart() {
        JingjueCombatState.splashBonusPct += 30;
        firstAttack = true;
    }

    @Override
    public void atTurnStart() {
        firstAttack = true;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        discount(card);
    }

    @Override
    public void onRefreshHand() {
        if (AbstractDungeonReady()) {
            for (AbstractCard c : com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.hand.group) {
                discount(c);
            }
        }
    }

    private boolean AbstractDungeonReady() {
        return com.megacrit.cardcrawl.dungeons.AbstractDungeon.player != null;
    }

    private void discount(AbstractCard card) {
        if (firstAttack && card.type == AbstractCard.CardType.ATTACK && card.costForTurn > 0) {
            card.setCostForTurn(0);
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            firstAttack = false;
        }
        if (c instanceof AbstractJingjueCard && ((AbstractJingjueCard) c).splashPct <= 0) {
            ((AbstractJingjueCard) c).splashPct = 30;
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new QueenScepterRelic();
    }
}
