package GUI;

import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class WizardFrame extends javax.swing.JFrame {

    private ArrayList<WizardPanel> m_panels = new ArrayList<>();
    private int m_currentPanelIndex = -1;
    
    /**
     * Creates new form WizardFrame
     */
    public WizardFrame() {
        initComponents();
    }
    
    public void addPanel(WizardPanel panel) {
        panel.setParent(this);
        m_panels.add(panel);
    }
    
    public void addPanel(WizardPanel panel, int index) {
        panel.setParent(this);
        m_panels.add(index, panel);
    }
    
    public int getPanelCount() {
        return m_panels.size();
    }
    
    public void next() {
        if (m_currentPanelIndex < m_panels.size() - 1) {
            m_currentPanelIndex++;
            setPanel();
        }
    }
    
    public void previous() {
        if (m_currentPanelIndex > 0) {
            m_currentPanelIndex--;
            setPanel();
        }
    }
    
    public int getCurrentPanelIndex() {
        return m_currentPanelIndex;
    }
    
    public void setCurrentPanelIndex(int index) {
        if (index >= m_panels.size())
            m_currentPanelIndex = m_panels.size() - 1;
        else if (index < 0)
            m_currentPanelIndex = 0;
        else
            m_currentPanelIndex = index;
        
        setPanel();
    }
    
    private void setPanel() {
        contentPanel.removeAll();
        contentPanel.add(m_panels.get(m_currentPanelIndex));
        
        if (m_currentPanelIndex == m_panels.size() - 1) {
            nextButton.setEnabled(false);
            backButton.setEnabled(true);
        }
        else if (m_currentPanelIndex == 0) {
            nextButton.setEnabled(true);
            backButton.setEnabled(false);
        }
        else {
            nextButton.setEnabled(true);
            backButton.setEnabled(true);
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        nextButton.setText("Next >");

        backButton.setText("< Back");

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 244, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 242, Short.MAX_VALUE)
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(backButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton nextButton;
    // End of variables declaration//GEN-END:variables
}
