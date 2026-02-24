package com.wzh.classes;

import com.wzh.util.MapUtil;

import javax.swing.*;
import java.awt.*;

public class NumberBlock extends JLabel {
    public int num;
    public Point point = new Point(-1, -1);

    public NumberBlock() {
        this.setFont(new Font("Arial", Font.BOLD, 40));
        this.setHorizontalAlignment(SwingConstants.CENTER); // 文字水平居中
        this.setVerticalAlignment(SwingConstants.CENTER); // 文字垂直居中
        this.setOpaque(false);
    }

    public NumberBlock(int num) {
        this();
        this.num = num;
        this.setText(String.valueOf(num));
    }

    private Color getColor(int value) {
        return switch (value) {
            case 2 -> new Color(238, 228, 218); // 极浅米色
            case 4 -> new Color(237, 224, 200); // 浅米色
            case 8 -> new Color(242, 177, 121); // 浅橙色
            case 16 -> new Color(245, 149, 99);  // 橙色
            case 32 -> new Color(246, 124, 95);  // 深橙色
            case 64 -> new Color(246, 94, 59);   // 红色
            case 128 -> new Color(237, 207, 114); // 浅黄色
            case 256 -> new Color(237, 204, 97);  // 黄色
            case 512 -> new Color(237, 200, 80);  // 金黄色
            case 1024 -> new Color(237, 197, 63);  // 亮金色
            case 2048 -> new Color(237, 194, 46);  // 纯金色
            case 4096 -> new Color(165, 224, 132); // 浅绿色
            case 8192 -> new Color(113, 180, 127); // 深绿色
            default -> new Color(60, 58, 50);    // 黑色 (通关后的极高数值)
        };
    }

    public void setPoint(int x, int y) {
        this.point.x = x;
        this.point.y = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getColor(num));
        g.fillRoundRect(0, 0, getWidth(), getHeight(), MapUtil.ARC, MapUtil.ARC);

        this.setOpaque(false);
        super.paintComponent(g);
    }
}
