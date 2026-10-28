# -*- coding: utf-8 -*-
"""归框前后对比预览：focus(目镜/枪 before-after) + 全部遗物深色卡底总览。"""
import os, numpy as np
from PIL import Image, ImageDraw

NEW = r"C:\Users\38220\Desktop\ProTemplate-master\src\main\resources\hongwenResources\images\relics"
OLD = r"C:\Users\38220\Desktop\ProTemplate-master\tmp_relic_drafts\_pre_shrink"
PREVIEW = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"

def load(p):
    return Image.open(p).convert("RGBA")

def cardbg(s, c=(28, 26, 34)):
    return Image.new("RGB", (s, s), c)

# ---- focus: 目镜/枪 before-after ----
focus = [("PrismOpticSystem", "目镜"), ("VioletAurora", "枪")]
cell = 200
sheet = Image.new("RGB", (cell * 2 * len(focus) + 20, cell + 70), (15, 15, 15))
d = ImageDraw.Draw(sheet)
for i, (name, label) in enumerate(focus):
    col = i * 2
    for j, tag in enumerate(["OLD", "NEW"]):
        src = OLD if tag == "OLD" else NEW
        im = load(os.path.join(src, name + ".png")).resize((cell, cell), Image.LANCZOS)
        cellbg = cardbg(cell)
        cellbg.paste(im, (0, 0), im)
        x = 10 + (col + j) * cell
        sheet.paste(cellbg, (x, 30))
        d.text((x, 8), f"{label} {tag}", fill=(180, 180, 180))
sheet.save(PREVIEW + r"\relic_resize_focus.png")
print("focus done")

# ---- 全部遗物深色卡底总览 ----
files = sorted([f for f in os.listdir(NEW) if f.endswith(".png") and not f.endswith("Outline.png")])
cols = 7
S = 150
rows = (len(files) + cols - 1) // cols
sheet2 = Image.new("RGB", (cols * S + 10, rows * S + 30), (15, 15, 15))
d2 = ImageDraw.Draw(sheet2)
for idx, f in enumerate(files):
    r, c = divmod(idx, cols)
    im = load(os.path.join(NEW, f)).resize((S - 10, S - 10), Image.LANCZOS)
    cb = cardbg(S - 10)
    cb.paste(im, (0, 0), im)
    x = 5 + c * S
    y = 5 + r * S
    sheet2.paste(cb, (x, y))
    d2.text((x, y + S - 10), f[:-4], fill=(120, 120, 120))
sheet2.save(PREVIEW + r"\relic_resize_grid.png")
print("grid done", len(files), "relics")
