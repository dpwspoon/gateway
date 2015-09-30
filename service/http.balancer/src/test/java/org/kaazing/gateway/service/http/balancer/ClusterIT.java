package org.kaazing.gateway.service.http.balancer;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.kaazing.gateway.server.test.GatewayClusterRule;
import org.kaazing.gateway.server.test.config.GatewayConfiguration;
import org.kaazing.gateway.server.test.config.builder.GatewayConfigurationBuilder;
import org.kaazing.test.util.ITUtil;

public class ClusterIT {

    private GatewayClusterRule rule = new GatewayClusterRule() {
        {
            URI balancerURI = URI.create("ws://gateway.example.com:8000");
            URI clusterMember1URI =URI.create("tcp://localhost:8555");
            URI clusterMember2URI =URI.create("tcp://localhost:8556");
            
            GatewayConfiguration config1 = new GatewayConfigurationBuilder()
                    .cluster()
                        .accept(clusterMember1URI)
                        .connect(clusterMember2URI)
                        .name("clusterName")
                    .done()
                    .service()
                        .type("balancer")
                        .accept(balancerURI)
                        .acceptOption("ws.bind", "7001")
                    .done()
                    .service()
                        .type("echo")
                        .accept(URI.create("tcp://localhost:8000"))
                        .balance(balancerURI)
                    .done()
                .done();
            GatewayConfiguration config2= new GatewayConfigurationBuilder()
                    .cluster()
                    .name("clusterName")
                    .accept(clusterMember2URI)
                    .connect(clusterMember1URI)
                .done()
                .service()
                    .type("balancer")
                    .accept(balancerURI)
                    .acceptOption("ws.bind", "7000")
                .done()
                .service()
                    .type("echo")
                    .accept(URI.create("tcp://localhost:8001"))
                    .balance(balancerURI)
                .done()
            .done();
            init(config1, config2);
        }
    };
    
    @Rule
    public RuleChain chain = ITUtil.createRuleChain(rule, 30, SECONDS);

    @Test
    public void testLaunchCluster() throws Exception {
        throw new Exception("Excpetion");
    }
}
