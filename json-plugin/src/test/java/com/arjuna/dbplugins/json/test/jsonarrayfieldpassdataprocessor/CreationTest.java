/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonarrayfieldpassdataprocessor;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.core.DataFlowNodeLifeCycleControl;
import com.arjuna.dbutilities.testsupport.dataflownodes.lifecycle.TestJEEDataFlowNodeLifeCycleControl;
import com.arjuna.dplugins.json.JSONArrayFieldPassDataProcessor;

public class CreationTest
{
    @Test
    public void creationBasics()
    {
        String              name       = "JSON Array Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldPassDataProcessor = new JSONArrayFieldPassDataProcessor(name, properties);

        String              readName       = jsonArrayFieldPassDataProcessor.getName();
        Map<String, String> readProperties = jsonArrayFieldPassDataProcessor.getProperties();

        assertEquals("Unexpected name", name, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "b,d", readProperties.get(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME));
    }

    @Test
    public void resetNameProperties()
    {
        String              name       = "JSON Array Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldPassDataProcessor = new JSONArrayFieldPassDataProcessor(name, properties);

        String              newName       = "New JSON Array Field Pass Data Processor";
        Map<String, String> newProperties = new HashMap<String, String>();
        newProperties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "a,c");

        try
        {
            jsonArrayFieldPassDataProcessor.setName(newName);
            jsonArrayFieldPassDataProcessor.setProperties(newProperties);
        }
        catch (Throwable throwable)
        {
            fail("Unexpected exception: " + throwable.toString());
        }

        String              readName       = jsonArrayFieldPassDataProcessor.getName();
        Map<String, String> readProperties = jsonArrayFieldPassDataProcessor.getProperties();

        assertEquals("Unexpected name", newName, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "a,c", readProperties.get(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME));
    }

    @Test
    public void dataTransportClasses()
    {
        String              name       = "JSON Array Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldPassDataProcessor = new JSONArrayFieldPassDataProcessor(name, properties);

        Collection<Class<?>> dataConsumerDataClasses = jsonArrayFieldPassDataProcessor.getDataConsumerDataClasses();
        assertNotNull("Unexpected 'null' dataConsumerDataClasses", dataConsumerDataClasses);
        assertEquals("Unexpected dataConsumerDataClasses size", 1, dataConsumerDataClasses.size());
        assertTrue("Unexpected content in dataConsumerDataClasses", dataConsumerDataClasses.contains(String.class));

        Collection<Class<?>> dataProviderDataClasses = jsonArrayFieldPassDataProcessor.getDataProviderDataClasses();
        assertNotNull("Unexpected 'null' dataProviderDataClasses", dataProviderDataClasses);
        assertEquals("Unexpected dataProviderDataClasses size", 1, dataProviderDataClasses.size());
        assertTrue("Unexpected content in dataProviderDataClasses", dataProviderDataClasses.contains(String.class));
    }

    @Test
    public void dataTransport()
    {
        DataFlowNodeLifeCycleControl dataFlowNodeLifeCycleControl = new TestJEEDataFlowNodeLifeCycleControl();

        String              name       = "JSON Array Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldPassDataProcessor = new JSONArrayFieldPassDataProcessor(name, properties);

        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), jsonArrayFieldPassDataProcessor, null);

        DataConsumer<String> dataConsumerString = jsonArrayFieldPassDataProcessor.getDataConsumer(String.class);
        assertNotNull("Unexpected 'null' dataConsumer<String>", dataConsumerString);

        DataConsumer<Integer> dataConsumerInteger = jsonArrayFieldPassDataProcessor.getDataConsumer(Integer.class);
        assertNull("Expected 'null' dataConsumer<Integer>", dataConsumerInteger);

        DataProvider<String> dataProviderString = jsonArrayFieldPassDataProcessor.getDataProvider(String.class);
        assertNotNull("Unexpected 'null' dataProvider<String>", dataProviderString);

        DataProvider<Integer> dataProviderInteger = jsonArrayFieldPassDataProcessor.getDataProvider(Integer.class);
        assertNull("Expected 'null' dataProvider<Integer>", dataProviderInteger);

        dataFlowNodeLifeCycleControl.removeDataFlowNode(jsonArrayFieldPassDataProcessor);
    }
}
