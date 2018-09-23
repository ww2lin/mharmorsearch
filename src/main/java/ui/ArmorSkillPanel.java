package ui;

import armorsetsearch.skillactivation.SkillActivationRequirement;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ArmorSkillPanel extends JPanel{

    private static final int LIST_SIZE = 200;
    private JList<SkillActivationRequirement> skillActivationRequirementJList;
    private List<SkillActivationRequirement> modelList;

    public ArmorSkillPanel(List<SkillActivationRequirement> listData) {
        super();
        modelList = listData;
        skillActivationRequirementJList = new JList<>(new Vector<>(modelList));
        skillActivationRequirementJList.setCellRenderer(new SkillListRender());
        JScrollPane scrollPane = new JScrollPane(skillActivationRequirementJList);
        scrollPane.setPreferredSize(new Dimension(LIST_SIZE, LIST_SIZE * 2));
        add(scrollPane);

    }

    private static class SkillListRender extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (renderer instanceof JLabel && value instanceof SkillActivationRequirement) {
                ((JLabel) renderer).setText(((SkillActivationRequirement) value).getDisplayText());
            }
            return renderer;
        }
    }

    public List<SkillActivationRequirement> getSelectedValues(){
        return skillActivationRequirementJList.getSelectedValuesList();
    }

    public List<SkillActivationRequirement> getAll(){
        return modelList;
    }

    public void reset(List<SkillActivationRequirement> skillActivationRequirements){
        modelList = skillActivationRequirements;
        skillActivationRequirementJList.setListData(new Vector<>(modelList));
    }

    public void add(List<SkillActivationRequirement> skillActivationRequirements){
        skillActivationRequirements.forEach(skillActivationRequirement -> {
            boolean hasSameKindSkill = false;
            for (SkillActivationRequirement activationRequirement : modelList) {
                if (skillActivationRequirement.getKind().equalsIgnoreCase(activationRequirement.getKind())) {
                    hasSameKindSkill = true;
                }
            }
            if (!hasSameKindSkill) {
                modelList.add(skillActivationRequirement);
            }
        });
        skillActivationRequirementJList.setListData(new Vector<>(modelList));
    }

    public void remove(){
        List<SkillActivationRequirement> filterOut = getSelectedValues();
        modelList = modelList.stream().filter(skillActivationRequirement -> !filterOut.contains(skillActivationRequirement)).collect(Collectors.toList());
        skillActivationRequirementJList.setListData(new Vector<>(modelList));
    }

    public void removeAll(){
        modelList.clear();
        skillActivationRequirementJList.setListData(new Vector<>(modelList));
    }

}
