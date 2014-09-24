/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.qpid.jms.provider.amqp.message;

import static org.apache.qpid.jms.provider.amqp.message.AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING;
import static org.apache.qpid.jms.provider.amqp.message.AmqpDestinationHelper.REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME;
import static org.apache.qpid.jms.provider.amqp.message.AmqpDestinationHelper.TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.qpid.jms.JmsDestination;
import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.JmsTemporaryQueue;
import org.apache.qpid.jms.JmsTemporaryTopic;
import org.apache.qpid.jms.JmsTopic;
import org.junit.Test;
import org.mockito.Mockito;

public class AmqpDestinationHelperTest {

    private final AmqpDestinationHelper helper = AmqpDestinationHelper.INSTANCE;

    //--------------- Test getJmsDestination method --------------------------//

    @Test
    public void testGetJmsDestinationWithNullAddressAndNullConsumerDestReturnsNull() throws Exception {
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getToAddress()).thenReturn(null);
        Mockito.when(message.getAnnotation(
            TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(QUEUE_ATTRIBUTES_STRING);

        assertNull(helper.getJmsDestination(message, null));
    }

    @Test
    public void testGetJmsDestinationWithNullAddressWithConsumerDestReturnsSameConsumerDestObject() throws Exception {
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getToAddress()).thenReturn(null);
        Mockito.when(message.getAnnotation(
            TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(QUEUE_ATTRIBUTES_STRING);
        JmsQueue consumerDestination = new JmsQueue("ConsumerDestination");

        assertSame(consumerDestination, helper.getJmsDestination(message, consumerDestination));
    }

    @Test
    public void testGetJmsDestinationWithoutTypeAnnotationWithQueueConsumerDest() throws Exception {
        String testAddress = "testAddress";
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getToAddress()).thenReturn(testAddress);
        Mockito.when(message.getAnnotation(TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(null);
        JmsQueue consumerDestination = new JmsQueue("ConsumerDestination");

        JmsDestination destination = helper.getJmsDestination(message, consumerDestination);
        assertNotNull(destination);
        assertTrue(destination.isQueue());
        assertFalse(destination.isTemporary());
        assertEquals(testAddress, destination.getName());
    }

    //--------------- Test getJmsReplyTo method ------------------------------//

    @Test
    public void testGetJmsReplyToWithNullAddressAndNullConsumerDestReturnsNull() throws Exception {
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(null);
        Mockito.when(message.getAnnotation(
            REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(QUEUE_ATTRIBUTES_STRING);

        assertNull(helper.getJmsDestination(message, null));
    }

    @Test
    public void testGetJmsReplyToWithNullAddressWithConsumerDestReturnsNull() throws Exception {
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(null);
        Mockito.when(message.getAnnotation(
            REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(QUEUE_ATTRIBUTES_STRING);
        JmsQueue consumerDestination = new JmsQueue("ConsumerDestination");

        assertNull(helper.getJmsReplyTo(message, consumerDestination));
    }

    @Test
    public void testGetJmsReplyToWithoutTypeAnnotationWithQueueConsumerDest() throws Exception {
        String testAddress = "testAddress";
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(testAddress);
        Mockito.when(message.getAnnotation(REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(null);
        JmsQueue consumerDestination = new JmsQueue("ConsumerDestination");

        JmsDestination destination = helper.getJmsReplyTo(message, consumerDestination);
        assertNotNull(destination);
        assertTrue(destination.isQueue());
        assertFalse(destination.isTemporary());
        assertEquals(testAddress, destination.getName());
    }

    @Test
    public void testGetJmsReplyToWithoutTypeAnnotationWithTopicConsumerDest() throws Exception {
        String testAddress = "testAddress";
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(testAddress);
        Mockito.when(message.getAnnotation(REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(null);
        JmsTopic consumerDestination = new JmsTopic("ConsumerDestination");

        JmsDestination destination = helper.getJmsReplyTo(message, consumerDestination);
        assertNotNull(destination);
        assertTrue(destination.isTopic());
        assertFalse(destination.isTemporary());
        assertEquals(testAddress, destination.getName());
    }

    @Test
    public void testGetJmsReplyToWithoutTypeAnnotationWithTempQueueConsumerDest() throws Exception {
        String testAddress = "testAddress";
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(testAddress);
        Mockito.when(message.getAnnotation(REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(null);
        JmsTemporaryQueue consumerDestination = new JmsTemporaryQueue("ConsumerDestination");

        JmsDestination destination = helper.getJmsReplyTo(message, consumerDestination);
        assertNotNull(destination);
        assertTrue(destination.isQueue());
        assertTrue(destination.isTemporary());
        assertEquals(testAddress, destination.getName());
    }

    @Test
    public void testGetJmsReplyToWithoutTypeAnnotationWithTempTopicConsumerDest() throws Exception {
        String testAddress = "testAddress";
        AmqpJmsMessageFacade message = Mockito.mock(AmqpJmsMessageFacade.class);
        Mockito.when(message.getReplyToAddress()).thenReturn(testAddress);
        Mockito.when(message.getAnnotation(REPLY_TO_TYPE_MSG_ANNOTATION_SYMBOL_NAME)).thenReturn(null);
        JmsTemporaryTopic consumerDestination = new JmsTemporaryTopic("ConsumerDestination");

        JmsDestination destination = helper.getJmsReplyTo(message, consumerDestination);
        assertNotNull(destination);
        assertTrue(destination.isTopic());
        assertTrue(destination.isTemporary());
        assertEquals(testAddress, destination.getName());
    }

    //--------------- Test setToAddress method -------------------------------//

    //--------------- Test setReplyToAddress method --------------------------//

    //--------------- Test Support Methods -----------------------------------//

    @Test
    public void testSplitAttributeWithExtranerousCommas() throws Exception {

        Set<String> set = new HashSet<String>();
        set.add(AmqpDestinationHelper.QUEUE_ATTRIBUTE);
        set.add(AmqpDestinationHelper.TEMPORARY_ATTRIBUTE);

        // test a single comma separator produces expected set
        assertEquals(set, helper.splitAttributes(AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + "," +
                                                 AmqpDestinationHelper.TEMPORARY_ATTRIBUTE));

        // test trailing comma doesn't alter produced set
        assertEquals(set, helper.splitAttributes(AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + "," +
                                                 AmqpDestinationHelper.TEMPORARY_ATTRIBUTE + ","));

        // test leading comma doesn't alter produced set
        assertEquals(set, helper.splitAttributes("," + AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + ","
                                                     + AmqpDestinationHelper.TEMPORARY_ATTRIBUTE));

        // test consecutive central commas don't alter produced set
        assertEquals(set, helper.splitAttributes(AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + ",," +
                                                 AmqpDestinationHelper.TEMPORARY_ATTRIBUTE));

        // test consecutive trailing commas don't alter produced set
        assertEquals(set, helper.splitAttributes(AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + "," +
                                                 AmqpDestinationHelper.TEMPORARY_ATTRIBUTE + ",,"));

        // test consecutive leading commas don't alter produced set
        assertEquals(set, helper.splitAttributes("," + AmqpDestinationHelper.QUEUE_ATTRIBUTES_STRING + ","
                                                     + AmqpDestinationHelper.TEMPORARY_ATTRIBUTE));
    }
}
