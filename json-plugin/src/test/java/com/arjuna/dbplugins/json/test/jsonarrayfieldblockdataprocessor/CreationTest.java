/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonarrayfieldblockdataprocessor;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.jee.DataFlowNodeLifeCycleControl;
import com.arjuna.dplugins.json.JSONArrayFieldBlockDataProcessor;

public class CreationTest
{
    @Test
    public void creationBasics()
    {
        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldBlockDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);

        String              readName       = jsonArrayFieldBlockDataProcessor.getName();
        Map<String, String> readProperties = jsonArrayFieldBlockDataProcessor.getProperties();

        assertEquals("Unexpected name", name, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "b,d", readProperties.get(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME));
    }

    @Test
    public void resetNameProperties()
    {
        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldBlockDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);

        String              newName       = "New JSON Array Field Block Data Processor";
        Map<String, String> newProperties = new HashMap<String, String>();
        newProperties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "a,c");

        try
        {
            jsonArrayFieldBlockDataProcessor.setName(newName);
            jsonArrayFieldBlockDataProcessor.setProperties(newProperties);
        }
        catch (Throwable throwable)
        {
            fail("Unexpected exception: " + throwable.toString());
        }

        String              readName       = jsonArrayFieldBlockDataProcessor.getName();
        Map<String, String> readProperties = jsonArrayFieldBlockDataProcessor.getProperties();

        assertEquals("Unexpected name", newName, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "a,c", readProperties.get(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME));
    }

    @Test
    public void dataTransportClasses()
    {
        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldBlockDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);

        Collection<Class<?>> dataConsumerDataClasses = jsonArrayFieldBlockDataProcessor.getDataConsumerDataClasses();
        assertNotNull("Unexpected 'null' dataConsumerDataClasses", dataConsumerDataClasses);
        assertEquals("Unexpected dataConsumerDataClasses size", 1, dataConsumerDataClasses.size());
        assertTrue("Unexpected content in dataConsumerDataClasses", dataConsumerDataClasses.contains(String.class));

        Collection<Class<?>> dataProviderDataClasses = jsonArrayFieldBlockDataProcessor.getDataProviderDataClasses();
        assertNotNull("Unexpected 'null' dataProviderDataClasses", dataProviderDataClasses);
        assertEquals("Unexpected dataProviderDataClasses size", 1, dataProviderDataClasses.size());
        assertTrue("Unexpected content in dataProviderDataClasses", dataProviderDataClasses.contains(String.class));
    }

    @Test
    public void dataTransport()
    {
        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldBlockDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);

        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(jsonArrayFieldBlockDataProcessor, null);

        DataConsumer<String> dataConsumerString = jsonArrayFieldBlockDataProcessor.getDataConsumer(String.class);
        assertNotNull("Unexpected 'null' dataConsumer<String>", dataConsumerString);

        DataConsumer<Integer> dataConsumerInteger = jsonArrayFieldBlockDataProcessor.getDataConsumer(Integer.class);
        assertNull("Expected 'null' dataConsumer<Integer>", dataConsumerInteger);

        DataProvider<String> dataProviderString = jsonArrayFieldBlockDataProcessor.getDataProvider(String.class);
        assertNotNull("Unexpected 'null' dataProvider<String>", dataProviderString);

        DataProvider<Integer> dataProviderInteger = jsonArrayFieldBlockDataProcessor.getDataProvider(Integer.class);
        assertNull("Expected 'null' dataProvider<Integer>", dataProviderInteger);
    }
}
