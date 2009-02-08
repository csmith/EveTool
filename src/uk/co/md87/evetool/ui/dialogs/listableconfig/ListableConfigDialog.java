/*
 * Copyright (c) 2009 Chris Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.co.md87.evetool.ui.dialogs.listableconfig;

import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import uk.co.md87.evetool.ui.components.ListablePanel;
import uk.co.md87.evetool.ui.listable.Listable;
import uk.co.md87.evetool.ui.listable.ListableConfig;
import uk.co.md87.evetool.ui.listable.ListableConfig.BasicConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.CompoundConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.ConfigElement;
import uk.co.md87.evetool.ui.listable.ListableConfig.LiteralConfigElement;
import uk.co.md87.evetool.ui.listable.ListableParser;

/**
 *
 * TODO: Document ListableConfigDialog
 * @author chris
 */
public class ListableConfigDialog extends JDialog implements ActionListener,
        ItemListener, KeyListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 10;

    private final ListableConfig config;
    private final Listable sample;
    private final ListableParser parser;

    private final Set<String> retrievables;
    private final Map<String, List<JComponent>> components
            = new HashMap<String, List<JComponent>>();

    private final JPanel configPanel, previewPanel;
    private final ListablePanel panel;

    public ListableConfigDialog(final Window owner, final ListableConfig config,
            final Listable sample) {
        super(owner, "Display Configuration", ModalityType.APPLICATION_MODAL);

        setLayout(new MigLayout("wrap 1, fill", "[fill]", "[|fill|fill]"));

        this.config = config;
        this.sample = sample;
        this.parser = new ListableParser(sample.getClass());
        
        this.retrievables = parser.getRetrievableNames();
        
        this.configPanel = new JPanel(new MigLayout("fill", "[fill]"));
        this.previewPanel = new JPanel(new MigLayout("fill", "[fill]", "[fill]"));
        
        configPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));

        this.panel = new ListablePanel(sample, parser, config);
        
        previewPanel.add(panel);

        add(configPanel);
        add(previewPanel);

        initConfigPanel();
        layoutConfigPanel();

        pack();

        setLocationRelativeTo(owner);
        setResizable(false);
    }

    protected void initConfigPanel() {
        components.put("tl", getComponents(config.topLeft));
        components.put("tr", getComponents(config.topRight));
        components.put("bl", getComponents(config.bottomLeft));
        components.put("br", getComponents(config.bottomRight));
    }

    protected void layoutConfigPanel() {
        configPanel.removeAll();
        
        JButton addButton = new JButton("+");
        addButton.addActionListener(this);

        configPanel.add(new JLabel("Top left:", JLabel.RIGHT));
        addComponents("tl");
        configPanel.add(addButton);

        addButton = new JButton("+");
        addButton.addActionListener(this);
        configPanel.add(new JLabel("Bottom left:", JLabel.RIGHT), "newline");
        addComponents("bl");
        configPanel.add(addButton);

        addButton = new JButton("+");
        addButton.addActionListener(this);
        configPanel.add(new JLabel("Top right:", JLabel.RIGHT), "newline");
        addComponents("tr");
        configPanel.add(addButton);

        addButton = new JButton("+");
        addButton.addActionListener(this);
        configPanel.add(new JLabel("Bottom right:", JLabel.RIGHT), "newline");
        addComponents("br");
        configPanel.add(addButton);
    }

    protected void addComponents(final String location) {
        String firstText = ", split " + components.get(location).size();
        for (JComponent component : components.get(location)) {
            final int compWidth = component instanceof JComboBox ? 150 : 100;
            configPanel.add(component, "growy, width " + compWidth + "!" + firstText);
            firstText = "";
        }
    }

    protected List<JComponent> getComponents(final ConfigElement element) {
        final List<JComponent> res = new ArrayList<JComponent>();

        if (element instanceof LiteralConfigElement) {
            final JTextField tf = new JTextField(((LiteralConfigElement) element).getText());
            tf.addKeyListener(this);
            res.add(tf);
        } else if (element instanceof BasicConfigElement) {
            final JComboBox box = new JComboBox(retrievables.toArray());
            box.addItemListener(this);
            box.setSelectedItem(((BasicConfigElement) element).getName());
            res.add(box);
        } else {
            for (ConfigElement subElement : ((CompoundConfigElement) element).getElements()) {
                res.addAll(getComponents(subElement));
            }
        }

        return res;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            final JPopupMenu menu = new JPopupMenu();

            JMenuItem mi = new JMenuItem("New string");
            mi.addActionListener(this);
            menu.add(mi);

            mi = new JMenuItem("New variable");
            mi.addActionListener(this);
            menu.add(mi);

            menu.show((JComponent) e.getSource(), 0,
                    ((JComponent) e.getSource()).getHeight());
        } else if (e.getSource() instanceof JMenuItem) {
            
        }
    }

    protected void rebuildConfig() {
        for (String loc : new String[]{"tl", "tr", "bl", "br"}) {
            final List<ConfigElement> elements = new ArrayList<ConfigElement>();

            if (!components.containsKey(loc)) {
                continue;
            }

            for (JComponent component : components.get(loc)) {
                if (component instanceof JTextField) {
                    elements.add(new LiteralConfigElement(((JTextField) component).getText()));
                } else if (component instanceof JComboBox) {
                    elements.add(new BasicConfigElement((String)
                            ((JComboBox) component).getSelectedItem()));
                }
            }

            final ConfigElement res = new CompoundConfigElement(
                    elements.toArray(new ConfigElement[0]));

            if (loc.equals("tl")) {
                config.topLeft = res;
            } else if (loc.equals("tr")) {
                config.topRight = res;
            } else if (loc.equals("br")) {
                config.bottomRight = res;
            } else if (loc.equals("bl")) {
                config.bottomLeft = res;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void itemStateChanged(final ItemEvent e) {
        rebuildConfig();
        panel.listableUpdated(sample);
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(final KeyEvent e) {
        rebuildConfig();
        panel.listableUpdated(sample);
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(final KeyEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(final KeyEvent e) {
        // Do nothing
    }

}
