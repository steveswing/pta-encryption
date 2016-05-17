
You may need to replace limited strength JCE policy files with their unlimited strength counterparts in %JAVA_HOME%\jre\lib\security:
 
local_policy.jar
US_export_policy.jar

You can find the unlimited strength jurisdiction policy files for JDK 7 here: http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip 
