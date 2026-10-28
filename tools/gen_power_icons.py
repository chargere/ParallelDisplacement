# -*- coding: utf-8 -*-
"""
power 图标批量生成脚本（美术流水线）
====================================
来源：game-icons.net（CC BY 3.0），仓库 https://github.com/game-icons/icons
规则：每个 power 对应一个 SVG -> 改色 -> 渲染 84x84 / 32x32 PNG ->
     落盘 src/main/resources/hongwenResources/images/powers/{ID}{84|32}.png

用法：
  python tools/gen_power_icons.py            # 生成全部映射表里的图标
  python tools/gen_power_icons.py Breach ...  # 只重生成指定 power

换色/换风格：改 COLOR_BUFF / COLOR_DEBUFF / COLOR_NEUTRAL。
加新图标：往 MAPPING 里加一行 "PowerID": "作者/图标名"。
"""
import os, re, sys, json, glob

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ICONS = os.path.join(ROOT, "tmp_decompile", "game-icons")
OUT = os.path.join(ROOT, "src", "main", "resources", "hongwenResources", "images", "powers")
SRC_POWERS = os.path.join(ROOT, "src", "main", "java", "yourmod", "powers")

COLOR_BUFF = "#E8B54D"      # buff：暖金
COLOR_DEBUFF = "#A66BD4"    # debuff：冷紫
COLOR_NEUTRAL = "#D9D9D9"   # 兜底：浅灰

# PowerID -> game-icons svg 相对路径（不含 .svg）
MAPPING = {
    "AdmitCommittee": "delapouite/vote",
    "Afk": "delapouite/gamepad",
    "AfterSheatheWarmth": "lorc/campfire",
    "AuroraCommand": "delapouite/lightning-dome",
    "BalanceHeal": "lorc/scales",
    "BladeDoctrine": "lorc/rune-sword",
    "BladeReady": "lorc/sparkling-sabre",
    "BornWithIt": "delapouite/cosmic-egg",
    "Breach": "lorc/edge-crack",
    "BurnHeaven": "lorc/burning-meteor",
    "CannotGainBlock": "delapouite/broken-wall",
    "ChargeStance": "caro-asercion/tesla-coil",
    "ChatMute": "delapouite/mute",
    "ConductAmp": "delapouite/imperial-crown",
    "Creditor": "delapouite/wallet",
    "DamuhouAmmoCycle": "delapouite/machine-gun-magazine",
    "DamuhouAmmoRation": "sbed/ammo-box",
    "DamuhouBattlefieldRhythm": "delapouite/metronome",
    "DamuhouBeltFeed": "delapouite/shotgun-rounds",
    "DamuhouCalmAim": "lorc/on-target",
    "DamuhouCombatFatigue": "lorc/muscle-fat",
    "DamuhouFocus": "delapouite/crosshair",
    "DamuhouGunFu": "felbrigg/dodge",
    "DamuhouHowlForm": "lorc/wolf-howl",
    "DamuhouKeepMoving": "lorc/run",
    "DamuhouLeftover": "delapouite/reload-gun-barrel",
    "DamuhouLowProfile": "darkzaitzev/running-ninja",
    "DamuhouMiddleCounts": "delapouite/multiple-targets",
    "DamuhouMuscleMemory": "delapouite/biceps",
    "DamuhouMysterious": "lorc/ghost",
    "DamuhouPremonition": "lorc/crystal-ball",
    "DamuhouSlowDraw": "skoll/winchester-rifle",
    "DamuhouSmoke": "darkzaitzev/smoke-bomb",
    "DamuhouSpareMag": "sbed/rifle",
    "DamuhouSpiteCombo": "delapouite/angry-eyes",
    "DamuhouStepOff": "delapouite/plain-arrow",
    "DamuhouTacticalBreath": "lorc/energy-breath",
    "DamuhouVioletGlow": "skoll/laser-burst",
    "DamuhouWeakSpot": "lorc/magnifying-glass",
    "DebtLedger": "delapouite/contract",
    "DefyFate": "delapouite/backward-time",
    "DelayTax": "delapouite/post-stamp",
    "DualGuard": "delapouite/attached-shield",
    "EchoEnergy": "lorc/echo-ripples",
    "EmberFire": "lorc/burning-embers",
    "EmberThunderEternal": "lorc/lightning-helix",
    "EmberThunderOrders": "lorc/focused-lightning",
    "EnergyFraudDebt": "delapouite/take-my-money",
    "EternalAurora": "caro-asercion/tarot-17-the-star",
    "EternalBurn": "lorc/meteor-impact",
    "EternalStormAnchorPower": "lorc/anchor",
    "FamousEffect": "delapouite/megaphone",
    "FavorMark": "lorc/wax-seal",
    "FetalFail": "lorc/dinosaur-egg",
    "FirstAttackEmber": "delapouite/torch",
    "FirstAttackStatic": "lorc/lightning-branches",
    "FreezeCrack": "lorc/crystalize",
    "FrostBiteMark": "lorc/frostfire",
    "FrostCapRift": "lorc/earth-crack",
    "FrostCold": "lorc/snowflake-1",
    "FrostPayDiscount": "delapouite/two-coins",
    "FrostRidge": "lorc/icicles-fence",
    "HealHalve": "lorc/candle-flame",
    "HeavenFireDestiny": "delapouite/fire-gem",
    "HeavenFireUndying": "delapouite/old-lantern",
    "HowDoYouKnow": "delapouite/think",
    "IAmEgg": "delapouite/raw-egg",
    "IceDustEcho": "lorc/snowflake-2",
    # ---- 精绝王者（Jj*）：取自 game-icons.net，题材为盗墓 / 精绝古城 / 虚数时空 ----
    "JjAltarHourglass": "lorc/sands-of-time",
    "JjBlackFace": "lorc/crystal-eye",
    "JjBoundlessEye": "delapouite/all-seeing-eye",
    "JjCorridor": "lorc/maze",
    "JjDantianAltar": "delapouite/dragon-orb",
    "JjDapuForm": "lorc/scarab-beetle",
    "JjDreadForm": "lorc/dread-skull",
    "JjErosion": "sbed/acid",
    "JjExecuteForce": "lorc/guillotine",
    "JjEyeMark": "delapouite/eye-target",
    "JjHourglass": "lorc/hourglass",
    "JjHunt": "delapouite/deer-track",
    "JjImaginary": "lorc/spectre",
    "JjImmolate": "delapouite/pyre",
    "JjReverseWalker": "lorc/back-forth",
    "JjSerpentForce": "delapouite/sand-snake",
    "JjSkirmishStone": "lorc/floating-crystal",
    "JjSnakeBone": "lorc/snake-totem",
    "JjTimeDilation": "lorc/heavy-timer",
    "JjTimeField": "lorc/bubble-field",
    "JjTimeTurbulence": "lorc/whirlwind",
    "JjWalkingIllusion": "lorc/mirror-mirror",
    "JjWorldlyPearl": "lorc/concentration-orb",
    "JjZagrammaEye": "delapouite/eye-of-horus",
    "JustLikeThis": "delapouite/sofa",
    "LastLineOfChart": "delapouite/galaxy",
    "LateAwareness": "skoll/pocket-watch",
    "MagneticBomb": "lorc/magnet-blast",
    "MemoryBurnout": "lorc/burning-book",
    "NextCardBoost": "delapouite/coffee-cup",
    "NextSkillDiscount": "lorc/icicles-aura",
    "NextTurnDrawUseless": "delapouite/rule-book",
    "NextTurnEnergy": "lorc/lightning-electron",
    "NextTurnEnergyLoss": "sbed/unplugged",
    "NextTurnFrost": "lorc/snowing",
    "NextTurnTempStr": "lorc/muscle-up",
    "NextTurnWeak": "delapouite/spark-plug",
    "NoSkills": "delapouite/smartphone",
    "PassiveAggression": "lorc/archery-target",
    "PrismAmp": "delapouite/convergence-target",
    "PrismDamp": "delapouite/triangle-target",
    "QuietStargaze": "delapouite/telescope",
    "RageForm": "delapouite/devil-mask",
    "RedHeatExtreme": "lorc/fluffy-flame",
    "RefinedAsh": "lorc/dust-cloud",
    "RefinedAshForm": "lorc/atomic-slashes",
    "ReturnToVoid": "lorc/vortex",
    "ScorchingAssault": "delapouite/flamethrower",
    "SharedWarmth": "delapouite/fireplace",
    "Silence": "lorc/silence",
    "SilentVigil": "delapouite/watchtower",
    "SkipNextDraw": "delapouite/player-next",
    "SkipNextTurn": "delapouite/player-time",
    "StarExtinguish": "delapouite/knocked-out-stars",
    "StarMark": "delapouite/seven-pointed-star",
    "StarMarkSense": "lorc/bolt-eye",
    "StardustSettle": "lorc/pollen-dust",
    "StaticMark": "lorc/spark-spirit",
    "StaticResonance": "lorc/static-waves",
    "SwordAlongTheWay": "delapouite/sword-brandish",
    "ThinFrostDiscipline": "lorc/crystal-bars",
    "ThunderNetEffect": "lorc/arcing-bolt",
    "ThunderScarMark": "lorc/thunder-struck",
    "TigerCrouchCannon": "lorc/cannon",
    "UndyingWall": "delapouite/stone-wall",
    "UnfinishedStarforge": "delapouite/sword-mold",
    "UnyieldingEmber": "lorc/afterburn",
    "Useless": "delapouite/trash-can",
    "UselessAscend": "lorc/angel-wings",
    "UselessEconomy": "delapouite/coins-pile",
    "VeilStatic": "delapouite/theater-curtains",
    "WarIntent": "lorc/flame-claws",
    "WasteCrown": "lorc/crown",
}


def build_id_index():
    """PowerID -> java 文件路径（ID 与类名不保证同名，需全量扫）"""
    idx = {}
    # 递归：power 类分角色放在 powers/<角色>/ 子目录里，只扫一层会全漏
    for f in glob.glob(os.path.join(SRC_POWERS, "**", "*.java"), recursive=True):
        s = open(f, encoding="utf-8", errors="ignore").read()
        for m in re.finditer(r'String\s+POWER_ID\s*=\s*makeID\("([^"]+)"\)', s):
            idx[m.group(1)] = f
    return idx


ID_INDEX = build_id_index()


def power_type(pid):
    """从 java 源码抓 PowerType：BUFF / DEBUFF / NEUTRAL"""
    path = ID_INDEX.get(pid)
    if not path:
        return "NEUTRAL"
    s = open(path, encoding="utf-8", errors="ignore").read()
    if re.search(r"PowerType\.DEBUFF", s):
        return "DEBUFF"
    if re.search(r"PowerType\.BUFF", s):
        return "BUFF"
    return "NEUTRAL"


def colorize(svg_text, color):
    """game-icons svg 结构 = 铺满黑底的 path + fill=#fff 的图形 path。
    产出：删黑底 -> 图形换目标色 -> 透明底彩色剪影"""
    svg_text = svg_text.replace('<path d="M0 0h512v512H0z"/>', "", 1)
    if 'fill="#fff"' in svg_text:
        svg_text = svg_text.replace('fill="#fff"', 'fill="%s"' % color)
    else:
        svg_text = svg_text.replace("<svg ", '<svg fill="%s" ' % color, 1)
    return svg_text


def render(svg_path, color, out_base):
    import resvg_py
    raw = open(svg_path, encoding="utf-8").read()
    svg = colorize(raw, color)
    for size, suffix in ((84, "84"), (32, "32")):
        png = resvg_py.svg_to_bytes(svg_string=svg, width=size, height=size)
        with open(os.path.join(OUT, "%s%s.png" % (out_base, suffix)), "wb") as f:
            f.write(bytes(png))


def main():
    only = set(sys.argv[1:])
    missing = [k for k, v in MAPPING.items()
               if not os.path.exists(os.path.join(ICONS, v + ".svg"))]
    if missing:
        print("[x] 映射表里这些 svg 不存在：")
        for m in missing:
            print("   ", m, "->", MAPPING[m])
        sys.exit(1)

    ok, fail = 0, []
    for pid, rel in sorted(MAPPING.items()):
        if only and pid not in only:
            continue
        t = power_type(pid)
        color = {"BUFF": COLOR_BUFF, "DEBUFF": COLOR_DEBUFF}.get(t, COLOR_NEUTRAL)
        try:
            render(os.path.join(ICONS, rel + ".svg"), color, pid)
            ok += 1
        except Exception as e:
            fail.append((pid, repr(e)))
    print("生成 %d 个（84px + 32px）" % ok)
    for pid, e in fail:
        print("[x]", pid, e)


if __name__ == "__main__":
    main()
