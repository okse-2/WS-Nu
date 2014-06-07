//-----------------------------------------------------------------------------
// Copyright (C) 2014 Tormod Haugland and Inge Edward Haulsaunet
//
// This file is part of WS-Nu.
//
// WS-Nu is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// WS-Nu is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with WS-Nu. If not, see <http://www.gnu.org/licenses/>.
//-----------------------------------------------------------------------------

package org.ntnunotif.wsnu.examples.services;

import org.ntnunotif.wsnu.base.internal.SoapForwardingHub;
import org.ntnunotif.wsnu.services.implementations.notificationproducer.GenericNotificationProducer;

/**
 * A minimalistic producer example.
 */
public class MinimalisticProducer {

    private GenericNotificationProducer producer;

    private final String producerEndpoint = "myProducer";

    private SoapForwardingHub myHub;

    /**
     * Everything that needs to be done to set the system up can be done in the constructor.
     */
    public MinimalisticProducer() {
        // Instantiate the producer
        producer = new GenericNotificationProducer();

        // By calling the producers quickbuild method, we are starting both the producer and the rest of the
        // system, all in one.
        myHub = producer.quickBuild(producerEndpoint);

        // This is all that is strictly necessary to have your producer up and running. For a slightly more advanced example
        // see "BasicProducerUse"

    }
}