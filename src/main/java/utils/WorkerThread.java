package utils;

import armorsetsearch.ArmorSearchWrapper;
import armorsetsearch.filter.ArmorSetFilter;
import armorsetsearch.skillactivation.SkillActivationRequirement;
import interfaces.OnSearchResultProgress;
import java.util.List;
import models.GeneratedArmorSet;

/**
 * Do the search in a thread, to avoid blocking the UI thread
 */
public class WorkerThread extends Thread {
    private OnSearchResultProgress onSearchResultProgress;
    private ArmorSearchWrapper armorSearchWrapper;
    private List<SkillActivationRequirement> desiredSkills;
    private final int uniqueSetSearchLimit;
    private final int decorationSearchLimit;
    private List<ArmorSetFilter> armorSetFilters;

    private boolean stop = false;

    public WorkerThread(OnSearchResultProgress onSearchResultProgress, ArmorSearchWrapper armorSearchWrapper, List<SkillActivationRequirement> desiredSkills, int uniqueSetSearchLimit, int decorationSearchLimit, List<ArmorSetFilter> armorSetFilters) {
        this.onSearchResultProgress = onSearchResultProgress;
        this.armorSearchWrapper = armorSearchWrapper;
        this.desiredSkills = desiredSkills;
        this.uniqueSetSearchLimit = uniqueSetSearchLimit;
        this.decorationSearchLimit = decorationSearchLimit;
        this.armorSetFilters = armorSetFilters;
    }

    @Override
    public void run() {
        List<GeneratedArmorSet> generatedArmorSets = armorSearchWrapper.search(armorSetFilters, desiredSkills, uniqueSetSearchLimit, decorationSearchLimit, onSearchResultProgress);
        onSearchResultProgress.onComplete(generatedArmorSets);

    }
}
