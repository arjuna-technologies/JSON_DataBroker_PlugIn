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

public class JSONFieldFilterDataProcessor implements DataProcessor
{
    private static final Logger logger = Logger.getLogger(JSONFieldFilterDataProcessor.class.getName());

    public static final String JSON_FIELDSPASSED_PROPERTYNAME = "JSON Fields Passed";

    public JSONFieldFilterDataProcessor(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "JSONFieldFilterDataProcessor: " + name + ", " + properties);

        _fields = new LinkedList<String>();

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

    @PostConfig
    public void config()
    {
        String fieldsText = _properties.get(JSON_FIELDSPASSED_PROPERTYNAME);

        _fields.clear();
        if (fieldsText != null)
            for (String field: fieldsText.split(","))
                _fields.add(field.trim());
    }
 
    public void filter(String data)
    {
        try
        {
            JSONObject inputJSONObject  = new JSONObject(data);
            JSONObject outputJSONObject = new JSONObject();

            for (String field: _fields)
            {
                try
                {
                    Object fieldObjectValue = inputJSONObject.get(field);

                    outputJSONObject.put(field, fieldObjectValue);
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

    private List<String> _fields;

    private String               _name;
    private Map<String, String>  _properties;
    private DataFlow             _dataFlow;
    @DataConsumerInjection(methodName="filter")
    private DataConsumer<String> _dataConsumer;
    @DataProviderInjection
    private DataProvider<String> _dataProvider;
}
