package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import yourmod.DamuhouCharacter;
import yourmod.actions.EasyModalChoiceAction;
import yourmod.cards.damuhou.DamuhouOpticChoice;
import yourmod.powers.BreachPower;
import yourmod.powers.PrismAmpPower;
import yourmod.powers.PrismDampPower;
import yourmod.util.DamuhouCombatState;
import yourmod.util.Wiz;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

/**
 * 棱镜光学分析系统：多怪每两回合选红点/倍镜；单怪回合开始 2 层破绽。
 */
public class PrismOpticSystem extends AbstractEasyRelic {
    public static final String ID = makeID("PrismOpticSystem");
    public static final float BUFF = 1.5f;
    public static final float NERF = 0.75f;
    public static final int SINGLE_BREACH = 2;
    public static final int WINDOW = 2;

    public enum Mode {
        NONE,
        RED_DOT,
        SCOPE
    }

    public Mode mode = Mode.NONE;
    public int turnsLeft = 0;
    public boolean pauseTimer = false;

    public PrismOpticSystem() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, DamuhouCharacter.Enums.DAMUHOU_COLOR);
    }

    public static PrismOpticSystem get() {
        if (AbstractDungeon.player == null) {
            return null;
        }
        AbstractRelic r = AbstractDungeon.player.getRelic(ID);
        return r instanceof PrismOpticSystem ? (PrismOpticSystem) r : null;
    }

    @Override
    public void atBattleStart() {
        DamuhouCombatState.onBattleStart();
        mode = Mode.NONE;
        turnsLeft = 0;
        pauseTimer = false;
        grayscale = false;
        counter = -1;
        stopPulse();
    }

    @Override
    public void atTurnStartPostDraw() {
        ArrayList<AbstractMonster> living = livingEnemies();
        if (living.size() <= 1) {
            enterSingle(living);
            return;
        }
        enterMulti();
    }

    private void enterSingle(ArrayList<AbstractMonster> living) {
        pauseTimer = true;
        mode = Mode.NONE;
        grayscale = true;
        stopPulse();
        counter = -1;
        clearMarks();
        if (living.size() == 1) {
            flash();
            AbstractMonster m = living.get(0);
            addToBot(new RelicAboveCreatureAction(m, this));
            addToBot(new ApplyPowerAction(m, AbstractDungeon.player, new BreachPower(m, SINGLE_BREACH), SINGLE_BREACH));
        }
    }

    private void enterMulti() {
        grayscale = false;
        if (pauseTimer) {
            pauseTimer = false;
            turnsLeft = 0;
            mode = Mode.NONE;
        }
        if (turnsLeft <= 0) {
            openChoice();
        } else {
            setCounter(turnsLeft);
            refreshMarks();
        }
    }

    private void openChoice() {
        mode = Mode.NONE;
        clearMarks();
        flash();
        beginLongPulse();
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new DamuhouOpticChoice(makeID("PrismRedDot"), () -> selectMode(Mode.RED_DOT)));
        choices.add(new DamuhouOpticChoice(makeID("PrismScope"), () -> selectMode(Mode.SCOPE)));
        addToBot(new EasyModalChoiceAction(choices, 1, DESCRIPTIONS[1]));
    }

    public void selectMode(Mode selected) {
        mode = selected;
        turnsLeft = WINDOW;
        pauseTimer = false;
        grayscale = false;
        stopPulse();
        flash();
        setCounter(turnsLeft);
        refreshMarks();
    }

    /** 回旋：红点换倍镜，倍镜换红点。没选瞄时不改。 */
    public void swapAim() {
        if (mode == Mode.RED_DOT) {
            selectMode(Mode.SCOPE);
        } else if (mode == Mode.SCOPE) {
            selectMode(Mode.RED_DOT);
        }
    }

    /** 定镜：弹出红点 / 倍镜。 */
    public void chooseAim() {
        openChoice();
    }

    public static void swapIfPresent() {
        PrismOpticSystem r = get();
        if (r != null) {
            r.swapAim();
        }
    }

    public static void chooseIfPresent() {
        PrismOpticSystem r = get();
        if (r != null) {
            r.chooseAim();
        }
    }

    public static void setIfPresent(Mode selected) {
        PrismOpticSystem r = get();
        if (r != null && selected != null && selected != Mode.NONE) {
            r.selectMode(selected);
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (mode == Mode.NONE) {
            return;
        }
        if (livingEnemies().size() <= 1) {
            clearMarks();
            return;
        }
        refreshMarks();
    }

    @Override
    public void onPlayerEndTurn() {
        if (pauseTimer || mode == Mode.NONE) {
            return;
        }
        if (livingEnemies().size() >= 2 && turnsLeft > 0) {
            turnsLeft--;
            setCounter(turnsLeft);
        }
    }

    private void refreshMarks() {
        clearMarks();
        if (mode == Mode.NONE) {
            return;
        }
        ArrayList<AbstractMonster> living = livingEnemies();
        if (living.size() <= 1) {
            return;
        }
        AbstractMonster nearest = living.get(0);
        AbstractMonster farthest = living.get(living.size() - 1);
        AbstractMonster amp = mode == Mode.RED_DOT ? nearest : farthest;
        AbstractMonster damp = mode == Mode.RED_DOT ? farthest : nearest;
        addToBot(new ApplyPowerAction(amp, AbstractDungeon.player, new PrismAmpPower(amp), 1));
        addToBot(new ApplyPowerAction(damp, AbstractDungeon.player, new PrismDampPower(damp), 1));
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(yourmod.powers.DamuhouMiddleCountsPower.POWER_ID)
                && living.size() >= 3) {
            for (int i = 1; i < living.size() - 1; i++) {
                AbstractMonster mid = living.get(i);
                addToBot(new ApplyPowerAction(mid, AbstractDungeon.player, new PrismAmpPower(mid), 1));
            }
        }
    }

    public void refreshMarksNow() {
        refreshMarks();
    }

    private void clearMarks() {
        if (AbstractDungeon.getMonsters() == null || AbstractDungeon.getMonsters().monsters == null) {
            return;
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m == null) {
                continue;
            }
            addToBot(new RemoveSpecificPowerAction(m, AbstractDungeon.player, PrismAmpPower.POWER_ID));
            addToBot(new RemoveSpecificPowerAction(m, AbstractDungeon.player, PrismDampPower.POWER_ID));
        }
    }

    private static ArrayList<AbstractMonster> livingEnemies() {
        ArrayList<AbstractMonster> living = Wiz.getEnemies();
        living.removeIf(m -> m == null || m.halfDead);
        living.sort((a, b) -> Float.compare(a.hb.cX, b.hb.cX));
        return living;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PrismOpticSystem();
    }
}
