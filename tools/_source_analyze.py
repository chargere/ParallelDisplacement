# -*- coding: utf-8 -*-
"""分析战斗精英遗物来源图：物体检测 + 主色 + 生成标注缩略图。
用途：模型读不了图时，把图切成编号区域让用户指认映射。
"""
import os
import numpy as np
from scipy import ndimage
from PIL import Image, ImageDraw, ImageFont

SRC = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "tmp_relic_drafts", "source")
OUT = r"C:\Users\38220\WorkBuddy\2026-10-23-14-53-22\.workbuddy\preview"

FILES = ["战斗精英-来源1.jpg", "战斗精英-来源2.png"]


def detect_objects(im_gray, thresh=60, min_area=800):
    a = np.asarray(im_gray, dtype=np.float32)
    bright = (a > thresh).astype(np.uint8)
    lab, n = ndimage.label(bright)
    objs = []
    for i in range(1, n + 1):
        ys, xs = np.where(lab == i)
        if len(ys) < min_area:
            continue
        objs.append((int(len(ys)), int(xs.min()), int(ys.min()), int(xs.max()), int(ys.max())))
    objs.sort(reverse=True)
    return objs


def dominant_colors(im, k=4):
    arr = np.asarray(im.convert("RGB").resize((64, 64))).reshape(-1, 3).astype(np.float32)
    # 简单聚类：取亮度分布的主要色调
    from collections import Counter
    q = (arr // 32 * 32).astype(np.uint8)
    cnt = Counter(map(tuple, q))
    return [("#%02X%02X%02X" % c, n) for c, n in cnt.most_common(k)]


def main():
    rows = []
    for fname in FILES:
        path = os.path.join(SRC, fname)
        im = Image.open(path).convert("RGB")
        w, h = im.size
        g = im.convert("L")
        objs = detect_objects(g)
        cols = dominant_colors(im)
        rows.append((fname, im, objs, cols))
        print("=" * 60)
        print(fname, im.size)
        print("  主色:", cols)
        print("  检测到物体(面积, x0,y0,x1,y1):")
        for o in objs[:12]:
            print("   ", o)

    # 生成标注图：每张图缩小到宽900，画检测框+编号
    fnt = None
    try:
        fnt = ImageFont.truetype(r"C:\Windows\Fonts\msyh.ttc", 16)
    except Exception:
        pass
    panels = []
    for fname, im, objs, cols in rows:
        w, h = im.size
        scale = 900 / w
        tw, th = int(w * scale), int(h * scale)
        panel = im.resize((tw, th))
        d = ImageDraw.Draw(panel)
        for idx, (area, x0, y0, x1, y1) in enumerate(objs[:12]):
            d.rectangle([x0 * scale, y0 * scale, x1 * scale, y1 * scale], outline=(255, 80, 80), width=3)
            d.text((x0 * scale + 2, y0 * scale - 20), str(idx + 1), fill=(255, 80, 80), font=fnt)
        panels.append(panel)
    total_h = sum(p.size[1] for p in panels) + 30
    sheet = Image.new("RGB", (max(p.size[0] for p in panels), total_h), (20, 20, 20))
    y = 0
    for panel in panels:
        sheet.paste(panel, (0, y))
        y += panel.size[1] + 30
    out_path = os.path.join(OUT, "source_annotated.png")
    sheet.save(out_path)
    print("\n标注图已存:", out_path)


if __name__ == "__main__":
    main()
