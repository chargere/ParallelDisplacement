package yourmod.cards.comingsoon;

import yourmod.cards.AbstractEasyCard;
import yourmod.ComingSoonTimeCharacter;
import yourmod.util.DayNightSystem;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/** 具有昼夜双面文本与效果的时间旅人卡牌基类。 */
public abstract class AbstractDayNightCard extends AbstractEasyCard {
    private boolean phaseInitialized;
    private boolean phaseDay;
    private boolean previewing;

    protected AbstractDayNightCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, cost, type, rarity, target, ComingSoonTimeCharacter.Enums.TIME_COLOR);
    }

    public final void refreshPhase() {
        if (previewing) return;
        boolean nowDay = DayNightSystem.isDay();
        if (phaseInitialized && phaseDay == nowDay) return;
        phaseInitialized = true;
        phaseDay = nowDay;
        updatePhaseText(nowDay);
        initializeTitle();
        initializeDescription();
    }

    @Override
    public void update() {
        super.update();
        boolean shiftHeld = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        boolean shouldPreview = shiftHeld && hb != null && hb.hovered;
        if (shouldPreview == previewing) return;
        previewing = shouldPreview;
        phaseInitialized = true;
        phaseDay = previewing ? !DayNightSystem.isDay() : DayNightSystem.isDay();
        updatePhaseText(phaseDay);
        initializeTitle();
        initializeDescription();
    }

    protected final boolean isDayPhase() {
        return phaseDay;
    }

    protected final void setPhaseText(String dayName, String dayDescription,
                                      String nightName, String nightDescription) {
        name = originalName = phaseDay ? dayName : nightName;
        rawDescription = phaseDay ? dayDescription : nightDescription;
    }

    protected abstract void updatePhaseText(boolean day);
}
