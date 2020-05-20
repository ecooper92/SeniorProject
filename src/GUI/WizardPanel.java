/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import javax.swing.JPanel;

/**
 *
 * @author Eric
 */
public class WizardPanel extends JPanel {
    
    private WizardFrame m_Parent = null;
    public WizardFrame getParent() {
        return m_Parent;
    }
    
    public void setParent(WizardFrame frame) {
        m_Parent = frame;
    }
}
