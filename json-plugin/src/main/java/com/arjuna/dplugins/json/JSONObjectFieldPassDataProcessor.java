/*
 * Copyright (c) 2014, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.json;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlow;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.jee.annotation.DataConsumerInjection;
import com.arjuna.databroker.data.jee.annotation.DataProviderInjection;
import com.arjuna.databroker.data.jee.annotation.PostConfig;
import com.arjuna.databroker.data.jee.annotation.PostCreated;

public class JSONObjectFieldPassDataProcessor implements DataProcessor
{
    private static final Logger logger = Logger.getLogger(JSONObjectFieldPassDataProcessor.class.getName());

    public static final String FIELDSPASSED_PROPERTYNAME = "Fields Passed";

    public JSONObjectFieldPassDataProcessor(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "JSONObjectFieldPassDataProcessor: " + name + ", " + properties);

        _fieldsPassed = new LinkedList<String>();

        _name       = name;
        _properties = properties;
        _dataFlow   = null;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(_properties);
    }

    @Override
    public void setProperties(Map<String, String> properties)
    {
        _properties = properties;
    }

    @Override
    public DataFlow getDataFlow()
    {
        return _dataFlow;
    }

    @Override
    public void setDataFlow(DataFlow dataFlow)
    {
        _dataFlow = dataFlow;
    }

    @PostCreated
    public void setup()
    {
        config();
    }
    
    @PostConfig
    public void config()
    {
        String fieldsPassedProperty = _properties.get(FIELDSPASSED_PROPERTYNAME);

        _fieldsPassed.clear();
        if (fieldsPassedProperty != null)
            for (String fieldPassed: fieldsPassedProperty.split(","))
                _fieldsPassed.add(fieldPassed.trim());
    }
 
    public void filter(String data)
    {
        try
        {
            JSONObject inputJSONObject  = new JSONObject(data);
            JSONObject outputJSONObject = new JSONObject();

            for (String fieldPassed: _fieldsPassed)
            {
                try
                {
                    Object fieldPassedObjectValue = inputJSONObject.get(fieldPassed);

                    outputJSONObject.put(fieldPassed, fieldPassedObjectValue);
                }
                catch (JSONException jsonException)
                {
                }
            }

            _dataProvider.produce(outputJSONObject.toString());
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Failed to process");
        }
    }
    
    @Override
    public Collection<Class<?>> getDataConsumerDataClasses()
    {
        Set<Class<?>> dataConsumerDataClasses = new HashSet<Class<?>>();

        dataConsumerDataClasses.add(String.class);

        return dataConsumerDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataConsumer<T> getDataConsumer(Class<T> dataClass)
    {
        if (dataClass == String.class)
            return (DataConsumer<T>) _dataConsumer;
        else
            return null;
    }

    @Override
    public Collection<Class<?>> getDataProviderDataClasses()
    {
        Set<Class<?>> dataProviderDataClasses = new HashSet<Class<?>>();

        dataProviderDataClasses.add(String.class);

        return dataProviderDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataProvider<T> getDataProvider(Class<T> dataClass)
    {
        if (dataClass == String.class)
            return (DataProvider<T>) _dataProvider;
        else
            return null;
    }

    private List<String> _fieldsPassed;

    private String               _name;
    private Map<String, String>  _properties;
    private DataFlow             _dataFlow;
    @DataConsumerInjection(methodName="filter")
    private DataConsumer<String> _dataConsumer;
    @DataProviderInjection
    private DataProvider<String> _dataProvider;
}
