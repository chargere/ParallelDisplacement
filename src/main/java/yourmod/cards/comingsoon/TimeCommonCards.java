package yourmod.cards.comingsoon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.ComingSoonTimeCharacter;
import yourmod.cards.AbstractEasyCard;
import yourmod.util.ComboSystem;
import yourmod.util.DayNightSystem;
import yourmod.util.Wiz;

import static yourmod.ModFile.makeID;

/** 时间旅人普通攻击与技能白卡。每张牌保持单一、清晰的基础用途。 */
public abstract class TimeCommonCards extends AbstractEasyCard {
    protected TimeCommonCards(String id, String displayName, int cost, CardType type, CardTarget target, String desc) {
        super(id, cost, type, CardRarity.COMMON, target, ComingSoonTimeCharacter.Enums.TIME_COLOR);
        name = originalName = displayName;
        rawDescription = desc;
        initializeTitle();
        initializeDescription();
    }

    protected void combo() { ComboSystem.record(AbstractDungeonHolder.player(), this); }
    protected void hit(AbstractMonster m) { dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL); }
    protected void block() { blck(); }

    @Override public boolean canUpgrade() { return !upgraded; }

    private static final class AbstractDungeonHolder {
        private static AbstractPlayer player() { return com.megacrit.cardcrawl.dungeons.AbstractDungeon.player; }
    }

    public static class YiShun extends TimeCommonCards {
        public static final String ID = makeID("YiShun");
        public YiShun() { super(ID, "一瞬", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 7; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(2); }
    }
    public static class DuanXu extends TimeCommonCards {
        public static final String ID = makeID("DuanXu");
        public DuanXu() { super(ID, "断续", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害 2 次。"); baseDamage = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { for (int i=0;i<2;i++) hit(m); combo(); }
        public void upp() { upgradeDamage(1); }
    }
    public static class ChongDie extends TimeCommonCards {
        public static final String ID = makeID("ChongDie");
        public ChongDie() { super(ID, "重叠", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害 3 次。"); baseDamage = 3; }
        public void use(AbstractPlayer p, AbstractMonster m) { for (int i=0;i<3;i++) hit(m); combo(); }
        public void upp() { upgradeDamage(1); }
    }
    public static class ZheFan extends TimeCommonCards {
        public static final String ID = makeID("ZheFan");
        public ZheFan() { super(ID, "折返", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 7; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(3); }
    }
    public static class CuoPai extends TimeCommonCards {
        public static final String ID = makeID("CuoPai");
        public CuoPai() { super(ID, "错拍", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。抽 1 张牌。"); baseDamage = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); addToBot(new DrawCardAction(p, 1)); combo(); }
        public void upp() { upgradeDamage(2); }
    }
    public static class ZhuiPai extends TimeCommonCards {
        public static final String ID = makeID("ZhuiPai");
        public ZhuiPai() { super(ID, "追拍", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(2); }
    }
    public static class YuKe extends TimeCommonCards {
        public static final String ID = makeID("YuKe");
        public YuKe() { super(ID, "余刻", 1, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 6; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(2); }
    }
    public static class HuiXiangRen extends TimeCommonCards {
        public static final String ID = makeID("HuiXiangRen");
        public HuiXiangRen() { super(ID, "回响刃", 0, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 3; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(2); }
    }
    public static class XianShou extends TimeCommonCards {
        public static final String ID = makeID("XianShou");
        public XianShou() { super(ID, "先手", 0, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。若这是本回合第一张牌，伤害 +3。"); baseDamage = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(1); }
    }
    public static class JieShi extends TimeCommonCards {
        public static final String ID = makeID("JieShi");
        public JieShi() { super(ID, "截时", 2, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 14; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(4); }
    }
    public static class ZhongKe extends TimeCommonCards {
        public static final String ID = makeID("ZhongKe");
        public ZhongKe() { super(ID, "终刻", 2, CardType.ATTACK, CardTarget.ENEMY, "造成 !D! 点伤害。"); baseDamage = 10; }
        public void use(AbstractPlayer p, AbstractMonster m) { hit(m); combo(); }
        public void upp() { upgradeDamage(3); }
    }

    public static class PianKeFangYu extends TimeCommonCards {
        public static final String ID = makeID("PianKeFangYu");
        public PianKeFangYu() { super(ID, "片刻防御", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。"); baseBlock = 6; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); combo(); }
        public void upp() { upgradeBlock(3); }
    }
    public static class CuoBu extends TimeCommonCards {
        public static final String ID = makeID("CuoBu");
        public CuoBu() { super(ID, "错步", 0, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。"); baseBlock = 3; exhaust = true; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class HuanShi extends TimeCommonCards {
        public static final String ID = makeID("HuanShi");
        public HuanShi() { super(ID, "缓时", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。"); baseBlock = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class LiuHen extends TimeCommonCards {
        public static final String ID = makeID("LiuHen");
        public LiuHen() { super(ID, "留痕", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。"); baseBlock = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class HuiKan extends TimeCommonCards {
        public static final String ID = makeID("HuiKan");
        public HuiKan() { super(ID, "回看", 1, CardType.SKILL, CardTarget.SELF, "抽 1 张牌。消耗。"); exhaust = true; }
        public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new DrawCardAction(p, 1)); combo(); }
        public void upp() { upgradeBlock(0); }
    }
    public static class XiaoZhun extends TimeCommonCards {
        public static final String ID = makeID("XiaoZhun");
        public XiaoZhun() { super(ID, "校准", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。抽 1 张牌。"); baseBlock = 3; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); addToBot(new DrawCardAction(p, 1)); combo(); }
        public void upp() { upgradeBlock(3); }
    }
    public static class JieShiSkill extends TimeCommonCards {
        public static final String ID = makeID("JieShiSkill");
        public JieShiSkill() { super(ID, "借时", 1, CardType.SKILL, CardTarget.SELF, "获得 1 点能量。消耗。"); exhaust = true; }
        public void use(AbstractPlayer p, AbstractMonster m) { addToBot(new GainEnergyAction(1)); combo(); }
        public void upp() { exhaust = true; }
    }
    public static class TingZhi extends TimeCommonCards {
        public static final String ID = makeID("TingZhi");
        public TingZhi() { super(ID, "停滞", 1, CardType.SKILL, CardTarget.ALL_ENEMY, "对所有敌人施加 1 层虚弱。获得 4 点格挡。"); baseBlock = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1)); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class HuanWei extends TimeCommonCards {
        public static final String ID = makeID("HuanWei");
        public HuanWei() { super(ID, "换位", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。抽 1 张牌。"); baseBlock = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); addToBot(new DrawCardAction(p, 1)); combo(); }
        public void upp() { upgradeBlock(3); }
    }
    public static class ZhouHen extends TimeCommonCards {
        public static final String ID = makeID("ZhouHen");
        public ZhouHen() { super(ID, "昼痕", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。昼：获得 1 点星念。夜：抽 1 张牌。"); baseBlock = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); if (DayNightSystem.isDay()) Wiz.applyToSelf(new yourmod.powers.XingNianPower(p,1)); else addToBot(new DrawCardAction(p,1)); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class RiXi extends TimeCommonCards {
        public static final String ID = makeID("RiXi");
        public RiXi() { super(ID, "日息", 1, CardType.SKILL, CardTarget.SELF, "昼：获得 !B! 点格挡并获得 1 点星念。夜：获得 3 点格挡并抽 1 张牌。"); baseBlock = 5; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); if (DayNightSystem.isDay()) Wiz.applyToSelf(new yourmod.powers.XingNianPower(p,1)); else addToBot(new DrawCardAction(p,1)); combo(); }
        public void upp() { upgradeBlock(2); }
    }
    public static class ChenMuQiDao extends TimeCommonCards {
        public static final String ID = makeID("ChenMuQiDao");
        public ChenMuQiDao() { super(ID, "晨暮祈祷", 1, CardType.SKILL, CardTarget.SELF, "获得 4 点格挡。昼夜不同，卡面会改变。"); baseBlock = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); combo(); }
        public void upp() { exhaust = true; }
    }
    public static class YeMu extends TimeCommonCards {
        public static final String ID = makeID("YeMu");
        public YeMu() { super(ID, "夜幕", 1, CardType.SKILL, CardTarget.SELF, "获得 !B! 点格挡。若为夜晚，抽 1 张牌。"); baseBlock = 4; }
        public void use(AbstractPlayer p, AbstractMonster m) { block(); if (DayNightSystem.isNight()) addToBot(new DrawCardAction(p,1)); combo(); }
        public void upp() { upgradeBlock(3); }
    }
}
