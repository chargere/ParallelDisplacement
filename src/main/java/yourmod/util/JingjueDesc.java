package yourmod.util;

/**
 * 精绝关键词必须单独成词，否则 ExtendedUI / BaseMod 会在「获得沙漏时」中间插入 #y，
 * 卡面上就会印出字面量 #y。
 */
public final class JingjueDesc {
    private static final String LINE_MARK = "\uE000";
    private static final String[] KEYWORDS = {
            "沙漏", "时滞", "虚数", "眼印", "溅射", "处决", "虚化", "业火", "血印", "时空乱流", "侵蚀度"
    };

    private JingjueDesc() {
    }

    public static String isolate(String raw) {
        if (raw == null || raw.isEmpty()) {
            return raw;
        }
        String s = raw.replace("#y处决线", "处决线");
        for (String k : KEYWORDS) {
            s = s.replace("#y" + k, k);
        }
        s = s.replace("处决线", LINE_MARK);
        for (String k : KEYWORDS) {
            s = pad(s, k);
        }
        s = pad(s.replace(LINE_MARK, "处决线"), "处决线");
        s = s.replaceAll(" {2,}", " ");
        return s;
    }

    private static String pad(String src, String keyword) {
        StringBuilder out = new StringBuilder(src.length() + 8);
        int i = 0;
        while (i < src.length()) {
            int idx = src.indexOf(keyword, i);
            if (idx < 0) {
                out.append(src, i, src.length());
                break;
            }
            out.append(src, i, idx);
            if (idx > 0 && !leftBound(src.charAt(idx - 1))) {
                out.append(' ');
            }
            out.append(keyword);
            int end = idx + keyword.length();
            if (end < src.length() && !rightBound(src.charAt(end))) {
                out.append(' ');
            }
            i = end;
        }
        return out.toString();
    }

    private static boolean leftBound(char c) {
        return c == ' ' || c == '\n' || c == '#' || c == '[' || c == '!';
    }

    private static boolean rightBound(char c) {
        return c == ' ' || c == '\n' || c == '。' || c == '，' || c == '：' || c == '；'
                || c == '、' || c == '%' || c == ']' || c == '!' || c == ',';
    }
}
