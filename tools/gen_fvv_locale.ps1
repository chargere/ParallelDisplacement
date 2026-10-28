$ErrorActionPreference = "Stop"
$root = "c:\Users\38220\Desktop\ProTemplate-master"
$utf8 = New-Object System.Text.UTF8Encoding $false

# --- Keyword ---
$kwPath = Join-Path $root "src\main\resources\hongwenResources\localization\eng\Keywordstrings.json"
$kw = [System.IO.File]::ReadAllText($kwPath, [System.Text.Encoding]::UTF8)
if ($kw -notmatch '"没用"') {
  $kw = $kw.TrimEnd()
  if ($kw.EndsWith("]")) {
    $kw = $kw.Substring(0, $kw.Length - 1).TrimEnd()
    if (-not $kw.EndsWith(",")) { $kw = $kw + "," }
    $kw += @"

  {
    "PROPER_NAME": "没用",
    "NAMES": [
      "没用"
    ],
    "DESCRIPTION": "角色层数。战斗结束清空。多数窝囊效果的来源，可消耗换取爆发。"
  }
]
"@
    [System.IO.File]::WriteAllText($kwPath, $kw, $utf8)
  }
}

# --- Powers ---
$powerEntries = @'
  "${ModID}:Useless": {
    "NAME": "没用",
    "DESCRIPTIONS": [
      "你有 #b",
      " 层 #y没用。战斗结束清空。"
    ]
  },
  "${ModID}:LateAwareness": {
    "NAME": "迟到觉悟",
    "DESCRIPTIONS": [
      "回合结束时，若本回合未造成伤害，获得 #b",
      " 层 #y没用。"
    ]
  },
  "${ModID}:AdmitCommittee": {
    "NAME": "承认委员",
    "DESCRIPTIONS": [
      "每当获得 #y没用，获得 #b1 点力量（每回合最多 #b",
      " 次）。"
    ]
  },
  "${ModID}:PassiveAggression": {
    "NAME": "被动攻击",
    "DESCRIPTIONS": [
      "弃牌时有 #b50% 概率获得 #b1 层 #y没用（每回合最多 #b",
      " 次）。"
    ]
  },
  "${ModID}:Afk": {
    "NAME": "挂机",
    "DESCRIPTIONS": [
      "连续 #b2 个回合每回合打牌不超过 #b",
      " 张：下一回合开始获得 #b2 能量与 #b3 层 #y没用。"
    ]
  },
  "${ModID}:ChatMute": {
    "NAME": "群聊屏蔽",
    "DESCRIPTIONS": [
      "本回合受到的攻击伤害减少 #b",
      " 点。"
    ]
  },
  "${ModID}:UselessEconomy": {
    "NAME": "废物经济",
    "DESCRIPTIONS": [
      "每层 #y没用 使打击类攻击伤害 +1（不消耗）。",
      "每层 #y没用 使所有攻击伤害 +1（不消耗）。"
    ]
  },
  "${ModID}:JustLikeThis": {
    "NAME": "我就这样",
    "DESCRIPTIONS": [
      "回合开始时获得 #b",
      " 层 #y没用。每回合第一张牌费用 -1。"
    ]
  },
  "${ModID}:BornWithIt": {
    "NAME": "胎里带来",
    "DESCRIPTIONS": [
      "无「胎没用了呀」时开战获得 #b5 层 #y没用。有该遗物时，每回合一次可将获得 #y没用 改为抽 #b1。"
    ]
  },
  "${ModID}:WasteCrown": {
    "NAME": "废物王冠",
    "DESCRIPTIONS": [
      "#y没用 结算时有效下限按至少 #b10。回合结束若 #y没用 ≥ #b5：抽 #b1 并获得 #b5 点格挡。"
    ]
  },
  "${ModID}:FamousEffect": {
    "NAME": "名人效应",
    "DESCRIPTIONS": [
      "打出名称含「废 / 没用 / 毛」的牌时抽 #b1（每回合限 #b",
      " 次）。"
    ]
  },
  "${ModID}:FetalFail": {
    "NAME": "胎教失败",
    "DESCRIPTIONS": [
      "每回合第一次获得 #y没用 时额外 +#b",
      "。每回合第一次失去生命时获得 #b1 层 #y没用。"
    ]
  },
  "${ModID}:UselessAscend": {
    "NAME": "毛用飞升",
    "DESCRIPTIONS": [
      "剩余 #b",
      " 回合：打牌费用 -1。回合结束清空 #y没用 并抽 #b2。"
    ]
  },
  "${ModID}:NoSkills": {
    "NAME": "已读不回",
    "DESCRIPTIONS": [
      "本回合不能再打出技能牌。",
      "本回合不能打出技能牌。"
    ]
  },
  "${ModID}:NextTurnEnergyLoss": {
    "NAME": "能量透支",
    "DESCRIPTIONS": [
      "下回合开始时失去 #b",
      " 点能量。"
    ]
  },
  "${ModID}:EnergyFraudDebt": {
    "NAME": "诈骗账单",
    "DESCRIPTIONS": [
      "下回合开始时失去 #b",
      " 点能量。"
    ]
  },
  "${ModID}:NextCardBoost": {
    "NAME": "今日停更",
    "DESCRIPTIONS": [
      "下一张牌的伤害与格挡 +50%。",
      "本回合牌的伤害与格挡 +50%。"
    ]
  },
  "${ModID}:BalanceHeal": {
    "NAME": "平衡调整",
    "DESCRIPTIONS": [
      "本场战斗中，你对自己造成的生命损失改为回复等量生命。"
    ]
  },
  "${ModID}:IAmEgg": {
    "NAME": "我是蛋蛋",
    "DESCRIPTIONS": [
      "每回合第一张费用 ≥ #b2 的牌退回 #b1 点能量，并获得 #b1 层 #y没用。"
    ]
  },
  "${ModID}:SkipNextTurn": {
    "NAME": "跳过回合",
    "DESCRIPTIONS": [
      "下回合开始时立刻结束你的回合。"
    ]
  },
  "${ModID}:SkipNextDraw": {
    "NAME": "跳过抽牌",
    "DESCRIPTIONS": [
      "跳过下一次抽牌。"
    ]
  },
  "${ModID}:NextTurnDrawUseless": {
    "NAME": "说明书收益",
    "DESCRIPTIONS": [
      "下回合开始时抽 #b",
      " 张牌，并获得等量 #y没用。"
    ]
  }
'@

$psPath = Join-Path $root "src\main\resources\hongwenResources\localization\eng\Powerstrings.json"
$ps = [System.IO.File]::ReadAllText($psPath, [System.Text.Encoding]::UTF8).TrimEnd()
if ($ps.EndsWith("}")) {
  # remove closing brace of last entry's parent - the file ends with }\n}
  $ps = $ps.Substring(0, $ps.Length - 1).TrimEnd()
  if (-not $ps.EndsWith(",")) { $ps = $ps + "," }
  $ps = $ps + "`n" + $powerEntries.TrimEnd() + "`n}`n"
  [System.IO.File]::WriteAllText($psPath, $ps, $utf8)
}

# --- Cards ---
$cards = @'
  "${ModID}:AdmitUseless": { "NAME": "承认很废", "DESCRIPTION": "获得 !M! 层 #y没用。 NL 抽 #b1 张牌。" },
  "${ModID}:UsefulPunch": { "NAME": "有用一拳", "DESCRIPTION": "造成 !D! 点伤害。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:ReallyHurts": { "NAME": "真的会疼", "DESCRIPTION": "造成 !D! 点伤害。 NL 失去 !M! 点生命。" },
  "${ModID}:ThinkLife": { "NAME": "思考人生", "DESCRIPTION": "抽 !M! 张牌，弃 #b1 张牌。 NL 若本回合未造成伤害，获得 !hongwen:m2! 层 #y没用。" },
  "${ModID}:UselessStrike": { "NAME": "毛用打击", "DESCRIPTION": "造成 !D! 点伤害。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:UselessDefend": { "NAME": "废物格挡", "DESCRIPTION": "获得 !B! 点格挡。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:RageStroke": { "NAME": "怒撸一下", "DESCRIPTION": "造成 !D! 点伤害。 NL 本回合每层 #y没用 使伤害 +1（不消耗）。" },
  "${ModID}:LieFlat": { "NAME": "躺平", "DESCRIPTION": "获得 !M! 层 #y没用。 NL 结束你的回合。", "UPGRADE_DESCRIPTION": "获得 !M! 层 #y没用。 NL 获得 !B! 点格挡。 NL 结束你的回合。" },
  "${ModID}:PickTrash": { "NAME": "捡垃圾", "DESCRIPTION": "弃最多 !M! 张牌。 NL 每弃 #b1 张：获得 #b1 层 #y没用 并抽 #b1。" },
  "${ModID}:VerbalPromise": { "NAME": "口头承诺", "DESCRIPTION": "获得 !B! 点格挡。 NL 下回合开始失去 #b1 点能量。" },
  "${ModID}:FakeDiligent": { "NAME": "假勤快", "DESCRIPTION": "造成 !D! 点伤害。 NL 获得 #b1 层 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "造成 !D! 点伤害。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:SelfCritique": { "NAME": "自我批判", "DESCRIPTION": "获得 !M! 层虚弱。 NL 获得 !hongwen:m2! 层 #y没用。" },
  "${ModID}:PassBuck": { "NAME": "推锅", "DESCRIPTION": "对一名敌人施加 !M! 层虚弱。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:NotBad": { "NAME": "还行吧", "DESCRIPTION": "造成 !D! 点伤害。 NL 若 #y没用 ≥ !M!，再造成 !D! 点伤害。" },
  "${ModID}:SlackDefend": { "NAME": "摸鱼防御", "DESCRIPTION": "获得 !B! 点格挡。 NL 将 #b1 张 *摸鱼* 洗入抽牌堆。" },
  "${ModID}:LateStrike": { "NAME": "迟到打击", "DESCRIPTION": "造成 !D! 点伤害。 NL 若为本回合第 #b3 张及以后打出的牌，改为造成 !hongwen:sd! 点伤害。" },
  "${ModID}:BuckHammer": { "NAME": "甩锅锤", "DESCRIPTION": "造成 !D! 点伤害。 NL 若手牌 ≥ #b5，获得 #b1 层 #y没用。" },
  "${ModID}:Insignificant": { "NAME": "微不足道", "DESCRIPTION": "保留。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:BatteryDead": { "NAME": "电量耗尽", "DESCRIPTION": "弃光手牌。 NL 每弃 #b1 张获得 #b1 层 #y没用（最多 !M!）。" },
  "${ModID}:SurfaceWork": { "NAME": "表面功夫", "DESCRIPTION": "造成等同于本回合已获格挡的伤害（上限 !M!）。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:InnerDrain": { "NAME": "内耗", "DESCRIPTION": "失去 #b1 点能量。 NL 抽 !M! 张牌。 NL 获得 !hongwen:m2! 层 #y没用。" },
  "${ModID}:AlmostOk": { "NAME": "差不多了", "DESCRIPTION": "造成 !D! 点伤害 #b2 次。 NL 第二次仅当 #y没用 ≥ #b2。" },
  "${ModID}:ReadNoReply": { "NAME": "已读不回", "DESCRIPTION": "获得 !B! 点格挡。 NL 本回合不能再打出技能。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:ReplyRead": { "NAME": "已回已读", "DESCRIPTION": "若本回合打过技能：造成 !hongwen:sd! 点伤害。 NL 否则造成 !D! 点伤害并获得 !M! 层 #y没用。" },
  "${ModID}:KeyboardWarrior": { "NAME": "键盘侠", "DESCRIPTION": "对随机敌人造成 !D! 点伤害。 NL 获得 #b1 层 #y没用。 NL 可再支付 #b1 层 #y没用 重复一次。" },
  "${ModID}:SlackAddiction": { "NAME": "摸鱼成癖", "DESCRIPTION": "将 #b2 张 *摸鱼* 洗入抽牌堆。 NL 抽 !M! 张牌。" },
  "${ModID}:OvertimeStrike": { "NAME": "加班打击", "DESCRIPTION": "造成 !D! 点伤害。 NL 若手牌 ≤ #b2，再获得 #b1 层 #y没用。" },
  "${ModID}:BuckProduct": { "NAME": "甩锅产品", "DESCRIPTION": "对一名敌人施加 !M! 层脆弱。 NL 获得 !hongwen:m2! 层 #y没用。" },
  "${ModID}:CopyPaste": { "NAME": "复制粘贴", "DESCRIPTION": "复制手牌中一张非稀有牌。 NL 复制品费用 +1 且消耗。 NL 获得 #b1 层 #y没用。", "UPGRADE_DESCRIPTION": "复制手牌中一张非稀有牌。 NL 复制品消耗。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:NonsenseTalk": { "NAME": "废话连篇", "DESCRIPTION": "抽 #b3 张牌，弃 !M! 张牌。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:WasteRecycle": { "NAME": "废物利用", "DESCRIPTION": "花光 #y没用：每层获得 !B! 点格挡并抽 #b1（最多抽 !M!）。" },
  "${ModID}:BreakDefense": { "NAME": "破防输出", "DESCRIPTION": "造成 !D! 点伤害。 NL 花光 #y没用，每层伤害 +!M!。" },
  "${ModID}:PlayDead": { "NAME": "装死", "DESCRIPTION": "获得 #b1 层无实体。 NL 获得 !M! 层 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "获得 #b1 层无实体。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:LateAwareness": { "NAME": "迟到觉悟", "DESCRIPTION": "回合结束时，若本回合未造成伤害，获得 !M! 层 #y没用。" },
  "${ModID}:PeerReview": { "NAME": "同行评价", "DESCRIPTION": "保留。 NL 选择：获得 !M! 层 #y没用；或花 !hongwen:m2! 层 #y没用 抽牌。" },
  "${ModID}:JustRun": { "NAME": "直接跑路", "DESCRIPTION": "消耗。 NL 失去全部 #y没用，每层对随机敌人造成 !D! 点伤害。" },
  "${ModID}:FakeHustle": { "NAME": "无效内卷", "DESCRIPTION": "造成 !M! × 本回合已打牌数（含此牌）点伤害。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:LeaveNote": { "NAME": "请假条", "DESCRIPTION": "获得 !B! 点格挡。 NL 将 #b1 张 *摸鱼* 洗入抽牌堆。" },
  "${ModID}:ArgueLine": { "NAME": "对线", "DESCRIPTION": "造成 !D! 点伤害。 NL 若敌人意图攻击：获得 !M! 层 #y没用；否则获得 !B! 点格挡。" },
  "${ModID}:AdmitCommittee": { "NAME": "承认委员", "DESCRIPTION": "每当获得 #y没用，获得 #b1 点力量（每回合最多 !M! 次）。" },
  "${ModID}:SlackCombo": { "NAME": "摆烂连招", "DESCRIPTION": "造成 !D! 点伤害。 NL 若上一张是技能，再造成 !D! 点伤害并获得 #b1 层 #y没用。" },
  "${ModID}:ReviewMeeting": { "NAME": "复盘会议", "DESCRIPTION": "从弃牌堆选 #b1 张入手。 NL 攻击则 +#b1 #y没用；技能则获得 !B! 点格挡。", "UPGRADE_DESCRIPTION": "从弃牌堆选 #b1 张入手。 NL 攻击则 +#b1 #y没用；技能则获得 !B! 点格挡。 NL 再抽 #b1。" },
  "${ModID}:EnergyFraud": { "NAME": "能量诈骗", "DESCRIPTION": "获得 #b2 点能量。 NL 下回合开始失去 #b2 点能量。 NL 获得 #b1 层 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "获得 #b2 点能量。 NL 下回合开始失去 #b2 点能量。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:ControlTest": { "NAME": "对照实验", "DESCRIPTION": "对所有敌人造成 !D! 点伤害。 NL 每层 #y没用 对生命最低的敌人再造成 !M! 点伤害。" },
  "${ModID}:PassiveAggression": { "NAME": "被动攻击", "DESCRIPTION": "弃牌时 #b50% 获得 #b1 层 #y没用（每回合最多 !M! 次）。" },
  "${ModID}:UnsubscribeLife": { "NAME": "退订人生", "DESCRIPTION": "消耗。 NL 移除手中所有状态与诅咒；每张获得 !M! 层 #y没用 并抽 #b1。" },
  "${ModID}:OpenBox": { "NAME": "开盒", "DESCRIPTION": "随机：+#b3 #y没用 / 抽 #b2 / 自损 #b3 再 +#b5 #y没用 / 洗入 #b1 *说明书*。", "UPGRADE_DESCRIPTION": "随机：+#b3 #y没用 / 抽 #b2 / 自损 #b3 再 +#b5 #y没用。" },
  "${ModID}:AfkHang": { "NAME": "挂机", "DESCRIPTION": "连续 #b2 回合每回合打牌 ≤ !M!：第三回合开始获得 #b2 能量与 #b3 层 #y没用。" },
  "${ModID}:PaidPoop": { "NAME": "带薪拉屎", "DESCRIPTION": "获得 #b1 点能量。 NL 获得 #b2 层 #y没用。 NL 抽 #b1。 NL 消耗。", "UPGRADE_DESCRIPTION": "获得 #b1 点能量。 NL 获得 #b2 层 #y没用。 NL 抽 #b1。" },
  "${ModID}:ChatMute": { "NAME": "群聊屏蔽", "DESCRIPTION": "本回合受到的攻击伤害 -!M!。 NL 获得 #b2 层 #y没用。" },
  "${ModID}:ExamStrike": { "NAME": "考核打击", "DESCRIPTION": "造成等同于本场已获得 #y没用 总次数的伤害（上限 !M!）。" },
  "${ModID}:NumberScapegoat": { "NAME": "数值背锅", "DESCRIPTION": "一名敌人力量 -!M!（本场）。 NL 获得 #b2 层 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "一名敌人力量 -!M!（本场）。 NL 获得 #b2 层 #y没用。" },
  "${ModID}:WasteEconomy": { "NAME": "废物经济", "DESCRIPTION": "每层 #y没用 使打击类伤害 +1（不消耗）。", "UPGRADE_DESCRIPTION": "每层 #y没用 使所有攻击伤害 +1（不消耗）。" },
  "${ModID}:UltimateUseless": { "NAME": "终极没用", "DESCRIPTION": "造成（#y没用 × #y没用）点伤害（上限 #b99），清空 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "造成（#y没用 × #y没用）点伤害（上限 #b99），清空 #y没用。" },
  "${ModID}:JustLikeThis": { "NAME": "我就这样", "DESCRIPTION": "回合开始获得 !M! 层 #y没用。 NL 每回合第一张牌费用 -1。" },
  "${ModID}:SocialDeath": { "NAME": "社会死亡", "DESCRIPTION": "施加 !M! 层易伤与虚弱。 NL 获得 !hongwen:m2! 层 #y没用。 NL 消耗。" },
  "${ModID}:TheoryCorrect": { "NAME": "理论正确", "DESCRIPTION": "固有。消耗。 NL 抽牌堆与弃牌堆无攻击：获得 !M! 层 #y没用；否则获得 #b2 层并抽 #b1。" },
  "${ModID}:FillerStrike": { "NAME": "凑数打击", "DESCRIPTION": "消耗。 NL 将 !M! 张 #b0 费「打击」加入手牌（回合结束消耗）。 NL 获得 #b1 层 #y没用。" },
  "${ModID}:BornWithIt": { "NAME": "胎里带来", "DESCRIPTION": "虚无。 NL 无「胎没用了呀」则开战 +#b5 #y没用；有则每回合一次可将获得 #y没用 改为抽 #b1。", "UPGRADE_DESCRIPTION": "无「胎没用了呀」则开战 +#b5 #y没用；有则每回合一次可将获得 #y没用 改为抽 #b1。" },
  "${ModID}:RageFinale": { "NAME": "怒撸终章", "DESCRIPTION": "造成 !D! 点伤害。 NL 本场每获得过 #b1 次 #y没用，伤害 +1。 NL 消耗。", "UPGRADE_DESCRIPTION": "造成 !D! 点伤害。 NL 本场每获得过 #b1 次 #y没用，伤害 +1。" },
  "${ModID}:GroupSlack": { "NAME": "集体摆烂", "DESCRIPTION": "对所有敌人施加 !M! 层虚弱与易伤。 NL 获得 !hongwen:m2! 层 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "对所有敌人施加 !M! 层虚弱与易伤。 NL 获得 !hongwen:m2! 层 #y没用。" },
  "${ModID}:WasteCrown": { "NAME": "废物王冠", "DESCRIPTION": "#y没用 有效下限按至少 #b10。 NL 回合结束若 #y没用 ≥ #b5：抽 #b1 并获得 #b5 点格挡。" },
  "${ModID}:OneClickBreak": { "NAME": "一键破防", "DESCRIPTION": "消耗至少 #b1 层 #y没用：对所有敌人造成 !D! × 层数 点伤害。" },
  "${ModID}:ExistCrisis": { "NAME": "存在危机", "DESCRIPTION": "弃光手牌再抽等量。 NL 获得等于弃牌数的 #y没用。 NL 消耗。", "UPGRADE_DESCRIPTION": "弃光手牌再抽等量。 NL 获得等于弃牌数的 #y没用。" },
  "${ModID}:FamousEffect": { "NAME": "名人效应", "DESCRIPTION": "打出名称含「废 / 没用 / 毛」的牌时抽 #b1（每回合限 !M!）。" },
  "${ModID}:SelfUltimatum": { "NAME": "自我通牒", "DESCRIPTION": "若 #y没用 ≥ #b8：造成 !D! 点伤害并清空。 NL 否则获得 !M! 层 #y没用。 NL 消耗。" },
  "${ModID}:NoWords": { "NAME": "没话说", "DESCRIPTION": "消耗。 NL 获得 !M! 层 #y没用。 NL 抽 #b1。" },
  "${ModID}:CasualHit": { "NAME": "顺手一击", "DESCRIPTION": "造成 !D! 点伤害。 NL 若本回合打过 #b0 费牌，再造成 !D! 点伤害并获得 #b2 层 #y没用。" },
  "${ModID}:FetalFail": { "NAME": "胎教失败", "DESCRIPTION": "每回合第一次获得 #y没用 时额外 +!M!。 NL 每回合第一次失血时获得 #b1 层 #y没用。" },
  "${ModID}:UselessFly": { "NAME": "毛用飞升", "DESCRIPTION": "消耗。 NL 若 #y没用 ≥ !M!：下 #b2 回合费用 -1，回合结束清空并抽 #b2；否则 #y没用 翻倍（上限 !hongwen:m2!）。" },
  "${ModID}:ManualSet": { "NAME": "说明书集", "DESCRIPTION": "将 !M! 张 *说明书* 洗入抽牌堆。 NL 每张使下回合开始抽 #b1 并 +#b1 #y没用。" },
  "${ModID}:PlacentaAscend": { "NAME": "胎盘升天", "DESCRIPTION": "消耗全部 #y没用 与全部摸鱼，每层/张对全体造成 !D! 点伤害，然后跳过下回合。 NL 消耗。", "UPGRADE_DESCRIPTION": "消耗全部 #y没用 与全部摸鱼，每层/张对全体造成 !D! 点伤害。 NL 消耗。" },
  "${ModID}:BattleUseless": { "NAME": "开战没用", "DESCRIPTION": "固有。保留。消耗。 NL 获得 !M! 层 #y没用。" },
  "${ModID}:TodayOffline": { "NAME": "今日停更", "DESCRIPTION": "下一张牌的数值 +#b50%。", "UPGRADE_DESCRIPTION": "本回合牌的数值 +#b50%。" },
  "${ModID}:BalancePatch": { "NAME": "平衡调整", "DESCRIPTION": "本场自损生命改为回复等量。" },
  "${ModID}:LeaveReview": { "NAME": "打个好评", "DESCRIPTION": "获得 !B! 点格挡。 NL 将 #b1 张 *已读回执* 洗入抽牌堆。" },
  "${ModID}:ReportReward": { "NAME": "举报有奖", "DESCRIPTION": "造成 !D! 点伤害。 NL 若目标有虚弱或易伤，获得 #b3 层 #y没用。" },
  "${ModID}:ServerDown": { "NAME": "服务崩了", "DESCRIPTION": "所有敌人本回合力量 -!M!。 NL 跳过下次抽牌。 NL 获得 #b4 层 #y没用。" },
  "${ModID}:IAmEgg": { "NAME": "我是蛋蛋", "DESCRIPTION": "虚无。 NL 每回合第一张费用 ≥ #b2 的牌退回 #b1 能量并获得 #b1 层 #y没用。", "UPGRADE_DESCRIPTION": "每回合第一张费用 ≥ #b2 的牌退回 #b1 能量并获得 #b1 层 #y没用。" },
  "${ModID}:RageCert": { "NAME": "怒撸认证", "DESCRIPTION": "消耗。 NL 发现 #b1 张本角色稀有牌，以 #b0 费入手（消耗）。 NL 获得 #b2 层 #y没用。", "UPGRADE_DESCRIPTION": "发现 #b1 张本角色稀有牌，以 #b0 费入手（消耗）。 NL 获得 #b2 层 #y没用。" },
  "${ModID}:SlackOff": { "NAME": "摸鱼", "DESCRIPTION": "抽到时获得 #b1 层 #y没用，消耗。" },
  "${ModID}:SlackOffHard": { "NAME": "更摸", "DESCRIPTION": "抽到时获得 #b2 层 #y没用，消耗。" },
  "${ModID}:ManualBook": { "NAME": "说明书", "DESCRIPTION": "抽到时失去 #b1 点生命，获得 #b1 层 #y没用，消耗。" },
  "${ModID}:OvertimeMail": { "NAME": "加班邮件", "DESCRIPTION": "抽到时弃 #b1 张牌，获得 #b1 层 #y没用，消耗。" },
  "${ModID}:ReadReceipt": { "NAME": "已读回执", "DESCRIPTION": "保留。 NL 抽 #b1 张牌。 NL 获得 #b1 层 #y没用。 NL 消耗。" },
  "${ModID}:PlacentaShard": { "NAME": "胎盘碎片", "DESCRIPTION": "虚无。 NL 造成 !D! 点伤害。 NL 获得 #b1 层 #y没用。 NL 消耗。" }
'@

$csPath = Join-Path $root "src\main\resources\hongwenResources\localization\eng\Cardstrings.json"
$cs = [System.IO.File]::ReadAllText($csPath, [System.Text.Encoding]::UTF8).TrimEnd()
# Update existing FvvStrike/Defend descriptions if needed - already exist
if ($cs -notmatch 'AdmitUseless') {
  $cs = $cs.Substring(0, $cs.Length - 1).TrimEnd()
  if (-not $cs.EndsWith(",")) { $cs = $cs + "," }
  $cs = $cs + "`n" + $cards.TrimEnd() + "`n}`n"
  [System.IO.File]::WriteAllText($csPath, $cs, $utf8)
  Write-Host "Cardstrings appended"
} else {
  Write-Host "Cardstrings already has AdmitUseless"
}

Write-Host "Localization done"
