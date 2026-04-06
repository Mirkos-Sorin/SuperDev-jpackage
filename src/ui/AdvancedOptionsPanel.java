package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 高级选项页面面板
 * 提供JVM参数、启动器参数、模块配置等高级选项
 */
public class AdvancedOptionsPanel extends JPanel {

    private MainFrame mainFrame;

    // 高级选项组件
    private JTextArea javaOptsArea;
    private JTextArea launchArgsArea;
    private JTextField modulesField;
    private JTextField runtimeImgField;
    private JCheckBox consoleCheckBox;
    private JCheckBox verboseCheckBox;
    private JTextField winMenuGroupField;
    private JTextField jpackagePathField;

    public AdvancedOptionsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND_LIGHT);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        initComponents();
    }

    private void initComponents() {
        // 顶部标题
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 中间内容区域
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(AppTheme.BACKGROUND_LIGHT);

        // JVM运行时参数卡片
        JPanel jvmCard = createJVMCard();
        contentPanel.add(jvmCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // 启动器参数卡片
        JPanel launchCard = createLaunchCard();
        contentPanel.add(launchCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // 模块与运行时卡片
        JPanel moduleCard = createModuleCard();
        contentPanel.add(moduleCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // 系统特定选项卡片
        JPanel sysCard = createSystemCard();
        contentPanel.add(sysCard);
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
     * 创建顶部标题区域
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("高级选项");
        titleLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 24));
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("配置JVM参数、运行时选项和系统特定设置");
        subtitleLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        subtitleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    /**
     * 创建JVM运行时参数卡片
     */
    private JPanel createJVMCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 180);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // 卡片标题
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("JVM 运行时参数");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        titlePanel.add(cardTitle);

        titlePanel.add(Box.createHorizontalGlue());

        JLabel hintLabel = new JLabel("每行一个参数");
        hintLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 11));
        hintLabel.setForeground(AppTheme.TEXT_MUTED);
        titlePanel.add(hintLabel);

        card.add(titlePanel);

        card.add(Box.createVerticalStrut(10));

        // 描述
        JLabel descLabel = new JLabel("设置最大内存、编码等 JVM 选项");
        descLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        descLabel.setForeground(AppTheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        card.add(Box.createVerticalStrut(15));

        // 文本区域
        javaOptsArea = AppTheme.createModernTextArea();
        javaOptsArea.setText("-Xmx512m\n-Dfile.encoding=UTF-8");
        javaOptsArea.setRows(4);

        JScrollPane scrollPane = new JScrollPane(javaOptsArea);
        scrollPane.setBackground(AppTheme.INPUT_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.INPUT_BORDER, 1, true));
        scrollPane.getViewport().setBackground(AppTheme.INPUT_BG);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // 自定义滚动条
        JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
        vScrollBar.setPreferredSize(new Dimension(6, 0));
        vScrollBar.setBackground(AppTheme.INPUT_BG);

        card.add(scrollPane);

        return card;
    }

    /**
     * 创建启动器参数卡片
     */
    private JPanel createLaunchCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 160);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // 卡片标题
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("启动器参数 (Launcher Arguments)");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        titlePanel.add(cardTitle);

        titlePanel.add(Box.createHorizontalGlue());

        JLabel hintLabel = new JLabel("每行一个");
        hintLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 11));
        hintLabel.setForeground(AppTheme.TEXT_MUTED);
        titlePanel.add(hintLabel);

        card.add(titlePanel);

        card.add(Box.createVerticalStrut(10));

        // 描述
        JLabel descLabel = new JLabel("传递给应用程序的参数");
        descLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 12));
        descLabel.setForeground(AppTheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descLabel);

        card.add(Box.createVerticalStrut(15));

        // 文本区域
        launchArgsArea = AppTheme.createModernTextArea();
        launchArgsArea.setText("--config\n/path/to/config.ini");
        launchArgsArea.setRows(3);

        JScrollPane scrollPane = new JScrollPane(launchArgsArea);
        scrollPane.setBackground(AppTheme.INPUT_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.INPUT_BORDER, 1, true));
        scrollPane.getViewport().setBackground(AppTheme.INPUT_BG);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JScrollBar vScrollBar = scrollPane.getVerticalScrollBar();
        vScrollBar.setPreferredSize(new Dimension(6, 0));
        vScrollBar.setBackground(AppTheme.INPUT_BG);

        card.add(scrollPane);

        return card;
    }

    /**
     * 创建模块与运行时卡片
     */
    private JPanel createModuleCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 180);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // 卡片标题
        JLabel cardTitle = new JLabel("模块与运行时");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cardTitle);

        card.add(Box.createVerticalStrut(15));

        // 额外模块行
        JPanel modulesRow = createInputRow("额外模块 (--add-modules):", getModulesField());
        modulesRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.add(modulesRow);

        card.add(Box.createVerticalStrut(15));

        // 运行时镜像行
        JPanel runtimeRow = createRuntimeRow();
        runtimeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.add(runtimeRow);

        card.add(Box.createVerticalStrut(15));

        // jpackage路径行
        JPanel jpackageRow = createJpackagePathRow();
        jpackageRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.add(jpackageRow);

        return card;
    }

    /**
     * 创建系统特定选项卡片
     */
    private JPanel createSystemCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 220);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // 卡片标题
        JLabel cardTitle = new JLabel("系统特定选项");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cardTitle);

        card.add(Box.createVerticalStrut(15));

        // 复选框行
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.setBackground(AppTheme.BACKGROUND_CARD);

        consoleCheckBox = AppTheme.createModernCheckBox("显示控制台窗口 (Windows)");
        consoleCheckBox.setSelected(false);
        checkBoxPanel.add(consoleCheckBox);

        checkBoxPanel.add(Box.createVerticalStrut(10));

        verboseCheckBox = AppTheme.createModernCheckBox("详细日志模式 (--verbose)");
        verboseCheckBox.setSelected(false);
        checkBoxPanel.add(verboseCheckBox);

        card.add(checkBoxPanel);

        card.add(Box.createVerticalStrut(15));

        // Windows菜单组行
        JPanel menuGroupRow = createInputRow("开始菜单组名:", getWinMenuGroupField());
        menuGroupRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.add(menuGroupRow);

        return card;
    }

    /**
     * 创建输入行
     */
    private JPanel createInputRow(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(180, 40));
        label.setMaximumSize(new Dimension(180, 40));
        panel.add(label);

        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(textField);

        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    /**
     * 创建运行时镜像选择行
     */
    private JPanel createRuntimeRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel("运行时镜像:");
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(180, 40));
        label.setMaximumSize(new Dimension(180, 40));
        panel.add(label);

        runtimeImgField = AppTheme.createModernTextField();
        runtimeImgField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        runtimeImgField.setToolTipText("自定义 JRE 路径 (可选)");
        panel.add(runtimeImgField);

        panel.add(Box.createHorizontalStrut(10));

        JButton browseBtn = createModernButton("浏览", AppTheme.BUTTON_SECONDARY);
        browseBtn.addActionListener(e -> mainFrame.selectRuntime());
        panel.add(browseBtn);

        return panel;
    }

    /**
     * 创建jpackage路径选择行
     */
    private JPanel createJpackagePathRow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel("jpackage 路径:");
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(180, 40));
        label.setMaximumSize(new Dimension(180, 40));
        panel.add(label);

        jpackagePathField = AppTheme.createModernTextField();
        jpackagePathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        jpackagePathField.setToolTipText("手动指定 jpackage 可执行文件路径");
        mainFrame.setJpackagePathField(jpackagePathField);
        panel.add(jpackagePathField);

        panel.add(Box.createHorizontalStrut(10));

        JButton browseBtn = createModernButton("选择", AppTheme.BUTTON_PRIMARY);
        browseBtn.addActionListener(e -> mainFrame.selectJpackagePath());
        panel.add(browseBtn);

        return panel;
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
        button.setBorder(new EmptyBorder(10, 18, 10, 18));

        return button;
    }

    // Getter方法
    public JTextArea getJavaOptsArea() {
        return javaOptsArea;
    }

    public JTextArea getLaunchArgsArea() {
        return launchArgsArea;
    }

    public JTextField getModulesField() {
        if (modulesField == null) {
            modulesField = AppTheme.createModernTextField();
            modulesField.setToolTipText("留空则自动分析 (例如: java.base,java.sql)");
        }
        return modulesField;
    }

    public JTextField getRuntimeImgField() {
        return runtimeImgField;
    }

    public JCheckBox getConsoleCheckBox() {
        return consoleCheckBox;
    }

    public JCheckBox getVerboseCheckBox() {
        return verboseCheckBox;
    }

    public JTextField getWinMenuGroupField() {
        if (winMenuGroupField == null) {
            winMenuGroupField = AppTheme.createModernTextField();
            winMenuGroupField.setText("My Application");
        }
        return winMenuGroupField;
    }

    public JTextField getJpackagePathField() {
        return jpackagePathField;
    }
}
