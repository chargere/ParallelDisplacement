package yourmod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.abstracts.DynamicVariable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import yourmod.cards.AbstractEasyCard;
import yourmod.cards.cardvars.AbstractEasyDynamicVariable;
import yourmod.potions.AbstractEasyPotion;
import yourmod.relics.AbstractEasyRelic;
import yourmod.unlock.DominicUnlocks;
import yourmod.unlock.HongwenUnlocks;
import yourmod.util.DamuhouFireMode;
import yourmod.util.ProAudio;
import java.nio.charset.StandardCharsets;

/**
 * Mod 主入口：向 BaseMod 注册卡牌、遗物、角色、文本、关键词、音效等。
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class ModFile implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        SetUnlocksSubscriber,
        AddAudioSubscriber,
        StartGameSubscriber {

    /** Mod 唯一 ID，资源文件夹名也依赖它（hongwenResources） */
    public static final String modID = "hongwen";

    /** 给内容加前缀，避免和其他 Mod 的 ID 冲突，如 hongwen:Strike */
    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    /** 角色主题色（火焰橙） */
    public static Color characterColor = new Color(0.95f, 0.40f, 0.12f, 1f);
    /** 霜星旅者主题色（霜蓝） */
    public static Color dominicColor = new Color(0.55f, 0.75f, 0.95f, 1f);
    /** 风光占位主题色（青绿） */
    public static Color windColor = new Color(0.45f, 0.82f, 0.55f, 1f);
    /** 时间旅人主题色（赛博青） */
    public static Color timeColor = new Color(0.22f, 0.92f, 1f, 1f);
    /** fvv蛋蛋主题色（蛋黄） */
    public static Color fvvColor = new Color(0.98f, 0.82f, 0.28f, 1f);
    /** 战斗精英主题色（紫极光） */
    public static Color damuhouColor = new Color(0.62f, 0.38f, 0.92f, 1f);
    /** 精绝王者主题色（沙金） */
    public static Color jingjueColor = new Color(0.79f, 0.64f, 0.15f, 1f);

    // —— 角色立绘 / 卡牌框 / 选人界面等资源路径 ——
    public static final String SHOULDER1 = makeCharacterPath("mainChar/shoulder.png");
    public static final String SHOULDER2 = makeCharacterPath("mainChar/shoulder2.png");
    public static final String CORPSE = makeCharacterPath("mainChar/corpse.png");
    private static final String ATTACK_S_ART = makeImagePath("512/attack.png");
    private static final String SKILL_S_ART = makeImagePath("512/skill.png");
    private static final String POWER_S_ART = makeImagePath("512/power.png");
    private static final String CARD_ENERGY_S = makeImagePath("512/energy.png");
    private static final String TEXT_ENERGY = makeImagePath("512/text_energy.png");
    private static final String ATTACK_L_ART = makeImagePath("1024/attack.png");
    private static final String SKILL_L_ART = makeImagePath("1024/skill.png");
    private static final String POWER_L_ART = makeImagePath("1024/power.png");
    private static final String CARD_ENERGY_L = makeImagePath("1024/energy.png");
    private static final String CHARSELECT_BUTTON = makeImagePath("charSelect/charButton.png");
    private static final String CHARSELECT_PORTRAIT = makeImagePath("charSelect/charBG.png");
    private static final String DOMINIC_CHARSELECT_BUTTON = makeImagePath("charSelect/dominicButton.png");
    private static final String DOMINIC_CHARSELECT_PORTRAIT = makeImagePath("charSelect/dominicBG.png");
    private static final String WIND_CHARSELECT_BUTTON = makeImagePath("charSelect/windButton.png");
    private static final String WIND_CHARSELECT_PORTRAIT = makeImagePath("charSelect/windBG.png");
    private static final String TIME_CHARSELECT_BUTTON = makeImagePath("charSelect/timeButton.png");
    private static final String TIME_CHARSELECT_PORTRAIT = makeImagePath("charSelect/timeBG.png");
    private static final String FVV_CHARSELECT_BUTTON = makeImagePath("charSelect/fvvButton.png");
    private static final String FVV_CHARSELECT_PORTRAIT = makeImagePath("charSelect/fvvBG.png");
    private static final String DAMUHOU_CHARSELECT_BUTTON = makeImagePath("charSelect/damuhouButton.png");
    private static final String DAMUHOU_CHARSELECT_PORTRAIT = makeImagePath("charSelect/damuhouBG.png");
    private static final String JINGJUE_CHARSELECT_BUTTON = makeImagePath("charSelect/timeButton.png");
    private static final String JINGJUE_CHARSELECT_PORTRAIT = makeImagePath("charSelect/timeBG.png");

    /** 已做本地化的语言；不在列表里则回退 eng */
    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    /** 返回本地化文件夹名，如 eng */
    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public ModFile() {
        BaseMod.subscribe(this);
        BaseMod.addSaveField("hongwenDamuhouFullAuto", new CustomSavable<Boolean>() {
            @Override
            public Boolean onSave() {
                return DamuhouFireMode.fullAuto;
            }

            @Override
            public void onLoad(Boolean saved) {
                DamuhouFireMode.fullAuto = Boolean.TRUE.equals(saved);
            }
        });

        // 注册本角色卡牌颜色与卡框贴图
        BaseMod.addColor(CharacterFile.Enums.TODO_COLOR, characterColor, characterColor, characterColor,
                characterColor, characterColor, characterColor, characterColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(DominicCharacter.Enums.DOMINIC_COLOR, dominicColor, dominicColor, dominicColor,
                dominicColor, dominicColor, dominicColor, dominicColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(ComingSoonWindCharacter.Enums.WIND_COLOR, windColor, windColor, windColor,
                windColor, windColor, windColor, windColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(ComingSoonTimeCharacter.Enums.TIME_COLOR, timeColor, timeColor, timeColor,
                timeColor, timeColor, timeColor, timeColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(FvvEggCharacter.Enums.FVV_COLOR, fvvColor, fvvColor, fvvColor,
                fvvColor, fvvColor, fvvColor, fvvColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(DamuhouCharacter.Enums.DAMUHOU_COLOR, damuhouColor, damuhouColor, damuhouColor,
                damuhouColor, damuhouColor, damuhouColor, damuhouColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
        BaseMod.addColor(JingjueCharacter.Enums.JINGJUE_COLOR, jingjueColor, jingjueColor, jingjueColor,
                jingjueColor, jingjueColor, jingjueColor, jingjueColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
    }

    // —— 拼资源路径的小工具 ——
    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath)
    {
        return modID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    /** ModTheSpire 启动时调用的入口 */
    public static void initialize() {
        ModFile thismod = new ModFile();
    }

    /** 注册鸿文待解锁卡组（通关推进，见 {@link HongwenUnlocks}） */
    @Override
    public void receiveSetUnlocks() {
        HongwenUnlocks.register();
        DominicUnlocks.register();
    }

    /** 注册角色，并自动扫描添加药水 */
    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new CharacterFile(CharacterFile.characterStrings.NAMES[1], CharacterFile.Enums.THE_TODO),
            CHARSELECT_BUTTON, CHARSELECT_PORTRAIT, CharacterFile.Enums.THE_TODO);
        BaseMod.addCharacter(new DominicCharacter(DominicCharacter.characterStrings.NAMES[1], DominicCharacter.Enums.THE_DOMINIC),
            DOMINIC_CHARSELECT_BUTTON, DOMINIC_CHARSELECT_PORTRAIT, DominicCharacter.Enums.THE_DOMINIC);
        BaseMod.addCharacter(new ComingSoonWindCharacter(ComingSoonWindCharacter.characterStrings.NAMES[1], ComingSoonWindCharacter.Enums.THE_WIND),
            WIND_CHARSELECT_BUTTON, WIND_CHARSELECT_PORTRAIT, ComingSoonWindCharacter.Enums.THE_WIND);
        BaseMod.addCharacter(new ComingSoonTimeCharacter(ComingSoonTimeCharacter.characterStrings.NAMES[1], ComingSoonTimeCharacter.Enums.THE_TIME),
            TIME_CHARSELECT_BUTTON, TIME_CHARSELECT_PORTRAIT, ComingSoonTimeCharacter.Enums.THE_TIME);
        BaseMod.addCharacter(new FvvEggCharacter(FvvEggCharacter.characterStrings.NAMES[1], FvvEggCharacter.Enums.THE_FVV),
            FVV_CHARSELECT_BUTTON, FVV_CHARSELECT_PORTRAIT, FvvEggCharacter.Enums.THE_FVV);
        BaseMod.addCharacter(new DamuhouCharacter(DamuhouCharacter.characterStrings.NAMES[1], DamuhouCharacter.Enums.THE_DAMUHOU),
            DAMUHOU_CHARSELECT_BUTTON, DAMUHOU_CHARSELECT_PORTRAIT, DamuhouCharacter.Enums.THE_DAMUHOU);
        BaseMod.addCharacter(new JingjueCharacter(JingjueCharacter.characterStrings.NAMES[1], JingjueCharacter.Enums.THE_JINGJUE),
            JINGJUE_CHARSELECT_BUTTON, JINGJUE_CHARSELECT_PORTRAIT, JingjueCharacter.Enums.THE_JINGJUE);

        new AutoAdd(modID)
            .packageFilter(AbstractEasyPotion.class)
            .any(AbstractEasyPotion.class, (info, potion) -> {
                if (potion.pool == null)
                    BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID);
                else
                    BaseMod.addPotion(potion.getClass(), potion.liquidColor, potion.hybridColor, potion.spotsColor, potion.ID, potion.pool);
            });
    }

    /** 自动扫描并注册遗物 */
    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyRelic.class)
                .any(AbstractEasyRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    /** 注册卡牌动态变量，并自动扫描添加所有卡牌 */
    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
            .packageFilter(AbstractEasyDynamicVariable.class)
            .any(DynamicVariable.class, (info, var) -> 
                BaseMod.addDynamicVariable(var));
        // 禁止 setDefaultSeen(true)：AutoAdd 会对 seen 卡调用 UnlockTracker.unlockCard，
        // 把 SetUnlocks 里刚锁上的五组内容全部洗成已解锁。
        new AutoAdd(modID)
                .packageFilter(AbstractEasyCard.class)
                .setDefaultSeen(false)
                .cards();
        // 状态/诅咒衍生（@AutoAdd.Ignore）需手动注册进卡池库
        BaseMod.addCard(new yourmod.cards.fvv.SlackOff());
        BaseMod.addCard(new yourmod.cards.fvv.SlackOffHard());
        BaseMod.addCard(new yourmod.cards.fvv.ManualBook());
        BaseMod.addCard(new yourmod.cards.fvv.OvertimeMail());
        BaseMod.addCard(new yourmod.cards.fvv.ReadReceipt());
        BaseMod.addCard(new yourmod.cards.fvv.PlacentaShard());
        BaseMod.addCard(new yourmod.cards.jingjue.JjBloodMark());
        HongwenUnlocks.enforceLocks();
        DominicUnlocks.enforceLocks();
    }

    /** 加载本地化 JSON（卡牌名、遗物说明等） */
    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, modID + "Resources/localization/" + getLangString() + "/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, modID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, modID + "Resources/localization/" + getLangString() + "/Charstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, modID + "Resources/localization/" + getLangString() + "/Powerstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/" + getLangString() + "/UIstrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, modID + "Resources/localization/" + getLangString() + "/Orbstrings.json");
        BaseMod.loadCustomStringsFile(StanceStrings.class, modID + "Resources/localization/" + getLangString() + "/Stancestrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, modID + "Resources/localization/" + getLangString() + "/Potionstrings.json");
    }

    /** 注册自定义音效 */
    @Override
    public void receiveAddAudio() {
        for (ProAudio a : ProAudio.values())
            BaseMod.addAudio(makeID(a.name()), makePath("audio/" + a.name().toLowerCase() + ".ogg"));
    }

    /** 新开一局默认 half auto。读档不进这里，或进了也不清已读档的开关。 */
    @Override
    public void receiveStartGame() {
        if (!CardCrawlGame.loadingSave) {
            DamuhouFireMode.resetToHalf();
        }
    }

    /** 从 Keywordstrings.json 注册关键词（如烬火、静电印记） */
    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/" + getLangString() + "/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                String desc = yourmod.util.JingjueDesc.isolate(keyword.DESCRIPTION);
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, desc);
            }
        }
    }
}
