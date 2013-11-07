/*
* Copyright 2013 by the digital.me project (http:\\www.dime-project.eu).
*
* Licensed under the EUPL, Version 1.1 only (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.dime.view;

import eu.dime.model.Model;
import eu.dime.model.displayable.ProfileAttributeItem;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author simon
 */
public class ProfileAttrEditor extends javax.swing.JDialog {

    private final ProfileAttributeItem profileAttr;
    private final ModelViewer mv;
    private final Map<JLabel, JTextField> valueFields = new HashMap();
    private DefaultComboBoxModel<ProfileAttributeItem.VALUE_CATEGORIES> categoryListModel = null;
    

    /**
     * Creates new form ProfileAttrEditor
     */
    public ProfileAttrEditor(ModelViewer parent, boolean modal, ProfileAttributeItem profileAttr) {
        super(parent, modal);
        this.mv = parent;
        this.profileAttr = profileAttr;

        initComponents();
        initCategorySelector();
        showProfileAttribute();
    }

    private void initCategorySelector() {
        categoryListModel = new DefaultComboBoxModel<ProfileAttributeItem.VALUE_CATEGORIES>(ProfileAttributeItem.VALUE_CATEGORIES.values());

        categorySelectCombo.setModel(categoryListModel);
        
        //set selected category
        ProfileAttributeItem.VALUE_CATEGORIES categoryType = profileAttr.getCategoryType();
        
        if (categoryType!=null){        
            categorySelectCombo.setSelectedItem(categoryType.toString());
        }
    }

    private void setSize(JComponent component, int width) {
        Dimension mySize = new Dimension(width, 10);
        component.setMaximumSize(mySize);
        component.setMinimumSize(mySize);
        component.setPreferredSize(mySize);
        component.setSize(mySize);
    }

    private void showProfileAttribute() {
        fieldPanel.removeAll();
        valueFields.clear();

        Map<String, String> values = profileAttr.getValue();

        GridLayout myLayout = new GridLayout(values.size(), 2, 5, 5);


        fieldPanel.setLayout(myLayout);

        int i = 0;
        for (Map.Entry<String, String> entry : values.entrySet()) {


            JLabel myLabel = new JLabel(entry.getKey());
            setSize(myLabel, 100);
            JTextField myTextField = new JTextField(entry.getValue());

            setSize(myTextField,
                    150);

            fieldPanel.add(myLabel);
            fieldPanel.add(myTextField);
            valueFields.put(myLabel, myTextField);
            
            i++;
        }
        fieldPanel.validate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        fieldPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        categorySelectCombo = new javax.swing.JComboBox();
        saveButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        fieldPanel.setLayout(new java.awt.GridLayout());

        jLabel1.setText("Edit ProfileAttribute");

        categorySelectCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categorySelectComboActionPerformed(evt);
            }
        });
        categorySelectCombo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                categorySelectComboPropertyChange(evt);
            }
        });

        saveButton.setText("save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        closeButton.setText("close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(categorySelectCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(fieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(categorySelectCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fieldPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(closeButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void categorySelectComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categorySelectComboActionPerformed
        
        if ((categorySelectCombo.getSelectedIndex() == -1)
                || (categoryListModel == null)) {
            return;
        }

        ProfileAttributeItem.VALUE_CATEGORIES category = categoryListModel.getElementAt(
                categorySelectCombo.getSelectedIndex());

        profileAttr.updateCategoryRelatedFields(category);
        this.showProfileAttribute();
    }//GEN-LAST:event_categorySelectComboActionPerformed

    private void categorySelectComboPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_categorySelectComboPropertyChange
        
    }//GEN-LAST:event_categorySelectComboPropertyChange

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        
        //read textfields
        for (Map.Entry<JLabel, JTextField> entry : valueFields.entrySet()){
            profileAttr.setValueEntry(entry.getKey().getText(), entry.getValue().getText());
        }
        
        
        Model.getInstance().updateItem(mv.getMRC(), profileAttr);
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox categorySelectCombo;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel fieldPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
