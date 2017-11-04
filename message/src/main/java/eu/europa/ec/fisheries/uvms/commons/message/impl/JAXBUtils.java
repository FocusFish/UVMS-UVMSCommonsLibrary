/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.commons.message.impl;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class JAXBUtils {

	private JAXBUtils(){

	}

	/**
	 * Marshalls a JAXB Object to a XML String representation.
	 *
	 * @param <T>
	 * 			@param data @return @throws
	 */
	public static <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		final StringWriter sw = new StringWriter();
		marshaller.marshal(data, sw);
		return sw.toString();
	}

	/**
	 * Unmarshalls A textMessage to the desired Object. The object must be the root
	 * object of the unmarshalled message!
	 *
	 * @param <R>
	 * 			@param textMessage @param clazz @return @throws
	 */
	public static <R> R unmarshallTextMessage(final TextMessage textMessage, final Class clazz)
			throws JAXBException, JMSException {
		final JAXBContext jc = JAXBContext.newInstance(clazz);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		final StringReader sr = new StringReader(textMessage.getText());
		return (R) unmarshaller.unmarshal(sr);
	}

}