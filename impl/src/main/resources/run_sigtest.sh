
# Sample shell script to setup and run the sigtest generation
M2=~/.m2/repository
CDI_VERSION=4.0.0-RC1
ATINJECT_VERSION=2.0.1
EL_VERSON=5.0.0
INTERCEPTOR_VERSION=2.1.0-RC1
SIGTEST_VERSION=1.2

CDI_API_JAR=${M2}/jakarta/enterprise/jakarta.enterprise.cdi-api/${CDI_VERSION}/jakarta.enterprise.cdi-api-${CDI_VERSION}.jar
ATINJECT_API_JAR=${M2}/jakarta/inject/jakarta.inject-api/${ATINJECT_VERSION}/jakarta.inject-api-${ATINJECT_VERSION}.jar
EL_API_JAR=${M2}/jakarta/el/jakarta.el-api/${EL_VERSON}/jakarta.el-api-${EL_VERSON}.jar
INTERCEPTOR_API_JAR=${M2}/jakarta/interceptor/jakarta.interceptor-api/${INTERCEPTOR_VERSION}/jakarta.interceptor-api-${INTERCEPTOR_VERSION}.jar
SIGTEST_JAR=${M2}/org/netbeans/tools/sigtest-maven-plugin/${SIGTEST_VERSION}/sigtest-maven-plugin-${SIGTEST_VERSION}.jar

CLASSPATH="${CLASSPATH}:${CDI_API_JAR}"
CLASSPATH="${CLASSPATH}:${ATINJECT_API_JAR}"
CLASSPATH="${CLASSPATH}:${EL_API_JAR}"
CLASSPATH="${CLASSPATH}:${INTERCEPTOR_API_JAR}"

java -jar  "${SIGTEST_JAR}" Setup -classpath "${CLASSPATH}" -Package jakarta.decorator -Package jakarta.enterprise -FileName cdi-api.sig -static
