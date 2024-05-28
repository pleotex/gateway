package com.egt.gateway.mapper;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;

public class XmlParser {
    private XmlParser(){}

    public static <T> T parseXml(String request, Class<T> generic){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(generic);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(request);

            return generic.cast(unmarshaller.unmarshal(reader));
        } catch (JAXBException e) {
            throw new RuntimeException("Error parsing XML file",e);
        }
    }
}
