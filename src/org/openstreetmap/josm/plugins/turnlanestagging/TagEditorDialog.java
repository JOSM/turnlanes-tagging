package org.openstreetmap.josm.plugins.turnlanestagging;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.plugins.turnlanestagging.preset.ui.TabularPresetSelector;
import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.ImageProvider;

public class TagEditorDialog extends JDialog {

    /**
     * the unique instance
     */
    static private TagEditorDialog instance = null;

    static public TagEditorDialog getInstance() {
        if (instance == null) {
            instance = new TagEditorDialog();
        }
        return instance;
    }
    static public final Dimension PREFERRED_SIZE = new Dimension(700, 500);

    //constructor
    protected TagEditorDialog() {
        build();
    }

    private OKAction okAction = null;
    private CancelAction cancelAction = null;

    protected void build() {

        getContentPane().setLayout(new BorderLayout());

        // basic UI prpoperties
        setModal(true);
        setSize(PREFERRED_SIZE);
        setTitle(tr("Turn Lanes Editor"));

        // ===================Panel de Selector preestablecido================
//              TabularPresetSelector presetSelector = new TabularPresetSelector();
//                presetSelector.addPresetSelectorListener(
//                        new IPresetSelectorListener() {
//                            public void itemSelected(TaggingPreset item) {
//                                tagEditor.stopEditing();
//                                tagEditor.getModel().applyPreset(item);
//                                tagEditor.requestFocusInTopLeftCell();
//                            }
//                        }
//                );
//        
//        
        JPanel pnlPresetSelector = new TabularPresetSelector();

        pnlPresetSelector.setLayout(new BorderLayout());
//               pnlPresetSelector.add(presetSelector, BorderLayout.CENTER);               

        pnlPresetSelector.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // ==================Panel de Tags================================
        JPanel pnlTagGrid = new JPanel();
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                pnlPresetSelector,
                pnlTagGrid
        );

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(buildButtonRow(), BorderLayout.SOUTH);
    }

    // ==================Build Buttons================================
    protected JPanel buildButtonRow() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnl.add(new JButton(okAction = new OKAction()));
        // getModel().addPropertyChangeListener(okAction);
        pnl.add(new JButton(cancelAction = new CancelAction()));
        return pnl;
    }

    class CancelAction extends AbstractAction {

        public CancelAction() {
            putValue(NAME, tr("Cancel"));
            putValue(SMALL_ICON, ImageProvider.get("cancel"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(SHORT_DESCRIPTION, tr("Abort tag editing and close dialog"));
        }

        public void actionPerformed(ActionEvent arg0) {
            setVisible(false);
        }
    }

    class OKAction extends AbstractAction implements PropertyChangeListener {

        public OKAction() {
            putValue(NAME, tr("OK"));
            putValue(SMALL_ICON, ImageProvider.get("ok"));
            putValue(SHORT_DESCRIPTION, tr("Apply edited tags and close dialog"));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl ENTER"));
        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showConfirmDialog(null, "Aceptar");
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
