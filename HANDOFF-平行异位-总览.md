# 项目交接 · 平行异位（Parallel Displacement）

**路径：** `C:\Users\38220\Desktop\ProTemplate-master`  
**作者：** 乌鲁鲁爱堵桥  
**ModID：** `hongwen`（**不要改**，资源目录 `hongwenResources/`、存档字段、路径全绑死）  
**产物：** `target/ParallelDisplacement.jar`（jar 名与显示名无关，别被误导）  
**上次可运行构建：** 2026-10-23（资源层改动后手工打补丁，见 §3）

> 旧文档 `HANDOFF-鸿文君主.md` 只讲第一个角色，**本文件是全局交接**，优先看这个。

---

## 1. 这项目是什么

杀戮尖塔（ModTheSpire）多角色模组，基于 **ProTemplate**。七人阵容，各自机制独立、互不共享卡池。设定是"现实里认识的人，在另一条时间线的塔里活成了别的版本"（详见 `平行异位-简介.txt`）。

| 指标         | 数量                 |
| ---------- | ------------------ |
| Java 文件    | 728                |
| 卡牌         | **473**            |
| 能力 (power) | **117**            |
| 遗物         | **36**             |
| 角色         | **7**（5 实装 + 2 占位） |

---

## 2. 七个角色的状态

| 角色     | 类                         | 机制                   | 卡牌   | 状态     |
| ------ | ------------------------- | -------------------- | ---- | ------ |
| 鸿文君主   | `CharacterFile`           | 焰/雷双相、烬火、霜偿、星痕       | 通用池  | ✅ 实装   |
| 多米尼克   | `DominicCharacter`        | 霜/星系                 | 87   | ✅ 实装   |
| 达穆候    | `DamuhouCharacter`        | 枪械、连击、破绽、火控（半自动/全自动） | 106  | ✅ 实装   |
| 精绝王者   | `JingjueCharacter`        | 盗墓、虚数、沙漏、时滞、处决       | 97   | ✅ 实装   |
| fvv 蛋蛋 | `FvvEggCharacter`         | 整活线：攒"没用"换爆发         | 89   | ✅ 实装   |
| 风      | `ComingSoonWindCharacter` | 未定                   | 2 占位 | 🚧 未开工 |
| 时      | `ComingSoonTimeCharacter` | 未定                   | 2 占位 | 🚧 未开工 |

**未定角色的连带项，别乱动：**

- `LightFeatherGaleBlade`（光羽岚刃）、`TimeGlassAlbum`（时璃相簿）—— 这两个遗物描述写着"敬请期待"，属于风/时，遗物图也故意没做。

---

## 3. 环境与构建（⚠️ 这段最重要）

### Maven 坏了

`mvn` 命令报 `找不到或无法加载主类 org.codehaus.plexus.classworlds.launcher.Launcher`（安装损坏，与项目无关）。**现在打包靠 IDEA 的 Maven 面板**，或者用下面的手工方式。

### 只改了资源？用 jar 打补丁（2026-10-23 全部美术改动都这么做的）

```bash
cd src/main/resources
"C:/Program Files/Java/jdk1.8.0_491/bin/jar.exe" uf \
  "C:/Users/38220/Desktop/ProTemplate-master/target/ParallelDisplacement.jar" \
  hongwenResources/images/powers
```

- jar 就是 zip，**路径必须和包内一致**：`hongwenResources/images/...`
- 改了 **Java 代码**就必须重新编译，jar uf 只够换资源
- 每次改完顺手 `unzip -l target/*.jar | grep <路径>` 验一下

### 环境速查

- JDK：`C:\Program Files\Java\jdk1.8.0_491`（项目 source/target = 1.8）
- Python venv：`C:\Users\38220\.workbuddy\binaries\python\envs\default`（已装 Pillow、resvg-py）
- 图标库：`tmp_decompile/game-icons/`（4239 个 SVG，**别删**，补图标要用）
- 遗物草图：`tmp_relic_drafts/raw_*.png`（1024 原始图，**别删**，可重跑后处理）

---

## 4. 美术流水线（已建成，直接复用）

### 4.1 Power 图标 → `tools/gen_power_icons.py`

```
python tools/gen_power_icons.py            # 全量
python tools/gen_power_icons.py Breach     # 单张重做
```

- 源：game-icons.net（**CC BY 3.0，发布时必须在模组简介署名**）
- 映射表 `MAPPING` 在脚本里，改一行就换图标
- 颜色按 Java 源码的 `PowerType` 自动分：**buff 金 `#E8B54D` / debuff 紫 `#A66BD4`**
- **当前 117/117 全覆盖，零遗漏**
- 坑：PowerID 与类名不同名（`Breach` → `BreachPower.java`），脚本用 `ID_INDEX` 全量扫描解决

### 4.2 遗物图 → `tools/gen_relic_icons.py`

```
# 1) 把 AI 生成的 1024 图放进 tmp_relic_drafts/raw_{遗物ID}.png
python tools/gen_relic_icons.py            # 自动：去水印 → 裁方 → 128px → 生成 outline
```

- **当前 34/36 完成**（跳过光羽岚刃、时璃相簿）
- AI prompt 模板（风格统一靠这个，别改骨架词）：
  ```
  Slay the Spire style game relic icon, square composition: a single {物件描述},
  centered, thick dark outline, painterly roguelike deckbuilder item art,
  isolated on dark muted background, no text, no watermark, game asset
  ```
- **AI 生图并行调用会撞文件名**（同一秒完成 → 互相覆盖，实测丢过 2 张）。**一次只并行 2 张**，生成后立刻按时间戳归位并核对数量。

---

## 5. 已完成的（2026-10-23 这轮）

| 事项         | 结果                                                                                          |
| ---------- | ------------------------------------------------------------------------------------------- |
| 鸿文君主选人按钮偏大 | ✅ 修好。根因：键帽画满画布 100%，其余角色只占 86% → 按占位 bbox 等比缩放对齐。备份 `backup_charSelect/charButton.orig.png` |
| Power 图标   | ✅ 117/117，从 1 张占位图到全套                                                                       |
| 遗物图        | ✅ 34 组（主图 + outline），含精绝王者整包 15 个                                                           |
| 卡图         | ❌ 未动（473 张全靠 `CardArtRoller` 程序化着色兜底）                                                       |

---

## 6. 待办（按优先级）

**P0 — 上线风险**

1. **建 git 仓库**：728 个文件裸奔，改崩没法回滚。今天就能做（`git init` + 首次提交）
2. **修 mvn** 或确立"IDEA 打包"为唯一流程，写进文档
3. **发布前署名**：game-icons 图标 CC BY 3.0，简介里加一句来源声明

**P1 — 美术**  
4\. 卡图 473 张（AI 批量；先定风格模板再跑，别东一张西一张）  
5\. 风/时两个角色定稿后补遗物图（脚本现成）  
6\. 五个角色的选人按钮还是同一张占位图（19713 字节那张），需要各自定制

**P2 — 设计**  
7\. **达穆候连击数不可心算**（详见 §7）  
8\. 只有 `eng` 一份本地化（内容是中文，靠 `SupportedLanguages` 回退）。要出英文版得拆 `zhs`/`eng` 两套

**P3 — 卫生**  
9\. 模板残留：包名 `yourmod`、枚举 `TODO_COLOR` / `THE_TODO` / `makeID("ModdedCharacter")`  
10\. 死资源：`charSelect/charButton.jpg`、整个 `todomodResources/`（都没被引用）

---

## 7. 已知的坑（别踩）

| 坑               | 说明                                                                                                                                                                                                                      |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **AutoAdd 洗解锁** | `setDefaultSeen(true)` 会调 `UnlockTracker.unlockCard`，把五组锁全洗掉。当前是 `false` + `enforceLocks()`，**别改回去**                                                                                                                    |
| **连击来源过多**      | `AbstractDamuhouCard.resolveHits()` 叠了 6+ 个加成源（printedCombo / consumeComboBonus / firstAttackCombo / thisTurnFirstAttackCombo / nthCardIndex / focusExtra / SpareMagPower），外加"伤害≥8 视作 0 连击"硬编码。玩家算不出打几下。要动就**砍来源**，别调数值 |
| **火控模式存档**      | `ModFile` 用 `CustomSavable` 持久化 `fullAuto`，`receiveStartGame` 里判断 `!loadingSave` 才重置。改这块注意别让读档被重置                                                                                                                       |
| **语言回退**        | 中文文本写在 `eng` 下，靠 `SupportedLanguages = {ENG}` + `getLangString()` 回退。中英文客户端都显示中文                                                                                                                                        |
| **选人按钮**        | 七个按钮都是 220×220，视觉大小取决于内容占画布比例（标准约 86%）                                                                                                                                                                                  |

---

## 8. 换号后第一步

1. 读本文件 + `平行异位-简介.txt`（搞清楚这是啥）
2. `git init` + 首次提交（P0，别拖）
3. <span style="background-color:rgb(243, 245, 247)">一个</span>**发布前必须做的事**

<span style="background-color:rgb(243, 245, 247)">：game-icons 是 CC BY 3.0 授权，允许商用但要求署名——发布模组时在简介里加一句“图标来自 game-icons.net (CC BY 3.0)”即可，别忘了</span>

1. 从 §6 挑一件开工。想继续做美术→ §4 脚本现成，跑一遍就知道现状
2. 验证方式：改资源 → §3 的 jar 补丁 → 进游戏看

**关键文件速查：**

```
ModFile.java                     主入口（注册/本地化/存档/音频）
cards/AbstractEasyCard.java      卡牌基类
cards/damuhou/AbstractDamuhouCard.java  火控抽象（最漂亮的一处设计）
powers/AbstractEasyPower.java    power 基类（读 images/powers/{ID}32.png + 84.png）
relics/AbstractEasyRelic.java    遗物基类
util/CardArtRoller.java          缺卡图时的程序化兜底
tools/gen_power_icons.py         power 图标流水线
tools/gen_relic_icons.py         遗物图流水线
```
