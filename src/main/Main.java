package main;

import ui.MainFrame;
import javax.swing.*;

/**
 * SuperDev for jpackage - 主程序入口
 * 基于JDK jpackage的专业Java应用打包工具
 */
public class Main {
    public static void main(String[] args) {
        // 设置LookAndFeel为系统默认风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启用高DPI支持
        System.setProperty("sun.java2d.uiScale.enabled", "true");

        // 在事件调度线程上创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
