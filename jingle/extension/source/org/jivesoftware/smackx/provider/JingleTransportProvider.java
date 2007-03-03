/**
 * $RCSfile$
 * $Revision: 7329 $
 * $Date: 2007-02-28 20:59:28 -0300 (qua, 28 fev 2007) $
 *
 * Copyright 2003-2005 Jive Software.
 *
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.jingle.nat.TransportCandidate;
import org.jivesoftware.smackx.jingle.nat.ICECandidate;
import org.jivesoftware.smackx.packet.JingleTransport;
import org.jivesoftware.smackx.packet.JingleTransport.JingleTransportCandidate;
import org.xmlpull.v1.XmlPullParser;

/**
 * Provider for a Jingle transport element
 *
 * @author Alvaro Saurin <alvaro.saurin@gmail.com>
 */
public abstract class JingleTransportProvider implements PacketExtensionProvider {

    /**
     * Creates a new provider. ProviderManager requires that every
     * PacketExtensionProvider has a public, no-argument constructor
     */
    public JingleTransportProvider() {
        super();
    }

    /**
     * Obtain the corresponding TransportNegotiator instance.
     *
     * @return a new TransportNegotiator instance
     */
    protected JingleTransport getInstance() {
        return new JingleTransport();
    }

    /**
     * Parse a iq/jingle/transport element.
     *
     * @param parser the structure to parse
     * @return a transport element.
     * @throws Exception
     */
    public PacketExtension parseExtension(final XmlPullParser parser) throws Exception {
        boolean done = false;
        JingleTransport trans = getInstance();

        while (!done) {
            int eventType = parser.next();
            String name = parser.getName();

            if (eventType == XmlPullParser.START_TAG) {
                if (name.equals(JingleTransportCandidate.NODENAME)) {
                    JingleTransportCandidate jtc = parseCandidate(parser);
                    if (jtc != null) trans.addCandidate(jtc);
                }
                else {
                    throw new Exception("Unknown tag \"" + name + "\" in transport element.");
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (name.equals(JingleTransport.NODENAME)) {
                    done = true;
                }
            }
        }

        return trans;
    }

    protected abstract JingleTransportCandidate parseCandidate(final XmlPullParser parser)
            throws Exception;

    /**
     * RTP-ICE profile
     */
    public static class Ice extends JingleTransportProvider {

        /**
         * Defauls constructor.
         */
        public Ice() {
            super();
        }

        /**
         * Obtain the corresponding TransportNegotiator.Ice instance.
         *
         * @return a new TransportNegotiator.Ice instance
         */
        protected JingleTransport getInstance() {
            return new JingleTransport.Ice();
        }

        /**
         * Parse a iq/jingle/transport/candidate element.
         *
         * @param parser the structure to parse
         * @return a candidate element
         * @throws Exception
         */
        protected JingleTransportCandidate parseCandidate(XmlPullParser parser) throws Exception {
            ICECandidate mt = new ICECandidate();

            String channel = parser.getAttributeValue("", "channel");
            String generation = parser.getAttributeValue("", "generation");
            String ip = parser.getAttributeValue("", "ip");
            String name = parser.getAttributeValue("", "name");
            String network = parser.getAttributeValue("", "network");
            String username = parser.getAttributeValue("", "username");
            String password = parser.getAttributeValue("", "password");
            String port = parser.getAttributeValue("", "port");
            String preference = parser.getAttributeValue("", "preference");
            String proto = parser.getAttributeValue("", "proto");
            String type = parser.getAttributeValue("", "type");

            if (channel != null) {
                mt.setChannel(new TransportCandidate.Channel(channel));
            }

            if (generation != null) {
                try {
                    mt.setGeneration(Integer.parseInt(generation));
                }
                catch (Exception e) {
                }
            }

            if (ip != null) {
                mt.setIp(ip);
            }
            else {
                return null;
            }

            if (name != null) {
                mt.setName(name);
            }

            if (network != null) {
                try {
                    mt.setNetwork(Integer.parseInt(network));
                }
                catch (Exception e) {
                }
            }

            if (username != null) {
                mt.setUsername(username);
            }

            if (password != null) {
                mt.setPassword(password);
            }

            if (port != null) {
                try {
                    mt.setPort(Integer.parseInt(port));
                }
                catch (Exception e) {
                }
            }

            if (preference != null) {
                try {
                    mt.setPreference(Integer.parseInt(preference));
                }
                catch (Exception e) {
                }
            }

            if (proto != null) {
                mt.setProto(new TransportCandidate.Protocol(proto));
            }

            if (type != null) {
                mt.setType(type);
            }

            return new JingleTransport.Ice.Candidate(mt);
        }
    }

    /**
     * Raw UDP profile
     */
    public static class RawUdp extends JingleTransportProvider {

        /**
         * Defauls constructor.
         */
        public RawUdp() {
            super();
        }

        /**
         * Obtain the corresponding TransportNegotiator.RawUdp instance.
         *
         * @return a new TransportNegotiator.RawUdp instance
         */
        protected JingleTransport getInstance() {
            return new JingleTransport.RawUdp();
        }

        /**
         * Parse a iq/jingle/transport/candidate element.
         *
         * @param parser the structure to parse
         * @return a candidate element
         * @throws Exception
         */
        protected JingleTransportCandidate parseCandidate(XmlPullParser parser) throws Exception {
            TransportCandidate.Fixed mt = new TransportCandidate.Fixed();

            String generation = parser.getAttributeValue("", "generation");
            String ip = parser.getAttributeValue("", "ip");
            String name = parser.getAttributeValue("", "name");
            String port = parser.getAttributeValue("", "port");

            //System.out.println();

            if (generation != null) {
                try {
                    mt.setGeneration(Integer.parseInt(generation));
                }
                catch (Exception e) {
                }
            }

            if (ip != null) {
                mt.setIp(ip);
            }

            if (name != null) {
                mt.setName(name);
            }

            if (port != null) {
                try {
                    mt.setPort(Integer.parseInt(port));
                }
                catch (Exception e) {
                }
            }
            return new JingleTransport.RawUdp.Candidate(mt);
        }
    }
}
