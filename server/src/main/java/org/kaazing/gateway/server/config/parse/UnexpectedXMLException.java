package org.kaazing.gateway.server.config.parse;

import javax.xml.stream.Location;

public class UnexpectedXMLException extends Exception {

    private static final long serialVersionUID = 870977889115032522L;

    public UnexpectedXMLException(Location location, String string) {
        super("Unexpected xml of type " + string + " at " + location);
    }

}
