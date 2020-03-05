# Import base image
FROM openjdk:8u212-jre

# Create log file directory and set permission
RUN groupadd -r gwas-audit-service && useradd -r --create-home -g gwas-audit-service gwas-audit-service
RUN if [ ! -d /var/log/gwas/ ];then mkdir /var/log/gwas/;fi
RUN chown -R gwas-audit-service:gwas-audit-service /var/log/gwas

# Move project artifact
ADD target/gwasdepo-audit-service-*.jar /home/gwas-audit-service/
USER gwas-audit-service

# Launch application server
ENTRYPOINT exec $JAVA_HOME/bin/java $XMX $XMS -jar -Dspring.profiles.active=$ENVIRONMENT /home/gwas-audit-service/gwasdepo-audit-service-*.jar
