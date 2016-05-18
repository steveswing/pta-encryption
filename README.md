This code relates to this forum post: http://communities.rightnow.com/posts/97d6f47718

The post describes the values I initially attempted. I have changed to "simpler" values with this example to try to get something working.

This example uses JDK built-in cryptographic functions unlike the forum post where I was using JSafeJCE provider shipping with Weblogic 12.1.2.0.0.

Use maven 3.3.3+
JDK7+

`mvn clean test`

`mvn jetty:run` (Requires JDK 8)

In the browser: http://localhost:8080/

`curl http://localhost:8080/api/chat/pta-token`

Expected output:

`{"data":"u95~yRnxx9_rep1jDYr0dq4ADUkRSAI59dp_BmwffJ3iaLqDNp1O5dxHXheqKb7rd_LeJFy~ORleo1tFzSe~yKVQJ9uSfVR7QtzgFz67EsLKKCEhziN4sLqywoeDsPIXaY_1aVSar~5Sd4PHF2agLw**"}`

When running If you encounter...

    java.security.InvalidKeyException: Illegal key size

	at javax.crypto.Cipher.checkCryptoPerm(Cipher.java:1039)
	at javax.crypto.Cipher.implInit(Cipher.java:805)
	at javax.crypto.Cipher.chooseProvider(Cipher.java:864)
	at javax.crypto.Cipher.init(Cipher.java:1396)
	at javax.crypto.Cipher.init(Cipher.java:1327)

You need to replace limited strength JCE policy files with their unlimited strength counterparts in %JAVA_HOME%\jre\lib\security:
 
local_policy.jar
US_export_policy.jar

You can find the unlimited strength jurisdiction policy files for JDK 7 here: http://download.oracle.com/otn-pub/java/jce/7/UnlimitedJCEPolicyJDK7.zip 
For JDK 8 here: http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip

### Current RightNow Settings

| RightNow Configuration Key | Configuration Value |
| --- | --- |
| `PTA_ENABLED`| Yes |
| `PTA_ENCRYPTION_KEYGEN`| 3 |
| `PTA_ENCRYPTION_METHOD`| aes128 |
| `PTA_ENCRYPTION_PADDING`| 1 |
| `PTA_ERROR_URL`| http://localhost:8080/error_code/%error_code%/error/%error% |
| `PTA_EXTERNAL_LOGIN_URL`| |
| `PTA_EXTERNAL_LOGOUT_SCRIPT_URL`| |
| `PTA_EXTERNAL_POST_LOGOUT_URL`| |
| `PTA_IGNORE_CONTACT_PASSWORD`| Yes |
| `PTA_SECRET_KEY`| `jm2OqbUO5iQSt/mcR/3MPwSYwg4ERB2g` |
| `PTA_ENCRYPTION_IV`| |
| `PTA_ENCRYPTION_SALT`| |

For `PTA_ENCRYPTION_KEYGEN` I would prefer to use `2 - RSSL_KEYGEN_PKCS5_V20`. However, from Java's perspective this is a whole family of key generation
options. See forum post above.

RightNow Current Version: February 2016 SP2 (Build 257 - 04/24/16 18:36)
