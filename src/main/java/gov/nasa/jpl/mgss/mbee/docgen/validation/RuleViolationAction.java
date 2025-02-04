/*******************************************************************************
 * Copyright (c) <2013>, California Institute of Technology ("Caltech").  
 * U.S. Government sponsorship acknowledged.
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 *  - Redistributions of source code must retain the above copyright notice, this list of 
 *    conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright notice, this list 
 *    of conditions and the following disclaimer in the documentation and/or other materials 
 *    provided with the distribution.
 *  - Neither the name of Caltech nor its operating division, the Jet Propulsion Laboratory, 
 *    nor the names of its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER  
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.nasa.jpl.mgss.mbee.docgen.validation;

import java.util.Collection;

import javax.swing.KeyStroke;


import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.annotation.Annotation;
import com.nomagic.magicdraw.annotation.AnnotationManager;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.validation.RuleViolationResult;

public abstract class RuleViolationAction extends MDAction implements IRuleViolationAction {
    
    private static final long serialVersionUID = 1L;

    public RuleViolationAction(String paramString1, String paramString2,
            KeyStroke paramKeyStroke, String paramString3) {
        super(paramString1, paramString2, paramKeyStroke, paramString3);
        // TODO Auto-generated constructor stub
    }

    protected Annotation annotation;
    private RuleViolationResult rvr;
    private ValidationWindowRun vwr;
    
    @Override
    public void setAnnotation(Annotation anno) {
        annotation = anno;
    }
    
    @Override
    public void setRuleViolationResult(RuleViolationResult rvr) {
        this.rvr = rvr;
    }
    
    @Override
    public void setValidationWindowRun(ValidationWindowRun vwr) {
        this.vwr = vwr;
    }

    public void removeViolationAndUpdateWindow() {
        vwr.results.remove(rvr);
        AnnotationManager.getInstance().remove(annotation);
        AnnotationManager.getInstance().update();
        //ValidationResultsWindowManager.updateValidationResultsWindow(vwr.id, vwr.title, vwr.runData, vwr.results);
    }
    
    public void removeViolationsAndUpdateWindow(Collection<Annotation> annos) {
        for (Annotation anno: annos) {
            vwr.results.remove(vwr.mapping.get(anno));
            AnnotationManager.getInstance().remove(anno);
        }
        AnnotationManager.getInstance().update();
        //ValidationResultsWindowManager.updateValidationResultsWindow(vwr.id, vwr.title, vwr.runData, vwr.results);
    }
    
    public void saySuccess() {
        Application.getInstance().getGUILog().log("[INFO] Successful");
    }
}
