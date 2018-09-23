package utils;

import armorsetsearch.skillactivation.SkillActivationRequirement;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import models.CharmData;
import models.Decoration;
import models.Equipment;
import models.EquipmentType;

/**
 * This is a mess, have to clean this up at some point.
 */
public class CsvReader {
    public static List<Equipment> getEquipmentFromCsvFile(String path, EquipmentType equipmentType) {
        CSVReader reader = null;
        try {
            int id = 0;
            List<Equipment> lst = new ArrayList<>();
            reader = new CSVReader(new FileReader(path));
            String[] nextLine;

            // skip over the header
            reader.readNext();

            // go over the CSV file line by line.
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Equipment equipment = CsvToModel.csvEquipmentRowToModel(nextLine, equipmentType);
                equipment.setId(id++);
                lst.add(equipment);
            }
            return lst;
        } catch (IOException e) {
            return Collections.emptyList();
        } finally {

            // Messy steps to clean up the file reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, List<SkillActivationRequirement>> getSkillActivationRequirementFromCsvFile(String path) {
        CSVReader reader = null;
        try {
            Map<String, List<SkillActivationRequirement>> skillActivationChart = new LinkedHashMap<>();
            reader = new CSVReader(new FileReader(path));
            int id = 0;
            String[] nextLine;

            // skip over the header
            reader.readNext();

            // go over the CSV file line by line.
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                SkillActivationRequirement skillActivationRequirement = CsvToModel.csvSkillActivationRequirementRowToModel(nextLine, id++);

                // Check to see if this kind of skill already exists, if so append it to the same list
                String kind = skillActivationRequirement.getKind();

                List<SkillActivationRequirement> skillActivationRequirements = skillActivationChart.get(kind);
                if (skillActivationRequirements == null) {
                    skillActivationRequirements = new LinkedList<>();
                }
                skillActivationRequirements.add(skillActivationRequirement);

                skillActivationChart.put(kind, skillActivationRequirements);

            }
            return skillActivationChart;
        } catch (IOException e) {
            return Collections.emptyMap();
        } finally {
            // Messy steps to clean up the file reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Map<String, List<Decoration>> getDecorationFromCsvFile(String path) {
        CSVReader reader = null;
        try {
            int id = 0;
            Map<String, List<Decoration>> decorationMap = new HashMap<>();
            reader = new CSVReader(new FileReader(path));
            String[] nextLine;

            // skip over the header
            reader.readNext();

            // go over the CSV file line by line.
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                Decoration decoration = CsvToModel.csvDecorationRowToModel(nextLine);
                decoration.setId(id++);
                decoration.getArmorSkills().forEach(armorSkill -> {
                    List<Decoration> decorationList = decorationMap.get(armorSkill.kind);
                    if (decorationList == null) {
                        decorationList = new LinkedList<>();
                    }
                    decorationList.add(decoration);
                    decorationMap.put(armorSkill.kind, decorationList);
                });
            }
            return decorationMap;
        } catch (IOException e) {
            return Collections.emptyMap();
        } finally {
            // Messy steps to clean up the file reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static Map<String, List<CharmData>> getCharmFromCsvFile(String path) {
        CSVReader reader = null;
        try {
            Map<String, List<CharmData>> charmMap = new HashMap<>();
            reader = new CSVReader(new FileReader(path));
            String[] header;
            String[] nextLine1;
            String[] nextLine2;

            // skip over the header
            header = reader.readNext();

            // go over the CSV file line by line.
            while ((nextLine1 = reader.readNext()) != null && (nextLine2 = reader.readNext()) != null) {
                List<CharmData> charmDatas = CsvToModel.csvCharmRowToModel(header, nextLine1, nextLine2);
                String skillKind = nextLine1[0];
                charmMap.put(skillKind, charmDatas);
            }
            return charmMap;
        } catch (IOException e) {
            return Collections.emptyMap();
        } finally {
            // Messy steps to clean up the file reader
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
