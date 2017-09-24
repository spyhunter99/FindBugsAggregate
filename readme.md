# Findbugs Aggregated Maven Site Report

This plugin (is not perfect) will take Findbugs reports of your maven project, then create a new report with `mvn site`
that lists each module, along with a summary of the results from the analysis.

## Limitations

Maven reporting plugins have no mechanism for depending on the output of another reporting plugin. More over, there's 
no way to specify that this plugin must run after the actual find bugs analysis on all child modules.

Unfortunately, this means you have to run `mvn site` **twice**.


## Usage

Put this in your parent pom file.

````xml
	<project>
	...
	
	  <reporting>
        <plugins>
             <plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>findbugs-maven-plugin</artifactId>
				<version>3.0.5</version>
			  </plugin>
             <plugin>
                <groupId>com.github.spyhunter99</groupId>
                <artifactId>findbugs-report-plugin</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <reportSets>
                    <reportSet>
                        <reports>
                            <report>findbugs-aggregate</report>
                        </reports>
                    </reportSet>
                </reportSets>
            </plugin>
        </plugins>
    </reporting>

````

## Execute it

````
//build your project
mvn clean install
//build the initial site, including the findbugs analysis
mvn site
//build it again, this time the aggregate report is generated
mvn site
//stage the site to ensure that all the relative links are functional
mvn site:stage
````