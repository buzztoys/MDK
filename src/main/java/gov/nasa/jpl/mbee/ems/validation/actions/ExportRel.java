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
package gov.nasa.jpl.mbee.ems.validation.actions;

import gov.nasa.jpl.mbee.ems.ExportUtility;
import gov.nasa.jpl.mgss.mbee.docgen.validation.IRuleViolationAction;
import gov.nasa.jpl.mgss.mbee.docgen.validation.RuleViolationAction;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.nomagic.magicdraw.annotation.Annotation;
import com.nomagic.magicdraw.annotation.AnnotationAction;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;

public class ExportRel extends RuleViolationAction implements AnnotationAction, IRuleViolationAction {
    private static final long serialVersionUID = 1L;
    private Element element;
    
    public ExportRel(Element e) {
    	//JJS--MDEV-567 fix: changed 'Export' to 'Commit'
    	//
        super("ExportRel", "Commit rel", null, null);
        this.element = e;
    }
    
    @Override
    public boolean canExecute(Collection<Annotation> arg0) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(Collection<Annotation> annos) {
        JSONObject send = new JSONObject();
        JSONArray infos = new JSONArray();
        Set<Element> set = new HashSet<Element>();
        for (Annotation anno: annos) {
            Element e = (Element)anno.getTarget();
            set.add(e);
            JSONObject info = new JSONObject();
            JSONObject specialization = new JSONObject();
            info.put("specialization", specialization);
            specialization.put("source", ModelHelper.getClientElement(e).getID());
            specialization.put("target", ModelHelper.getSupplierElement(e).getID());
            info.put("sysmlid", e.getID());
            infos.add(info);
        }
        if (!ExportUtility.okToExport(set))
            return;
        send.put("elements", infos);
        String url = ExportUtility.getPostElementsUrl();
        if (url == null) {
            return;
        }
        if (ExportUtility.send(url, send.toJSONString())) {
            this.removeViolationsAndUpdateWindow(annos);
            ExportUtility.sendProjectVersions();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!ExportUtility.okToExport(element))
            return;
        JSONObject info = new JSONObject();
        JSONArray elements = new JSONArray();
        JSONObject send = new JSONObject();
        JSONObject specialization = new JSONObject();
        info.put("specialization", specialization);
        specialization.put("source", ModelHelper.getClientElement(element).getID());
        specialization.put("target", ModelHelper.getSupplierElement(element).getID());
        info.put("sysmlid", e.getID());
        elements.add(info);
        send.put("elements", elements);
        String url = ExportUtility.getPostElementsUrl();
        if (url == null) {
            return;
        }
        if (ExportUtility.send(url, send.toJSONString())) {
            this.removeViolationAndUpdateWindow();
            ExportUtility.sendProjectVersion(element);
        }

    }
}

