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
package fish.focus.uvms.commons.service.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.time.Instant;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class SimpleTracingInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleTracingInterceptor.class);

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception {

        Instant start = Instant.now();

        try {
            LOG.info(String.format("[START] %s ", context.getMethod().getName()));

            return context.proceed();
        }
        finally{
            Instant stop = Instant.now();
            LOG.info(String.format("[END] It took %s to evaluate the message.", Duration.between(start, stop)));
        }
    }
}