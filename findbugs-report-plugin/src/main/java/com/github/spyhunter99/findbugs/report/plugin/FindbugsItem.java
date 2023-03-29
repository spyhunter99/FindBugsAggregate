/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.spyhunter99.findbugs.report.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author AO
 */
public class FindbugsItem {

    private boolean findbugs = false;

    public FindbugsItem(boolean isFindbugs) {
        findbugs = isFindbugs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.moduleName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FindbugsItem other = (FindbugsItem) obj;
        if (!Objects.equals(this.moduleName, other.moduleName)) {
            return false;
        }
        return true;
    }

    private String moduleName;
    private List<FindbugsReportMetric> reportDirs = new ArrayList<FindbugsReportMetric>();

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<FindbugsReportMetric> getReportDirs() {
        return reportDirs;
    }

    public void setReportDirs(List<FindbugsReportMetric> reportDirs) {
        this.reportDirs = reportDirs;
    }

    public boolean isFindBugs() {
        return findbugs;
    }
}
