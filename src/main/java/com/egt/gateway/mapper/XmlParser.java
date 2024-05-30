package com.egt.gateway.mapper;

import com.egt.gateway.dto.json.CurrentRateJsonResponseDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

public class XmlParser {
    private XmlParser(){}

    public static <T> T unmarshal(String request, Class<T> generic){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(generic);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(request);

            return generic.cast(unmarshaller.unmarshal(reader));
        } catch (JAXBException e) {
            throw new RuntimeException("Error parsing XML file",e);
        }
    }

    public static <T> String marshal(T obj){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException("Error parsing XML file",e);
        }
    }
}
