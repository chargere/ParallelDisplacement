#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""S1+S2: Build Dominic (霜星旅者) cards+mechanics doc -> final_draft.md + formatted HTML."""
import os, re, json

PROJ = "C:/Users/38220/Desktop/ProTemplate-master"
CARDS_DIR = os.path.join(PROJ, "src/main/java/yourmod/cards/dominic")
RELICS_DIR = os.path.join(PROJ, "src/main/java/yourmod/relics")
CARDSTR = os.path.join(PROJ, "src/main/resources/hongwenResources/localization/eng/Cardstrings.json")
RELICSTR = os.path.join(PROJ, "src/main/resources/hongwenResources/localization/eng/Relicstrings.json")
OUT = "C:/Users/38220/WorkBuddy/2026-10-23-14-53-22/output/dominic_docx"
DOMINIC_RELICS = ["AuroraPocketWatch", "ClosedStarChart", "HatchFrost",
                  "MirrorPocketWatch", "ThreeDayHatch", "UnfinishedStarforge"]

RARITY = {"BASIC": "基础", "COMMON": "普通", "UNCOMMON": "罕见", "RARE": "稀有"}
CTYPE = {"ATTACK": "攻击", "SKILL": "技能", "POWER": "能力", "STATUS": "状态", "CURSE": "诅咒"}
TARGET = {"ENEMY": "单体·敌", "ALL_ENEMY": "全体·敌", "SELF": "自身", "NONE": "—"}
FPTYPE = {"DELAY": "迟滞", "FREEZE_CRACK": "冻裂", "STAR_EXTINGUISH": "星灭", "RETURN_TO_VOID": "归墟"}

def load_json(p):
    with open(p, encoding="utf-8") as f:
        return json.load(f)

def sfx(k):
    return k.split(":")[-1]

cardstr = load_json(CARDSTR); relicstr = load_json(RELICSTR)
cl10n = {sfx(k): v for k, v in cardstr.items()}
rl10n = {sfx(k): v for k, v in relicstr.items()}

def cname(cid):
    return (cl10n.get(cid, {}) or {}).get("NAME") or cid
def cdesc(cid):
    e = cl10n.get(cid, {}) or {}
    if "DESCRIPTION" in e: return e["DESCRIPTION"]
    if "DESCRIPTIONS" in e and e["DESCRIPTIONS"]: return e["DESCRIPTIONS"][0]
    return ""
def rname(rid):
    return (rl10n.get(rid, {}) or {}).get("NAME") or rid
def rdesc(rid):
    e = rl10n.get(rid, {}) or {}
    if "DESCRIPTIONS" in e and e["DESCRIPTIONS"]: return e["DESCRIPTIONS"][0]
    if "DESCRIPTION" in e: return e["DESCRIPTION"]
    return ""

def norm(s, c):
    s = s.replace("NL", "\n")
    s = s.replace("!D!", c["dmg"] or "X")
    s = s.replace("!M!", c["magic"] or "X")
    s = s.replace("!B!", c["block"] or "X")
    s = s.replace("!hongwen:sd!", "【关联伤害】")
    s = s.replace("!hongwen:m2!", "【关联数值】")
    return s.strip()

def norm_relic(s):
    s = s.replace("#b", "").replace("#y", "")
    return s.strip()

# parse cards
cards = []
for fn in sorted(os.listdir(CARDS_DIR)):
    if not fn.endswith(".java"): continue
    src = open(os.path.join(CARDS_DIR, fn), encoding="utf-8").read()
    m = re.search(r'ID\s*=\s*makeID\("([^"]+)"\)', src) or re.search(r'public static final String ID = "([^"]+)"', src)
    if not m: continue
    cid = m.group(1)
    cm = re.search(r'super\(\s*ID,\s*([^,]+),\s*CardType\.(\w+),\s*CardRarity\.(\w+),\s*CardTarget\.(\w+)\s*\)', src)
    cost = cm.group(1).strip() if cm else "?"
    ctype = cm.group(2) if cm else "?"
    crarity = cm.group(3) if cm else "?"
    ctarget = cm.group(4) if cm else "?"
    fp = re.search(r'setFrostPay\(([^)]*)\)', src)
    frostpay = fp.group(1).strip() if fp else ""
    def num(lbl):
        mm = re.search(lbl + r'\s*=\s*(\d+)', src); return mm.group(1) if mm else ""
    dmg, blk, mag = num(r'baseDamage'), num(r'baseBlock'), num(r'baseMagicNumber\s*=\s*magicNumber') or num(r'baseMagicNumber')
    flags = []
    for f in ["exhaust = true", "ethereal = true", "innate = true", "selfRetain = true", "retain = true"]:
        if f in src: flags.append(f.split(" = ")[0])
    cards.append(dict(id=cid, cost=cost, type=ctype, rarity=crarity, target=ctarget,
                      frostpay=frostpay, dmg=dmg, block=blk, magic=mag, flags=",".join(flags),
                      name=cname(cid), desc=norm(cdesc(cid), dict(dmg=dmg, magic=mag, block=blk))))

# parse relics
relics = []
for rid in DOMINIC_RELICS:
    path = os.path.join(RELICS_DIR, rid + ".java")
    tier = "?"
    if os.path.exists(path):
        s = open(path, encoding="utf-8").read()
        tm = re.search(r'RelicTier\.(\w+)', s)
        tier = tm.group(1) if tm else "?"
    relics.append(dict(id=rid, name=rname(rid), desc=norm_relic(rdesc(rid)), tier=tier))

TIER = {"STARTER": "起始", "COMMON": "普通", "UNCOMMON": "罕见", "RARE": "稀有", "BOSS": "Boss", "SHOP": "商店"}

def fp_label(fp):
    if not fp: return "—"
    parts = [p.strip() for p in fp.split(",")]
    cost = parts[0]; typ = parts[1].replace("FrostPayType.", ""); val = parts[2] if len(parts) > 2 else "1"
    return "霜偿%s·%s%s" % (cost, FPTYPE.get(typ, typ), ("%s" % val if typ in ("DELAY", "FREEZE_CRACK", "RETURN_TO_VOID") else ""))

# ============ S1: markdown ============
md = []
md.append("# 霜星旅者（Dominic）卡牌与机制总览\n")
md.append("> 平行异位（ParallelDisplacement）mod 五名自定角色之一。设计内核：**拥有强大的数值，但需要付出代价**——以「霜寒」为资源、以「霜偿」为代价账单，并以「星痕」为星线主轴。\n")
md.append("## 一、核心机制\n")
md.append("### 1. 霜寒（FrostCold）— 资源\n")
md.append("- 战斗中获取的层数资源，上限 **10**（遗物「霜裂」可提升至 15）。\n")
md.append("- **回合开始**：将当前霜寒全额转为临时力量，半数转为临时敏捷；随后霜寒**减半**（能力「永夜极光」改为固定失去 2 层）。\n")
md.append("- **溢出**：超过上限的霜寒转为「冰尘」伤害（默认 ×3），并对随机敌人施加 1 层星痕。\n")
md.append("\n### 2. 霜偿（FrostPay）— 代价账单\n")
md.append("带霜偿的牌打出时，需二选一：**消耗霜寒取消代价**（按住 Shift 直接取消），或**承受下列代价**。四类代价：\n")
md.append("| 代价 | 效果 |\n|---|---|\n| 迟滞 | 本回合手牌中与其相邻的左右两张牌费用 +X |\n| 冻裂 | 本回合失去 X 点敏捷（下回合恢复） |\n| 星灭 | 本回合不能再打出攻击牌 |\n| 归墟 | 本回合接下来 X 张打出的牌被消耗 |\n")
md.append("\n### 3. 星痕（StarMark）— 星线主轴（救活后）\n")
md.append("- 敌方 **debuff**：每层使其受到的普通攻击 **+4 伤害**；被普通攻击命中时消耗 1 层。\n")
md.append("- 消耗触发回响：每层被消耗的星痕给予 **+1 格挡**（可见闭环），若持有能力「星痕感应」则额外获得临时力量。\n")
md.append("- 打法：用铺痕卡（星轨重踏 / 星灰筛 / 星图残页 / 星尘沉淀）给敌人叠星痕，再用**任意攻击**引爆——既加伤又被消耗回资源。\n")
md.append("\n### 4. 其它\n")
md.append("- **冰尘**：霜寒溢出的附带伤害，可被能力放大（冰尘回响 / 冰尘风暴）。\n")
md.append("- **精灰**：能力「精灰形态」将霜寒减半损失的层数转为精灰，攻击加伤，满 10 精灰生成「星灰绽放」。\n")
md.append("\n## 二、卡牌总览\n")

order = {"ATTACK": 0, "SKILL": 1, "POWER": 2, "STATUS": 3, "CURSE": 4}
for ctype_key, title in [("ATTACK", "攻击牌"), ("SKILL", "技能牌"), ("POWER", "能力牌")]:
    grp = [c for c in cards if c["type"] == ctype_key]
    md.append("\n### %s（%d 张）\n" % (title, len(grp)))
    md.append("| 名称 | 费 | 稀有 | 目标 | 霜偿 | 效果 |\n|---|---|---|---|---|---|")
    for c in grp:
        extra = ("【%s】" % c["flags"]) if c["flags"] else ""
        md.append("| %s | %s | %s | %s | %s | %s%s |" % (
            c["name"], c["cost"], RARITY.get(c["rarity"], c["rarity"]),
            TARGET.get(c["target"], c["target"]), fp_label(c["frostpay"]),
            c["desc"].replace("\n", " / "), extra))
md.append("\n> 注：`【关联伤害】`/`【关联数值】` 为跨角色（鸿文）关键字引用，原工程以 `!hongwen:sd!` 等占位，本文以中文标注。\n")

md.append("\n## 三、专属遗物\n")
md.append("| 名称 | 类型 | 效果 |\n|---|---|---|")
for r in relics:
    md.append("| %s | %s | %s |" % (r["name"], TIER.get(r["tier"], r["tier"]), r["desc"]))
md.append("")
md_text = "\n".join(md)
with open(os.path.join(OUT, "stage1", "final_draft.md"), "w", encoding="utf-8") as f:
    f.write(md_text)

# ============ S2: html ============
def esc(s):
    return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

html = []
html.append("""<!DOCTYPE html>
<html lang="zh-CN"><head><meta charset="utf-8">
<style>
:root{--primary:#1f3a5f;--accent:#c0392b;}
body{font-family:"Microsoft YaHei","SimSun",serif;color:#222;line-height:1.6;margin:24px;}
h1{color:#1f3a5f;border-bottom:2px solid var(--primary);padding-bottom:6px;}
h2{color:#1f3a5f;border-left:4px solid var(--primary);padding-left:8px;margin-top:26px;}
h3{color:#2c5282;margin-top:16px;}
p,li{font-size:10.5pt;}
table{border-collapse:collapse;width:100%;margin:8px 0 18px;font-size:9.5pt;}
th{background:#1f3a5f;color:#fff;text-align:left;padding:5px 8px;}
td{border:1px solid #b9c4d0;padding:5px 8px;vertical-align:top;}
tr:nth-child(even) td{background:#f3f6fa;}
.tag{font-size:8.5pt;padding:1px 5px;border-radius:3px;color:#fff;}
.t-common{background:#78909c;} .t-uncommon{background:#43a047;} .t-rare{background:#ef6c00;} .t-basic{background:#90a4ae;}
.note{color:#666;font-size:9pt;font-style:italic;}
</style></head><body>""")

html.append("<h1>霜星旅者（Dominic）卡牌与机制总览</h1>")
html.append('<p class="note">平行异位（ParallelDisplacement）mod 五名自定角色之一。设计内核：<b>拥有强大的数值，但需要付出代价</b>——以「霜寒」为资源、以「霜偿」为代价账单，并以「星痕」为星线主轴。</p>')

html.append("<h2>一、核心机制</h2>")
html.append("<h3>1. 霜寒（FrostCold）— 资源</h3>")
html.append("<ul><li>战斗中获取的层数资源，上限 <b>10</b>（遗物「霜裂」可提升至 15）。</li>")
html.append("<li><b>回合开始</b>：将当前霜寒全额转为临时力量，半数转为临时敏捷；随后霜寒<b>减半</b>（能力「永夜极光」改为固定失去 2 层）。</li>")
html.append("<li><b>溢出</b>：超过上限的霜寒转为「冰尘」伤害（默认 ×3），并对随机敌人施加 1 层星痕。</li></ul>")

html.append("<h3>2. 霜偿（FrostPay）— 代价账单</h3>")
html.append("<p>带霜偿的牌打出时，需二选一：<b>消耗霜寒取消代价</b>（按住 Shift 直接取消），或<b>承受下列代价</b>。四类代价：</p>")
html.append("<table><thead><tr><th>代价</th><th>效果</th></tr></thead><tbody>")
html.append("<tr><td>迟滞</td><td>本回合手牌中与其相邻的左右两张牌费用 +X</td></tr>")
html.append("<tr><td>冻裂</td><td>本回合失去 X 点敏捷（下回合恢复）</td></tr>")
html.append("<tr><td>星灭</td><td>本回合不能再打出攻击牌</td></tr>")
html.append("<tr><td>归墟</td><td>本回合接下来 X 张打出的牌被消耗</td></tr>")
html.append("</tbody></table>")

html.append("<h3>3. 星痕（StarMark）— 星线主轴（救活后）</h3>")
html.append("<ul><li>敌方 <b>debuff</b>：每层使其受到的普通攻击 <b>+4 伤害</b>；被普通攻击命中时消耗 1 层。</li>")
html.append("<li>消耗触发回响：每层被消耗的星痕给予 <b>+1 格挡</b>（可见闭环），若持有能力「星痕感应」则额外获得临时力量。</li>")
html.append("<li>打法：用铺痕卡（星轨重踏 / 星灰筛 / 星图残页 / 星尘沉淀）给敌人叠星痕，再用<b>任意攻击</b>引爆——既加伤又被消耗回资源。</li></ul>")

html.append("<h3>4. 其它</h3>")
html.append("<ul><li><b>冰尘</b>：霜寒溢出的附带伤害，可被能力放大（冰尘回响 / 冰尘风暴）。</li>")
html.append("<li><b>精灰</b>：能力「精灰形态」将霜寒减半损失的层数转为精灰，攻击加伤，满 10 精灰生成「星灰绽放」。</li></ul>")

html.append("<h2>二、卡牌总览</h2>")

for ctype_key, title in [("ATTACK", "攻击牌"), ("SKILL", "技能牌"), ("POWER", "能力牌")]:
    grp = [c for c in cards if c["type"] == ctype_key]
    html.append("<h3>%s（%d 张）</h3>" % (title, len(grp)))
    html.append("<table><thead><tr><th>名称</th><th>费</th><th>稀有</th><th>目标</th><th>霜偿</th><th>效果</th></tr></thead><tbody>")
    for c in grp:
        r = RARITY.get(c["rarity"], c["rarity"])
        rc = {"COMMON":"t-common","UNCOMMON":"t-uncommon","RARE":"t-rare","BASIC":"t-basic"}.get(c["rarity"],"")
        extra = (' <span class="note">[%s]</span>' % c["flags"]) if c["flags"] else ""
        eff = esc(c["desc"]).replace("\n", "<br>")
        html.append("<tr><td>%s</td><td>%s</td><td><span class='tag %s'>%s</span></td><td>%s</td><td>%s</td><td>%s%s</td></tr>" % (
            esc(c["name"]), esc(c["cost"]), rc, r, TARGET.get(c["target"], c["target"]),
            esc(fp_label(c["frostpay"])), eff, extra))
    html.append("</tbody></table>")

html.append('<p class="note">注：「【关联伤害】」/「【关联数值】」为跨角色（鸿文）关键字引用，原工程以 !hongwen:sd! 等占位，本文以中文标注。</p>')

html.append("<h2>三、专属遗物</h2>")
html.append("<table><thead><tr><th>名称</th><th>类型</th><th>效果</th></tr></thead><tbody>")
for r in relics:
    html.append("<tr><td>%s</td><td>%s</td><td>%s</td></tr>" % (esc(r["name"]), TIER.get(r["tier"], r["tier"]), esc(r["desc"])))
html.append("</tbody></table>")

html.append("</body></html>")
html_text = "\n".join(html)
with open(os.path.join(OUT, "stage2", "formatted-霜星旅者.html"), "w", encoding="utf-8") as f:
    f.write(html_text)

print("cards:", len(cards), "relics:", len(relics))
print("md bytes:", len(md_text), "html bytes:", len(html_text))
print("Wrote stage1/final_draft.md and stage2/formatted-霜星旅者.html")
