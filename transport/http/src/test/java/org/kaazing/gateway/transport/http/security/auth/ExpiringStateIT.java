package org.kaazing.gateway.transport.http.security.auth;

import static org.kaazing.test.util.ITUtil.createRuleChain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kaazing.gateway.server.test.GatewayClusterRule;
import org.kaazing.gateway.server.test.GatewayRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.gateway.util.feature.EarlyAccessFeatures;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;



import org.kaazing.test.util.ITUtil;
import org.kaazing.test.util.ResolutionTestUtils;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.kaazing.test.util.ITUtil.createRuleChain;



public class ExpiringStateIT {


    private final K3poRule k3po = new K3poRule();

    private static String networkInterface = ResolutionTestUtils.getLoopbackInterface();

    private GatewayClusterRule rule = new GatewayClusterRule() {
        {

            String clusterMember1URI = "tcp://localhost:8081";
            String clusterMember2URI = "tcp://localhost:8082";
            String clusterMember1URIcl = "tcp://localhost:5941";
            String clusterMember2URIcl = "tcp://localhost:5942";


//            GatewayConfiguration config1 = createGatewayConfigBuilder(
//                    clusterMember1URI, clusterMember2URI, "7001", "8000");
//            GatewayConfiguration config2 = createGatewayConfigBuilder(
//                    clusterMember2URI, clusterMember1URI, "7000", "8001");

    private GatewayConfiguration createGatewayConfigBuilder(String clusterMember1URI,
                                                            String clusterMember2URI, String servicePort) {
        String balancerURI1 = "ws://gateway.example.com:8001";
        return new GatewayConfigurationBuilder()
                .cluster()
                  .accept(clusterMember1URIcl)
                  .connect(clusterMember2URIcl)
                  .name("clusterName")
                .done()
                .service()
                  .type("directory")
                  .accept("tcp://localhost:" + servicePort)
                  .accept(clusterMember1URI)
                  .accept(clusterMember2URI)
                .done()
                .security()
                  .trustStore(trustStore)
                  .keyStore(keyStore)
                  .keyStorePassword(password)
                  .keyStoreFile(getKeystoreFileLocation())
                  .realm()
                    .name("demo")
                    .description("Kaazing WebSocket Gateway Demo")
                    .httpChallengeScheme("Basic")
//                    .httpQueryParameter("token")
//                .userPrincipalClass("org.kaazing.gateway.management.test.util.TokenCustomLoginModule$RolePrincipal")
//                .userPrincipalClass("org.kaazing.gateway.management.test.util.TokenCustomLoginModule$UserPrincipal")
//                .loginModule()
//                .type("class:org.kaazing.gateway.management.test.util.TokenCustomLoginModule")
                .success("required")
                .done()
                .done()
                .done();
    }



    @Rule
    public TestRule chain = k3po;

    @Test
    @Specification({
            "challenge.rejected.then.accepted/request",
            "challenge.rejected.then.accepted/response" })
    public void expiringState() throws Exception {


        k3po.finish();
    }





}
