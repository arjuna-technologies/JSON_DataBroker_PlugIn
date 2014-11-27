/*
 * Copyright (c) 2014, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonarrayfieldblockdataprocessor;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.dplugins.json.JSONArrayFieldBlockDataProcessor;

public class CreationTest
{
    @Test
    public void creationBasics()
    {
        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DataProcessor jsonArrayFieldPassDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);

        assertEquals("Unexpected name", name, jsonArrayFieldPassDataProcessor.getName());

        Collection<Class<?>> dataConsumerDataClasses = jsonArrayFieldPassDataProcessor.getDataConsumerDataClasses();
        assertNotNull("Unexpected 'null' dataConsumerDataClasses", dataConsumerDataClasses);
        assertEquals("Unexpected dataConsumerDataClasses size", 1, dataConsumerDataClasses.size());
        assertTrue("Unexpected content in dataConsumerDataClasses", dataConsumerDataClasses.contains(String.class));

        Collection<Class<?>> dataProviderDataClasses = jsonArrayFieldPassDataProcessor.getDataProviderDataClasses();
        assertNotNull("Unexpected 'null' dataProviderDataClasses", dataProviderDataClasses);
        assertEquals("Unexpected dataProviderDataClasses size", 1, dataProviderDataClasses.size());
        assertTrue("Unexpected content in dataProviderDataClasses", dataProviderDataClasses.contains(String.class));
    }
}
