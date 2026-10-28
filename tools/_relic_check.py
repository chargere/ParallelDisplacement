# -*- coding: utf-8 -*-
"""遗物图抠图验收总览：主图放棋盘格(验透明) + 叠游戏深底(验观感)"""
import os
from PIL import Image, ImageDraw, ImageFont

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master"
REL = os.path.join(ROOT, "src", "main", "resources", "hongwenResources", "images", "relics")
OUT = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview\relics_final_check.png"

CN = {
    "CompassionFire": "怜悯の火", "NoMercy": "无人怜悯", "FurnaceBreath": "熔炉之息",
    "ConductEcho": "雷纹回响", "UnsentFlower": "未送出的花", "PlagueEmberAsh": "荒疫残烬",
    "RainyPromise": "雨天的约定", "EternalStormAnchor": "永恒风暴的锚点",
    "AuroraPocketWatch": "极光怀表", "MirrorPocketWatch": "镜像怀表", "ClosedStarChart": "闭合星图",
    "UnfinishedStarforge": "未竟的星锻", "HatchFrost": "霜之孵", "ThreeDayHatch": "三日孵",
    "ObservationHalt": "观测停止", "PrismOpticSystem": "棱镜光学", "SwordTasselAlong": "剑穗随行",
    "VioletAurora": "紫极光", "UselessFetus": "无用之胎", "PlagueEmberAsh2": "荒疫残烬2",
}
# 中文映射不全也没关系，缺的显示英文 ID


def checker(size, cell=10):
    n = size // cell + 1
    arr = np.zeros((size, size, 3), dtype=np.uint8)
    for i in range(n):
        for j in range(n):
            v = (232, 232, 232) if (i + j) % 2 == 0 else (188, 188, 188)
            arr[i * cell:(i + 1) * cell, j * cell:(j + 1) * cell] = v
    return Image.fromarray(arr)


import numpy as np

COLS = 6
S = 128
PAD = 14
files = sorted(f for f in os.listdir(REL)
               if f.endswith(".png") and not f.endswith("Outline.png"))
files = [f for f in files if f[:-4] != "TodoItem"]
rows = (len(files) + COLS - 1) // COLS
CELL_W, CELL_H = S + 26, S + 52
W = COLS * CELL_W + PAD * 2
H = rows * CELL_H + PAD * 2 + 40

sheet = Image.new("RGB", (W, H), (255, 255, 255))
d = ImageDraw.Draw(sheet)
f_t = ImageFont.truetype(r"C:\Windows\Fonts\msyh.ttc", 18)
f_l = ImageFont.truetype(r"C:\Windows\Fonts\msyh.ttc", 12)
d.text((PAD, 10), "遗物图 · 透明化验收（左半棋盘=透明通道 / 右半叠游戏深底=实际观感）", font=f_t, fill=(30, 30, 30))

for i, fn in enumerate(files):
    rid = fn[:-4]
    r, c = divmod(i, COLS)
    x = PAD + c * CELL_W
    y = 40 + r * CELL_H
    im = Image.open(os.path.join(REL, fn)).convert("RGBA")
    half = checker(S // 2)
    cell = Image.new("RGB", (S, S), (58, 48, 38))
    cell.paste(half, (0, 0))
    cell.paste(half, (S // 2, S // 2))
    cell.paste(im, (0, 0), im)
    sheet.paste(cell, (x, y))
    name = CN.get(rid, rid)
    d.text((x, y + S + 4), name, font=f_l, fill=(30, 30, 30))
    a = np.asarray(im.split()[3])
    d.text((x, y + S + 20), "透明 %.0f%%" % ((a < 16).mean() * 100), font=f_l, fill=(150, 150, 150))

sheet.save(OUT)
print("saved", OUT, sheet.size, "张数", len(files))
