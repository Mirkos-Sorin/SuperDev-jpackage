package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 核心配置页面面板
 * 包含打包所需的基本信息输入
 */
public class CoreConfigPanel extends JPanel {

    private MainFrame mainFrame;

    // 核心配置组件
    private JComboBox<String> installerTypeCombo;
    private JTextField jarPathField;
    private JTextField mainClassField;
    private JTextField appNameField;
    private JTextField versionField;
    private JTextField iconPathField;
    private JComboBox<String> presetCombo;

    // 构建按钮
    private JButton buildButton;

    public CoreConfigPanel(MainFrame mainFrame) {
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

        // 基本配置卡片
        JPanel basicCard = createBasicConfigCard();
        contentPanel.add(basicCard);
        contentPanel.add(Box.createVerticalStrut(20));

        // 快捷预设卡片
        JPanel presetCard = createPresetCard();
        contentPanel.add(presetCard);
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

        // 底部构建按钮
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 创建顶部标题区域
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("核心配置");
        titleLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 24));
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("设置应用程序的基本打包参数");
        subtitleLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        subtitleLabel.setForeground(AppTheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitleLabel);

        return panel;
    }

    /**
     * 创建基本配置卡片
     */
    private JPanel createBasicConfigCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 500);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));

        // 卡片标题
        JLabel cardTitle = new JLabel("基本信息");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 16));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cardTitle);

        card.add(Box.createVerticalStrut(20));

        // 目标平台
        JPanel platformRow = createFormRow("目标平台:", createPlatformPanel());
        card.add(platformRow);
        card.add(Box.createVerticalStrut(15));

        // JAR文件选择
        JPanel jarRow = createJarSelectionPanel();
        card.add(jarRow);
        card.add(Box.createVerticalStrut(15));

        // 自动推断主类
        JPanel inferRow = createInferPanel();
        card.add(inferRow);
        card.add(Box.createVerticalStrut(15));

        // 主类名称
        JPanel mainClassRow = createFormRow("主类名称:", getMainClassField());
        card.add(mainClassRow);
        card.add(Box.createVerticalStrut(15));

        // 应用程序名称
        JPanel appNameRow = createFormRow("应用程序名称:", getAppNameField());
        card.add(appNameRow);
        card.add(Box.createVerticalStrut(15));

        // 版本号
        JPanel versionRow = createFormRow("版本号:", getVersionField());
        card.add(versionRow);
        card.add(Box.createVerticalStrut(15));

        // 应用图标
        JPanel iconRow = createIconSelectionPanel();
        card.add(iconRow);

        return card;
    }

    /**
     * 创建快捷预设卡片
     */
    private JPanel createPresetCard() {
        JPanel card = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, 100);
            }
        };
        card.setLayout(new BorderLayout(15, 0));
        card.setBackground(AppTheme.BACKGROUND_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));

        // 左侧：标签
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel cardTitle = new JLabel("快捷预设");
        cardTitle.setFont(AppTheme.getChineseFont(Font.BOLD, 14));
        cardTitle.setForeground(AppTheme.TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(cardTitle);

        JLabel hintLabel = new JLabel("选择常用配置模板");
        hintLabel.setFont(AppTheme.getChineseFont(Font.PLAIN, 11));
        hintLabel.setForeground(AppTheme.TEXT_MUTED);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(hintLabel);

        // 中间：预设选择
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setBackground(AppTheme.BACKGROUND_CARD);

        String[] presets = {"标准应用", "游戏/高性能", "调试模式", "最小化运行"};
        String[] presetKeys = {"standard", "game", "debug", "minimal"};
        presetCombo = AppTheme.createModernComboBox();
        for (String preset : presets) {
            presetCombo.addItem(preset);
        }
        presetCombo.putClientProperty("presetKeys", presetKeys);
        centerPanel.add(presetCombo);

        centerPanel.add(Box.createHorizontalStrut(10));

        JButton applyPresetBtn = createModernButton("应用预设", AppTheme.BUTTON_PRIMARY);
        applyPresetBtn.addActionListener(e -> {
            String mode = presetKeys[presetCombo.getSelectedIndex()];
            mainFrame.applyPreset(mode);
        });
        centerPanel.add(applyPresetBtn);

        // 右侧：快速开始
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        rightPanel.setBackground(AppTheme.BACKGROUND_CARD);

        JButton quickStartBtn = createModernButton("快速开始", AppTheme.BUTTON_SUCCESS);
        quickStartBtn.addActionListener(e -> {
            mainFrame.switchPage(1);
        });
        rightPanel.add(quickStartBtn);

        card.add(leftPanel, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 创建底部构建按钮区域
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_LIGHT);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        panel.add(Box.createHorizontalGlue());

        buildButton = createModernButton("开始打包", AppTheme.BUTTON_SUCCESS);
        buildButton.setFont(AppTheme.getChineseFont(Font.BOLD, 15));
        buildButton.setPreferredSize(new Dimension(180, 50));
        buildButton.setMaximumSize(new Dimension(180, 50));
        buildButton.setEnabled(false);
        buildButton.addActionListener(e -> mainFrame.startBuild());

        panel.add(buildButton);
        panel.add(Box.createHorizontalGlue());

        mainFrame.setBuildButton(buildButton);

        return panel;
    }

    /**
     * 创建平台选择面板
     */
    private JPanel createPlatformPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel platformLabel = new JLabel(mainFrame.getCurrentOS());
        platformLabel.setFont(AppTheme.getChineseFont(Font.BOLD, 12));
        platformLabel.setForeground(AppTheme.ACCENT_TEAL);
        platformLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        platformLabel.setOpaque(true);
        platformLabel.setBackground(new Color(AppTheme.ACCENT_TEAL.getRed(), AppTheme.ACCENT_TEAL.getGreen(), AppTheme.ACCENT_TEAL.getBlue(), 30));
        panel.add(platformLabel);

        panel.add(Box.createHorizontalStrut(15));

        installerTypeCombo = AppTheme.createModernComboBox();
        updateInstallerTypes();
        panel.add(installerTypeCombo);

        panel.add(Box.createHorizontalGlue());

        mainFrame.setInstallerTypeCombo(installerTypeCombo);

        return panel;
    }

    /**
     * 更新安装包类型选项
     */
    private void updateInstallerTypes() {
        installerTypeCombo.removeAllItems();
        String os = mainFrame.getCurrentOS();

        if (os.equals("Windows")) {
            installerTypeCombo.addItem("exe - Windows安装包");
            installerTypeCombo.addItem("msi - Windows安装程序");
        } else if (os.equals("Darwin")) {
            installerTypeCombo.addItem("dmg - macOS磁盘镜像");
            installerTypeCombo.addItem("pkg - macOS安装包");
        } else if (os.equals("Linux")) {
            installerTypeCombo.addItem("deb - Debian包");
            installerTypeCombo.addItem("rpm - RedHat包");
            installerTypeCombo.addItem("app-image - Linux应用镜像");
        } else {
            installerTypeCombo.addItem("exe - Windows安装包");
        }
    }

    /**
     * 创建JAR文件选择面板
     */
    private JPanel createJarSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel("目标JAR文件:");
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(120, 40));
        label.setMaximumSize(new Dimension(120, 40));
        panel.add(label);

        jarPathField = AppTheme.createModernTextField();
        jarPathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        jarPathField.setToolTipText("选择编译好的 .jar 文件");
        mainFrame.setJarPathField(jarPathField);

        panel.add(jarPathField);

        panel.add(Box.createHorizontalStrut(10));

        JButton browseBtn = createModernButton("浏览", AppTheme.BUTTON_SECONDARY);
        browseBtn.addActionListener(e -> mainFrame.selectJar());
        panel.add(browseBtn);

        return panel;
    }

    /**
     * 创建自动推断面板
     */
    private JPanel createInferPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel("");
        label.setPreferredSize(new Dimension(120, 40));
        label.setMaximumSize(new Dimension(120, 40));
        panel.add(label);

        JButton inferBtn = createModernButton("自动检测主类", AppTheme.BUTTON_SECONDARY);
        inferBtn.addActionListener(e -> mainFrame.inferMainClass());
        panel.add(inferBtn);

        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    /**
     * 创建图标选择面板
     */
    private JPanel createIconSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel("应用图标:");
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(120, 40));
        label.setMaximumSize(new Dimension(120, 40));
        panel.add(label);

        iconPathField = AppTheme.createModernTextField();
        iconPathField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        iconPathField.setToolTipText("可选：选择应用程序图标文件");
        mainFrame.setIconPathField(iconPathField);

        panel.add(iconPathField);

        panel.add(Box.createHorizontalStrut(10));

        JButton iconBtn = createModernButton("选择图标", AppTheme.BUTTON_SECONDARY);
        iconBtn.addActionListener(e -> mainFrame.selectIcon());
        panel.add(iconBtn);

        return panel;
    }

    /**
     * 创建表单行
     */
    private JPanel createFormRow(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(AppTheme.BACKGROUND_CARD);

        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.getChineseFont(Font.PLAIN, 13));
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setPreferredSize(new Dimension(120, 40));
        label.setMaximumSize(new Dimension(120, 40));
        panel.add(label);

        if (field instanceof JTextField) {
            ((JTextField) field).setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        }
        panel.add(field);
        panel.add(Box.createHorizontalGlue());

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
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        return button;
    }

    // Getter方法
    public JComboBox<String> getInstallerTypeCombo() {
        return installerTypeCombo;
    }

    public JTextField getJarPathField() {
        return jarPathField;
    }

    public JTextField getMainClassField() {
        if (mainClassField == null) {
            mainClassField = AppTheme.createModernTextField();
            mainClassField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            mainClassField.setToolTipText("例如: com.example.Main");
            mainFrame.setMainClassField(mainClassField);
        }
        return mainClassField;
    }

    public JTextField getAppNameField() {
        if (appNameField == null) {
            appNameField = AppTheme.createModernTextField();
            appNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            appNameField.setText("MyApplication");
            mainFrame.setAppNameField(appNameField);
        }
        return appNameField;
    }

    public JTextField getVersionField() {
        if (versionField == null) {
            versionField = AppTheme.createModernTextField();
            versionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            versionField.setText("1.0.0");
            mainFrame.setVersionField(versionField);
        }
        return versionField;
    }

    public JTextField getIconPathField() {
        return iconPathField;
    }

    public JComboBox<String> getPresetCombo() {
        return presetCombo;
    }

    public JButton getBuildButton() {
        return buildButton;
    }
}
