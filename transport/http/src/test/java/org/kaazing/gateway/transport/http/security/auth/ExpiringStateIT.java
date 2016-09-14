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

import java.util.concurrent.TimeUnit;

import org.kaazing.test.util.ITUtil;
import org.kaazing.test.util.ResolutionTestUtils;








public class ExpiringStateIT {



    private final K3poRule k3po = new K3poRule();

    private static String networkInterface = ResolutionTestUtils.getLoopbackInterface();

    private GatewayClusterRule gwRule = new GatewayClusterRule() {
        {

            String clusterMember1URI = "tcp://localhost:8081";
            String clusterMember2URI = "tcp://localhost:8082";
            String clusterMember1URIcl = "tcp://localhost:5941";
            String clusterMember2URIcl = "tcp://localhost:5942";


            GatewayConfiguration config1 = createGatewayConfigBuilder(clusterMember1URIcl, clusterMember2URIcl, clusterMember1URI);
            GatewayConfiguration config2 = createGatewayConfigBuilder(clusterMember2URIcl, clusterMember1URIcl, clusterMember2URI);

            init(config1, config2);
        }
    };


    private GatewayConfiguration createGatewayConfigBuilder(String acceptClusterLink,
                                                            String connectClusterLink,
                                                            String acceptMemberLink) {
        return new GatewayConfigurationBuilder()
                .property("login.module.expiring.state", "true")
                .cluster()
                  .accept(acceptClusterLink)
                  .connect(connectClusterLink)
                  .name("clusterName")
                .done()
                .service()
                  .type("directory")
                  .accept("tcp://localhost:8080")
                  .accept(acceptMemberLink)
                .done()
                .security()
                  .realm()
                    .name("demo")
                    .description("Kaazing WebSocket Gateway Demo")
                    .httpChallengeScheme("Basic")
                    .done()
                  .done()
//                    .httpQueryParameter("token")
//                .loginModule()
//                .type("class:org.kaazing.gateway.management.test.util.TokenCustomLoginModule")
                .done();

    }

    @Rule
    public TestRule chain = createRuleChain(10, TimeUnit.SECONDS).around(k3po).around(gwRule);


    @Test
    @Specification({
            "challenge.rejected.then.accepted/request",
            "challenge.rejected.then.accepted/response" })
    public void expiringState() throws Exception {


        k3po.finish();
    }





}
