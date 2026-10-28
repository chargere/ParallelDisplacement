# 项目交接（平行异位 / 鸿文君主）

**路径：** `c:\Users\38220\Desktop\ProTemplate-master`  
**模组显示名：** 平行异位 | Parallel Displacement  
**作者：** 乌鲁鲁爱堵桥  
**版本：** 1.10（2026.8.26）  
**产物 jar：** `ParallelDisplacement.jar`（`mvn package` 会拷到 Steam `mods/`）

**文档：**
- 设定长文 / 工坊简介：`平行异位-简介.txt`
- **卡牌+遗物总览（1.03）：** `鸿文君主-卡牌遗物总览.txt`（8 遗物 / 96 卡）
- 本交接：`HANDOFF-鸿文君主.md`
- **第二人设定（未实装）：** `霜星旅者-设定.txt`（多米尼克·梅隆蒂科 / 星辰×冰雪；机制待定）
- 解锁设计原文：桌面 `新建 文本文档 (4).txt` / 工程内相关设计稿

---

## 定位

四人组模组（现实原型 → 架空支流）。当前已实装第一人：**鸿文君主 · 凯文·齐亚腾**（焰 / 雷双相）。后三人未开工。

技术 ModID 仍为 **`hongwen`**，资源目录 **`hongwenResources/`**（改显示名时未动 ModID，避免炸路径与旧档）。

---

## 1.03 本轮变更摘要（务必读）

### 无色整活卡（新包 `cards/colorless/`）
| 卡 | 稀有 | 要点 |
|----|------|------|
| **磁吸炸弹** | 罕见 1 费 | 指定敌人，3 回合后造成伤害（`MagneticBombPower`，`atEndOfRound` 倒计时） |
| **虎蹲炮** | 罕见 2→1 费 | 指定敌人，3 回合后 `StunMonsterAction(amount2)` 晕 2 回合；**不要**再走 Pending 中转 |
| **动力推进** | 稀有 1 费 | 无实体；下回合虚弱+易伤（`NextTurnWeakPower`）；升级去消耗 |

晕眩实现：StSLib `StunMonsterPower`（意图变晕眩 + 跳过 `takeTurn`），**不是**观者腾跃。

### Bug 修复
| 问题 | 处理 |
|------|------|
| **雷纹滤网** 预见弃牌不上静电 | `DiscardHookPatch` 补挂 `ScryAction.moveToDiscardPile`（预见不走 `DiscardSpecificCardAction`） |
| **虎蹲炮** 不触发 | 去掉 `TigerCrouchPendingPower`；倒计时归零直接 `StunMonsterAction` |
| **天火！** 一口气结算 | 改为 X 段逐次伤害 + 段间 `WaitAction` |
| **时序残响** 升级无效果 | 未升级消耗；升级去消耗 |
| **五组解锁卡不上锁** | 根因：`AutoAdd.setDefaultSeen(true)` → `UnlockTracker.unlockCard` 洗锁。改为 `setDefaultSeen(false)` + `HongwenUnlocks.enforceLocks()`。**遗物锁本来就是好的**（只 markSeen） |

### 解锁档位（`HongwenUnlocks`）
- 0 卡：祭礼 / 雷铸残片 / 雷烬双咏  
- 1 物：熔炉之息 / 雷纹回响 / 未送出的花  
- 2 卡：未竟的工图 / 牢郑跑路了 / 万军之敕  
- 3 物：荒疫残烬 / 雨天的约定 / 永恒风暴的锚点  
- 4 卡：沉默的守望 / 旧日之名 / 天火·不灭  

进度：角色通关得分条。`enforceLocks()` 按 `UnlockTracker.getUnlockLevel` 强制锁/解锁，并纠正被 `unlockCard` 写坏的 pref。

### 已知未修 / 次要（文档 4 审计残留）
- **雷纹回响**：`ScorchingAssaultPower` 普通传导循环里 `onConduct()` 按副目标多次调用 → 可能多次回血、传导计数偏高（连带万军之敕费 0）
- **荒疫残烬**：开局怜悯の火挂烬火会吃掉「首次双倍」；`HongwenHooks.onBattleStart` 只挂在初始遗物上
- **天火·不灭**：生命已 &lt;10 时致命伤不会拉回 10；「每场限 1」按能力实例
- **未竟的工图**：选牌 UI 未过滤状态/诅咒，选错可能白耗
- **万军之敕**：文档无升级，代码有（印记/增伤升级）
- 卡面多数缺图 → `CardArtRoller` 随机

---

## 核心机制（长期）

- **烬火**（敌）+ **燎原猛攻**（玩家，计数 1–6；不够层不触发、不推进）
- **静电印记**传导（单体 NORMAL；源不扣、副目标扣 1；默认 THORNS）
- 枢纽：`powers/ScorchingAssaultPower.java`、`util/HongwenHooks.java`
- 击晕：StSLib；万象静止 = `StunMonsterPower(1)` 本回合

### 遗物一览（8）
怜悯の火（初始）、无人怜悯（Boss 替换）、熔炉之息、雷纹回响、未送出的花、荒疫残烬、雨天的约定、永恒风暴的锚点 — 详表见总览 txt。

---

## 关键路径

| 用途 | 路径 |
|------|------|
| 入口 | `ModFile.java`、`CharacterFile.java` |
| 解锁 | `unlock/HongwenUnlocks.java`（`register` + `enforceLocks`） |
| 弃牌钩子 | `patches/DiscardHookPatch.java`（DiscardSpecific + Scry） |
| 无色卡 | `cards/colorless/` |
| 核心能力 | `powers/ScorchingAssaultPower.java` |
| 本地化 | `hongwenResources/localization/eng/`（中文写在 eng） |
| 内容总览 | `鸿文君主-卡牌遗物总览.txt` |

包结构卡牌：`basic`（Strike 等）/ `common` / `uncommon` / `rare` / `colorless` / `derived`。

---

## 构建与运行

```text
mvn package
```

ModTheSpire：BaseMod、StSLib、本模组。Steam 路径见 `pom.xml` → `Steam.path`。

---

## 可接着做

1. 修传导 `onConduct` 每事件一次；荒疫残烬开战触发；天火不灭 HP&lt;10 / 全场限一  
2. 未竟的工图选牌过滤；实机回归滤网 / 虎蹲炮 / 解锁档  
3. 美术：选人、卡面、遗物图标  
4. 技术债：`yourmod` 包名、`THE_TODO` 枚举；可选正式 `zhs`  
5. 后三个角色（同工程加 Character）  
6. 平衡

---

## 新对话怎么开

`@HANDOFF-鸿文君主.md` + 需要时 `@鸿文君主-卡牌遗物总览.txt` / `@平行异位-简介.txt`，直接说下一步即可。
