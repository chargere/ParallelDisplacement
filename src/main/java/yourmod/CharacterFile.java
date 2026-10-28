package yourmod;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import yourmod.cards.Defend;
import yourmod.cards.StarfireCrossbow;
import yourmod.cards.Strike;
import yourmod.cards.ThunderEdge;
import yourmod.relics.CompassionFire;

import java.util.ArrayList;

import static yourmod.CharacterFile.Enums.TODO_COLOR;
import static yourmod.ModFile.*;

/**
 * 鸿文君主角色类：初始卡组、遗物、选人界面与各种角色外观回调。
 */
public class CharacterFile extends CustomPlayer {

    static final String ID = makeID("ModdedCharacter");
    static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;

    /** 选人界面主标题下的副标题（NAMES[2]） */
    public static String getSelectSubtitle() {
        if (NAMES != null && NAMES.length > 2 && NAMES[2] != null) {
            return NAMES[2];
        }
        return "凯文·齐亚腾";
    }


    public CharacterFile(String name, PlayerClass setClass) {
        // 能量球贴图 + Spriter 动画
        super(name, setClass, new CustomEnergyOrb(orbTextures, makeCharacterPath("mainChar/orb/vfx.png"), null), new SpriterAnimation(
                makeCharacterPath("mainChar/static.scml")));
        // 碰撞盒、能量（每回合 3 点）等基础参数
        initializeClass(null,
                SHOULDER1,
                SHOULDER2,
                CORPSE,
                getLoadout(), 20.0F, -10.0F, 166.0F, 327.0F, new EnergyManager(3));


        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);
    }

    /** 选人界面展示的生命、金、初始遗物/卡组等 */
    @Override
    public CharSelectInfo getLoadout() {
        // 参数：当前生命, 最大生命, 充能球栏, 初始金币, 每回合抽牌
        return new CharSelectInfo(NAMES[0], TEXT[0],
                72, 72, 0, 99, 5, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    /** 初始 11 张：5 打击 + 4 格挡 + 流星火弩 + 雷鸣虹锋 */
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            retVal.add(Strike.ID);
        }
        for (int i = 0; i < 4; i++) {
            retVal.add(Defend.ID);
        }
        retVal.add(StarfireCrossbow.ID);
        retVal.add(ThunderEdge.ID);
        return retVal;
    }

    /** 初始遗物：怜悯の火 */
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(CompassionFire.ID);
        return retVal;
    }

    /** 选中角色时的音效与震动 */
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("UNLOCK_PING", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false);
    }

    /** 能量球各层贴图 */
    private static final String[] orbTextures = {
            makeCharacterPath("mainChar/orb/layer1.png"),
            makeCharacterPath("mainChar/orb/layer2.png"),
            makeCharacterPath("mainChar/orb/layer3.png"),
            makeCharacterPath("mainChar/orb/layer4.png"),
            makeCharacterPath("mainChar/orb/layer5.png"),
            makeCharacterPath("mainChar/orb/layer6.png"),
            makeCharacterPath("mainChar/orb/layer1d.png"),
            makeCharacterPath("mainChar/orb/layer2d.png"),
            makeCharacterPath("mainChar/orb/layer3d.png"),
            makeCharacterPath("mainChar/orb/layer4d.png"),
            makeCharacterPath("mainChar/orb/layer5d.png"),
    };

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "UNLOCK_PING";
    }

    /** 进阶模式下最大生命惩罚 */
    @Override
    public int getAscensionMaxHPLoss() {
        return 8;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return TODO_COLOR;
    }

    @Override
    public Color getCardTrailColor() {
        return characterColor.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    /** 部分事件用到的“代表卡” */
    @Override
    public AbstractCard getStartCardForEvent() {
        return new StarfireCrossbow();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new CharacterFile(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return characterColor.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return characterColor.cpy();
    }

    /** 打心脏时的斩击特效 */
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.FIRE};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    /** 角色职业枚举与卡牌颜色枚举（改名时两处要一起改） */
    public static class Enums {
        //TODO: Change these.
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_TODO;
        @SpireEnum(name = "TODO_COLOR")
        public static AbstractCard.CardColor TODO_COLOR;
        @SpireEnum(name = "TODO_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
}
