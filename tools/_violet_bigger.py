# -*- coding: utf-8 -*-
"""单独把 VioletAurora(大枪) 从原始备份重缩到 TARGET，主图+outline 对齐。"""
import os, numpy as np
from PIL import Image

ROOT = r"C:\Users\38220\Desktop\ProTemplate-master\src\main\resources\hongwenResources\images\relics"
SRC = r"C:\Users\38220\Desktop\ProTemplate-master\tmp_relic_drafts\_pre_shrink"
TARGET = 0.80
P = 4

for base in ["VioletAurora"]:
    main = Image.open(os.path.join(SRC, base + ".png")).convert("RGBA")
    a = np.asarray(main)[:, :, 3]
    ys, xs = np.where(a > 40)
    bb = (max(0, xs.min() - P), max(0, ys.min() - P),
          min(main.width, xs.max() + 1 + P), min(main.height, ys.max() + 1 + P))
    W = bb[2] - bb[0]; H = bb[3] - bb[1]; maxd = max(W, H)
    scale = TARGET * 128.0 / maxd
    nw, nh = int(round(W * scale)), int(round(H * scale))
    nm = main.crop(bb).resize((nw, nh), Image.LANCZOS)
    c = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
    c.paste(nm, ((128 - nw) // 2, (128 - nh) // 2), nm)
    c.save(os.path.join(ROOT, base + ".png"))

    ol = Image.open(os.path.join(SRC, base + "Outline.png")).convert("RGBA")
    oln = ol.crop(bb).resize((nw, nh), Image.LANCZOS)
    c2 = Image.new("RGBA", (128, 128), (0, 0, 0, 0))
    c2.paste(oln, ((128 - nw) // 2, (128 - nh) // 2), oln)
    c2.save(os.path.join(ROOT, base + "Outline.png"))
    print(base, "maxFrac %.2f -> %.2f" % (maxd / 128.0, TARGET), "size", (nw, nh))
