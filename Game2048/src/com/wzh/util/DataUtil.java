package com.wzh.util;

import com.wzh.classes.NumberBlock;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DataUtil {
    private DataUtil() {
    }

    public static final Point INVALID_POINT = new Point(-1, -1);

    public static Point GetRandomPos(NumberBlock[][] blocks) {
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (blocks[i][j] == null) list.add(new Point(i, j));
            }
        }
        // 游戏失败
        if (list.isEmpty()) return INVALID_POINT;

        Random rand = new Random();
        int i = rand.nextInt(list.size());
        return list.get(i);
    }
}
