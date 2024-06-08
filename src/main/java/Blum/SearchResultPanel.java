package Blum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

public class SearchResultPanel extends JPanel {
    private static final int BUTTON_SIZE = 150;
    private static final int BUTTON_GAP = 20;

    private MainFrame mainFrame;
    private List<Product> searchResults;

    public SearchResultPanel(MainFrame mainFrame, List<Product> searchResults) {
        this.mainFrame = mainFrame;
        this.searchResults = searchResults;
        setLayout(new BorderLayout());

        if (searchResults != null && !searchResults.isEmpty()) {
            // 검색 결과를 상품 버튼으로 표시
            JPanel resultPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP, BUTTON_GAP);

            for (Product product : searchResults) {
                String imagePath = product.getImage1();
                if (imagePath != null && !imagePath.isEmpty()) {
                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl != null) {
                        ImageIcon imageIcon = new ImageIcon(imageUrl);
                        Image image = imageIcon.getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH);
                        JButton productButton = new JButton(new ImageIcon(image));
                        productButton.setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
                        productButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainFrame.showProductInfoPanel(product.getProductId());
                            }
                        });
                        resultPanel.add(productButton, gbc);
                        gbc.gridx++;
                        if (gbc.gridx == 3) {
                            gbc.gridx = 0;
                            gbc.gridy++;
                        }
                    }
                }
            }

            JScrollPane scrollPane = new JScrollPane(resultPanel);
            add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel emptyLabel = new JLabel("검색 결과가 없습니다.");
            emptyLabel.setHorizontalAlignment(JLabel.CENTER);
            add(emptyLabel, BorderLayout.CENTER);
        }

        // 뒤로 가기 버튼 추가
        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainPanel();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.NORTH);
    }
}