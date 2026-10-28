#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Extract Dominic (霜星旅者) cards + relics metadata + localization for docx generation."""
import os, re, json

PROJ = "C:/Users/38220/Desktop/ProTemplate-master"
CARDS_DIR = os.path.join(PROJ, "src/main/java/yourmod/cards/dominic")
RELICS_DIR = os.path.join(PROJ, "src/main/java/yourmod/relics")
CARDSTR = os.path.join(PROJ, "src/main/resources/hongwenResources/localization/eng/Cardstrings.json")
RELICSTR = os.path.join(PROJ, "src/main/resources/hongwenResources/localization/eng/Relicstrings.json")

DOMINIC_RELICS = ["AuroraPocketWatch", "ClosedStarChart", "HatchFrost",
                  "MirrorPocketWatch", "ThreeDayHatch", "UnfinishedStarforge"]

def load_json(path):
    with open(path, encoding="utf-8") as f:
        return json.load(f)

def suffix(key):
    return key.split(":")[-1]

# ---- localization ----
cardstr = load_json(CARDSTR)
relicstr = load_json(RELICSTR)
card_l10n = {suffix(k): v for k, v in cardstr.items()}
relic_l10n = {suffix(k): v for k, v in relicstr.items()}

def card_name(cid):
    e = card_l10n.get(cid, {})
    return e.get("NAME") or cid

def card_desc(cid):
    e = card_l10n.get(cid, {})
    if "DESCRIPTION" in e:
        return e["DESCRIPTION"]
    if "DESCRIPTIONS" in e and e["DESCRIPTIONS"]:
        return e["DESCRIPTIONS"][0]
    return ""

def relic_name(rid):
    e = relic_l10n.get(rid, {})
    return e.get("NAME") or rid

def relic_desc(rid):
    e = relic_l10n.get(rid, {})
    if "DESCRIPTIONS" in e and e["DESCRIPTIONS"]:
        return e["DESCRIPTIONS"][0]
    if "DESCRIPTION" in e:
        return e["DESCRIPTION"]
    return ""

# ---- parse cards ----
cards = []
for fn in sorted(os.listdir(CARDS_DIR)):
    if not fn.endswith(".java"):
        continue
    path = os.path.join(CARDS_DIR, fn)
    src = open(path, encoding="utf-8").read()
    m = re.search(r'ID\s*=\s*makeID\("([^"]+)"\)', src)
    if not m:
        m = re.search(r'public static final String ID = "([^"]+)"', src)
    if not m:
        continue
    cid = m.group(1)
    cm = re.search(r'super\(\s*ID,\s*([^,]+),\s*CardType\.(\w+),\s*CardRarity\.(\w+),\s*CardTarget\.(\w+)\s*\)', src)
    cost = cm.group(1).strip() if cm else "?"
    ctype = cm.group(2) if cm else "?"
    crarity = cm.group(3) if cm else "?"
    ctarget = cm.group(4) if cm else "?"
    fp = re.search(r'setFrostPay\(([^)]*)\)', src)
    frostpay = fp.group(1).strip().replace(" ", "") if fp else ""
    def num(label):
        mm = re.search(label + r'\s*=\s*(\d+)', src)
        return mm.group(1) if mm else ""
    dmg = num(r'baseDamage')
    blk = num(r'baseBlock')
    mag = num(r'baseMagicNumber\s*=\s*magicNumber')
    if not mag:
        mag = num(r'baseMagicNumber')
    flags = []
    for f in ["exhaust = true", "ethereal = true", "innate = true", "selfRetain = true", "retain = true"]:
        if f in src:
            flags.append(f.split(" = ")[0])
    cards.append(dict(id=cid, file=fn, cost=cost, type=ctype, rarity=crarity,
                      target=ctarget, frostpay=frostpay, dmg=dmg, block=blk, magic=mag,
                      flags=",".join(flags),
                      name_zh=card_name(cid), desc=card_desc(cid)))

# ---- parse relics ----
relics = []
for rid in DOMINIC_RELICS:
    path = os.path.join(RELICS_DIR, rid + ".java")
    if not os.path.exists(path):
        relics.append(dict(id=rid, name=relic_name(rid), desc=relic_desc(rid), tier="?", color="?"))
        continue
    src = open(path, encoding="utf-8").read()
    tm = re.search(r'RelicTier\.(\w+)', src)
    tier = tm.group(1) if tm else "?"
    cm = re.search(r'(Dominic|Hongwen|Jingjue|Damuhou|Fvv)\w*Character\.Enums\.(\w+)_COLOR', src)
    color = cm.group(1) if cm else "?"
    relics.append(dict(id=rid, name=relic_name(rid), desc=relic_desc(rid), tier=tier, color=color))

# ---- output ----
out = os.path.join(PROJ, "tools", "dominic_extract.txt")
with open(out, "w", encoding="utf-8") as f:
    f.write("=== CARDS (%d) ===\n" % len(cards))
    order = {"ATTACK": 0, "SKILL": 1, "POWER": 2, "STATUS": 3, "CURSE": 4}
    cards.sort(key=lambda c: (order.get(c["type"], 9), c["rarity"], c["name_zh"]))
    for c in cards:
        f.write("\n[%s] %s  (id=%s)\n" % (c["type"], c["name_zh"], c["id"]))
        f.write("  cost=%s rarity=%s target=%s frostpay=[%s] dmg=%s block=%s magic=%s flags=%s\n"
                % (c["cost"], c["rarity"], c["target"], c["frostpay"], c["dmg"], c["block"], c["magic"], c["flags"]))
        f.write("  DESC: %s\n" % c["desc"].replace("\n", " "))
    f.write("\n\n=== RELICS (%d) ===\n" % len(relics))
    for r in relics:
        f.write("\n[%s] %s  (tier=%s color=%s)\n" % (r["id"], r["name"], r["tier"], r["color"]))
        f.write("  DESC: %s\n" % r["desc"].replace("\n", " "))

# also print summary
print("CARDS:", len(cards))
print("RELICS:", len(relics))
for t in ["ATTACK", "SKILL", "POWER"]:
    print("  %s: %d" % (t, sum(1 for c in cards if c["type"] == t)))
print("Wrote", out)
