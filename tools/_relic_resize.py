# -*- coding: utf-8 -*-
"""遗物统一归框：主体最长边占画布比例归一到 TARGET（只缩不放），居中留边，
对齐原版遗物框法。主图与 outline 用同一 bbox/比例，保证对齐。
"""
import os, numpy as np, cv2
from PIL import Image

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master\src\main\resources\hongwenResources\images\relics"
SRC = r"C:\Users\38220\Desktop\ProTemplate-master\tmp_relic_drafts\_pre_shrink"  # 原始尺寸备份，避免二次缩放累积模糊
TARGET = 0.55  # 主体最长边 / 128
PAD = 4

def subject_bbox(img):
    a = np.asarray(img)[:, :, 3]
    ys, xs = np.where(a > 40)
    if len(ys) == 0:
        return None
    return (max(0, xs.min() - PAD), max(0, ys.min() - PAD),
            min(img.width, xs.max() + 1 + PAD), min(img.height, ys.max() + 1 + PAD))

changed = []
for f in sorted(os.listdir(ROOT)):
    if not f.endswith(".png") or f.endswith("Outline.png"):
        continue
    base = f[:-4]
    out_p = os.path.join(ROOT, f)
    ol_p = os.path.join(ROOT, base + "Outline.png")
    main = Image.open(os.path.join(SRC, f)).convert("RGBA")
    bb = subject_bbox(main)
    if bb is None:
        continue
    W = bb[2] - bb[0]; H = bb[3] - bb[1]
    maxd = max(W, H)
    if maxd / 128.0 <= TARGET:
        continue  # 只缩不放
    scale = TARGET * 128.0 / maxd
    nw, nh = max(1, int(round(W * scale))), max(1, int(round(H * scale)))
    crop = main.crop(bb)
    newmain = crop.resize((nw, nh), Image.LANCZOS)
    canvas = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
    canvas.paste(newmain, ((128 - nw) // 2, (128 - nh) // 2), newmain)
    canvas.save(out_p)

    if os.path.exists(ol_p):
        ol = Image.open(os.path.join(SRC, base + "Outline.png")).convert("RGBA")
        ol_crop = ol.crop(bb)
        ol_new = ol_crop.resize((nw, nh), Image.LANCZOS)
        ol_canvas = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
        ol_canvas.paste(ol_new, ((128 - nw) // 2, (128 - nh) // 2), ol_new)
        ol_canvas.save(ol_p)
    changed.append((base, round(maxd / 128.0, 2), round(TARGET, 2)))

print("changed %d relics:" % len(changed))
for c in changed:
    print("  ", c[0], "%.2f->%.2f" % (c[1], c[2]))
