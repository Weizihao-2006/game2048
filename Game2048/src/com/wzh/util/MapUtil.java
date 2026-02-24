package com.wzh.util;

import java.awt.*;

public class MapUtil {
    // 屏幕大小设定
    public static final int GAME_WIDTH = 688;
    public static final int GAME_HEIGHT = 800;

    // 边框大小设定
    public static final int BORDER_STARTX = 40;
    public static final int BORDER_STARTY = 150;
    public static final int BORDER_SIZE = 580;
    public static final int ARC = 15; // 圆角弧度

    public static final int GAP_SIZE = 15;
    public static final int BLOCK_SIZE = (BORDER_SIZE - 5 * GAP_SIZE) / 4; // 方块大小

    public static final int BLOCK_STARTX = BORDER_STARTX + GAP_SIZE;
    public static final int BLOCK_STARTY = BORDER_STARTY + GAP_SIZE;

    private MapUtil() {
    }

    // 把逻辑坐标转换成真实坐标
    public static Point ChangeLogicPosToActPos(int x, int y) {
        int retx = BLOCK_STARTX + x * (GAP_SIZE + BLOCK_SIZE);
        int rety = BLOCK_STARTY + y * (GAP_SIZE + BLOCK_SIZE);
        return new Point(retx, rety);
    }
}
