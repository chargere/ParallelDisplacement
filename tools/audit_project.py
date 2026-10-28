# -*- coding: utf-8 -*-
"""
平行异位 模组静态审计脚本
交叉核对：Java 类 ID  <->  图片资源  <->  本地化字符串
"""
import os, re, json, sys, collections

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
JAVA = os.path.join(ROOT, "src", "main", "java", "yourmod")
RES = os.path.join(ROOT, "src", "main", "resources", "hongwenResources")
IMG = os.path.join(RES, "images")
LOC = os.path.join(RES, "localization", "eng")

def read(p):
    try:
        with open(p, "r", encoding="utf-8", errors="ignore") as f:
            return f.read()
    except Exception:
        return ""

# ---------- 1. 收集 Java 侧 ID ----------
power_ids, relic_ids, card_ids = {}, {}, {}
base_classes = set()

for dirpath, _, files in os.walk(JAVA):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        p = os.path.join(dirpath, fn)
        s = read(p)
        cls = fn[:-5]
        rel = os.path.relpath(p, JAVA)

        if "/powers/" in "/" + rel.replace("\\", "/"):
            m = re.search(r'POWER_ID\s*=\s*makeID\(\s*"([^"]+)"', s) or \
                re.search(r'POWER_ID\s*=\s*"([^"]+)"', s)
            if m:
                power_ids[cls] = m.group(1)
        elif "/relics/" in "/" + rel.replace("\\", "/"):
            m = re.search(r'String\s+ID\s*=\s*makeID\(\s*"([^"]+)"', s) or \
                re.search(r'ID\s*=\s*makeID\(\s*"([^"]+)"', s)
            if m:
                relic_ids[cls] = m.group(1)
        elif "/cards/" in "/" + rel.replace("\\", "/"):
            if cls.startswith("Abstract") or cls in ("CardVars",):
                base_classes.add(cls); continue
            m = re.search(r'public\s+static\s+final\s+String\s+ID\s*=\s*makeID\(\s*"([^"]+)"', s) or \
                re.search(r'ID\s*=\s*makeID\(\s*"([^"]+)"', s)
            if m:
                card_ids[cls] = m.group(1)

# ---------- 2. 图片资源 ----------
pw_icons = {f[:-6] for f in os.listdir(os.path.join(IMG, "powers")) if f.endswith("32.png")}
pw_big = {f[:-6] for f in os.listdir(os.path.join(IMG, "powers")) if f.endswith("84.png")}
relic_imgs = {f[:-4] for f in os.listdir(os.path.join(IMG, "relics"))
              if f.endswith(".png") and not f.endswith("Outline.png")}
relic_out = {f[:-11] for f in os.listdir(os.path.join(IMG, "relics"))
             if f.endswith("Outline.png")}

# ---------- 3. 本地化 ----------
def load(fn):
    p = os.path.join(LOC, fn)
    if not os.path.exists(p):
        return {}
    try:
        return json.loads(read(p))
    except Exception as e:
        print(f"  [JSON 解析失败] {fn}: {e}")
        return {}

loc_card = load("Cardstrings.json")
loc_power = load("Powerstrings.json")
loc_relic = load("Relicstrings.json")
loc_kw = load("Keywordstrings.json")

# ---------- 4. 报告 ----------
print("=" * 66)
print("平行异位 · 静态审计报告")
print("=" * 66)

print(f"\n[规模]")
print(f"  Power 类(有 ID) : {len(power_ids)}")
print(f"  遗物类(有 ID)   : {len(relic_ids)}")
print(f"  卡牌类(有 ID)   : {len(card_ids)}  (另有基类 {len(base_classes)})")

# Power 图标覆盖
print(f"\n[Power 图标覆盖]  图标32={len(pw_icons)}  图标84={len(pw_big)}")
miss_icon = [k for k, v in power_ids.items() if v not in pw_icons]
orphan_icon = sorted(pw_icons - set(power_ids.values()))
print(f"  缺图 power   : {len(miss_icon)}")
for k in sorted(miss_icon)[:15]:
    print(f"      - {k} -> {power_ids[k]}")
if len(miss_icon) > 15:
    print(f"      ... 共 {len(miss_icon)} 个")
print(f"  孤儿图标(无对应类): {len(orphan_icon)}  {orphan_icon[:12]}")
sz = sorted(pw_icons ^ pw_big)
print(f"  32/84 尺寸不配对: {len(sz)}  {sz[:10]}")

# 遗物图覆盖
print(f"\n[遗物图覆盖]  主图={len(relic_imgs)}  outline={len(relic_out)}")
miss_r = [k for k, v in relic_ids.items() if v not in relic_imgs]
print(f"  缺图遗物 : {len(miss_r)}  -> {[relic_ids[k] for k in sorted(miss_r)]}")
no_out = sorted(relic_imgs - relic_out)
print(f"  缺 outline: {len(no_out)}  {no_out[:12]}")

# 本地化覆盖
print(f"\n[本地化覆盖]")
for name, loc, ids in (("Cardstrings", loc_card, card_ids),
                       ("Powerstrings", loc_power, power_ids),
                       ("Relicstrings", loc_relic, relic_ids)):
    miss = [k for k, v in ids.items() if v not in loc]
    extra = sorted(set(loc.keys()) - set(ids.values()))
    print(f"  {name:13s} 条目={len(loc):4d}  缺失={len(miss):3d}  多余={len(extra):3d}")
    for k in sorted(miss)[:10]:
        print(f"      缺: {k} -> {ids[k]}")
    if len(miss) > 10:
        print(f"      ... 共 {len(miss)} 个")
    if extra[:8]:
        print(f"      余: {extra[:8]}")

# 卡牌本地化字段完整性
print(f"\n[卡牌本地化字段完整性]")
bad = []
for k, v in card_ids.items():
    e = loc_card.get(v)
    if not isinstance(e, dict):
        continue
    need = ("NAME", "DESCRIPTION")
    for n in need:
        if n not in e or not str(e[n]).strip():
            bad.append((k, v, n))
print(f"  缺 NAME/DESCRIPTION: {len(bad)}")
for b in bad[:12]:
    print(f"      - {b[0]} ({b[1]}) 缺 {b[2]}")

# 卡面图
cardart_dirs = ["1024", "512"]
print(f"\n[卡面图]")
for d in cardart_dirs:
    p = os.path.join(IMG, d)
    if os.path.isdir(p):
        n = len([f for f in os.listdir(p) if f.endswith(".png")])
        print(f"  images/{d}: {n} 张  (卡牌 {len(card_ids)} 张 -> 覆盖率 {n*100//max(len(card_ids),1)}%)")

# 关键词使用但未定义
print(f"\n[关键词定义]  Keywordstrings 条目 = {len(loc_kw)}")
