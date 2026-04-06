package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;

/**
 * 现代化主题样式定义
 * 提供扁平化设计、渐变色、美观的UI组件
 */
public class AppTheme {

    // 现代化深色配色方案
    public static final Color BACKGROUND_DARK = new Color(0x1a, 0x1a, 0x2e);
    public static final Color BACKGROUND_MEDIUM = new Color(0x16, 0x16, 0x28);
    public static final Color BACKGROUND_LIGHT = new Color(0x22, 0x22, 0x3c);
    public static final Color BACKGROUND_CARD = new Color(0x26, 0x26, 0x46);

    // 现代化渐变色
    public static final Color ACCENT_PURPLE = new Color(0x7c, 0x3a, 0xed);
    public static final Color ACCENT_BLUE = new Color(0x3b, 0x82, 0xf6);
    public static final Color ACCENT_CYAN = new Color(0x06, 0xb6, 0xd4);
    public static final Color ACCENT_TEAL = new Color(0x14, 0xb8, 0xa6);

    // 文字颜色
    public static final Color TEXT_PRIMARY = new Color(0xf8, 0xfa, 0xfc);
    public static final Color TEXT_SECONDARY = new Color(0x94, 0xa3, 0xb8);
    public static final Color TEXT_MUTED = new Color(0x64, 0x74, 0x8b);

    // 状态颜色
    public static final Color SUCCESS_COLOR = new Color(0x22, 0xc5, 0x5e);
    public static final Color WARNING_COLOR = new Color(0xf5, 0x9e, 0x0b);
    public static final Color ERROR_COLOR = new Color(0xef, 0x44, 0x44);
    public static final Color INFO_COLOR = new Color(0x3b, 0x82, 0xf6);

    // 输入组件颜色
    public static final Color INPUT_BG = new Color(0x31, 0x31, 0x4a);
    public static final Color INPUT_BORDER = new Color(0x47, 0x47, 0x6a);
    public static final Color INPUT_FOCUS = new Color(0x63, 0x5e, 0xdb);

    // 按钮颜色
    public static final Color BUTTON_PRIMARY = new Color(0x63, 0x5e, 0xdb);
    public static final Color BUTTON_PRIMARY_HOVER = new Color(0x7c, 0x6e, 0xf0);
    public static final Color BUTTON_SECONDARY = new Color(0x3f, 0x3f, 0x5c);
    public static final Color BUTTON_SECONDARY_HOVER = new Color(0x52, 0x52, 0x70);
    public static final Color BUTTON_SUCCESS = new Color(0x22, 0xc5, 0x5e);
    public static final Color BUTTON_DANGER = new Color(0xef, 0x44, 0x44);

    // 边框颜色
    public static final Color BORDER_COLOR = new Color(0x37, 0x3a, 0x55);
    public static final Color BORDER_HOVER = new Color(0x63, 0x5e, 0xdb);

    public static final String APP_NAME = "SuperDev for jpackage";
    public static final String APP_VERSION = "3.0.0";
    public static final String CONFIG_EXT = ".sdp";

    /**
     * 获取支持中文的字体
     */
    public static Font getChineseFont(int style, int size) {
        String[] fontNames = {"Microsoft YaHei UI", "PingFang SC", "SimHei", "Microsoft YaHei", "Segoe UI"};
        for (String name : fontNames) {
            try {
                Font font = new Font(name, style, size);
                if (font.canDisplay('中')) {
                    return font;
                }
            } catch (Exception e) {
                // 继续尝试下一个字体
            }
        }
        return new Font("Segoe UI", style, size);
    }

    /**
     * 创建现代化标题标签
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(getChineseFont(Font.BOLD, 32));
        label.setForeground(TEXT_PRIMARY);
        label.setBorder(new EmptyBorder(20, 0, 20, 0));
        return label;
    }

    /**
     * 创建章节标题标签
     */
    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(getChineseFont(Font.BOLD, 18));
        label.setForeground(TEXT_PRIMARY);
        label.setBorder(new EmptyBorder(0, 0, 15, 0));
        return label;
    }

    /**
     * 创建副标题标签
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(getChineseFont(Font.PLAIN, 14));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    /**
     * 创建信息标签
     */
    public static JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(getChineseFont(Font.PLAIN, 12));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    /**
     * 创建状态标签（带颜色）
     */
    public static JLabel createStatusLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(getChineseFont(Font.BOLD, 13));
        label.setForeground(color);
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        label.setOpaque(true);
        label.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        label.setBorder(BorderFactory.createLineBorder(color, 1, true));
        return label;
    }

    /**
     * 创建现代化卡片面板
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(BACKGROUND_CARD);
        card.setBorder(createRoundedBorder(ACCENT_PURPLE, 1, 12));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        return card;
    }

    /**
     * 创建带标题的卡片面板
     */
    public static JPanel createTitledCard(String title) {
        JPanel card = createCard();
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = createSectionLabel(title);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BACKGROUND_CARD);
        wrapper.add(titleLabel);
        wrapper.add(Box.createVerticalStrut(10));

        JPanel container = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BACKGROUND_CARD);
        container.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel result = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        };
        result.setLayout(new BorderLayout(0, 10));
        result.setBackground(BACKGROUND_CARD);
        result.add(wrapper, BorderLayout.NORTH);
        result.add(container, BorderLayout.CENTER);

        return result;
    }

    /**
     * 创建圆角边框
     */
    public static Border createRoundedBorder(Color color, int thickness, int radius) {
        return BorderFactory.createCompoundBorder(
            new RoundedCornerBorder(color, thickness, radius),
            new EmptyBorder(15, 15, 15, 15)
        );
    }

    /**
     * 创建现代化主按钮
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_PRIMARY.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(BUTTON_PRIMARY_HOVER);
                } else {
                    g2.setColor(BUTTON_PRIMARY);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(getChineseFont(Font.BOLD, 14));
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 25, 12, 25));
        return button;
    }

    /**
     * 创建现代化次要按钮
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_SECONDARY.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(BUTTON_SECONDARY_HOVER);
                } else {
                    g2.setColor(BUTTON_SECONDARY);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(getChineseFont(Font.PLAIN, 13));
        button.setForeground(TEXT_SECONDARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * 创建成功按钮
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_SUCCESS.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0x16, 0xa3, 0x4a));
                } else {
                    g2.setColor(BUTTON_SUCCESS);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(getChineseFont(Font.BOLD, 13));
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * 创建危险按钮
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(BUTTON_DANGER.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(0xdc, 0x26, 0x26));
                } else {
                    g2.setColor(BUTTON_DANGER);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(getChineseFont(Font.BOLD, 13));
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * 创建现代化文本输入框
     */
    public static JTextField createModernTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 背景
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // 焦点边框
                if (hasFocus()) {
                    g2.setColor(INPUT_FOCUS);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };

        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(getChineseFont(Font.PLAIN, 14));
        field.setBorder(new EmptyBorder(10, 15, 10, 15));
        field.setOpaque(false);
        field.setCaret(new DefaultCaret());
        return field;
    }

    /**
     * 创建现代化文本区域
     */
    public static JTextArea createModernTextArea() {
        JTextArea area = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        area.setBackground(INPUT_BG);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setFont(getChineseFont(Font.PLAIN, 13));
        area.setBorder(new EmptyBorder(10, 15, 10, 15));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        return area;
    }

    /**
     * 创建现代化下拉框
     */
    public static JComboBox<String> createModernComboBox() {
        JComboBox<String> combo = new JComboBox<String>() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // 箭头区域
                g2.setColor(TEXT_MUTED);
                int arrowX = getWidth() - 25;
                int arrowY = getHeight() / 2;
                g2.fillPolygon(new int[]{arrowX - 5, arrowX + 5, arrowX},
                              new int[]{arrowY - 3, arrowY - 3, arrowY + 3}, 3);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(getChineseFont(Font.PLAIN, 13));
        combo.setBorder(new EmptyBorder(8, 15, 8, 15));
        combo.setOpaque(false);
        return combo;
    }

    /**
     * 创建现代化复选框
     */
    public static JCheckBox createModernCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int boxHeight = 20;
                int y = (getHeight() - boxHeight) / 2;

                // 复选框背景
                if (isSelected()) {
                    g2.setColor(BUTTON_PRIMARY);
                } else {
                    g2.setColor(INPUT_BG);
                }
                g2.fillRoundRect(0, y, 20, boxHeight, 4, 4);

                // 边框
                g2.setColor(isSelected() ? BUTTON_PRIMARY : INPUT_BORDER);
                g2.drawRoundRect(0, y, 19, boxHeight - 1, 4, 4);

                // 勾选符号
                if (isSelected()) {
                    g2.setColor(TEXT_PRIMARY);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(4, y + 10, 8, y + 14);
                    g2.drawLine(8, y + 14, 16, y + 6);
                }

                g2.dispose();

                // 绘制文字
                setForeground(TEXT_PRIMARY);
                super.paintComponent(g);
            }
        };

        checkBox.setFont(getChineseFont(Font.PLAIN, 13));
        checkBox.setIconTextGap(10);
        checkBox.setContentAreaFilled(false);
        checkBox.setBorder(new EmptyBorder(5, 30, 5, 5));
        return checkBox;
    }

    /**
     * 创建现代化滚动面板
     */
    public static JScrollPane createModernScrollPane(JComponent view) {
        JScrollPane scrollPane = new JScrollPane(view) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BACKGROUND_LIGHT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        scrollPane.setBackground(BACKGROUND_LIGHT);
        scrollPane.setBorder(BorderFactory.createLineBorder(INPUT_BORDER, 1, true));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // 自定义滚动条
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, 0));
        verticalScrollBar.setBackground(INPUT_BG);
        verticalScrollBar.setUnitIncrement(16);

        return scrollPane;
    }

    /**
     * 创建进度条
     */
    public static JProgressBar createModernProgressBar() {
        JProgressBar progressBar = new JProgressBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 背景
                g2.setColor(INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                // 进度
                int progress = (int) (getPercentComplete() * getWidth());
                if (progress > 0) {
                    GradientPaint gradient = new GradientPaint(
                        0, 0, ACCENT_PURPLE, progress, 0, ACCENT_BLUE);
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, progress, getHeight(), 6, 6);
                }

                g2.dispose();
            }
        };

        progressBar.setBackground(INPUT_BG);
        progressBar.setForeground(ACCENT_BLUE);
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);
        progressBar.setStringPainted(false);
        return progressBar;
    }

    /**
     * 为按钮添加强调悬停效果
     */
    public static void addButtonHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.repaint();
            }
        });
    }

    /**
     * 圆角边框内部类
     */
    static class RoundedCornerBorder implements Border {
        private Color color;
        private int thickness;
        private int radius;

        RoundedCornerBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
