/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openstreetmap.josm.plugins.pdfimport.pdfbox.operators;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.operator.OperatorProcessor;
import org.openstreetmap.josm.plugins.pdfimport.pdfbox.PageDrawer;

/**
 * Implementation of sh operator for page drawer.
 *  See section 4.6.3 of the PDF 1.7 specification.
 *
 * @author <a href="mailto:Daniel.Wilson@BlackLocustSoftware.com">Daniel Wilson</a>
 * @version $Revision: 1.0 $
 */
public class SHFill extends OperatorProcessor
{

    /**
     * process : sh : shade fill the path or clipping area.
     * @param operator The operator that is being executed.
     * @param arguments List
     *
     * @throws IOException if there is an error during execution.
     */
    @Override
    public void process(PDFOperator operator, List<COSBase> arguments) throws IOException
    {
        PageDrawer drawer = (PageDrawer)context;
        drawer.drawPath(false, true, Path2D.WIND_NON_ZERO);
    }
}
