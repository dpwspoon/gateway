FROM java:openjdk-8u45-jdk

# Get the gateway
ADD target/gateway/*.tar.gz gateway

# Move gateway to standard directory /kaazing-gateway
RUN mv gateway/kaazing* kaazing-gateway && rmdir gateway

# Add Log4J settings to redirect to STDOUT
COPY src/main/resources/log4j-config.xml /kaazing-gateway/conf/

# Set Working Dir
WORKDIR kaazing-gateway

# Define default command
CMD ["bin/gateway.start"]
