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
import yourmod.cards.dominic.DominicDefend;
import yourmod.cards.dominic.DominicStrike;
import yourmod.cards.dominic.FrostStrike;
import yourmod.cards.dominic.Stargaze;
import yourmod.cards.dominic.WinterCurtain;
import yourmod.relics.AuroraPocketWatch;

import java.util.ArrayList;

import static yourmod.DominicCharacter.Enums.DOMINIC_COLOR;
import static yourmod.ModFile.*;

/**
 * 霜星旅者 · 多米尼克：初始卡组、遗物、选人界面。
 */
public class DominicCharacter extends CustomPlayer {

    public static final String ID = makeID("DominicCharacter");
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;

    /** 选人界面主标题下的副标题（NAMES[2]） */
    public static String getSelectSubtitle() {
        if (NAMES != null && NAMES.length > 2 && NAMES[2] != null) {
            return NAMES[2];
        }
        return "多米尼克·梅隆蒂科";
    }

    public DominicCharacter(String name, PlayerClass setClass) {
        super(name, setClass,
                new CustomEnergyOrb(orbTextures, makeCharacterPath("dominicChar/orb/vfx.png"), null),
                new SpriterAnimation(makeCharacterPath("dominicChar/static.scml")));
        initializeClass(null,
                makeCharacterPath("dominicChar/shoulder.png"),
                makeCharacterPath("dominicChar/shoulder2.png"),
                makeCharacterPath("dominicChar/corpse.png"),
                getLoadout(), 20.0F, -10.0F, 166.0F, 327.0F, new EnergyManager(3));

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                75, 75, 0, 99, 5, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            retVal.add(DominicStrike.ID);
        }
        for (int i = 0; i < 4; i++) {
            retVal.add(DominicDefend.ID);
        }
        retVal.add(FrostStrike.ID);
        retVal.add(WinterCurtain.ID);
        retVal.add(Stargaze.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(AuroraPocketWatch.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("UNLOCK_PING", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    private static final String[] orbTextures = {
            makeCharacterPath("dominicChar/orb/layer1.png"),
            makeCharacterPath("dominicChar/orb/layer2.png"),
            makeCharacterPath("dominicChar/orb/layer3.png"),
            makeCharacterPath("dominicChar/orb/layer4.png"),
            makeCharacterPath("dominicChar/orb/layer5.png"),
            makeCharacterPath("dominicChar/orb/layer6.png"),
            makeCharacterPath("dominicChar/orb/layer1d.png"),
            makeCharacterPath("dominicChar/orb/layer2d.png"),
            makeCharacterPath("dominicChar/orb/layer3d.png"),
            makeCharacterPath("dominicChar/orb/layer4d.png"),
            makeCharacterPath("dominicChar/orb/layer5d.png"),
    };

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "UNLOCK_PING";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 8;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return DOMINIC_COLOR;
    }

    @Override
    public Color getCardTrailColor() {
        return dominicColor.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new FrostStrike();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new DominicCharacter(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return dominicColor.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return dominicColor.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_DOMINIC;
        @SpireEnum(name = "DOMINIC_COLOR")
        public static AbstractCard.CardColor DOMINIC_COLOR;
        @SpireEnum(name = "DOMINIC_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
}
