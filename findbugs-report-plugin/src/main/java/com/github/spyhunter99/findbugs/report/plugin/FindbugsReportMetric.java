/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.spyhunter99.findbugs.report.plugin;

import java.io.File;

/**
 *
 * @author AO
 */
public class FindbugsReportMetric {

    private File reportDir;
    private long bugsP1=0,bugsP2=0, bugsP3=0;
    private long classes=0;
    private long bugs=0;
    private long errors=0;
    private long missingClasses=0;
    
    public File getReportDir() {
        return reportDir;
    }

    public void setReportDir(File reportDir) {
        this.reportDir = reportDir;
    }

    public long getClasses() {
        return classes;
    }

    public void setClasses(long classes) {
        this.classes = classes;
    }

    public long getBugs() {
        return bugs;
    }

    public void setBugs(long bugs) {
        this.bugs = bugs;
    }

    public long getErrors() {
        return errors;
    }

    public void setErrors(long errors) {
        this.errors = errors;
    }

    public long getMissingClasses() {
        return missingClasses;
    }

    public void setMissingClasses(long missingClasses) {
        this.missingClasses = missingClasses;
    }

        public long getBugsP1() {
                return bugsP1;
        }

        public void setBugsP1(long bugsP1) {
                this.bugsP1 = bugsP1;
        }

        public long getBugsP2() {
                return bugsP2;
        }

        public void setBugsP2(long bugsP2) {
                this.bugsP2 = bugsP2;
        }

        public long getBugsP3() {
                return bugsP3;
        }

        public void setBugsP3(long bugsP3) {
                this.bugsP3 = bugsP3;
        }

}
