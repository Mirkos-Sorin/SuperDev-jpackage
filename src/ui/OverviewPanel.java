package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * 概览页面面板
 * 显示欢迎信息、环境检测状态和快速操作
 * 支持手动选择jpackage路径功能
 */
public class OverviewPanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel envStatusLabel;
    private JLabel jpackagePathLabel;
    private JTextField jpackagePathField;
    private JButton selectJpackageBtn;
    private JButton refreshBtn;

    public OverviewPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        initComponents();
    }

    private void initComponents() {
        // 顶部欢迎区域
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 中间内容区域（可滚动）
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppTheme.BACKGROUND_LIGHT);

        // 系统状态卡片
        JPanel statusCard = createStatusCard();
        contentPanel.add(statusCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // jpackage配置卡片
        JPanel jpackageCard = createJpackageConfigCard();
        contentPanel.add(jpackageCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // 快速操作卡片
        JPanel actionCard = createActionCard();
        contentPanel.add(actionCard);
        contentPanel.add(Box.createVerticalGlue());

        // 包装到滚动面板
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(AppTheme.BACKGROUND_LIGHT);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND_LIGHT);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 自定义滚动条
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(AppTheme.INPUT_BG);
        verticalScrollBar.setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * 创建顶部欢迎区域
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // 应用标题
        JLabel titleLabel = new JLabel(AppTheme.APP_NAME);
        titleLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 28));
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        // 版本信息
        JLabel versionLabel = new JLabel("版本 " + AppTheme.APP_VERSION + " | 专业级 jpackage 图形化封装工具");
        versionLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        versionLabel.setForeground(AppTheme.TEXT_SECONDARY);
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(versionLabel);

        return panel;
    }

    /**
     * 创建系统状态卡片
     */
    private JPanel createStatusCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 180);
            }
        };
        card.setLayout(new BorderLayout(20, 15));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new javax.swing.border.EmptyBorder(20, 25, 20, 25)
        ));

        // 左侧：状态信息
        JPanel statusInfoPanel = new JPanel();
        statusInfoPanel.setLayout(new BoxLayout(statusInfoPanel, BoxLayout.Y_AXIS));
        statusInfoPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("系统环境检测");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusInfoPanel.add(cardTitle);

        statusInfoPanel.add(Box.createVerticalStrut(12));

        envStatusLabel = new JLabel("正在检测环境配置...");
        envStatusLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        envStatusLabel.setForeground(AppTheme.TEXT_SECONDARY);
        envStatusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusInfoPanel.add(envStatusLabel);

        statusInfoPanel.add(Box.createVerticalStrut(8));

        String currentOS = mainFrame.getCurrentOS();
        JLabel osLabel = new JLabel("当前平台: " + currentOS);
        osLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        osLabel.setForeground(AppTheme.TEXT_MUTED);
        osLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusInfoPanel.add(osLabel);

        // 右侧：刷新按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setBackground(AppTheme.BACKGROUND_CARD);

        refreshBtn = createModernButton("刷新检测", AppTheme.BUTTON_SECONDARY);
        refreshBtn.addActionListener(e -> mainFrame.checkEnvironment());
        buttonPanel.add(refreshBtn);

        card.add(statusInfoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 创建jpackage配置卡片
     */
    private JPanel createJpackageConfigCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 160);
            }
        };
        card.setLayout(new BorderLayout(20, 15));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new javax.swing.border.EmptyBorder(20, 25, 20, 25)
        ));

        // 左侧：配置信息
        JPanel configInfoPanel = new JPanel();
        configInfoPanel.setLayout(new BoxLayout(configInfoPanel, BoxLayout.Y_AXIS));
        configInfoPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JPanel titleRow = new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
        titleRow.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("jpackage 路径配置");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        titleRow.add(cardTitle);
        titleRow.add(Box.createHorizontalGlue());

        JLabel hintLabel = new JLabel("自动检测或手动指定");
        hintLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 11));
        hintLabel.setForeground(AppTheme.TEXT_MUTED);
        titleRow.add(hintLabel);

        configInfoPanel.add(titleRow);
        configInfoPanel.add(Box.createVerticalStrut(12));

        // 路径显示和选择
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BoxLayout(pathPanel, BoxLayout.X_AXIS));
        pathPanel.setBackground(AppTheme.BACKGROUND_CARD);

        jpackagePathField = createModernTextField();
        jpackagePathField.setText(System.getProperty("java.home") + File.separator + "bin" + File.separator + "jpackage");
        jpackagePathField.setEditable(false);
        jpackagePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        selectJpackageBtn = createModernButton("选择文件", AppTheme.BUTTON_PRIMARY);
        selectJpackageBtn.addActionListener(e -> selectJpackagePath());

        pathPanel.add(jpackagePathField);
        pathPanel.add(Box.createHorizontalStrut(10));
        pathPanel.add(selectJpackageBtn);

        configInfoPanel.add(pathPanel);
        configInfoPanel.add(Box.createVerticalStrut(10));

        jpackagePathLabel = new JLabel("状态: 等待自动检测...");
        jpackagePathLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        jpackagePathLabel.setForeground(AppTheme.TEXT_MUTED);
        jpackagePathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        configInfoPanel.add(jpackagePathLabel);

        card.add(configInfoPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * 创建快速操作卡片
     */
    private JPanel createActionCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 120);
            }
        };
        card.setLayout(new BorderLayout(20, 15));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new javax.swing.border.EmptyBorder(20, 25, 20, 25)
        ));

        // 左侧：操作说明
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("快速操作");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(cardTitle);

        infoPanel.add(Box.createVerticalStrut(8));

        JLabel descLabel = new JLabel("配置项目参数并生成安装包");
        descLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        descLabel.setForeground(AppTheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(descLabel);

        // 右侧：操作按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JButton gotoCoreBtn = createModernButton("开始配置", AppTheme.BUTTON_PRIMARY);
        gotoCoreBtn.addActionListener(e -> mainFrame.switchPage(1));
        buttonPanel.add(gotoCoreBtn);

        buttonPanel.add(Box.createHorizontalStrut(12));

        JButton cleanBtn = createModernButton("清理构建", AppTheme.BUTTON_DANGER);
        cleanBtn.addActionListener(e -> mainFrame.cleanBuild());
        buttonPanel.add(cleanBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 创建现代化按钮
     */
    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color normalColor = baseColor;
                Color hoverColor = baseColor.brighter();

                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(normalColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(AppTheme.getChineseFont(Font.BOLD, 13));
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new javax.swing.border.EmptyBorder(10, 22, 10, 22));
        button.setPreferredSize(new Dimension(100, 40));

        return button;
    }

    /**
     * 创建现代化文本输入框
     */
    private JTextField createModernTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 背景
                g2.setColor(AppTheme.INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // 焦点边框
                if (hasFocus()) {
                    g2.setColor(AppTheme.INPUT_FOCUS);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                } else {
                    g2.setColor(AppTheme.INPUT_BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setBackground(AppTheme.INPUT_BG);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setCaretColor(AppTheme.TEXT_PRIMARY);
        field.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        field.setBorder(new javax.swing.border.EmptyBorder(10, 15, 10, 15));
        field.setOpaque(false);

        return field;
    }

    /**
     * 选择jpackage路径
     */
    private void selectJpackagePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择 jpackage 可执行文件");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // 设置当前路径
        String currentPath = jpackagePathField.getText();
        if (currentPath != null && !currentPath.isEmpty()) {
            File currentFile = new File(currentPath);
            if (currentFile.exists()) {
                fileChooser.setCurrentDirectory(currentFile.getParentFile());
            }
        }

        // 添加文件过滤器
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String os = System.getProperty("os.name").toLowerCase();
                if (f.isDirectory()) return true;
                if (os.contains("win")) {
                    return f.getName().equals("jpackage.exe") || f.getName().equals("jpackage");
                }
                return f.getName().equals("jpackage");
            }

            @Override
            public String getDescription() {
                return "jpackage 可执行文件";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // 检查文件是否存在且可执行
            if (selectedFile.exists() && selectedFile.canExecute()) {
                jpackagePathField.setText(selectedFile.getAbsolutePath());
                jpackagePathLabel.setText("状态: 已选择自定义路径");
                jpackagePathLabel.setForeground(AppTheme.SUCCESS_COLOR);

                // 更新MainFrame中的jpackage路径
                mainFrame.setJpackagePath(selectedFile.getAbsolutePath());
            } else {
                jpackagePathLabel.setText("错误: 文件不存在或不可执行");
                jpackagePathLabel.setForeground(AppTheme.ERROR_COLOR);
                JOptionPane.showMessageDialog(this,
                    "选择的文件不存在或不可执行，请重新选择。",
                    "路径错误",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 更新环境状态显示
     */
    public void updateEnvStatus(String status, boolean isSuccess) {
        if (envStatusLabel != null) {
            envStatusLabel.setText(status);
            if (isSuccess) {
                envStatusLabel.setForeground(AppTheme.SUCCESS_COLOR);
            } else {
                envStatusLabel.setForeground(AppTheme.ERROR_COLOR);
            }
        }
    }

    /**
     * 更新jpackage路径显示
     */
    public void updateJpackagePath(String path, boolean isValid) {
        if (jpackagePathField != null) {
            jpackagePathField.setText(path);
        }
        if (jpackagePathLabel != null) {
            if (isValid) {
                jpackagePathLabel.setText("状态: 路径检测成功");
                jpackagePathLabel.setForeground(AppTheme.SUCCESS_COLOR);
            } else {
                jpackagePathLabel.setText("状态: 未找到 jpackage，请手动选择");
                jpackagePathLabel.setForeground(AppTheme.WARNING_COLOR);
            }
        }
    }

    /**
     * 获取当前的jpackage路径
     */
    public String getJpackagePath() {
        return jpackagePathField != null ? jpackagePathField.getText() : "";
    }

    /**
     * 设置jpackage路径
     */
    public void setJpackagePath(String path) {
        if (jpackagePathField != null) {
            jpackagePathField.setText(path);
        }
    }
}
