/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.spyhunter99.findbugs.report.plugin;

import generated.BugCollection;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * This is our maven reporting plugin
 *
 * @goal findbugs-aggregate
 * @phase site
 */
public class FindbugsReport extends AbstractMavenReport {

    /**
     * The vm line separator
     */
    private static final String EOL = System.getProperty("line.separator");

    /**
     * @param locales the list of locales dir to exclude
     * @param defaultLocale the default locale.
     * @return the comma separated list of default excludes and locales dir.
     * @see FileUtils#getDefaultExcludesAsString()
     * @since 1.1
     */
    private static String getDefaultExcludesWithLocales(List<Locale> locales, Locale defaultLocale) {
        String excludesLocales = FileUtils.getDefaultExcludesAsString();
        for (final Locale locale : locales) {
            if (!locale.getLanguage().equals(defaultLocale.getLanguage())) {
                excludesLocales = excludesLocales + ",**/" + locale.getLanguage() + "/*";
            }
        }

        return excludesLocales;
    }

    /**
     * The default locale.
     */
    private Locale defaultLocale;

    /**
     * The available locales list.
     */
    private List<Locale> localesList;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * A comma separated list of locales supported by Maven. The first valid
     * token will be the default Locale for this instance of the Java Virtual
     * Machine.
     *
     * @parameter expression="${locales}"
     * @readonly
     */
    @Parameter(property = "locales")
    private String locales;

    /**
     * Site renderer.
     *
     * @component
     */
    @Component
    private Renderer siteRenderer;

    /**
     * SiteTool.
     *
     * @component
     *
     */
    @Component
    private SiteTool siteTool;
    /**
     * Directory containing source for apt, fml and xdoc docs.
     *
     * @parameter expression="${basedir}/src/site"
     */
    @Parameter(defaultValue = "${basedir}/src/site", required = true)
    private File siteDirectory;

    /**
     * Directory containing generated sources for apt, fml and xdoc docs.
     *
     * @parameter expression="${project.build.directory}/generated-site"
     * @since 1.1
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-site", required = true)
    private File generatedSiteDirectory;

    /**
     * The temp Site dir to have all site and generated-site files.
     *
     * @since 1.1
     */
    private File siteDirectoryTmp;

    /**
     * The Maven Settings.
     *
     * @parameter expression="${settings}"
     * @since 1.1
     */
    @Parameter(defaultValue = "${settings}", readonly = true, required = true)
    private Settings settings;
    /**
     * The current version of this plugin.
     *
     * @parameter expression="${plugin.version}"
     */
    @Parameter(defaultValue = "${plugin.version}", readonly = true)
    private String pluginVersion;

    @Override
    protected MavenProject getProject() {
        return project;
    }

    // Not used by Maven site plugin but required by API!
    @Override
    protected Renderer getSiteRenderer() {
        return null; // Nobody calls this!
    }

    // Not used by Maven site plugin but required by API!
    // (The site plugin is only calling getOutputName(), the output dir is fixed!)
    @Override
    protected String getOutputDirectory() {
        return null; // Nobody calls this!
    }

    // Abused by Maven site plugin, a '/' denotes a directory path!
    public String getOutputName() {

        //String path = "someDirectoryInTargetSite/canHaveSubdirectory/OrMore";
        String outputFilename = "findbugs-aggregate";

        // The site plugin will make the directory (and regognize the '/') in the path,
        // it will also append '.html' onto the filename and there is nothing (yes, I tried)
        // you can do about that. Feast your eyes on the code that instantiates
        // this (good luck finding it):
        // org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext
        return //path + "/" +
                outputFilename;
    }

    public String getName(Locale locale) {
        return "Findbugs Warnings";
    }

    public String getDescription(Locale locale) {
        return "An aggregated Findbugs report";
    }

    @Override
    public boolean canGenerateReport() {
        return (project.isExecutionRoot());
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        //TODO current issue, if you run
        //mvn clean install
        //then mvn site (with findbugs as a report plugin)
        //findbugs will only run on the child modules, but it's AFTER the root project's
        //site stuff is done.

        //work around
        //mvn clean install
        //mvn findbugs:fingbugs
        //mvn site
        //other possible solutions add shutdown hook? a hack but it might work.
        try {

            List<FindbugsItem> jacocoReports = copyResources(project);

            Set<FindbugsItem> set = new HashSet<>();
            set.addAll(jacocoReports);
            Sink sink = getSink();

            //simple table, module | coverage metric?
            sink.head();
            sink.title();       //html/head/title
            sink.text(getName(locale));
            sink.title_();
            sink.head_();
            sink.body();

            sink.section1();    //div
            sink.sectionTitle1();
            sink.rawText(project.getName() + " - Findbugs Aggregated Report");
            sink.sectionTitle1_();

            sink.section2();    //div
            sink.sectionTitle2();
            sink.rawText("Findbugs Warnings");
            sink.sectionTitle2_();

            sink.paragraph();
            sink.table();
            sink.tableRow();
            sink.tableHeaderCell();
            sink.rawText("Module");
            sink.tableHeaderCell_();

            sink.tableHeaderCell();
            sink.rawText("Classes");
            sink.tableHeaderCell_();
            
             sink.tableHeaderCell();
            sink.rawText("Missing Classes");
            sink.tableHeaderCell_();


            sink.tableHeaderCell();
            sink.rawText("High");
            sink.tableHeaderCell_();

            sink.tableHeaderCell();
            sink.rawText("Medium");
            sink.tableHeaderCell_();

            sink.tableHeaderCell();
            sink.rawText("Low");
            sink.tableHeaderCell_();

            sink.tableHeaderCell();
            sink.rawText("Total Bugs");
            sink.tableHeaderCell_();

           
           
            sink.tableRow_();

            boolean success = false;
            //for reach module
            long totalBugs = 0;
            long totalBugsP1 = 0;
            long totalBugsP2 = 0;
            long totalBugsP3 = 0;
            long totalMissingClasses = 0;
            long totalClasses = 0;

            Iterator<FindbugsItem> iterator = set.iterator();
            while (iterator.hasNext()) {
                FindbugsItem next = iterator.next();
                if (next.getReportDirs().isEmpty()) {

                } else {
                    success = true;
                    for (int k = 0; k < next.getReportDirs().size(); k++) {

                        totalMissingClasses += next.getReportDirs().get(k).getMissingClasses();
                        totalClasses += next.getReportDirs().get(k).getClasses();
                        totalBugsP3 += next.getReportDirs().get(k).getBugsP3();
                        totalBugsP2 += next.getReportDirs().get(k).getBugsP2();
                        totalBugsP1 += next.getReportDirs().get(k).getBugsP1();
                        totalBugs += next.getReportDirs().get(k).getBugs();

                        sink.tableRow();
                        sink.tableCell();
                        sink.link(next.getModuleName() + "/findbugs.html");
                        sink.rawText(next.getModuleName());
                        sink.link_();
                        sink.tableCell_();

                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getClasses() + "");
                        sink.tableCell_();

                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getMissingClasses() + "");
                        sink.tableCell_();
                        
                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getBugsP1() + "");
                        sink.tableCell_();

                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getBugsP2() + "");
                        sink.tableCell_();

                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getBugsP3() + "");
                        sink.tableCell_();

                        sink.tableCell();
                        sink.rawText(next.getReportDirs().get(k).getBugs() + "");
                        sink.tableCell_();

                    

                        sink.tableRow_();
                    }
                }
            }
            if (success) {
                sink.tableRow();
                sink.tableCell();
                sink.rawText("Total");
                sink.link_();
                sink.tableCell_();

                sink.tableCell();
                sink.rawText(totalClasses + "");
                sink.tableCell_();
                
                
                sink.tableCell();
                sink.rawText(totalMissingClasses + "");
                sink.tableCell_();


                sink.tableCell();
                sink.rawText(totalBugsP1 + "");
                sink.tableCell_();

                sink.tableCell();
                sink.rawText(totalBugsP2 + "");
                sink.tableCell_();

                sink.tableCell();
                sink.rawText(totalBugsP3 + "");
                sink.tableCell_();

                sink.tableCell();
                sink.rawText(totalBugs + "");
                sink.tableCell_();

                sink.tableRow_();
            }

            sink.table_();

            if (!success) {
                sink.rawText("No findbugsXml.xml reports found. Did you run findbugs before mvn site?");
            }
            sink.paragraph_();
            sink.section2_();    //div
            sink.section1_();

        } catch (IOException ex) {
            Logger.getLogger(FindbugsReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the default locale from <code>siteTool</code>.
     * @see #getAvailableLocales()
     */
    private Locale getDefaultLocale() {
        if (this.defaultLocale == null) {
            this.defaultLocale = getAvailableLocales().get(0);
        }

        return this.defaultLocale;
    }

    /**
     * @return the available locales from <code>siteTool</code>.
     * @see SiteTool#getAvailableLocales(String)
     */
    private List<Locale> getAvailableLocales() {
        if (this.localesList == null) {
            if (siteTool == null) {
                List<Locale> r = new ArrayList<>();
                r.add(Locale.US);
                this.localesList = r;
            } else {
                this.localesList = siteTool.getSiteLocales(locales);
            }
        }

        return this.localesList;
    }

    private List<FindbugsItem> copyResources(MavenProject project) throws IOException {
        if (project == null) {
            return Collections.EMPTY_LIST;
        }
        List<FindbugsItem> outDirs = new ArrayList<>();

        if ("pom".equalsIgnoreCase(project.getPackaging())) {
            for (int k = 0; k < project.getCollectedProjects().size(); k++) {
                outDirs.addAll(copyResources((MavenProject) project.getCollectedProjects().get(k)));
            }
        } else {
            File moduleBaseDir = project.getBasedir();
            File target = new File(moduleBaseDir, "target");
            if (target.exists()) {
                //FIXME propertize this
                File findbugsXml = new File(moduleBaseDir, "target/findbugsXml.xml");

                FindbugsItem item = new FindbugsItem();
                item.setModuleName(project.getArtifactId());

                if (findbugsXml.exists()) {
                    //since all artifacts should have unique names...this should be ok
                    FindbugsReportMetric report = getMetric(findbugsXml);
                    if (report != null) {
                        item.getReportDirs().add(report);
                    }
                }

                outDirs.add(item);

            }
        }

        return outDirs;
    }

    private FindbugsReportMetric getMetric(File string) throws IOException {
        FindbugsReportMetric metrics = new FindbugsReportMetric();
        metrics.setReportDir(string);

        BugCollection bugs = JAXB.unmarshal(string, BugCollection.class);
        if (bugs.getErrors() != null && bugs.getErrors().getMissingClasses() != null) {
            metrics.setMissingClasses(bugs.getErrors().getMissingClasses());
        }

        metrics.setBugs(bugs.getBugInstance().size());
        if (bugs.getErrors() != null && bugs.getErrors().getErrors() != null) {
            metrics.setErrors(bugs.getErrors().getErrors());
        }
        if (bugs.getFindBugsSummary().getPriority1() != null) {
            metrics.setBugsP1(bugs.getFindBugsSummary().getPriority1());
        }
        if (bugs.getFindBugsSummary().getPriority2() != null) {
            metrics.setBugsP2(bugs.getFindBugsSummary().getPriority2());
        }
        if (bugs.getFindBugsSummary().getPriority3() != null) {
            metrics.setBugsP3(bugs.getFindBugsSummary().getPriority3());
        }
        metrics.setBugs(bugs.getFindBugsSummary().getTotalBugs());
        metrics.setClasses(bugs.getFindBugsSummary().getTotalClasses());
        return metrics;

    }
}
