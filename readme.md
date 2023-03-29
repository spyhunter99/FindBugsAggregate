# Findbugs/Spotbugs Aggregated Maven Site Report

This plugin (is not perfect) will take Findbugs/spotbugs reports of your maven project, then create a new report with `mvn site`
that lists each module, along with a summary of the results from the analysis.

## Limitations

Maven reporting plugins have no mechanism for depending on the output of another reporting plugin. More over, there's 
no way to specify that this plugin must run after the actual find bugs analysis on all child modules.

Unfortunately, this means you have to run `findbugs:findbugs` or `spotbugs:spotbugs` before `mvn site`


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
                <version>1.0.2</version>
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
</project>
````

## Execute it

````
//build your project
mvn clean install
//run findbugs or spotbugs!
mvn findbugs:findbugs
//build the site
mvn site
//stage the site to ensure that all the relative links are functional
mvn site:stage
````