/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonobjectfieldblockdataprocessor;

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
import com.arjuna.dbplugins.json.JSONObjectFieldBlockDataProcessor;
import com.arjuna.dbutils.testsupport.dataflownodes.lifecycle.TestJEEDataFlowNodeLifeCycleControl;

public class CreationTest
{
    @Test
    public void creationBasics()
    {
        String              name       = "JSON Object Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonObjectFieldPassDataProcessor = new JSONObjectFieldBlockDataProcessor(name, properties);

        String              readName       = jsonObjectFieldPassDataProcessor.getName();
        Map<String, String> readProperties = jsonObjectFieldPassDataProcessor.getProperties();

        assertEquals("Unexpected name", name, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "b,d", readProperties.get(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME));
    }

    @Test
    public void resetNameProperties()
    {
        String              name       = "JSON Object Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonObjectFieldPassDataProcessor = new JSONObjectFieldBlockDataProcessor(name, properties);

        String              newName       = "New JSON Object Field Block Data Processor";
        Map<String, String> newProperties = new HashMap<String, String>();
        newProperties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "a,c");

        try
        {
            jsonObjectFieldPassDataProcessor.setName(newName);
            jsonObjectFieldPassDataProcessor.setProperties(newProperties);
        }
        catch (Throwable throwable)
        {
            fail("Unexpected exception: " + throwable.toString());
        }

        String              readName       = jsonObjectFieldPassDataProcessor.getName();
        Map<String, String> readProperties = jsonObjectFieldPassDataProcessor.getProperties();

        assertEquals("Unexpected name", newName, readName);
        assertNotNull("Unexpected 'null' properties", readProperties);
        assertEquals("Unexpected properties size", 1, readProperties.size());
        assertEquals("Unexpected properties value", "a,c", readProperties.get(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME));
    }

    @Test
    public void dataTransportClasses()
    {
        String              name       = "JSON Object Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonObjectFieldPassDataProcessor = new JSONObjectFieldBlockDataProcessor(name, properties);

        Collection<Class<?>> dataConsumerDataClasses = jsonObjectFieldPassDataProcessor.getDataConsumerDataClasses();
        assertNotNull("Unexpected 'null' dataConsumerDataClasses", dataConsumerDataClasses);
        assertEquals("Unexpected dataConsumerDataClasses size", 1, dataConsumerDataClasses.size());
        assertTrue("Unexpected content in dataConsumerDataClasses", dataConsumerDataClasses.contains(String.class));

        Collection<Class<?>> dataProviderDataClasses = jsonObjectFieldPassDataProcessor.getDataProviderDataClasses();
        assertNotNull("Unexpected 'null' dataProviderDataClasses", dataProviderDataClasses);
        assertEquals("Unexpected dataProviderDataClasses size", 1, dataProviderDataClasses.size());
        assertTrue("Unexpected content in dataProviderDataClasses", dataProviderDataClasses.contains(String.class));
    }

    @Test
    public void dataTransport()
    {
        DataFlowNodeLifeCycleControl dataFlowNodeLifeCycleControl = new TestJEEDataFlowNodeLifeCycleControl();

        String              name       = "JSON Object Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonObjectFieldPassDataProcessor = new JSONObjectFieldBlockDataProcessor(name, properties);

        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), jsonObjectFieldPassDataProcessor, null);

        DataConsumer<String> dataConsumerString = jsonObjectFieldPassDataProcessor.getDataConsumer(String.class);
        assertNotNull("Unexpected 'null' dataConsumer<String>", dataConsumerString);

        DataConsumer<Integer> dataConsumerInteger = jsonObjectFieldPassDataProcessor.getDataConsumer(Integer.class);
        assertNull("Expected 'null' dataConsumer<Integer>", dataConsumerInteger);

        DataProvider<String> dataProviderString = jsonObjectFieldPassDataProcessor.getDataProvider(String.class);
        assertNotNull("Unexpected 'null' dataProvider<String>", dataProviderString);

        DataProvider<Integer> dataProviderInteger = jsonObjectFieldPassDataProcessor.getDataProvider(Integer.class);
        assertNull("Expected 'null' dataProvider<Integer>", dataProviderInteger);

        dataFlowNodeLifeCycleControl.removeDataFlowNode(jsonObjectFieldPassDataProcessor);
    }
}
