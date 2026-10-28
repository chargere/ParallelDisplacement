package yourmod.cards.damuhou;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.EasyModalChoiceCard;
import yourmod.relics.PrismOpticSystem;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/** 改瞄：对调 / 红点 / 倍镜。 */
public class DamuhouRetune extends AbstractDamuhouCard {
    public static final String ID = makeID("DamuhouRetune");

    public DamuhouRetune() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 4;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        ArrayList<com.megacrit.cardcrawl.cards.AbstractCard> choices = new ArrayList<>();
        choices.add(new EasyModalChoiceCard("对调", "红点与倍镜互换。", PrismOpticSystem::swapIfPresent));
        choices.add(new EasyModalChoiceCard("红点", "切换为红点。", () ->
                PrismOpticSystem.setIfPresent(PrismOpticSystem.Mode.RED_DOT)));
        choices.add(new EasyModalChoiceCard("倍镜", "切换为倍镜。", () ->
                PrismOpticSystem.setIfPresent(PrismOpticSystem.Mode.SCOPE)));
        addToBot(new EasyModalChoiceAction(choices, 1, "改瞄"));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}
