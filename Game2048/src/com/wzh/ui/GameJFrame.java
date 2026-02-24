package com.wzh.ui;

import com.wzh.classes.NumberBlock;
import com.wzh.util.DataUtil;
import com.wzh.util.MapUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameJFrame extends JFrame {
    private NumberBlock[][] blocks = new NumberBlock[4][4];

    public GameJFrame() {
        this(MapUtil.GAME_WIDTH, MapUtil.GAME_HEIGHT);
    }

    public GameJFrame(int width, int height) {
        // 取消默认坐标
        this.setLayout(null);

        // 各种初始化逻辑
        initFrame(width, height);
        initJMenuBar();
        initBorder();
        initShadow();
        initNumBlock();
        initListener();

        // 设为可视化
        this.setVisible(true);
    }

    // 初始化界面
    private void initFrame(int width, int height) {
        this.setSize(width, height);
        this.setTitle("2048小游戏");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // 初始化菜单
    private void initJMenuBar() {
        JMenuBar bar = new JMenuBar();

        // 菜单项
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutUsJMenu = new JMenu("关于我们");

        // 小项
        JMenuItem restartItem = new JMenuItem("重新开始");

        JMenuItem exitItem = new JMenuItem("退出游戏");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuItem aboutUsItem = new JMenuItem("公众号");

        // 把小项添加到菜单项中
        functionJMenu.add(restartItem);
        functionJMenu.add(exitItem);

        aboutUsJMenu.add(aboutUsItem);

        // 最后把菜单项添加到bar中
        bar.add(functionJMenu);
        bar.add(aboutUsJMenu);

        this.setJMenuBar(bar);
    }

    // 初始化边框
    private void initBorder() {
        JLayeredPane layeredPane = this.getLayeredPane();

        JPanel borderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.GRAY);
                g.fillRoundRect(MapUtil.BORDER_STARTX, MapUtil.BORDER_STARTY, MapUtil.BORDER_SIZE, MapUtil.BORDER_SIZE, MapUtil.ARC, MapUtil.ARC);
            }
        };
        borderPanel.setOpaque(false);
        // 画布大小
        borderPanel.setBounds(0, 0, 900, 900);

        layeredPane.add(borderPanel, JLayeredPane.PALETTE_LAYER); // 置于下方
    }

    // 初始化阴影块
    private void initShadow() {
        JLayeredPane layeredPane = this.getLayeredPane();

        JPanel borderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        int x = MapUtil.ChangeLogicPosToActPos(i, j).x;
                        int y = MapUtil.ChangeLogicPosToActPos(i, j).y;
                        g.fillRoundRect(x, y, MapUtil.BLOCK_SIZE, MapUtil.BLOCK_SIZE, MapUtil.ARC, MapUtil.ARC);
                    }
                }
            }
        };
        borderPanel.setOpaque(false);
        // 画布大小
        borderPanel.setBounds(0, 0, 900, 900);

        layeredPane.add(borderPanel, JLayeredPane.POPUP_LAYER); // 置于上方
    }

    // 初始化数字方块
    private void initNumBlock() {
        // 生成两个随机方块
        CreateBlock();
        CreateBlock();
    }

    // 生成数字方块
    private void CreateBlock() {
        Point point = DataUtil.GetRandomPos(blocks);

        Random rand = new Random();
        int num = rand.nextDouble() > 0.9 ? 4 : 2;
        NumberBlock numberBlock = new NumberBlock(num);
        numberBlock.setPoint(point.x, point.y);

        numberBlock.setBounds(MapUtil.ChangeLogicPosToActPos(point.x, point.y).x, MapUtil.ChangeLogicPosToActPos(point.x, point.y).y, MapUtil.BLOCK_SIZE, MapUtil.BLOCK_SIZE);
        JLayeredPane layeredPane = this.getLayeredPane();
        layeredPane.add(numberBlock, JLayeredPane.DRAG_LAYER);
        blocks[point.x][point.y] = numberBlock;
    }

    // 在指定位置生成数字方块
    private void CreateBlock(int x, int y, int num) {
        NumberBlock numberBlock = new NumberBlock(num);
        numberBlock.setPoint(x, y);

        numberBlock.setBounds(MapUtil.ChangeLogicPosToActPos(x, y).x, MapUtil.ChangeLogicPosToActPos(x, y).y, MapUtil.BLOCK_SIZE, MapUtil.BLOCK_SIZE);
        JLayeredPane layeredPane = this.getLayeredPane();
        layeredPane.add(numberBlock, JLayeredPane.DRAG_LAYER);
        blocks[x][y] = numberBlock;
    }

    // 初始化键盘监听器
    private void initListener() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP -> moveUp();
                    case KeyEvent.VK_DOWN -> moveDown();
                    case KeyEvent.VK_LEFT -> moveLeft();
                    case KeyEvent.VK_RIGHT -> moveRight();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    // 向上移动
    private void moveUp() {
        boolean ifHaveMove = false;
        // 遍历四个列blocks[i][3~0]
        for (int i = 0; i < 4; i++) {
            NumberBlock previous = null;
            // 在一趟循环中处理合并
            for (int j = 0; j < 4; j++) {
                if (blocks[i][j] != null) {
                    if (previous != null && blocks[i][j].num == previous.num) {
                        ifHaveMove = true;
                        // 合并blocks[i][j]和previous
                        mergeBlocks(blocks[i][j], previous);
                        previous = null;
                        continue;
                    }
                    previous = blocks[i][j];
                }
            }

            // 在第二趟循环里重新排列
            int ptr = 0; // 从最上面存起
            boolean isBeforeNull = false;
            for (int k = 0; k < 4; k++) {
                if (blocks[i][k] != null) {
                    if (isBeforeNull) ifHaveMove = true;
                    isBeforeNull = false;
                    // 先从视觉上移动
                    moveBlockWithAnimation(blocks[i][k], MapUtil.ChangeLogicPosToActPos(i, ptr).x,
                            MapUtil.ChangeLogicPosToActPos(i, ptr).y);
                    // 更新blocks存储
                    blocks[i][ptr] = blocks[i][k];
                    if (k != ptr) blocks[i][k] = null;
                    // 更新point
                    blocks[i][ptr].setPoint(i, ptr);
                    ptr++;
                } else isBeforeNull = true;
            }
        }

        // 应该只有在移动过的情况下才新建方块
        if (ifHaveMove) CreateBlock();

        // 强制刷屏
        this.getLayeredPane().revalidate();
        this.getLayeredPane().repaint();
    }

    // 向下移动
    private void moveDown() {
        boolean ifHaveMove = false;
        // 遍历四个列blocks[i][0~3]
        for (int i = 0; i < 4; i++) {
            NumberBlock previous = null;
            // 在一趟循环中处理合并
            for (int j = 3; j >= 0; j--) {
                if (blocks[i][j] != null) {
                    if (previous != null && blocks[i][j].num == previous.num) {
                        ifHaveMove = true;
                        // 合并blocks[i][j]和previous
                        mergeBlocks(blocks[i][j], previous);
                        previous = null;
                        continue;
                    }
                    previous = blocks[i][j];
                }
            }

            // 在第二趟循环里重新排列
            int ptr = 3; // 从最底下存起
            boolean isBeforeNull = false;
            for (int k = 3; k >= 0; k--) {
                if (blocks[i][k] != null) {
                    if (isBeforeNull) ifHaveMove = true;
                    isBeforeNull = false;
                    // 先从视觉上移动
                    moveBlockWithAnimation(blocks[i][k], MapUtil.ChangeLogicPosToActPos(i, ptr).x,
                            MapUtil.ChangeLogicPosToActPos(i, ptr).y);
                    // 更新blocks存储
                    blocks[i][ptr] = blocks[i][k];
                    if (k != ptr) blocks[i][k] = null;
                    // 更新point
                    blocks[i][ptr].setPoint(i, ptr);
                    ptr--;
                } else isBeforeNull = true;
            }
        }

        // 应该只有在移动过的情况下才新建方块
        if (ifHaveMove) CreateBlock();

        // 强制刷屏
        this.getLayeredPane().revalidate();
        this.getLayeredPane().repaint();
    }

    // 向左移动
    private void moveLeft() {
        boolean ifHaveMove = false;
        // 遍历四个行blocks[0~3][i]
        for (int i = 0; i < 4; i++) {
            NumberBlock previous = null;
            // 在一趟循环中处理合并
            for (int j = 0; j < 4; j++) {
                if (blocks[j][i] != null) {
                    if (previous != null && blocks[j][i].num == previous.num) {
                        ifHaveMove = true;
                        // 合并blocks[j][i]和previous
                        mergeBlocks(blocks[j][i], previous);
                        previous = null;
                        continue;
                    }
                    previous = blocks[j][i];
                }
            }

            // 在第二趟循环里重新排列
            int ptr = 0; // 从最左边存起
            boolean isBeforeNull = false;
            for (int k = 0; k < 4; k++) {
                if (blocks[k][i] != null) {
                    if (isBeforeNull) ifHaveMove = true;
                    isBeforeNull = false;
                    // 先从视觉上移动
                    moveBlockWithAnimation(blocks[k][i], MapUtil.ChangeLogicPosToActPos(ptr, i).x,
                            MapUtil.ChangeLogicPosToActPos(ptr, i).y);
                    // 更新blocks存储
                    blocks[ptr][i] = blocks[k][i];
                    if (k != ptr) blocks[k][i] = null;
                    // 更新point
                    blocks[ptr][i].setPoint(ptr, i);
                    ptr++;
                } else isBeforeNull = true;
            }
        }

        // 应该只有在移动过的情况下才新建方块
        if (ifHaveMove) CreateBlock();

        // 强制刷屏
        this.getLayeredPane().revalidate();
        this.getLayeredPane().repaint();
    }

    // 向右移动
    private void moveRight() {
        boolean ifHaveMove = false;
        // 遍历四个行blocks[0~3][i]
        for (int i = 0; i < 4; i++) {
            NumberBlock previous = null;
            // 在一趟循环中处理合并
            for (int j = 3; j >= 0; j--) {
                if (blocks[j][i] != null) {
                    if (previous != null && blocks[j][i].num == previous.num) {
                        ifHaveMove = true;
                        // 合并blocks[j][i]和previous
                        mergeBlocks(blocks[j][i], previous);
                        previous = null;
                        continue;
                    }
                    previous = blocks[j][i];
                }
            }

            // 在第二趟循环里重新排列
            int ptr = 3; // 从最右边存起
            boolean isBeforeNull = false;
            for (int k = 3; k >= 0; k--) {
                if (blocks[k][i] != null) {
                    if (isBeforeNull) ifHaveMove = true;
                    isBeforeNull = false;
                    // 先从视觉上移动
                    moveBlockWithAnimation(blocks[k][i], MapUtil.ChangeLogicPosToActPos(ptr, i).x,
                            MapUtil.ChangeLogicPosToActPos(ptr, i).y);
                    // 更新blocks存储
                    blocks[ptr][i] = blocks[k][i];
                    if (k != ptr) blocks[k][i] = null;
                    // 更新point
                    blocks[ptr][i].setPoint(ptr, i);
                    ptr--;
                } else isBeforeNull = true;
            }
        }

        // 应该只有在移动过的情况下才新建方块
        if (ifHaveMove) CreateBlock();

        // 强制刷屏
        this.getLayeredPane().revalidate();
        this.getLayeredPane().repaint();
    }

    // 把a合并到b上
    private void mergeBlocks(NumberBlock a, NumberBlock b) {
        int num = b.num;
        Point point = b.point;
        this.removeBlockAt(a.point.x, a.point.y);
        this.removeBlockAt(b.point.x, b.point.y);
        this.CreateBlock(point.x, point.y, num * 2);
    }

    // 移除指定位置的方块
    private void removeBlockAt(int x, int y) {
        if (blocks[x][y] != null) {
            System.out.println("准备移除坐标为" + x + " " + y + "的方块");
            this.getLayeredPane().remove(blocks[x][y]); // 从容器删除真正的那个对象
            blocks[x][y] = null; // 清空账本
        }
    }

    // 方块移动的动画
    private void moveBlockWithAnimation(NumberBlock block, int targetX, int targetY) {
        int frames = 10; // 动画分10帧完成
        int delay = 10;  // 每帧间隔10毫秒

        // 计算每一帧位移多少
        int startX = block.getX();
        int startY = block.getY();
        double dx = (double) (targetX - startX) / frames;
        double dy = (double) (targetY - startY) / frames;

        Timer timer = new Timer(delay, new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                if (count < frames) {
                    // 每一帧移动一点点
                    block.setLocation(startX + (int) (dx * count), startY + (int) (dy * count));
                } else {
                    // 最后一帧强制对齐目标，并停止计时器
                    block.setLocation(targetX, targetY);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }
}
